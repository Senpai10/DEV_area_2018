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
	}
});

$.ajax({
	type: 'GET',
	url: 'http://localhost:8081/smConnections',
	success: function(data) {
		if (data.twitterCo == false) {
			$.ajax({
				type: 'GET',
				url: 'http://localhost:8081/twitterTokens',
				success: function(data) {
					if (data.twitterToken != "") {
						xhr = new XMLHttpRequest();
						var url = "http://localhost:8080/addTwitterToken";
						xhr.open("POST", url, true);
						xhr.setRequestHeader("Content-type", "application/json");
						xhr.setRequestHeader("Authorization", "Bearer " + userToken);
						xhr.onreadystatechange = function() {
							if (xhr.readyState == 4) {
								if (xhr.status == 200) {
									$('#btn-twitter').prop("onclick", null).off("click");
									$('#btn-twitter').empty();
									$('#btn-twitter').append('<i class="fab fa-twitter"></i> Logged in to Twitter !')
									$('#btn-twitter').attr('id', 'btn-success')
									console.log(data.twitterToken);
									var tConnection = {
										connected: true
									}
									$.ajax({
										type: 'POST',
										url: 'http://localhost:8081/tConnection',
										dataType: 'json',
										data: tConnection
									});
								}
							} else {
								console.log(xhr.status, xhr.responseText);
							}
						}
						var twitterTokens = JSON.stringify(data);
						xhr.send(twitterTokens);
					}
				}
			});
		} else {
			$('#btn-twitter').prop("onclick", null).off("click");
			$('#btn-twitter').empty();
			$('#btn-twitter').append('<i class="fab fa-twitter"></i> Logged in to Twitter !');
			$('#btn-twitter').attr('id', 'btn-success');
		}
		if (data.googleCo == false) {
			$.ajax({
				type: 'GET',
				url: 'http://localhost:8081/googleTokens',
				success: function(data) {
					if (data.googleToken != "") {
						xhr = new XMLHttpRequest();
						var url = "http://localhost:8080/addGoogleToken";
						xhr.open("POST", url, true);
						xhr.setRequestHeader("Content-type", "application/json");
						xhr.setRequestHeader("Authorization", "Bearer " + userToken);
						xhr.onreadystatechange = function() {
							if (xhr.readyState == 4) {
								if (xhr.status == 200) {
									$('#btn-google').prop("onclick", null).off("click");
									$('#btn-google').empty();
									$('#btn-google').append('<i class="fab fa-google"></i> Logged in to Google !')
									$('#btn-google').attr('id', 'btn-success')
									console.log(data.googleToken);
									var gConnection = {
										connected: true
									}
									$.ajax({
										type: 'POST',
										url: 'http://localhost:8081/gConnection',
										dataType: 'json',
										data: gConnection
									});
								}
							} else {
								console.log(xhr.status, xhr.responseText);
							}
						}
						var googleTokens = JSON.stringify(data);
						xhr.send(googleTokens);
					}
				}
			});
		} else {
			$('#btn-google').prop("onclick", null).off("click");
			$('#btn-google').empty();
			$('#btn-google').append('<i class="fab fa-google"></i> Logged in to Google !')
			$('#btn-google').attr('id', 'btn-success')
		}
	}
});

function twitterAuth() {
	window.location.href='http://127.0.0.1:8081/auth/twitter'
}

function googleAuth() {
	window.location.href='http://localhost:8081/auth/google'
}

function logout() {
	document.location.href = "/login";
}
