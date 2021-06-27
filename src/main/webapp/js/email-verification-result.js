let addText = document.getElementById("verification-result"),
	urlParams = new URLSearchParams(window.location.search),
	emailtoken = urlParams.get('token');

if (!urlParams.has('token') || (urlParams.get('token') == null)) {
	addText.insertAdjacentHTML("afterbegin", "No required token present. Cannot proceed. ");
	return;
}
let xmlhttp = new XMLHttpRequest();
xmlhttp.onreadystatechange = function() {
	if (this.readyState == 4 && this.status == 200) {
		var response = JSON.parse(this.responseText);
		if (response.res) {
			addText.insertAdjacentHTML("afterbegin", "Email validation successful. ");
		} else {
			addText.insertAdjacentHTML("afterbegin", "Sorry we could not verify your account. It may be that your account has already been verified. If this is not the case, please contact our support. ");
		}
	} else if(this.readyState == 4 && this.status != 200) {
		addText.insertAdjacentHTML("afterbegin", "Unable to verify email at this moment. Please try to create a new account or contact our support. ");
	}
}
xmlhttp.open("POST", "/mindhash/rest/user/verify-email", true);
xmlhttp.setRequestHeader("Content-type", "application/json");
xmlhttp.setRequestHeader("Accept", "application/json");
let json = {emailtoken}
xmlhttp.send(JSON.stringify(json));





