(function(){
	let token = sessionStorage.getItem("sessionToken");
	if (token == null) {
		location.href = "login.html";
	} else {
		let xmlhttp = new XMLHttpRequest();
		xmlhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				if (this.responseText === "NO") {
					location.href = "login.html";
				}
			}
		}
		xmlhttp.open("POST", "/mindhash/rest/user/isLoggedIn", true);
		xmlhttp.setRequestHeader("Accept", "application/json");
		xmlhttp.setRequestHeader("Authorization", token);
		xmlhttp.send();
	}
})();