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

function logout() {
	document.location.href = "/login";
}
