(function(){
	let token = sessionStorage.getItem("sessionToken");
	(function(){
		if (token == null) {
			location.href = "login.html";
		} else {
			let xmlhttp = new XMLHttpRequest();
			xmlhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					let userInfo = JSON.parse(this.responseText);
					var $avatar = document.querySelector(".avatar");
					if ($avatar != null) {
						$avatar.onclick = function() {
							if (userInfo.isadmin) {
								location.href = "admin.html";
							} else {
								location.href = "profile.html";
							}
						}
					}
				} else if (this.readyState == 4 && this.status == 511) {
					sessionStorage.removeItem("sessionToken");
					location.href = "login.html";
				}
			}
			xmlhttp.open("POST", "/mindhash/rest/user/isLoggedIn", true);
			xmlhttp.setRequestHeader("Accept", "application/json");
			xmlhttp.setRequestHeader("Authorization", token);
			xmlhttp.send();
		}
	})();
})();