from flask import Blueprint, render_template, abort, Flask, redirect, url_for, session, request
from flask_oauthlib.client import OAuth
from area_db import db
from area_tools import check_token
import time
import atexit
import requests
import json

SECRET_KEY = 'development key'
DEBUG = True
CR_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjM4NiwiaWRlbiI6IjI0MzgyMTE0NTIxNDY4MTA4OSIsIm1kIjp7fSwidHMiOjE1NTExOTM3NDExNTl9.U1n33CUb84o66rvlvO_onApYP6zfwEhxrn5d1mvwvbE"

def cr_clan_score_check(user, cr_data):
	"""
	Description\n
		Check function for CR action : clan score
	Parameters
		user : user dict\t
		cr_data : cr_data dict
	Return
		True or error message
	"""
	try:
		clan_tag = cr_data["clan_tag"]
		score = int(cr_data["score"])
	except:
		return "Clash Royale : Missing tag or score"
	url = "https://api.royaleapi.com/clan/" + clan_tag
	headers = {
		'auth': CR_KEY
	}
	response = requests.request("GET", url, headers=headers)
	if response.status_code != 200:
		return "Clash Royale : Invalid tag"
	if response.json()["score"] > score:
		return "Clash Royale : The score should be higher than the actual one"
	return True

def cr_record_check(user, cr_data):
	"""
	Description\n
		Check function for CR action : clash royale record
	Parameters
		user : user dict\t
		cr_data : cr_data dict
	Return
		True or error message
	"""
	try:
		tag = cr_data["tag"]
		record = int(cr_data["record"])
	except:
		return "Clash Royale : Missing tag or record"
	url = "https://api.royaleapi.com/player/" + tag
	headers = {
		'auth': CR_KEY
	}
	response = requests.request("GET", url, headers=headers)
	if response.status_code != 200:
		return "Clash Royale : Invalid player tag"
	if response.json()["trophies"] > record:
		return "Clash Royale : Your record should be higher than your actual trophies"
	return True

def cr_above_friend_check(user, cr_data):
	"""
	Description\n
		check function for CR action : above friend
	Parameters
		user : user dict\t
		cr_data : cr_data dict
	Return
		True or error message
	"""
	try:
		tag = cr_data["tag"]
		friend_tag = cr_data["friend_tag"]
	except:
		return "Clash Royale : Missing tag or friend tag"
	url = "https://api.royaleapi.com/player/" + tag + "," + friend_tag
	headers = {
		'auth': CR_KEY
	}
	response = requests.request("GET", url, headers=headers)
	if response.status_code != 200:
		return "Clash Royale : Invalid tag or friend tag"
	if response.json()[0]["trophies"] > response.json()[1]["trophies"]:
		return "Clash Royale : Your trophies shouldn't be higher than your friend one"
	return True

def cr_record(user, id, reaction, job_id):
	"""
	Description\n
		Action function for CR service : triggers when you hit a chosen trophies record
	Parameters
		user : user dict\t
		id : trigger id
		reaction : reaction function\t
		job_id : id of apscheduler job
	Return
		True or error message
	"""
	tag = user["triggers"][id]["action"]["data"]["tag"]
	url = "https://api.royaleapi.com/player/" + tag
	headers = {
		'auth': CR_KEY
	}
	response = requests.request("GET", url, headers=headers)
	data = response.json()
	trophies = data["trophies"]
	record = user["triggers"][id]["action"]["data"]["record"]
	if trophies >= int(record):
		reaction(user, id)

def cr_above_friend(user, id, reaction, job_id):
	"""
	Description\n
		Action function for CR service : triggers when you pass beyond a friend
	Parameters
		user : user dict\t
		id : trigger id
		reaction : reaction function\t
		job_id : id of apscheduler job
	Return
		True or error message
	"""
	tag = user["triggers"][id]["action"]["data"]["tag"]
	friend_tag = user["triggers"][id]["action"]["data"]["friend_tag"]
	url = "https://api.royaleapi.com/player/" + tag + "," + friend_tag
	headers = {
		'auth' : CR_KEY
	}
	response = requests.request("GET", url, headers=headers)
	data = response.json()
	trophies = data[0]["trophies"]
	friend_trophies = data[1]["trophies"]
	if trophies > friend_trophies:
		reaction(user, id)

def cr_clan_score(user, id, reaction, job_id):
	"""
	Description\n
		Action function for CR service : triggers when the clan hits a chosen score
	Parameters
		user : user dict\t
		id : trigger id
		reaction : reaction function\t
		job_id : id of apscheduler job
	Return
		True or error message
	"""
	clan_tag = user["triggers"][id]["action"]["data"]["clan_tag"]
	url = "https://api.royaleapi.com/clan/" + clan_tag
	headers = {
		'auth': CR_KEY
	}
	response = requests.request("GET", url, headers=headers)
	data = response.json()
	score = data["score"]
	goal = user["triggers"][id]["action"]["data"]["goal"]
	if score >= int(goal):
		reaction(user, id)