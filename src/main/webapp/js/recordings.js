let token = sessionStorage.getItem("sessionToken"),
	pageNum = 1,
	loading = false;
if (token != null) {
	loadRecordings(pageNum);
} else {
	sessionStorage.removeItem("sessionToken");
	location.href = "login.html";
}

window.onscroll= function(){
	if (!loading) {
		var scrollHeight = Math.max(document.documentElement.scrollHeight, document.body.scrollHeight),
			scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop,
			clientHeight = window.innerHeight || Math.min(document.documentElement.clientHeight,document.body.clientHeight);
		
		if(clientHeight + scrollTop >= scrollHeight){
			loading = true;
			pageNum++;
			loadRecordings(pageNum);
		}
	}
}

function loadRecordings(pageNum) {
	let xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = this.responseText,
				recordings = JSON.parse(response),
				htmlStr = "";
			for(var recording of recordings) {
				var id = recording.recordingID;
				htmlStr += '<li>'
								+ '<a href="dashboard.html?id=' + id + '&date='+ recording.date + '&lat=' + recording.latitude + '&lon=' + recording.longitude + '" class="box-layout">'
								+ '<div class="box-col">Recording' + (id > 9 ? id : ('0' + id) + "_" + recording.date) + '</div>'
								+ '<div class="box-col">' + recording.startTime + '</div>'
								+ '<div class="box-col">' + recording.endTime + '</div>'
								+ '<div class="box-col">' + recording.resolution + '</div>'
								+ '<div class="box-col">' + recording.frameRate + '</div>'
								+ '</a>'
							+ '</li>'
			}
			document.querySelector(".recordings-list").insertAdjacentHTML("beforeend", htmlStr);
			loading = false;
		} else if (this.readyState == 4 && this.status == 511) {
			location.href = "login.html";
		} else if (this.readyState == 4 && this.status != 200) {
			loading = false;
			pageNum--;
		}
	}
	xmlhttp.open("GET", "/mindhash/rest/recordings?pageNum=" + pageNum, true);
	xmlhttp.setRequestHeader("Accept", "application/json");
	xmlhttp.setRequestHeader("Authorization", token);
	xmlhttp.send();
}