function validateEmail(email) {
  var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(email);
}

function validForm(firstName, lastName, email, password, cfmPwd) {
	if (!validateEmail(email)) {
		alert("Invalid email address.")
		return false
	} else if (firstName === "") {
		alert("Please enter your first name.")
		return false
	} else if (lastName === "") {
		alert("Please enter your last name.")
		return false
	} else if (password === "") {
		alert("Please enter a password.")
		return false
	} else if (password != cfmPwd) {
		alert("Wrong password confirmation.")
		return false
	}
	return true
}

function registration() {
	var firstName = $('#first_name_id').val();
	var lastName = $('#last_name_id').val();
	var email = $('#email_id').val();
	var password = $('#password_id').val();
	var cfmPwd = $('#confirm_pwd_id').val();

	if (validForm(firstName, lastName, email, password, cfmPwd) === true) {
		var info = {
			firstName: firstName,
			lastName: lastName,
			email: email,
			password: password
		}
		xhr = new XMLHttpRequest();
		var url = "http://localhost:8080/register";
		xhr.open("POST", url, true);
		xhr.setRequestHeader("Content-type", "application/json");
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4) {	
				if (xhr.status == 200) {
					var json = JSON.parse(xhr.responseText);
					console.log(json);
					alert("Succesfully registered");
					document.location.href = "/login";
				} else {
					console.log(xhr.status, xhr.responseText);
					alert("error");
				}
			}
		}
		var data = JSON.stringify(info);
		xhr.send(data);
	}
}