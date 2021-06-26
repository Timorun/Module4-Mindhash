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

let xmlhttp = new XMLHttpRequest();
xmlhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        var user = JSON.parse(this.responseText);
        console.log(user);
		var admin = "";
        if (user.isadmin) {
            admin = "Yes";
        } else {
            admin = "No";
        }
		
		document.querySelector("#email").innerHTML = "Email: " +user.email;
		document.querySelector("#adminRight").innerHTML = "Admin Right: "  + admin;
		document.querySelector("#sessionSign").innerHTML = "Logged until: "  + user.sessionexpire + "<div id='logout' class='func'>Log out</div>";
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
						location.href = "login.html";
					}
				}
			}
			xmlhttp.open("POST", "/mindhash/rest/user/logout", true);
			xmlhttp.setRequestHeader("Accept", "application/json");
			xmlhttp.setRequestHeader("Authorization", token);
			xmlhttp.send();
		}
		
        if (user.isadmin) {
            document.querySelector("#adminFeatures").classList.remove("hide")
            document.title = "Admin Page";

            //get list of usermails
            let xmlhttp2 = new XMLHttpRequest();
            xmlhttp2.onreadystatechange = function () {
                if (this.readyState == 4 && this.status == 200) {
                    var response = this.responseText,
                        mailslist = JSON.parse(response),
                        htmlStr = "";
                    for (var mail of mailslist) {
                        htmlStr += "<option value=" + mail + ">" + mail + "</option>"
                    }
                    document.querySelector("#mails").innerHTML = htmlStr;

                    //get list of recordingids
                    let xmlhttp3 = new XMLHttpRequest();
                    xmlhttp3.onreadystatechange = function () {
                        if (this.readyState == 4 && this.status == 200) {
                            var response = this.responseText,
							    recordings = JSON.parse(response),
								htmlStr = "";
                            for (var recording of recordings) {
                                var id = recording.recordingID;
                                htmlStr += "<option value=" + id + ">" + id + "</option>"
                            }
                            document.querySelector('#recordingids').innerHTML = htmlStr;
                        } else if (this.readyState == 4 && this.status == 511) {
                            location.href = "login.html";
                        }
                    }
                    xmlhttp3.open("GET", "/mindhash/rest/recordings/all", true);
                    xmlhttp3.setRequestHeader("Accept", "application/json");
                    xmlhttp3.setRequestHeader("Authorization", token);
                    xmlhttp3.send();
                } else if (this.readyState == 4 && this.status == 511) {
                    location.href = "login.html";
                }
            }
            xmlhttp2.open("GET", "/mindhash/rest/user/mails", true);
            xmlhttp2.setRequestHeader("Accept", "application/json");
            xmlhttp2.setRequestHeader("Authorization", token);
            xmlhttp2.send();
        } else {
			document.querySelector("#adminFeatures").remove();
		}
    } else if (this.readyState == 4 && this.status == 511) {
        location.href = "login.html";
    }
}
xmlhttp.open("GET", "/mindhash/rest/user/info", true);
xmlhttp.setRequestHeader("Authorization", token);
xmlhttp.send();

function grantAccess() {
    var selectedmail = document.getElementById("mails").value;
    var selectedid = document.getElementById("recordingids").value;
    console.log(selectedmail+"\n"+selectedid);

    //Post grantaccess
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
            console.log(response);
            let responsemsg = document.querySelector("#responsemsg");
            if (responsemsg.classList.contains("hide")) {
                responsemsg.classList.remove("hide");
            }
            responsemsg.innerText = "Access Granted";
        } else if (this.readyState == 4 && this.status == 511) {
            if (responsemsg.classList.contains("hide")) {
                responsemsg.classList.remove("hide");
            }
            responsemsg.innerText = "You do not have admin rights";
        }
    }
    xmlhttp.open("POST", "/mindhash/rest/access/"+selectedmail+"/"+selectedid, true);
    xmlhttp.setRequestHeader("Authorization", token);
    xmlhttp.send();
}