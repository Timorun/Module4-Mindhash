var token = sessionStorage.getItem("sessionToken");
if (token == null) {
    location.href = "login.html";
}

let xmlhttp = new XMLHttpRequest();
xmlhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        var user = JSON.parse(this.responseText);
        console.log(user);

        if (user.isadmin) {
            //get list of recordingids
            if (token != null) {
                let xmlhttp2 = new XMLHttpRequest();
                xmlhttp2.onreadystatechange = function() {
                    if (this.readyState == 4 && this.status == 200) {
                        var response = this.responseText,
                            recordings = JSON.parse(response),
                            htmlStr = "<div class=\"section-container\">\n" +
                                "                <section class=\"form-wrapper\" id=\"accessForm\">\n" +
                                "                    <label> Grant access to a recording:</label>\n" +
                                "                    <input type=\"text\" class=\"form-input-field\" placeholder=\"user@mail.com\"></input>\n" +
                                "<label for=\"recordings\">Choose a recording:</label>\n" +
                                "<select name=\"cars\" id=\"cars\">";
                        for(var recording of recordings) {
                            var id = recording.recordingID;
                            htmlStr += "<option value="+id+">"+id+"</option>"
                        }
                        htmlStr += "</select><button  id=\"grant\" class=\"form-submit-btn\" onclick=\"grantAccess()\">Grant Access</button>" +
                            "                </section>\n"+
                            "            </div>"
                        document.getElementById('adminFeatures').innerHTML += htmlStr;
                    } else if (this.readyState == 4 && this.status == 511) {
                        location.href = "login.html";
                    }
                }
                xmlhttp2.open("GET", "/mindhash/rest/recordings", true);
                xmlhttp2.setRequestHeader("Accept", "application/json");
                xmlhttp2.setRequestHeader("Authorization", token);
                xmlhttp2.send();
            } else {
                sessionStorage.removeItem("sessionToken");
                location.href = "login.html";
            }
        } else {
			
		}
    } else if (this.readyState == 4 && this.status == 511) {
        location.href = "login.html";
    }
}
xmlhttp.open("GET", "/mindhash/rest/user/info", true);
xmlhttp.setRequestHeader("Authorization", token);
xmlhttp.send();

function grantAccess() {
    console.log("buttonclicked")
}