let token = sessionStorage.getItem("sessionToken");
if (token == null) {
	location.href = "login.html";
} else {
	let xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			let userInfo = JSON.parse(this.responseText);
			profile(userInfo);
		} else if (this.readyState == 4 && this.status == 511) {
			sessionStorage.removeItem("sessionToken");
			location.href = "login.html";
		} /*else if (this.readyState == 4 && this.status == 500) {
			
		}*/
	}
	xmlhttp.open("POST", "/mindhash/rest/user/isLoggedIn", true);
	xmlhttp.setRequestHeader("Accept", "application/json");
	xmlhttp.setRequestHeader("Authorization", token);
	xmlhttp.send();
}

function profile(userInfo) {
	if (userInfo.isadmin) {
		location.href = "admin.html";
	}
	document.querySelector("#email").innerHTML = "Email: " + userInfo.email;
	document.querySelector("#adminRight").innerHTML = "Admin Right: No";
	document.querySelector("#sessionSign").innerHTML = "Logged until: "  + userInfo.sessionexpire + "<div id='logout' class='func'>Log out</div>";
	document.querySelector("#mode").innerHTML = (localStorage.getItem("theme") == "dark" ? "Dark Mode" : "Light Mode") + "<div id='switch' class='func'>Switch</div>";;

	document.querySelector("#switch").onclick = function() {
		if (currentTheme == "dark") {
			document.body.classList.remove("dark-mode");
			document.body.classList.add("light-mode");
			currentTheme = "light";
		} else {
			document.body.classList.remove("light-mode");
			document.body.classList.add("dark-mode");
			currentTheme = "dark";
		}
		localStorage.setItem("theme", currentTheme);
	}

	document.querySelector("#logout").onclick = function() {
		let xmlhttp = new XMLHttpRequest();
		xmlhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				if (this.responseText === "logout") {
					sessionStorage.removeItem("sessionToken");
					location.href = "login.html";
				}
			}
		}
		xmlhttp.open("POST", "/mindhash/rest/user/logout", true);
		xmlhttp.setRequestHeader("Accept", "application/json");
		xmlhttp.setRequestHeader("Authorization", token);
		xmlhttp.send();
	}
}