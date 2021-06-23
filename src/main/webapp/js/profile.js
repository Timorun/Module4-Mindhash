var token = sessionStorage.getItem("sessionToken");
if (token == null) {
    location.href = "login.html";
}

document.getElementById('uploadForm').innerHTML += "<form action=\"uploadServlet\" method=\"post\" enctype=\"multipart/form-data\" onsubmit=\"return validate()\">\n" +
    "                        <label for=\"latitude\">Latitude:</label>\n" +
    "                        <input id=\"latitude\" name=\"latitude\" type=\"text\" placeholder=\"(e.g. 72.832...)\" class=\"form-input-field\" />\n" +
    "\n" +
    "                        <label for=\"longitude\">Longitude:</label>\n" +
    "                        <input id=\"longitude\" type=\"text\" placeholder=\"(e.g. 25.093...)\" class=\"form-input-field\" />\n" +
    "\n" +
    "                        <label for=\"framerate\">Framerate:</label>\n" +
    "                        <input type=\"text\" id=\"framerate\" placeholder=\"(e.g. 60)\" name=\"framerate\" class=\"form-input-field\">\n" +
    "\n" +
    "                        <label for=\"resolution\">Resolution:</label>\n" +
    "                        <input type=\"text\" id=\"resolution\" placeholder=\"(e.g. 1280x800)\" name=\"resolution\" class=\"form-input-field\">\n" +
    "\n" +
    "                        <label for=\"file\">JSON of the recording:</label>\n" +
    "                        <input type=\"file\" id=\"file\" name = \"file\"><br>\n" +
    "                        <br>\n" +
    "\n" +
    "                        <input type=\"submit\" value=\"Upload\" class=\"form-submit-btn\">\n" +
    "                        <div id=\"err-msg\" class=\"err-msg hide\"></div>\n" +
    "                    </form>"

document.getElementById('title').innerHTML = "Admin Page"


generalStr = "<div>Your email: " + total + "</div>" + generalStr + "<div>recording date: " + date + "</div>";
document.querySelector("#general-tit").insertAdjacentHTML("afterend", generalStr);


let xmlhttp = new XMLHttpRequest();
xmlhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {

        profileStr = "<div>all objects: " + total + "</div>" + generalStr + "<div>recording date: " + date + "</div>";
        document.querySelector("#general-tit").insertAdjacentHTML("afterend", profileStr);

        if (isadmin) {
            document.getElementById('uploadForm').innerHTML += ""
            document.getElementsByName('title').innerHTML = "Admin page"
        }
    }
}
xmlhttp.open("GET", "/mindhash/rest/recordings", true);
// xmlhttp.setRequestHeader("Accept", "application/json");
xmlhttp.setRequestHeader("Authorization", token);
xmlhttp.send();