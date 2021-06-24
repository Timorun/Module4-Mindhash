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
            document.getElementById('adminFeatures').innerHTML += "<h1>Admin Features</h1>\n" +
                "            <div class=\"section-container\">\n" +
                "                <section class=\"form-wrapper\" <form action=\"uploadServlet\" method=\"post\" enctype=\"multipart/form-data\" onsubmit=\"return validate()\">\n" +
                "                        <label for=\"latitude\">Latitude:</label>\n" +
                "                        <input id=\"latitude\" name=\"latitude\" type=\"text\" placeholder=\"(e.g. 72.832...)\" class=\"form-input-field\" />\n" +
                "\n" +
                "                        <label for=\"longitude\">Longitude:</label>\n" +
                "                        <input id=\"longitude\" name =\"longitude\" type=\"text\" placeholder=\"(e.g. 25.093...)\" class=\"form-input-field\" />\n" +
                "\n" +
                "                        <label for=\"framerate\">Framerate:</label>\n" +
                "                        <input type=\"text\" id=\"framerate\" name =\"framerate\" placeholder=\"(e.g. 60)\" name=\"framerate\" class=\"form-input-field\">\n" +
                "\n" +
                "                        <label for=\"resolution\">Resolution:</label>\n" +
                "                        <input type=\"text\" id=\"resolution\" name =\"resolution\" placeholder=\"(e.g. 1280x800)\" name=\"resolution\" class=\"form-input-field\">\n" +
                "\n" +
                "                        <label for=\"file\">JSON of the recording:</label>\n" +
                "                        <input type=\"file\" id=\"file\" name = \"file\"><br>\n" +
                "                        <br>\n" +
                "\n" +
                "                        <input type=\"submit\" value=\"Upload\" class=\"form-submit-btn\">\n" +
                "                        <div id=\"err-msg\" class=\"err-msg hide\"></div>\n" +
                "                    </form></section></div>" +
                "<div class=\"section-container\">\n" +
                "                <section class=\"form-wrapper\" id=\"accessForm\">\n" +
                "                    <label> Grant access to a recording:</label>\n" +
                "                    <input type=\"text\" class=\"form-input-field\" placeholder=\"user@mail.com\"></input>\n" +
                "                    <input type=\"integral\" class=\"form-input-field\" placeholder=\"(e.g. 1)\"></input>\n" +
                "                    <input id=\"grant\" type=\"submit\" value=\"Grant Access\" class=\"form-submit-btn\" />\n" +
                "                </section>\n" +
                "            </div>"

            document.getElementById('title').innerHTML = "Admin Page"
        }


    } else if (this.readyState == 4 && this.status == 511) {
        location.href = "login.html";
    }
}
xmlhttp.open("GET", "/mindhash/rest/user/"+token, true);
xmlhttp.send();