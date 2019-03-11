function trigger_reaction(reaction_id)
{
	var my_action = JSON.parse(localStorage.getItem('myAction'));
	var my_action_id = JSON.parse(localStorage.getItem('myAction_id'));
	if (reaction_id == 0)
	{
		var single_tweet = $('#single_tweet').val();
		var reaction_info = {
			msg: single_tweet
		}
	}
	else if (reaction_id == 1)
	{
		var dm_message = $('#dm_message').val();
		var dm_dest = $('#dm_dest').val();
		var reaction_info = {
			msg: dm_message,
			dest: dm_dest
		}
	}
	else if (reaction_id == 2)
	{
		var mail_message = $('#mail_message').val();
		var mail_dest = $('#mail_dest').val();
		var reaction_info = {
			message: mail_message,
			recipient: mail_dest
		}
	}

	var final = {
		action_id : my_action_id,
		reaction_id : reaction_id,
		action_data : my_action,
		reaction_data : reaction_info
	}
	console.log(final);
	xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/addTrigger";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-type", "application/json");
	xhr.setRequestHeader("Authorization", "Bearer " + userToken);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				console.log("Trigger Added!");
				document.location.href='/home_page';
			} else {
				//alert("Something went wrong with your informations");
				//document.location.href='/home_page';
				console.log(xhr.status, xhr.responseText);
			}
		}
	}
	var trigger = JSON.stringify(final);
	console.log(trigger);
	xhr.send(trigger);
	};
