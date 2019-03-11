from apscheduler.schedulers.background import BackgroundScheduler
from flask import request, Blueprint
from area_db import db
from area_cr import cr_record, cr_record_check, cr_above_friend, cr_above_friend_check, cr_clan_score, cr_clan_score_check
from area_tw import tw_post_tweet, tw_post_tweet_check, tw_send_dm, tw_send_dm_check, tw_reach_followers_check, tw_reach_followers, tw_reach_friend_followers_check, tw_reach_friend_followers
from area_tools import serialize_response, check_token, get_user
from area_cm import crypto_value_check, crypto_value
from area_gh import github_commit_check, github_commit, github_stars, github_stars_check
from area_em import send_mail, send_mail_check
from area_th import th_get_status, th_get_status_check
from area_wh import weather_alert_hot, weather_alert_hot_check, weather_alert_cold, weather_alert_cold_check
from area_yt import yt_subscribers_record_check, yt_subscribers_record, yt_subscribers_above_channel_check, yt_subscribers_above_channel, yt_new_video, yt_new_video_check
from area_lol import lol_levelup, lol_levelup_check, lol_bestof, lol_bestof_check
import time

action_array = [
    [cr_record, 180, cr_record_check],
    [cr_above_friend, 180, cr_above_friend_check],
    [cr_clan_score, 3600, cr_clan_score_check],
    [crypto_value, 60, crypto_value_check],
    [github_commit, 60, github_commit_check],
    [th_get_status, 360, th_get_status_check],
    [weather_alert_hot, 1800, weather_alert_hot_check],
    [weather_alert_cold, 1800, weather_alert_cold_check],
    [tw_reach_followers, 180, tw_reach_followers_check],
    [tw_reach_friend_followers, 180, tw_reach_friend_followers_check],
    [yt_subscribers_record, 60, yt_subscribers_record_check],
    [yt_subscribers_above_channel, 60, yt_subscribers_above_channel_check],
    [yt_new_video, 360, yt_new_video_check],
    [github_stars, 180, github_stars_check],
    [lol_levelup, 1200, lol_levelup_check],
    [lol_bestof, 1200, lol_bestof_check]
]

reaction_array = [
    [tw_post_tweet, tw_post_tweet_check],
    [tw_send_dm, tw_send_dm_check],
    [send_mail, send_mail_check]
]

area_trigger_bp = Blueprint('area_trigger_bp', __name__)

scheduler = BackgroundScheduler(timezone="Europe/Paris")
scheduler.start()

@area_trigger_bp.route("/rmvTriggers", methods=['POST'])
def rmv_triggers():
    """
    Description\n
        POST route to remove a trigger from a user list of triggers
    Json-data
        triggerId : trigger id of authenticated user\t
    Headers
        Authorization : Bearer token
    Return
        200 code for succes and 400 for failure
    """
    users = db.child('Users').get()
    authorization = request.headers.get("Authorization")
    token = check_token(authorization, users)
    if token == False:
        return serialize_response('Unkown user', {}, 400)
    user = get_user(authorization.split(' ')[1], users)
    val = user.val()
    data = request.json
    trigger_id = int(data["triggerId"])
    job_id = val["triggers"][trigger_id]["job_id"]
    try:
        if len(val["triggers"]) == 1:
            val["triggers"] = [-1]
        else:
            val["triggers"].pop(trigger_id)
    except:
        return serialize_response('Invalid trigger id', {}, 400)
    try:
        scheduler.remove_job(job_id)
        db.child("Users").child(user.key()).set(val)
    except:
        return serialize_response('Invalid trigger id', {}, 400)
    return serialize_response('Trigger deleted', {}, 200)

@area_trigger_bp.route("/getTriggers", methods=['GET'])
def get_triggers():
    """
    Description\n
        GET route to get all user triggers
    Json-data
        twitterToken : twitter token from oauth\t
        twitterSecret : twitter secret from oauth
    Headers
        Authorization : Bearer token
    Return
        200 code for succes and 400 for failure
    """
    users = db.child('Users').get()
    authorization = request.headers.get("Authorization")
    user = check_token(authorization, users)
    if user == False:
        return serialize_response('Unkown user', {}, 400)
    return serialize_response('Here are the triggers', {"triggers": user["triggers"]}, 200)

def add_trigger_to_db(job_id, user, key, action_id, reaction_id, action_data, reaction_data):
    """
    Description\n
        Add new trigger to database
    Parameters
        job_id : apscheduler jobid\t
        user : authenticated user\t
        key : \t
        action_id : index of array action
        reaction_id : index of reaction id
        action_data : json data linked to action
        reaction_data : json data linked to reaction
    Return
        trigger id
    """
    trigger = {
        "job_id": job_id,
        "action": {
            "id": action_id,
            "data": action_data
        },
        "reaction": {
            "id": reaction_id,
            "data": reaction_data
        }
    }
    triggers = user["triggers"]
    if (triggers[0] == -1):
        triggers[0] = trigger
    else:
        user["triggers"].append(trigger)
    db.child("Users").child(key).set(user)
    return len(user["triggers"]) - 1

@area_trigger_bp.route("/addTrigger", methods=['POST'])
def add_trigger():
    """
    Description\n
        POST route to add a new trigger
    Json-data
        action_id = array index of actions\t
        reaction_id = array index of reactions\t
        action_data = json data of action\t
        reaction_data = json data of reaction\t
    Headers
        Authorization : Bearer token
    Return
        200 code for succes and 400 for failure
    """
    users = db.child('Users').get()
    authorization = request.headers.get("Authorization")
    token = check_token(authorization, users)
    if token == False:
        return serialize_response('Unkown user', {}, 400)
    user = get_user(authorization.split(' ')[1], users)
    data = request.json
    try:
        action_id = int(data["action_id"])
        reaction_id = int(data["reaction_id"])
        action_data = data["action_data"]
        reaction_data = data["reaction_data"]
    except:
        return serialize_response('Missing parameters', {}, 400)
    action = action_array[action_id]
    reaction = reaction_array[reaction_id]
    action_res = action[2](user.val(), action_data)
    if action_res != True:
        return serialize_response(action_res, {}, 400)
    reaction_res = reaction[1](user.val(), reaction_data)
    if reaction_res != True:
        return serialize_response(reaction_res, {}, 400)
    job_id = user.key() + str(int(time.time() * 10000))
    trigger_id = add_trigger_to_db(job_id, user.val(), user.key(), action_id, reaction_id, action_data, reaction_data)
    val = user.val()
    args = [val, trigger_id, reaction[0], job_id]
    scheduler.add_job(func=action[0], trigger="interval", seconds=action[1], args=args, max_instances=100, id=job_id)
    return serialize_response('Here you are', {}, 200)