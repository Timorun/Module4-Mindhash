let	submitBtn = document.querySelector("#submit"),
	errMsg = document.querySelector("#err-msg"),
	emailInput =  document.querySelector("#email"),
	passwordInput = document.querySelector("#password"),
	confirmPasswordInput = document.querySelector("#confirm-password");

let regPass = /^(?=.*[0-9])(?=.*[A-Z]).{8,}$/,
	regEmail = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

emailInput.addEventListener("blur", function() {
	let email = emailInput.value.trim();
	if (email.length === 0) {
		if (errMsg.classList.contains("hide")) {
			errMsg.classList.remove("hide");
		}
		errMsg.innerText = "Please input your email address.";
	} else {
		if (regEmail.test(email)) {
			if (!errMsg.classList.contains("hide")) {
				errMsg.classList.add("hide");
			}
			errMsg.innerText = "";
		} else {
			if (errMsg.classList.contains("hide")) {
				errMsg.classList.remove("hide");
			}
			errMsg.innerText = "Incorrect email format";
		}
	}
});

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
	register();
	e.preventDefault();
});

document.querySelector("#register-form").addEventListener("submit", function(e) {
	register();
	e.preventDefault();
});

function register() {
	let email = emailInput.value.trim(),
		password = passwordInput.value.trim(),
		confirmPassword= confirmPasswordInput.value.trim();
			
	if (email.length === 0) {
		if (errMsg.classList.contains("hide")) {
			errMsg.classList.remove("hide");
		}
		errMsg.innerText = "Please input your email address.";
		return false;
	} else if (!regEmail.test(email)) {
		if (errMsg.classList.contains("hide")) {
			errMsg.classList.remove("hide");
		}
		errMsg.innerText = "Incorrect email format";
		return false;
	} if (password.length === 0) {
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
			var response = JSON.parse(this.responseText);
			if (response.res) {
				location.href = "reg-success.html";
			} else {
				if (errMsg.classList.contains("hide")) {
					errMsg.classList.remove("hide");
				}
				errMsg.innerText = response.errMsg;
			}
		} else if(this.readyState == 4 && this.status != 200) {
			if (errMsg.classList.contains("hide")) {
				errMsg.classList.remove("hide");
			}
			errMsg.innerText = "Unable to register. Please try again later.";
		}
	}
	xmlhttp.open("POST", "/mindhash/rest/user/register", true);
	xmlhttp.setRequestHeader("Content-type", "application/json");
	xmlhttp.setRequestHeader("Accept", "application/json");
	let json = {email, password}
	xmlhttp.send(JSON.stringify(json));
}