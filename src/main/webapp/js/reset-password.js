let	resetBtn = document.querySelector("#reset")

resetBtn.addEventListener("click", function(e) {
    resetPass()
    e.preventDefault();
});


function resetPass() {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = JSON.parse(this.responseText);
            if (response.res) {
                alert("Password reset link sent")
            } else {
                alert("Unable to send password reset link")
            }
        } else if(this.readyState == 4 && this.status != 200) {
                alert("An error occured. Please try again later.");
        }
    }
    xmlhttp.open("POST", "/mindhash/rest/user/password-reset", true);
    xmlhttp.setRequestHeader("Content-type", "application/json");
    xmlhttp.setRequestHeader("Accept", "application/json");
    let email = document.querySelector("#email").value;
    let json = {email}
    xmlhttp.send(JSON.stringify(json));
}




