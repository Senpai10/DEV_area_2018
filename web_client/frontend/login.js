function login() {
	var email = $('#inputEmail').val();
	var password = $('#inputPassword').val();

	var info = {
		email: email,
		password: password
	}
	xhr = new XMLHttpRequest();
	var url = "http://localhost:8080/login";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var json = JSON.parse(xhr.responseText);
				console.log(json.data.key);
				var userInfo = {
					email: email,
					token: json.data.key
				}
				$.ajax({
					type: 'POST',
					url: 'http://localhost:8081/userToken',
					dataType: 'json',
					data: userInfo
				});
				document.location.href = "/home_page";
			} else {
				console.log(xhr.status, xhr.responseText);
				alert("error");
			}
		}
	}
	var data = JSON.stringify(info);
	xhr.send(data);
}