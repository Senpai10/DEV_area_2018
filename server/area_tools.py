from flask import make_response, jsonify

def serialize_response(message, data, code):
    """
    Description\n
        Tool function to serialize the server reponses
    Parameters
        message : message to send to client\t
        data : data to send to client\t
        code : http response code to send to user\t
    Return
        serialized response
    """
    response = {
        "msg": message,
        "data": data
    }
    response = jsonify(response)
    return make_response(response, code)

def check_token(authorization, users):
    """
    Description\n
        Tool function to check authentication token
    Parameters
        authorization : authorization code\t
        users : database of users
    Return
        auhtenticated user in case of succes, False in case of failure 
    """
    if authorization == None or authorization.startswith("Bearer") == False:
        return False
    token = authorization.split(' ')[1]
    user = ["tmp"]
    for user_json in users.each():
        if user_json.key() == token:
            user = user_json.val()
            break
    if user == ["tmp"]:
        return False
    return user

def get_user(token, users):
    """
    Description\n
       Tool function to get user from a token
    Parameters
        token : authentication token of user\t
        users : database of users
    Return
        authenticated user
    """
    user = ["tmp"]
    for user_json in users.each():
        if user_json.key() == token:
            return user_json

def get_trigger_by_jobid(user, jobid):
    """
    Description\n
       Tool function to get trigger by jobid
    Parameters
        user : authenticated user\t
        jobid : apscheduler jobid
    Return
        if found, the trigger corresponding to the jobid, false otherwise
    """
    i = 0
    for trigger in user["triggers"]:
        if trigger["job_id"] == jobid:
            return trigger, i
        i = i + 1
    return False