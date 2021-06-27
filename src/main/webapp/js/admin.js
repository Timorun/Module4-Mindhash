let token = sessionStorage.getItem("sessionToken");
if (token == null) {
	location.href = "login.html";
} else {
	let xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			let userInfo = JSON.parse(this.responseText);
			admin(userInfo);
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

function admin(userInfo) {
	if (!userInfo.isadmin) {
		location.href = "profile.html";
	}
	document.querySelector("#email").innerHTML = "Email: " + userInfo.email;
	document.querySelector("#adminRight").innerHTML = "Admin Rights: Yes";
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
					document.querySelector('#recordingids2').innerHTML = htmlStr;
				} else if (this.readyState == 4 && this.status == 511) {
					location.href = "login.html";
				}
			}
			xmlhttp3.open("GET", "/mindhash/rest/recordings/all", true);
			xmlhttp3.setRequestHeader("Accept", "application/json");
			xmlhttp3.setRequestHeader("Authorization", token);
			xmlhttp3.send();
		} else if (this.readyState == 4 && this.status == 511) {
			sessionStorage.removeItem("sessionToken");
			location.href = "login.html";
		}
	}
	xmlhttp2.open("GET", "/mindhash/rest/user/mails", true);
	xmlhttp2.setRequestHeader("Accept", "application/json");
	xmlhttp2.setRequestHeader("Authorization", token);
	xmlhttp2.send();
}

function grantAccess() {
    var selectedmail = document.getElementById("mails").value;
    var selectedid = document.getElementById("recordingids").value;
    console.log(selectedmail+"\n"+selectedid);
    let responsemsg = document.querySelector("#responsemsg");
    if (responsemsg.classList.contains("hide")) {
        responsemsg.classList.remove("hide");
    }
    responsemsg.innerText = "Processing...";

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

function deleteRecording() {
    let responsemsg = document.querySelector("#deleteresponse");
    if (responsemsg.classList.contains("hide")) {
        responsemsg.classList.remove("hide");
    }
    responsemsg.innerText = "Processing...";

    var recordingid = document.getElementById("recordingids2").value;
    const cb = document.getElementById('deletecheck');

    if (!cb.checked) {
        //if checkbox not ticked show message
        let responsemsg = document.querySelector("#deleteresponse");
        if (responsemsg.classList.contains("hide")) {
            responsemsg.classList.remove("hide");
        }
        responsemsg.innerText = "Please acknowledge the conditions";
    } else {

        //Post deleteRecording
        let xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function () {
            let responsemsg = document.querySelector("#deleteresponse");
            if (this.readyState == 4 && this.status == 200) {
                var response = this.responseText;
                console.log(response);
                if (responsemsg.classList.contains("hide")) {
                    responsemsg.classList.remove("hide");
                }
                responsemsg.innerText = "Recording deleted";

            } else if (this.readyState == 4 && this.status == 511) {
                if (responsemsg.classList.contains("hide")) {
                    responsemsg.classList.remove("hide");
                }
                responsemsg.innerText = "You do not have admin rights";
            }
        }
        xmlhttp.open("POST", "/mindhash/rest/recordings/delete/"+recordingid, true);
        xmlhttp.setRequestHeader("Authorization", token);
        xmlhttp.send();
    }
}

function validate() {
    let lat = document.getElementById("latitude").value,
		lon = document.getElementById("longitude").value,
		framerate = document.getElementById("framerate").value,
		res = document.getElementById("resolution").value,
		file = document.getElementById("file"),
		errMsg = document.querySelector("#err-msg"),
		regres = /([\d ]{2,5}[x][\d ]{2,5})/,
		regint = /^-?[0-9]+$/;

    //check that latitude format is correct
    if (lat.length == 0) {
        if (errMsg.classList.contains("hide")) {
            errMsg.classList.remove("hide");
        }
        errMsg.innerText = "Please input your latitude.";
        return false;
    } else {
        if (Number(lat) == lat && !regint.test(lat)) {
            if (!errMsg.classList.contains("hide")) {
                errMsg.classList.add("hide");
            }
            errMsg.innerText = "";
        } else {
            if (errMsg.classList.contains("hide")) {
                errMsg.classList.remove("hide");
            }
            errMsg.innerText = "Incorrect latitude format. Please enter a decimal.";
            return false;
        }
    }

    //check that longitude format is correct
    if (lon.length == 0) {
        if (errMsg.classList.contains("hide")) {
            errMsg.classList.remove("hide");
        }
        errMsg.innerText = "Please input your longitude.";
        return false;
    } else {
        if (Number(lon) == lon && !regint.test(lon)) {
            if (!errMsg.classList.contains("hide")) {
                errMsg.classList.add("hide");
            }
            errMsg.innerText = "";
        } else {
            if (errMsg.classList.contains("hide")) {
                errMsg.classList.remove("hide");
            }
            errMsg.innerText = "Incorrect longitude format. Please enter a decimal.";
            return false;
        }
    }

    //check that framerate format is correct
    if (framerate.length == 0) {
        if (errMsg.classList.contains("hide")) {
            errMsg.classList.remove("hide");
        }
        errMsg.innerText = "Please input your framerate.";
        return false;
    } else {
        if (Number(framerate) == framerate && regint.test(framerate)) {
            if (!errMsg.classList.contains("hide")) {
                errMsg.classList.add("hide");
            }
            errMsg.innerText = "";
        } else {
            if (errMsg.classList.contains("hide")) {
                errMsg.classList.remove("hide");
            }
            errMsg.innerText = "Incorrect framerate format. Please enter an integer.";
            return false;
        }
    }

    //check that resolution format is correct
    if (res.length == 0) {
        if (errMsg.classList.contains("hide")) {
            errMsg.classList.remove("hide");
        }
        errMsg.innerText = "Please input your resolution.";
        return false;
    } else {
        if (regres.test(res)) {
            if (!errMsg.classList.contains("hide")) {
                errMsg.classList.add("hide");
            }
            errMsg.innerText = "";
        } else {
            if (errMsg.classList.contains("hide")) {
                errMsg.classList.remove("hide");
            }
            errMsg.innerText = "Incorrect resolution format. Please enter input of the following format: 720x540.";
            return false;
        }
    }

    //check that file format is correct
    if (file.files.length === 0) {
        if (errMsg.classList.contains("hide")) {
            errMsg.classList.remove("hide");
        }
        errMsg.innerText = "No files selected. Please upload a json"
        return false;
    } else {
        let fileName = file.files[0].name;
        let fileExtension = fileName.split('.').pop();
        if (fileExtension === 'json') {
            if (!errMsg.classList.contains("hide")) {
                errMsg.classList.add("hide");
            }
            errMsg.innerText = "";
        } else {
            if (errMsg.classList.contains("hide")) {
                errMsg.classList.remove("hide");
            }
            errMsg.innerText = "Incorrect file format. Please select a json.";
            return false;
        }
    }

}