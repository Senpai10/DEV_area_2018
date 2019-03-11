from flask import Blueprint, render_template, abort, Flask, redirect, url_for, session, request
from area_db import db
from area_tools import check_token, get_user, get_trigger_by_jobid
from bs4 import BeautifulSoup
import requests
import json

def lol_bestof_check(user, lol_data):
	"""
	Description\n
		Check function for LoL action : lol bestof
	Parameters
		user : user dict\t
		lol_data : lol_data dict
	Return
		True or error message
	"""
	try:
		summoner = lol_data["summoner"]
		region = lol_data["region"]
	except:
		return "LeagueOfLegends : missing summoner name or region"
	url = "https://lolprofile.net/fr/summoner/" + region + "/" + summoner
	response = requests.request("GET", url)
	if response.status_code != 200:
		return "LeagueOfLegends : Bad summoner_name or region"
	return True

def lol_bestof(user, id, reaction, job_id):
	"""
	Description\n
		Action function for LoL service : triggers when a player is in a bestof for a rankup
	Parameters
		user : user dict\t
		id : trigger id\t
		reaction : reaction function\t
		job_id : id of apscheduler job\t
	Return
		True or error message
	"""
	summoner_name = user["triggers"][id]["action"]["data"]["summoner"]
	region_name = user["triggers"][id]["action"]["data"]["region"]
	url = "https://lolprofile.net/fr/summoner/" + region_name + "/" + summoner_name
	response = requests.request("GET", url)
	page_content = BeautifulSoup(response.content, "html.parser")
	div_tag = page_content.find("span", {"class" : "lp"}).getText()
	current_lp = div_tag.split("/ ")[1].replace(" LP", "")
	if int(current_lp) >= 100:
		reaction(user, id)

def lol_levelup_check(user, lol_data):
	"""
	Description\n
		Check function for LoL action : lol levelup
	Parameters
		user : user dict\t
		lol_data : lol_data dict
	Return
		True or error message
	"""
	try:
		summoner = lol_data["summoner"]
		region = lol_data["region"]
	except:
		return "LeagueOfLegends : missing summoner name and region"
	url = "https://lolprofile.net/fr/summoner/" + region + "/" + summoner
	response = requests.request("GET", url)
	if response.status_code != 200:
		return "LeagueOfLegends : Bad summoner_name or region"
	return True

def lol_levelup(user, id, reaction, job_id):
	"""
	Description\n
		Action function for LoL service : triggers when a player levels up
	Parameters
		user : user dict\t
		id : trigger id
		reaction : reaction function
		job_id : id of apscheduler job
	Return
		True or error message
	"""
	summoner_name = user["triggers"][id]["action"]["data"]["summoner"]
	region_name = user["triggers"][id]["action"]["data"]["region"]
	url = "https://lolprofile.net/fr/summoner/" + region_name + "/" + summoner_name
	response = requests.request("GET", url)
	page_content = BeautifulSoup(response.content, "html.parser")
	div_tag = page_content.find("div", {"class" : "s-icon mhide"})
	current_level = div_tag.find("div")
	current_level = str(current_level).replace("<div>Niveau ", "").replace("</div>", "")
	current_user = (get_user(user["tokens"]["area"], db.child("Users").get())).val()
	trigger, trigger_id = get_trigger_by_jobid(current_user, job_id)
	try:
		level = trigger["action"]["data"]["level"]
		if (level != current_level):
			trigger["action"]["data"]["level"] = current_level
			current_user["triggers"][trigger_id] = trigger
			db.child("Users").child(user["tokens"]["area"]).set(current_user)
			reaction(current_user, id)
	except Exception as e:
		print(e)
		trigger["action"]["data"].update(level = current_level)
		current_user["triggers"][trigger_id] = trigger
		db.child("Users").child(user["tokens"]["area"]).set(current_user)