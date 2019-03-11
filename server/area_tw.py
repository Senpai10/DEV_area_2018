from area_db import db
from requests_oauthlib import OAuth1
from urllib import parse
import requests

cons_key = 'sii8TAx8gZxNNuoRzzrievfG5'
cons_secret = 'wCreM120FVL5b4YVfbu68gdQQA7WH3HSelat6YMGfgu2dM2aR8'

def get_friend_followers(user, friend_name):
	url = "https://api.twitter.com/1.1/users/show.json"
	auth = OAuth1(cons_key, cons_secret, user["tokens"]["twitterToken"], user["tokens"]["twitterSecret"])
	params = [('screen_name', friend_name)]
	r = requests.get(url, auth=auth, params=params)
	data = r.json()
	return False if r.status_code != 200 else data["followers_count"]

def get_tw_id(name, user):
	url = "https://api.twitter.com/1.1/users/show.json"
	params = [('screen_name', name)]
	auth = OAuth1(cons_key, cons_secret, user["tokens"]["twitterToken"], user["tokens"]["twitterSecret"])
	r = requests.get(url, auth=auth, params=params)
	data = r.json()
	return data["id"]

def tw_send_dm_check(user, tw_data):
	"""
	Description\n
		Check function for tw reaction: tw send dm
	Parameters
		user : user dict\t
		tw_data : tw_data dict
	Return
		True or error message
	"""
	try:
		token = user["tokens"]["twitterToken"]
		secret = user["tokens"]["twitterSecret"]
	except:
		return "Twitter : You're not connected"
	try:
		message = tw_data["msg"]
		dest = tw_data["dest"]
	except:
		return "Twitter : Missing message or recipient"
	url = "https://api.twitter.com/1.1/users/show.json"
	params=[('screen_name', dest)]
	auth = OAuth1(cons_key, cons_secret, user["tokens"]["twitterToken"], user["tokens"]["twitterSecret"])
	r = requests.get(url, auth=auth, params=params)
	if r.status_code != 200:
		return "Twitter : Invalid twitter screen name"
	return True

def tw_send_dm(user, id):
	"""
	Description\n
		Reaction function for tw service : sends a direct message to given user
	Parameters
		user : user dict\t
		id : trigger id
	"""
	message = user["triggers"][id]["reaction"]["data"]["msg"]
	dest = get_tw_id(user["triggers"][id]["reaction"]["data"]["dest"], user)
	url = "https://api.twitter.com/1.1/direct_messages/events/new.json"
	auth = OAuth1(cons_key, cons_secret, user["tokens"]["twitterToken"], user["tokens"]["twitterSecret"])
	json = {
		"event": {
			"type": "message_create",
			"message_create": {
				"target": {
					"recipient_id": dest
				},
				"message_data": {
					"text": message
				}
			}
		}
	}
	r = requests.post(url, auth=auth, json=json)

def tw_post_tweet_check(user, tw_data):
	"""
	Description\n
		Check function for tw reaction: tw post tweet
	Parameters
		user : user dict\t
		tw_data : tw_data dict
	Return
		True or error message
	"""
	try:
		token = user["tokens"]["twitterToken"]
		secret = user["tokens"]["twitterSecret"]
	except:
		return "Twitter : You're not connected"
	try:
		msg = tw_data["msg"]
	except:
		return "Twitter : Missing tweet content"
	if len(msg) > 0:
		return True
	return "Twitter : Empty tweet can't be published"

def tw_post_tweet(user, id):
	"""
	Description\n
		Reaction function for tw service : tweet through authenticated user
	Parameters
		user : user dict\t
		id : trigger id
	"""
	message = user["triggers"][id]["reaction"]["data"]["msg"]
	url = "https://api.twitter.com/1.1/statuses/update.json"
	params=[('status', message)]
	auth = OAuth1(cons_key, cons_secret, user["tokens"]["twitterToken"], user["tokens"]["twitterSecret"])
	r = requests.post(url, auth=auth, params=params)

def tw_reach_followers_check(user, tw_data):
	"""
	Description\n
		Check function for tw action: tw reach followers
	Parameters
		user : user dict\t
		tw_data : tw_data dict
	Return
		True or error message
	"""
	try:
		token = user["tokens"]["twitterToken"]
		secret = user["tokens"]["twitterSecret"]
	except:
		return "Twitter : You're not connected"
	try:
		followers = tw_data["followers"]
	except:
		return "Twitter : Missing followers count"
	url = "https://api.twitter.com/1.1/account/verify_credentials.json"
	auth = OAuth1(cons_key, cons_secret, user["tokens"]["twitterToken"], user["tokens"]["twitterSecret"])
	r = requests.get(url, auth=auth)
	if r.status_code != 200:
		return "Twitter : Can't identify you"
	data = r.json()
	if data["followers_count"] >= int(followers):
		return "Twitter : Followers count already reached"
	return True

def tw_reach_followers(user, id, reaction, job_id):
	"""
	Description\n
		Action function for tw service : triggers when a chosen followers number is hit
	Parameters
		user : user dict\t
		id : trigger id
		reaction : reaction function\t
		job_id : id of apscheduler job
	"""
	followers = user["triggers"][id]["action"]["data"]["followers"]
	url = "https://api.twitter.com/1.1/account/verify_credentials.json"
	auth = OAuth1(cons_key, cons_secret, user["tokens"]["twitterToken"], user["tokens"]["twitterSecret"])
	r = requests.get(url, auth=auth)
	data = r.json()
	if data["followers_count"] >= int(followers):
		reaction(user, id)

def tw_reach_friend_followers_check(user, tw_data):
	"""
	Description\n
		Check function for tw action: tw reach friend followers
	Parameters
		user : user dict\t
		tw_data : tw_data dict
	Return
		True or error message
	"""
	try:
		token = user["tokens"]["twitterToken"]
		secret = user["tokens"]["twitterSecret"]
	except:
		return "Twitter : You're not connected"
	try:
		friend_name = tw_data["friendName"]
	except:
		return "Twitter : Missing friend's name"
	friend_followers = get_friend_followers(user, friend_name)
	if friend_followers == False:
		return "Twitter : Invalid friend name"
	url = "https://api.twitter.com/1.1/account/verify_credentials.json"
	auth = OAuth1(cons_key, cons_secret, user["tokens"]["twitterToken"], user["tokens"]["twitterSecret"])
	r = requests.get(url, auth=auth)
	if r.status_code != 200:
		return "Twitter : Can't identify you"
	data = r.json()
	if data["followers_count"] >= friend_followers:
		return "Twitter : Followers count already reached"
	return True

def tw_reach_friend_followers(user, id, reaction, job_id):
	"""
	Description\n
		Action function for tw service : triggers when you have more followers than a chosen user
	Parameters
		user : user dict\t
		id : trigger id
		reaction : reaction function\t
		job_id : id of apscheduler job
	"""
	friend_name = user["triggers"][id]["action"]["data"]["friendName"]
	url = "https://api.twitter.com/1.1/account/verify_credentials.json"
	auth = OAuth1(cons_key, cons_secret, user["tokens"]["twitterToken"], user["tokens"]["twitterSecret"])
	r = requests.get(url, auth=auth)
	data = r.json()
	followers = data["followers_count"]
	friend_followers = get_friend_followers(user, friend_name)
	if followers >= friend_followers:
		reaction(user, id)