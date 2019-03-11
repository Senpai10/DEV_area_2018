from flask import Blueprint, render_template, abort, Flask, redirect, url_for, session, request
from area_db import db
from area_tools import check_token
import requests
import json

WH_KEY = "129cc3031e2449e2a24160950190503"

def weather_alert_hot_check(user, wh_data):
	"""
	Description\n
		Check function for wh action: weather alert hot
	Parameters
		user : user dict\t
		wh_data : wh_data dict
	Return
		True or error message
	"""
	try:
		city = wh_data["city"]
		temperature = int(wh_data["temperature"])
	except:
		return "WeatherService : Missing city or temperature value"
	url = "https://api.apixu.com/v1/current.json?key=" + WH_KEY + "&q=" + city
	response = requests.request("GET", url)
	data = response.json()
	try :
		value = data["current"]["temp_c"]
	except:
		return "WeatherService : Invalid city or temperature"
	if response.status_code != 200:
		return "WeatherService : Invalid city or temperature"
	if temperature < value:
		return "WeatherService : The temperature has already been reached"
	return True

def weather_alert_hot(user, id, reaction, job_id):
	"""
	Description\n
		Action function for weather service : triggers a chosen temperature of a chosen city gets too high
	Parameters
		user : user dict\t
		id : trigger id
		reaction : reaction function\t
		job_id : id of apscheduler job
	"""
	city = user["triggers"][id]["action"]["data"]["city"]
	temperature = user["triggers"][id]["action"]["data"]["temperature"]
	url = "https://api.apixu.com/v1/current.json?key=" + WH_KEY + "&q=" + city
	response = requests.request("GET", url)
	data = response.json()
	value = data["current"]["temp_c"]
	if value >= int(temperature):
		reaction(user, id)

def weather_alert_cold_check(user, wh_data):
	"""
	Description\n
		Check function for wh action: weather alert cold
	Parameters
		user : user dict\t
		wh_data : wh_data dict
	Return
		True or error message
	"""
	try:
		city = wh_data["city"]
		temperature = int(wh_data["temperature"])
	except:
		return "WeatherService : Missing city or temperature value"
	url = "https://api.apixu.com/v1/current.json?key=" + WH_KEY + "&q=" + city
	response = requests.request("GET", url)
	data = response.json()
	try :
		value = data["current"]["temp_c"]
	except:
		return "WeatherService : invalid City or temperature"
	if response.status_code != 200:
		return "WeatherService : Invalid City or temperature"
	if temperature > value:
		return "WeatherService : The temperature has already been reached"
	return True

def weather_alert_cold(user, id, reaction, job_id):
	"""
	Description\n
		Action function for weather service : triggers a chosen temperature of a chosen city gets too coold
	Parameters
		user : user dict\t
		id : trigger id
		reaction : reaction function\t
		job_id : id of apscheduler job
	"""
	city = user["triggers"][id]["action"]["data"]["city"]
	temperature = user["triggers"][id]["action"]["data"]["temperature"]
	url = "https://api.apixu.com/v1/current.json?key=" + WH_KEY + "&q=" + city
	response = requests.request("GET", url)
	data = response.json()
	value = data["current"]["temp_c"]
	if value <= int(temperature):
		reaction(user, id)