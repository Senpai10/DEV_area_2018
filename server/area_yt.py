from flask import Blueprint, render_template, abort, Flask, redirect, url_for, session, request
from area_db import db
from area_tools import check_token, get_user, get_trigger_by_jobid
import requests
import json


def yt_subscribers_record_check(user, yt_data):
	"""
	Description\n
		Check function for yt action: yt subscribers record
	Parameters
		user : user dict\t
		wh_data : wh_data dict
	Return
		True or error message
	"""
	try:
		channel = yt_data["channel"]
		subs = int(yt_data["subscribers"])
		token = user["tokens"]["googleToken"]
	except:
		return "YoutubeService : Missing channel or subscribers value or you are not connected to google."
	url = "https://www.googleapis.com/youtube/v3/channels?part=statistics,status&forUsername=" + channel
	headers = {
		'authorization': 'Bearer ' + user["tokens"]["googleToken"]
	}
	response = requests.request("GET", url, headers=headers)
	data = response.json()
	try :
		value = data["items"][0]["statistics"]["subscriberCount"]
	except:
		return "YoutubeService : invalid channel"
	if response.status_code != 200:
		return "YoutubeService : invalid channel"
	if int(value) > int(subs):
		return "YoutubeService : subscriberCount already reached"
	return True

def yt_subscribers_record(user, id, reaction, job_id):
	"""
	Description\n
		Action function for yt service : triggers when a given number of subscribers is hit
	Parameters
		user : user dict\t
		id : trigger id
		reaction : reaction function\t
		job_id : id of apscheduler job
	"""
	channel = user["triggers"][id]["action"]["data"]["channel"]
	subs = user["triggers"][id]["action"]["data"]["subscribers"]
	url = "https://www.googleapis.com/youtube/v3/channels?part=statistics,status&forUsername=" + channel
	headers = {
		'authorization': 'Bearer ' + user["tokens"]["googleToken"]
	}
	response = requests.request("GET", url, headers=headers)
	data = response.json()
	value = data["items"][0]["statistics"]["subscriberCount"]
	if int(value) >= int(subs):
		reaction(user, id)

def yt_subscribers_above_channel_check(user, yt_data):
	"""
	Description\n
		Check function for yt action: yt subscribers above channel
	Parameters
		user : user dict\t
		yt_data : yt_data dict
	Return
		True or error message
	"""
	try:
		channel = yt_data["channel"]
		friend_channel = yt_data["friend_channel"]
		token = user["tokens"]["googleToken"]
	except:
		return "YoutubeService : Missing channel or subscribers value"
	url = "https://www.googleapis.com/youtube/v3/channels?part=statistics,status&forUsername=" + channel
	headers = {
		'authorization': 'Bearer ' + user["tokens"]["googleToken"]
	}
	response = requests.request("GET", url, headers=headers)
	data = response.json()
	try :
		value = data["items"][0]["statistics"]["subscriberCount"]
	except:
		return "YoutubeService : invalid channel"
	if response.status_code != 200:
		return "YoutubeService : invalid channel"

	url = "https://www.googleapis.com/youtube/v3/channels?part=statistics,status&forUsername=" + friend_channel
	headers = {
		'authorization': 'Bearer ' + user["tokens"]["googleToken"]
	}
	response = requests.request("GET", url, headers=headers)
	data = response.json()
	try :
		value2 = data["items"][0]["statistics"]["subscriberCount"]
	except:
		return "YoutubeService : invalid channel"
	if response.status_code != 200:
		return "YoutubeService : invalid channel"

	if int(value) > int(value2):
		return "YoutubeService : channel already above friends channel"
	return True

def yt_subscribers_above_channel(user, id, reaction, job_id):
	"""
	Description\n
		Action function for yt service : triggers when a chosen channel gets more subscribers than another chosen one
	Parameters
		user : user dict\t
		id : trigger id
		reaction : reaction function\t
		job_id : id of apscheduler job
	"""
	channel = user["triggers"][id]["action"]["data"]["channel"]
	friend_channel = user["triggers"][id]["action"]["data"]["friend_channel"]
	url = "https://www.googleapis.com/youtube/v3/channels?part=statistics,status&forUsername=" + channel
	headers = {
		'authorization': 'Bearer ' + user["tokens"]["googleToken"]
	}
	response = requests.request("GET", url, headers=headers)
	data = response.json()
	value = data["items"][0]["statistics"]["subscriberCount"]

	url = "https://www.googleapis.com/youtube/v3/channels?part=statistics,status&forUsername=" + friend_channel
	headers = {
		'authorization': 'Bearer ' + user["tokens"]["googleToken"]
	}
	response = requests.request("GET", url, headers=headers)
	data = response.json()
	friend_value = data["items"][0]["statistics"]["subscriberCount"]
	if int(value) >= int(friend_value):
		reaction(user, id)

def yt_new_video_check(user, yt_data):
	"""
	Description\n
		Check function for yt action: yt new video
	Parameters
		user : user dict\t
		yt_data : yt_data dict
	Return
		True or error message
	"""
	try:
		channel = yt_data["channel"]
		token = user["tokens"]["googleToken"]
	except:
		return "YoutubeService : Missing channel"
	url = "https://www.googleapis.com/youtube/v3/channels?part=statistics,status&forUsername=" + channel
	headers = {
		'authorization': 'Bearer ' + user["tokens"]["googleToken"]
	}
	response = requests.request("GET", url, headers=headers)
	data = response.json()
	try :
		value = data["items"][0]["statistics"]["videoCount"]
	except:
		return "YoutubeService : invalid channel"
	if response.status_code != 200:
		return "YoutubeService : invalid channel"
	return True

def yt_new_video(user, id, reaction, job_id):
	"""
	Description\n
		Action function for yt service : triggers when new video from a chosen channel comes out
	Parameters
		user : user dict\t
		id : trigger id
		reaction : reaction function\t
		job_id : id of apscheduler job
	"""
	channel = user["triggers"][id]["action"]["data"]["channel"]
	url = "https://www.googleapis.com/youtube/v3/channels?part=statistics,status&forUsername=" + channel
	headers = {
		'authorization': 'Bearer ' + user["tokens"]["googleToken"]
	}
	response = requests.request("GET", url, headers=headers)
	data = response.json()
	value = data["items"][0]["statistics"]["videoCount"]
	current_user = (get_user(user["tokens"]["area"], db.child("Users").get())).val()
	trigger, trigger_id = get_trigger_by_jobid(current_user, job_id)
	try:
		video_count = trigger["action"]["data"]["video_count"]
		if (int(video_count) < int(value)):
			trigger["action"]["data"]["video_count"] = value
			current_user["triggers"][trigger_id] = trigger
			db.child("Users").child(user["tokens"]["area"]).set(current_user)
			reaction(current_user, id)
	except Exception as e:
		print(e)
		trigger["action"]["data"].update(video_count = value)
		current_user["triggers"][trigger_id] = trigger
		db.child("Users").child(user["tokens"]["area"]).set(current_user)