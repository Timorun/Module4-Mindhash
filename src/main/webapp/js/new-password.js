let	submitBtn = document.querySelector("#submit"),
    errMsg = document.querySelector("#err-msg"),
    passwordInput = document.querySelector("#password"),
    confirmPasswordInput = document.querySelector("#confirm-password");

let regPass = /^(?=.*[0-9])(?=.*[A-Z]).{8,}$/

let urlParams = new URLSearchParams(window.location.search);

passwordInput.addEventListener("blur", function() {
    let password = passwordInput.value.trim();
    if (password.length === 0) {
        if (errMsg.classList.contains("hide")) {
            errMsg.classList.remove("hide");
        }
        errMsg.innerText = "Please input your password.";
    } else {
        if (!regPass.test(password)) {
            if (errMsg.classList.contains("hide")) {
                errMsg.classList.remove("hide");
            }
            errMsg.innerText = "Password must be at least 8 characters long and contain at least one uppercase letter and one digit.";
        } else {
            if (!errMsg.classList.contains("hide")) {
                errMsg.classList.add("hide");
            }
            errMsg.innerText = "";
        }
    }
});

confirmPasswordInput.addEventListener("blur", function() {
    let password = passwordInput.value.trim(),
        confirmPassword= confirmPasswordInput.value.trim();
    if (password.length != 0 && confirmPassword.length != 0 && confirmPassword !== password) {
        if (errMsg.classList.contains("hide")) {
            errMsg.classList.remove("hide");
        }
        errMsg.innerText = "Confirm Password should match with Password";
        return false;
    }
});

submitBtn.addEventListener("click", function(e) {
    saveNewPass();
    e.preventDefault();
});

function saveNewPass() {
    if (!urlParams.has('token') || (urlParams.get('token') == null)) {
        if (errMsg.classList.contains("hide")) {
            errMsg.classList.remove("hide");
        }
        errMsg.innerText = "No required token present. Cannot proceed.";
        return;
    }

    let token = urlParams.get('token');

    let password = passwordInput.value.trim(),
        confirmPassword= confirmPasswordInput.value.trim();

     if (password.length === 0) {
        if (errMsg.classList.contains("hide")) {
            errMsg.classList.remove("hide");
        }
        errMsg.innerText = "Please input your password.";
        return false;
    } else if (!regPass.test(password)) {
        if (errMsg.classList.contains("hide")) {
            errMsg.classList.remove("hide");
        }
        errMsg.innerText = "Password must be at least 8 characters long and contain at least one uppercase letter and one digit";
        return false;
    } else if (password !== confirmPassword) {
        if (errMsg.classList.contains("hide")) {
            errMsg.classList.remove("hide");
        }
        errMsg.innerText = "Confirm Password should match with Password";
        return false;
    }

    if (errMsg.innerText !== "") {
        if (!errMsg.classList.contains("hide")) {
            errMsg.classList.add("hide");
        }
        errMsg.innerText = "";
    }

    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            document.querySelector("#password").value ="";
            document.querySelector("#confirm-password").value ="";
            var response = JSON.parse(this.responseText);
            if (response.res) {
                location.href = "reg-success.html";
            } else {
                if (errMsg.classList.contains("hide")) {
                    errMsg.classList.remove("hide");
                }
                errMsg.innerText = response.msg;
            }
        } else if(this.readyState == 4 && this.status != 200) {
            if (errMsg.classList.contains("hide")) {
                errMsg.classList.remove("hide");
            }
            errMsg.innerText = "Unable to reset password. Please try again later.";
        }
    }
    xmlhttp.open("POST", "/mindhash/rest/user/new-password", true);
    xmlhttp.setRequestHeader("Content-type", "application/json");
    xmlhttp.setRequestHeader("Accept", "application/json");
    let json = {token, password}
    xmlhttp.send(JSON.stringify(json));
}

