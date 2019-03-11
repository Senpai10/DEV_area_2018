from flask import Blueprint, render_template, abort, Flask, redirect, url_for, session, request
from area_db import db
from area_tools import check_token
import requests
import json

CC_KEY = "a54e50605a13b86cfeb42c44e00e0527021b79ad9de24369698419099c0479ba"

def crypto_value_check(user, cm_data):
	"""
	Description\n
		Check function for CM action : crypto value
	Parameters
		user : user dict\t
		cm_data : cm_data dict
	Return
		True or error message
	"""
	try:
		symbol = cm_data["symbol"]
		value_cap = int(cm_data["value_cap"])
	except:
		return "CryptoService : Missing cryptoccurrency or value cap"
	url = "https://min-api.cryptocompare.com/data/price?fsym=" + symbol + "&tsyms=USD,JPY,EUR"
	headers = {
		'authorization': 'Apikey ' + CC_KEY
	}
	response = requests.request("GET", url, headers=headers)
	data = response.json()
	try :
		value = data["EUR"]
	except:
		return "CryptoService : Invalid cryptocurrency"
	if response.status_code != 200:
		return "CryptoService : Invalid cryptocurrency"
	if value_cap < value:
		return "CryptoService : The value cap has already been reached"
	return True

def crypto_value(user, id, reaction, job_id):
	"""
	Description\n
		Action function for Crypto service : triggers when a chosen cryptocurrency hits a chosen
	Parameters
		user : user dict\t
		id : trigger id
		reaction : reaction function\t
		job_id : id of apscheduler job
	"""
	symbol = user["triggers"][id]["action"]["data"]["symbol"]
	value_cap = user["triggers"][id]["action"]["data"]["value_cap"]
	url = "https://min-api.cryptocompare.com/data/price?fsym=" + symbol + "&tsyms=USD,JPY,EUR"
	headers = {
		'authorization': 'Apikey ' + CC_KEY
	}
	response = requests.request("GET", url, headers=headers)
	data = response.json()
	value = data["EUR"]
	if value >= int(value_cap):
		reaction(user, id)