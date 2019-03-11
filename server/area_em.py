from flask import Blueprint, render_template, abort, Flask, redirect, url_for, session, request, current_app
from area_db import db
from area_tools import check_token
import requests
import json

def send_mail_check(user, em_data):
	"""
	Description\n
		Check function for CR reaction : send mail
	Parameters
		user : user dict\t
		em_data : em_data dict
	Return
		True or error message
	"""
	try:
		recipient = em_data["recipient"]
		message = em_data["message"]
	except:
		return False
	return True

def send_mail(user, id):
	"""
	Description\n
		Reaction function for EM service : sends an email to a chosen recipient
	Parameters
		user : user dict\t
		id : trigger id
	"""
	recipient = user["triggers"][id]["reaction"]["data"]["recipient"]
	message = user["triggers"][id]["reaction"]["data"]["message"]
	url = "http://localhost:8080/sendmail?recipient=" + recipient + "&message=" + message
	response = requests.request("GET", url)