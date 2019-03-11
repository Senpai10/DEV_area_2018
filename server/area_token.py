from flask import Blueprint, request
from area_db import db
from area_tools import serialize_response, check_token

area_token_bp = Blueprint('area_token_bp', __name__, template_folder="templates")

@area_token_bp.route('/addTwitterToken', methods=['POST'])
def addTwitterToken():
    """
    Description\n
        POST route to add a twitter token to an authenticated user
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
    token = check_token(authorization, users)
    if token == False:
        return serialize_response('Unkown user', {}, 400)
    user = token
    data = request.json
    try:
        twitter_token = data["twitterToken"]
        twitter_secret = data["twitterSecret"]
    except:
        return serialize_response('Missing parameters', {}, 400)
    user["tokens"]["twitterToken"] = twitter_token
    user["tokens"]["twitterSecret"] = twitter_secret
    db.child("Users").child(user["tokens"]["area"]).set(user)
    return serialize_response('Tokens added', {}, 200)

@area_token_bp.route('/addGoogleToken', methods=['POST'])
def addGoogleToken():
    """
    Description\n
        POST route to add a google token to an authenticated user
    Json-data
        googleToken : google token from oauth\t
        googleId : google id of user
    Headers
        Authorization : Bearer token
    Return
        200 code for succes and 400 for failure
    """
    users = db.child('Users').get()
    authorization = request.headers.get("Authorization")
    token = check_token(authorization, users)
    if token == False:
        return serialize_response('Unknown user', {}, 400)
    user = token
    data = request.json
    try:
        google_token = data["googleToken"]
        google_id = data["googleId"]
    except:
        return serialize_response('Missing parameters', {}, 400)
    user["tokens"]["googleToken"] = google_token
    user["tokens"]["googleId"] = google_id
    db.child("Users").child(user["tokens"]["area"]).set(user)
    return serialize_response('Tokens added', {}, 200)