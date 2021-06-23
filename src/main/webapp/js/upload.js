function validate() {
    let lat = document.getElementById("latitude").value;
    let lon = document.getElementById("longitude").value;
    let framerate = document.getElementById("framerate").value;
    let res = document.getElementById("resolution").value;
    let file = document.getElementById("file");
    let errMsg = document.querySelector("#err-msg");
    let regres = /([\d ]{2,5}[x][\d ]{2,5})/;
    let regint = /^-?[0-9]+$/;


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