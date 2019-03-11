from area_db import db
from area_tools import get_trigger_by_jobid, get_user
from collections import defaultdict
import requests

client_id = '5mt4b2xhw06al570zsjhdol1tbvoez'
secret_key = 'eo9wpgx9v5yyh75h49em99bvug1sv4'

def th_get_status_check(user, th_data):
	"""
	Description\n
		Check function for th action : th get status
	Parameters
		user : user dict\t
		th_data : th_data dict
	Return
		True or error message
	"""
	try:
		channel = th_data["channel"]
	except:
		return "Twitch : Missing channel"
	headers = {
		"Client-ID": client_id
	}
	url = "https://api.twitch.tv/kraken/channels/" + channel
	r = requests.get(url, headers=headers)
	if r.status_code != 200:
		return "Twitch : Invalid channel name"
	return True

def th_get_status(user, id, reaction, job_id):
	"""
	Description\n
		Action function for th service : triggers when a chosen streamer goes online
	Parameters
		user : user dict\t
		id : trigger id
		reaction : reaction function\t
		job_id : id of apscheduler job
	Return
		True or error message
	"""
	channel_name = user["triggers"][id]["action"]["data"]["channel"]
	current_user = (get_user(user["tokens"]["area"], db.child("Users").get())).val()
	trigger, trigger_id = get_trigger_by_jobid(current_user, job_id)
	headers = {
		"Client-ID": client_id
	}
	try:
		channel_id = trigger["action"]["data"]["channel_id"]
	except:
		url = "https://api.twitch.tv/kraken/channels/" + channel_name
		r = requests.get(url, headers=headers)
		data = r.json()
		channel_id = data["_id"]
		trigger["action"]["data"]["channel_id"] = channel_id
		current_user["triggers"][trigger_id] = trigger
		db.child("Users").child(user["tokens"]["area"]).set(current_user)
	headers["Accept"] = "application/vnd.twitchtv.v5+json"
	url = "https://api.twitch.tv/kraken/streams/" + str(channel_id)
	r = requests.get(url, headers=headers)
	data = r.json()
	if data["stream"] != None:
		reaction(user, id)