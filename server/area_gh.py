from flask import Blueprint, render_template, abort, Flask, redirect, url_for, session, request
from area_db import db
from area_tools import check_token, get_user, get_trigger_by_jobid
import requests
import json

client_id = "d6ee1871e482bf2d6aee"
client_secret = "b727434fae60eb1216eca56fbe0a89bae6bfdc34"

def github_commit_check(user, gh_data):
	"""
	Description\n
		Check function for GH action : github commit
	Parameters
		user : user dict\t
		gh_data : gh_data dict
	Return
		True or error message
	"""
	try:
		repo = gh_data["repo"]
		owner = gh_data["owner"]
	except:
		return "Github : Missing repository or owner"
	params = [('client_id', client_id), ('client_secret', client_secret)]
	url = "https://api.github.com/repos/" + owner + "/" + repo + "/commits"
	response = requests.request("GET", url, params=params)
	if response.status_code != 200:
		return "Github : Bad repository or owner"
	return True

def github_commit(user, id, reaction, job_id):
	"""
	Description\n
		Action function for GH service : triggers when a commit is made on a chosen repository from a chosen owner
	Parameters
		user : user dict\t
		id : trigger id
		reaction : reaction function\t
		job_id : id of apscheduler job
	"""
	repo = user["triggers"][id]["action"]["data"]["repo"]
	owner = user["triggers"][id]["action"]["data"]["owner"]
	params = [('client_id', client_id), ('client_secret', client_secret)]
	url = "https://api.github.com/repos/" + owner + "/" + repo + "/commits"
	response = requests.request("GET", url, params=params)
	data = response.json()
	request_commit = data[0]["sha"]
	current_user = (get_user(user["tokens"]["area"], db.child("Users").get())).val()
	trigger, trigger_id = get_trigger_by_jobid(current_user, job_id)
	try:
		last_commit = trigger["action"]["data"]["last_commit"]
		print ("the sha is " + str(request_commit) + " , last commit is " + str(last_commit))
		if (last_commit != request_commit):
			trigger["action"]["data"]["last_commit"] = request_commit
			current_user["triggers"][trigger_id] = trigger
			db.child("Users").child(user["tokens"]["area"]).set(current_user)
			reaction(current_user, id)
	except Exception as e:
		print(e)
		trigger["action"]["data"].update(last_commit = request_commit)
		current_user["triggers"][trigger_id] = trigger
		db.child("Users").child(user["tokens"]["area"]).set(current_user)

def github_stars_check(user, gh_data):
	"""
	Description\n
		Check function for GH action : github stars
	Parameters
		user : user dict\t
		gh_data : gh_data dict
	Return
		True or error message
	"""
	try:
		repo = gh_data["repo"]
		owner = gh_data["owner"]
		stars = gh_data["stars"]
	except:
		return "Github : Missing repository name, owner or stars objective"
	params = [('client_id', client_id), ('client_secret', client_secret)]
	url = "https://api.github.com/repos/" + owner + "/" + repo
	response = requests.request("GET", url, params=params)
	data = response.json()
	if response.status_code != 200:
		return "Github : Invalid repository name or owner"
	if data["stargazers_count"] >= int(stars):
		return "Github : Stars objective already reached"
	return True

def github_stars(user, id, reaction, job_id):
	"""
	Description\n
		Action function for GH service : triggers when a chosen value of stars is hit on a repository
	Parameters
		user : user dict\t
		id : trigger id
		reaction : reaction function\t
		job_id : id of apscheduler job
	"""
	repo = user["triggers"][id]["action"]["data"]["repo"]
	owner = user["triggers"][id]["action"]["data"]["owner"]
	stars = user["triggers"][id]["action"]["data"]["stars"]
	params = [('client_id', client_id), ('client_secret', client_secret)]
	url = "https://api.github.com/repos/" + owner + "/" + repo
	response = requests.request("GET", url, params=params)
	data = response.json()
	if data["stargazers_count"] >= int(stars):
		reaction(user, id)