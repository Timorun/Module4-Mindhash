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
            var admin = "Yes"
        } else {
            var admin = "No"
        }

        profileStr = "<section class=\"form-wrapper\">\n" +
            "                        <label> Email:</label>\n" +
            "                        <div type=\"text\" class=\"form-input-field\">"+ user.email +"</div>\n" +
            "                        <label> Admin rights:</label>\n" +
            "                        <div type=\"text\" class=\"form-input-field\">"+ admin +"</div>\n" +
            "                        <label> Your session will expire on:</label>\n" +
            "                        <div type=\"text\" class=\"form-input-field\">"+ user.sessionexpire +"</div>\n" +
            "                    </section>";
        document.querySelector("#general-tit").insertAdjacentHTML("afterend", profileStr);

        if (user.isadmin) {
            //add adminfeatures html
            document.getElementById('adminFeatures').innerHTML += "<h1>Admin Features</h1>\n" +
                "<div id=\"uploadform\" class=\"section-container\">\n" +
                "        <section class=\"form-wrapper\"><h4>Upload a recording</h4>" +
                "            <form action=\"/mindhash/rest/upload/json\" method=\"post\" enctype=\"multipart/form-data\" onsubmit=\"return validate()\">\n" +
                "                <label for=\"latitude\">Latitude:</label>\n" +
                "                <input id=\"latitude\" name=\"latitude\" type=\"text\" placeholder=\"(e.g. 72.832...)\" class=\"form-input-field\" />\n" +
                "                <label for=\"longitude\">Longitude:</label>\n" +
                "                <input id=\"longitude\" name =\"longitude\" type=\"text\" placeholder=\"(e.g. 25.093...)\" class=\"form-input-field\" />\n" +
                "                <label for=\"framerate\">Framerate:</label>\n" +
                "                <input type=\"text\" id=\"framerate\" name =\"framerate\" placeholder=\"(e.g. 60)\" name=\"framerate\" class=\"form-input-field\">\n" +
                "                <label for=\"resolution\">Resolution:</label>\n" +
                "                <input type=\"text\" id=\"resolution\" name =\"resolution\" placeholder=\"(e.g. 1280x800)\" name=\"resolution\" class=\"form-input-field\">\n" +
                "                <label for=\"file\">JSON of the recording:</label>\n" +
                "                <input type=\"file\" id=\"file\" name = \"file\">\n" +
                "                <input type=\"submit\" value=\"Upload\" class=\"form-submit-btn\">\n" +
                "                <div id=\"err-msg\" class=\"err-msg hide\"></div>\n" +
                "            </form>\n" +
                "        </section></div>"
            document.getElementById('title').innerHTML = "Admin Page"

            let htmlStr = "<div class=\"section-container\">\n" +
                            "                <section class=\"form-wrapper\" id=\"accessForm\"><h4> Grant access to a recording</h4></section></div>";
            document.getElementById('adminFeatures').innerHTML += htmlStr;

            //get list of usermails
            let xmlhttp2 = new XMLHttpRequest();
            xmlhttp2.onreadystatechange = function () {
                if (this.readyState == 4 && this.status == 200) {
                    var response = this.responseText;
                    mailslist = JSON.parse(response),
                        htmlStr ="<label for=\"recordings\">email:  </label>\n" +
                            "<select name=\"mails\" id=\"mails\">";
                    for (var mail of mailslist) {
                        htmlStr += "<option value=" + mail + ">" + mail + "</option>"
                    }
                    htmlStr += "</select>"
                    document.getElementById('accessForm').innerHTML += htmlStr;


                    //get list of recordingids
                    let xmlhttp3 = new XMLHttpRequest();
                    xmlhttp3.onreadystatechange = function () {
                        if (this.readyState == 4 && this.status == 200) {
                            var response = this.responseText;
                            recordings = JSON.parse(response);
                            htmlStr = "<br><label for=\"recordings\">RecordingID:  </label>\n" +
                                "<select name=\"recordingids\" id=\"recordingids\">";
                            for (var recording of recordings) {
                                var id = recording.recordingID;
                                htmlStr += "<option value=" + id + ">" + id + "</option>"
                            }
                            htmlStr += "</select><button  id=\"grant\" class=\"form-submit-btn\" onclick=\"grantAccess()\">Grant Access</button>" +
                                "<br><div id=\"responsemsg\" class=\"err-msg hide\"></div>"
                            document.getElementById('accessForm').innerHTML += htmlStr;
                        } else if (this.readyState == 4 && this.status == 511) {
                            location.href = "login.html";
                        }
                    }
                    xmlhttp3.open("GET", "/mindhash/rest/recordings", true);
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