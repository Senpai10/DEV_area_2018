var userEmail = "";
var userToken = "";

$.ajax({
	type: 'GET',
	url: 'http://localhost:8081/userToken',
	success: function(data) {
		if (data.token === "") {
			document.location.href = "/login";
		}
		userEmail = data.email;
		userToken = data.token;
		$("#userEmail").text(userEmail);
		var xhr = new XMLHttpRequest();
		var url = 'http://localhost:8080/getTriggers';
		xhr.open('GET', url, true);
		xhr.setRequestHeader("Authorization", "Bearer " + userToken);
		xhr.onreadystatechange = function () {
			if (xhr.readyState == 4) {
				if (xhr.status == 200) {
					var json = JSON.parse(xhr.responseText);
					console.log(json);
					displayTriggers(json);
				}
			}
		}
		xhr.send(null);
	}
});

function logout() {
	document.location.href = "/login";
}

function displayTriggers(json) {
	for (var i = 0; i < json.data.triggers.length; i++) {
		if (json.data.triggers[i] != -1) {
			addTrigger(i, json.data.triggers[i]);
		}
	}
}

function displayAction(actionInfos, id) {
	if (actionInfos.id === 0)
		$('#' + id + ' > .actionDesc').append('Clash Royale player ' + actionInfos.data.tag + ' exceeds ' + actionInfos.data.record + ' trophies');
	if (actionInfos.id === 1)
		$('#' + id + ' > .actionDesc').append('Clash Royale player ' + actionInfos.data.tag + ' beats friend ' + actionInfos.data.friend_tag);
	if (actionInfos.id === 2)
		$('#' + id + ' > .actionDesc').append('Clash Royale clan ' + actionInfos.data.clan_tag + ' exceeds ' + actionInfos.data.score + ' clan trophies');
	if (actionInfos.id === 3)
		$('#' + id + ' > .actionDesc').append('Check if ' + actionInfos.data.symbol + ' reaches ' + actionInfos.data.value_cap);
	if (actionInfos.id === 4)
		$('#' + id + ' > .actionDesc').append('New commit on Github repository ' + actionInfos.data.repo + ' owned by ' + actionInfos.data.owner);
	if (actionInfos.id === 5)
		$('#' + id + ' > .actionDesc').append('Twitch channel ' + actionInfos.data.channel + ' is streaming');
	if (actionInfos.id === 6)
		$('#' + id + ' > .actionDesc').append('Temperature in ' + actionInfos.data.city + ' exceeds ' + actionInfos.data.temperature + ' °C');
	if (actionInfos.id === 7)
		$('#' + id + ' > .actionDesc').append('Temperature in ' + actionInfos.data.city + ' falls behind ' + actionInfos.data.temperature + ' °C');
	if (actionInfos.id === 8)
		$('#' + id + ' > .actionDesc').append('Your Twitter account reaches ' + actionInfos.data.followers + ' followers');
	if (actionInfos.id === 9)
		$('#' + id + ' > .actionDesc').append('Your Twitter account reaches your friend ' + actionInfos.data.friendName + ' in followers');
	if (actionInfos.id === 10)
		$('#' + id + ' > .actionDesc').append('YouTube channel ' + actionInfos.data.channel + ' reaches ' + actionInfos.data.subscribers + ' subscribers');
	if (actionInfos.id === 11)
		$('#' + id + ' > .actionDesc').append('YouTube channel ' + actionInfos.data.channel + ' exceeds ' + actionInfos.data.friend_channel + ' in subscribers');
	if (actionInfos.id === 12)
		$('#' + id + ' > .actionDesc').append('YouTube channel ' + actionInfos.data.channel + ' released a new video');
	if (actionInfos.id === 13)
		$('#' + id + ' > .actionDesc').append('Github repository ' + actionInfos.data.repo + ' owned by ' + actionInfos.data.owner + ' has ' + actionInfos.data.stars + ' stars');
	if (actionInfos.id === 14)
		$('#' + id + ' > .actionDesc').append('League of Legends player ' + actionInfos.data.summoner + ' in ' + actionInfos.data.region + ' gained a level');
	if (actionInfos.id === 15)
		$('#' + id + ' > .actionDesc').append('League of Legends player ' + actionInfos.data.summoner + ' in ' + actionInfos.data.region + ' entered in a BO');
}

function displayReaction(reactionInfos, id) {
	if (reactionInfos.id === 0)
		$('#' + id + ' > .reactionDesc').append('Tweet "' + reactionInfos.data.msg + '"');
	if (reactionInfos.id === 1)
		$('#' + id + ' > .reactionDesc').append('Send Twitter DM "' + reactionInfos.data.msg + '" to ' + reactionInfos.data.dest);
	if (reactionInfos.id === 2)
		$('#' + id + ' > .reactionDesc').append('Email "' + reactionInfos.data.message + '" to ' + reactionInfos.data.recipient);
}

function addTrigger(id, triggerInfos) {
	$("#wrapper").append("<div class='triggerInfos' id='init' onclick='removeTrigger(this.id)'><span class='trash'><i class='fas fa-trash' onclick='removeTrigger(this.id)'></i></span><br><span class='actionDesc'>ACTION : </span><br><span class='reactionDesc'>REACTION : </span></div>");
	$('#init').attr('id', id);
	displayAction(triggerInfos.action, id);
	displayReaction(triggerInfos.reaction, id);
	console.log(triggerInfos);
	console.log(triggerInfos.action.data.symbol);
}

function removeTrigger(id) {
	var tId = {
		triggerId: id
	}
	var xhr = new XMLHttpRequest();
	var url = 'http://localhost:8080/rmvTriggers';
	xhr.open('POST', url, true);
	xhr.setRequestHeader("Content-type", "application/json");
	xhr.setRequestHeader("Authorization", "Bearer " + userToken);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				document.location.href = "/home_page";
			} else if (xhr.status == 400) {
				console.log(xhr.status, xhr.responseText);
				alert("error");
			}
		}
	}
	var jsontId = JSON.stringify(tId);
	console.log(jsontId);
	xhr.send(jsontId);
}
