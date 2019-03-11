from flask import Flask, request, make_response, jsonify
from area_db import db, fire_user
from area_tools import serialize_response, check_token
from werkzeug.security import generate_password_hash, check_password_hash
from area_token import area_token_bp
from area_trigger import area_trigger_bp
from flask_mail import Message, Mail
import time
import json
from flask_cors import CORS

app = Flask(__name__)
app.register_blueprint(area_trigger_bp)
app.secret_key = b'_5#y2L"FmQ8z\n\xec]/'
CORS(app)
mail_settings = {
    "MAIL_SERVER": 'smtp.gmail.com',
    "MAIL_PORT": 465,
    "MAIL_USE_TLS": False,
    "MAIL_USE_SSL": True,
    "MAIL_USERNAME": "areaepitech31@gmail.com",
    "MAIL_PASSWORD": "password2019"
}
app.config.update(mail_settings)
mail = Mail()
mail.init_app(app)

@app.after_request
def after_request(response):
  response.headers.add('Access-Control-Allow-Origin', '*')
  response.headers.add('Access-Control-Allow-Headers', 'Content-Type,Authorization')
  response.headers.add('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE,OPTIONS')
  response.headers.add('Access-Control-Allow-Credentials', 'true')
  return response

@app.route("/about.json", methods=['GET'])
def about():
    response = {
        "client": {
            "host": request.remote_addr
        },
        "server": {
            "current_time": int(time.time()),
            "services": [{
                "name": "Clash Royale",
                "actions": [{
                    "name": "record",
                    "description": "The user has reached a defined score."
                }, {
                    "name": "above_friend",
                    "description": "The user has passed beyond his friend's record."
                }, {
                    "name": "clan_score",
                    "description": "The clan score has reached a defined score."
                }],
                "reactions": [{}]
            }, {
                "name": "CryptoService",
                "actions": [{
                    "name": "crypto_value",
                    "description": "The defined cryptocurrency has reached a defined value."
                }],
                "reactions": [{}]
            }, {
                "name": "Github",
                "actions": [{
                    "name": "repo_commit",
                    "description": "Data has been commited on a defined repository."
                }, {
                    "name": "repo_stars",
                    "description": "The repository has reached X stars."
                }],
                "reactions": [{}]
            }, {
                "name": "Twitch",
                "actions": [{
                    "name": "get_live_status",
                    "description": "The channel is streaming."
                }],
                "reactions": [{}]
            }, {
                "name": "WeatherService",
                "actions": [{
                    "name": "hot_temperature",
                    "description": "The temparture in C is beyond X temperature."
                }, {
                    "name": "cold_temperature",
                    "description": "The temparture in C is below X temperature."
                }],
                "reactions": [{}]
            }, {
                "name": "Youtube",
                "actions": [{
                    "name": "sub_record",
                    "description": "The channel C has reached X subscribers."
                }, {
                    "name": "friend_sub",
                    "description": "The channel C has now more subscribers than channel H."
                }],
                "reactions": [{}]
            }, {
                "name": "MailService",
                "actions": [{}],
                "reactions": [{
                    "name": "send_mail",
                    "description": "The user receive a mail from areaepitech31@gmail.com."
                }]
            }, {
                "name": "League of Legends",
                "actions": [{
                    "name": "bestof",
                    "description": "The user has reached bestof in order to rank up."
                }, {
                    "name": "levelup",
                    "description": "The user has leveled up."
                }],
                "reactions": [{}]
            }, {
                "name": "Twitter",
                "actions": [{
                    "name": "followers_record",
                    "description": "The user has reached X followers."
                }, {
                    "name": "friend_followers",
                    "description": "The user has now more followers than an other twitto."
                }],
                "reactions": [{
                    "name": "post_tweet",
                    "description": "Post a tweet with predefined content."
                }, {
                    "name": "send_dm",
                    "description": "Send direct message to a predefined twitto."
                }]
            }]
        }
    }
    return make_response(jsonify(response))

@app.route("/register", methods=['POST'])
def register():
    data = request.json
    try:
        firstname = data['firstName']
        lastname = data['lastName']
        email = data['email']
        password = data['password']
    except:
        return serialize_response('Missing parameters', {}, 400)
    password = generate_password_hash(password)

    users = db.child('Users').get()
    for user in users.each():
        each_user = user.val()
        if each_user['email'] == email:
            return serialize_response('Email already in use', {}, 400)
    new_user = {
        "email": email,
        "password": password,
        "firstName": firstname,
        "lastName": lastname,
        "tokens": {
            "area": "t"
        },
        "triggers": [-1]
    }
    db.child("Users").push(new_user, fire_user['idToken'])
    users = db.child("Users").get()
    for user in users.each():
            each_user = user.val()
            if each_user['email'] == email:
                each_user['tokens']['area'] = user.key()
                db.child("Users").child(user.key()).set(each_user)
                return serialize_response('User added', {}, 200)
    return serialize_response('Error in request', {}, 400)

@app.route("/login", methods=['POST'])
def login():
    data = request.json
    try:
        email = data['email']
        password = data['password']
    except:
        return serialize_response('Missing parameters', {}, 400)
    users = db.child('Users').get()
    for user in users.each():
        each_user = user.val()
        if each_user['email'] == email:
            if check_password_hash(each_user['password'], password):
                return serialize_response('Here is the key', {"key" : user.key()}, 200)
            return serialize_response('Bad password', {}, 400)
    return serialize_response('Unknown user', {}, 400)

@app.route("/", methods=['GET'])
def index():
    return serialize_response('Nothing to see here', {}, 200)

@app.route("/sendmail", methods=['GET'])
def sendmail():
    recipient = request.args.get('recipient')
    message = request.args.get('message')
    msg = Message(sender="areaepitech31@gmail.com", recipients=[recipient], body=message)
    mail.send(msg)
    return serialize_response('Mail sent', {}, 200)

app.register_blueprint(area_token_bp)

app.run(host="0.0.0.0", debug=True, port=8080)