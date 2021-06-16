let pChart = null,
	bChart = null,
	weatherChart = null,
	maplayer = null;

document.querySelector(".logo").addEventListener("click", function () {
	if (currentTheme == "dark") {
		document.body.classList.remove("dark-mode");
		document.body.classList.add("light-mode");
		currentTheme = "light";
	} else {
		document.body.classList.remove("light-mode");
		document.body.classList.add("dark-mode");
		currentTheme = "dark";
	}

	if (pChart != null && bChart != null && weatherChart != null) {
		if (currentTheme == "dark") {
			pChart.options.color = "#c9d1d9";

			bChart.options.color = "#c9d1d9";
			bChart.options.scales.x.grid.borderColor = "#c9d1d9";
			bChart.options.scales.y.grid.borderColor = "#c9d1d9";
			bChart.options.scales.x.ticks.color = "#c9d1d9";
			bChart.options.scales.y.ticks.color = "#c9d1d9";

			weatherChart.options.color = "#c9d1d9";
			weatherChart.options.scales.x.grid.borderColor = "#c9d1d9";
			weatherChart.options.scales.y.grid.borderColor = "#c9d1d9";
			weatherChart.options.scales.x.grid.color = "rgba(255, 255, 255, 0.1)";
			weatherChart.options.scales.y.grid.color = "rgba(255, 255, 255, 0.1)";
			weatherChart.options.scales.x.ticks.color = "#c9d1d9";
			weatherChart.options.scales.y.ticks.color = "#c9d1d9";

			maplayer.setUrl("https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png");
		} else {
			pChart.options.color = "#333";

			bChart.options.color = "#333";
			bChart.options.scales.x.grid.borderColor = "#999";
			bChart.options.scales.y.grid.borderColor = "#999";
			bChart.options.scales.x.ticks.color = "#333";
			bChart.options.scales.y.ticks.color = "#333";

			weatherChart.options.color = "#333";
			weatherChart.options.scales.x.grid.borderColor = "#999";
			weatherChart.options.scales.y.grid.borderColor = "#999";
			weatherChart.options.scales.x.grid.color = "#e6e6e6";
			weatherChart.options.scales.y.grid.color = "#e6e6e6";
			weatherChart.options.scales.x.ticks.color = "#333";
			weatherChart.options.scales.y.ticks.color = "#333";

			maplayer.setUrl("https://api.mapbox.com/styles/v1/mapbox/streets-v11/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw");
		}

		pChart.update();
		bChart.update();
		weatherChart.update();
	}
	localStorage.setItem("theme", currentTheme);
});

let queryString = window.location.search,
	urlParams = new URLSearchParams(queryString),
	id = urlParams.get('id');

let xmlhttp = new XMLHttpRequest();
xmlhttp.onreadystatechange = function() {
	if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText,
			recording = JSON.parse(response),
			ctx = document.querySelector('#pie-chart').getContext('2d'),
			ctx2 = document.querySelector('#bar-chart').getContext('2d'),
		    ctx3 = document.querySelector('#weatherChart').getContext('2d');

		var htmlStr = "<div>Total Objects: " + recording.totalObjects + "</div>" +
			"<div>Pedestrians: " + recording.totalPedestrians + "</div>" +
			"<div>Vehicles: " + recording.totalVehicles + "</div>" +
			"<div>Two Wheelers: " + recording.totalTwoWheelers+ "</div>" +
			"<div>Recording Date: " + recording.date + "</div>";
		document.querySelector("#general-tit").insertAdjacentHTML("afterend", htmlStr);

		/*var percentOfVehicles = ((recording.totalVehicles / recording.totalObjects) * 100).toFixed(2),
			percentOfPedestrians = ((recording.totalPedestrians / recording.totalObjects) * 100).toFixed(2),
			percentOfTwoWheelers = (100 - percentOfVehicles - percentOfPedestrians).toFixed(2);*/

		pChart = new Chart(ctx, {
			type: 'pie',
			data: {
				labels: ['Vehicles', 'Pedestrians', 'Two wheelers'],
				datasets: [
					{
						backgroundColor: ["#4b77a9", "#5f255f", "#d21243"],
						data: [recording.totalVehicles, recording.totalPedestrians, recording.totalTwoWheelers],
						borderWidth: 0
					},
				]
			},
			options: {
				plugins: {
					tooltip: {
						callbacks: {
							label: function(TooltipItem, object) {
								return TooltipItem.label + ": " + TooltipItem.formattedValue + " (" + ((TooltipItem.formattedValue / recording.totalObjects) * 100).toFixed(2) + "%)";
							}
						}
					},
					legend: {
						onClick: function() {
							return false;
						}
					}
				},
				color: currentTheme == "dark" ? "#c9d1d9" : "#333",
				responsive: true,
				maintainAspectRatio: false
			}
		});

		bChart = new Chart(ctx2, {
			type: 'bar',
			data: {
				labels: ['Pedestrians', 'Two Wheelers', 'Vehicles'],
				datasets: [
					{
						label: "Maximum velocity",
						backgroundColor: ["#4b77a9"],
						data: [recording.pedestrians_max_velocity, recording.wheelers_max_velocity, recording.vehicles_max_velocity]
					}, {
						label: "Minimum velocity",
						backgroundColor: ["#5f255f"],
						data: [recording.pedestrians_min_velocity, recording.wheelers_min_velocity, recording.vehicles_min_velocity]
					}, {
						label: "Average velocity",
						backgroundColor: ["#d21243"],
						data: [recording.pedestriansAvgVelocity, recording.wheelersAvgVelocity, recording.vehiclesAvgVelocity]
					}
				]
			},
			options: {
				plugins: {
					legend: {
						display: true
					}
				},
				scales: {
					x: {
						grid: {
							borderColor: currentTheme == "dark" ? "#c9d1d9" : "#666",
							display: false
						},
						ticks: {
							color: currentTheme == "dark" ? "#c9d1d9" : "#666",
						}
					},
					y: {
						grid: {
							borderColor: currentTheme == "dark" ? "#c9d1d9" : "#666",
							display: false
						},
						ticks: {
							color: currentTheme == "dark" ? "#c9d1d9" : "#666",
						}
					}
				},
				color: currentTheme == "dark" ? "#c9d1d9" : "#333",
				responsive: true,
				maintainAspectRatio: false
			}
		});

		var mymap = L.map('map').setView([recording.latitude, recording.longitude], 18),
			mapUrl = currentTheme == "dark" ? "https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png"
									: "https://api.mapbox.com/styles/v1/mapbox/streets-v11/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw";
		maplayer = L.tileLayer(mapUrl, {
			attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
			subdomains: 'abcd',
			maxZoom: 28
		}).addTo(mymap);

		// get closest station based on coordinates
		let xmlhttp2 = new XMLHttpRequest();
		xmlhttp2.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				var response = this.responseText
				jsonstations = JSON.parse(response)
				for (i = 0; i < 2; i++) {
					if (jsonstations.data[i].active === true) {
						break;
					}
				}
				let closeststation = jsonstations.data[i].name.en,
					closestactivestationid = jsonstations.data[i].id;

				//get weather info from that stationid
				let xmlhttp3 = new XMLHttpRequest();
				xmlhttp3.onreadystatechange = function() {
					if (this.readyState == 4 && this.status == 200) {
						var response = this.responseText;
						hourlyweather = JSON.parse(response);
						//Chart mixing multiple weather charts
						weatherChart = new Chart(ctx3, {
							data: {
								labels: [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23],
								datasets: [{
									//Bar chart of precipitation per hour
									type: 'bar',
									data: [hourlyweather.data[0].prcp,hourlyweather.data[1].prcp,hourlyweather.data[2].prcp,hourlyweather.data[3].prcp,hourlyweather.data[4].prcp,hourlyweather.data[5].prcp,hourlyweather.data[6].prcp,hourlyweather.data[7].prcp,hourlyweather.data[8].prcp,hourlyweather.data[9].prcp,hourlyweather.data[10].prcp,hourlyweather.data[11].prcp,hourlyweather.data[12].prcp,hourlyweather.data[13].prcp,hourlyweather.data[14].prcp,hourlyweather.data[15].prcp,hourlyweather.data[16].prcp,hourlyweather.data[17].prcp,hourlyweather.data[18].prcp,hourlyweather.data[19].prcp,hourlyweather.data[20].prcp,hourlyweather.data[21].prcp,hourlyweather.data[22].prcp,hourlyweather.data[23].prcp],
									label: "Precipitation per hour(in mm), provided by station "+ closeststation,
									borderColor: "#3e95cd",
									backgroundColor: "rgba(83, 120, 158, 0.8)",
									order: 1
								},{
									//Line chart of temperature per hour
									type: 'line',
									data: [hourlyweather.data[0].temp,hourlyweather.data[1].temp,hourlyweather.data[2].temp,hourlyweather.data[3].temp,hourlyweather.data[4].temp,hourlyweather.data[5].temp,hourlyweather.data[6].temp,hourlyweather.data[7].temp,hourlyweather.data[8].temp,hourlyweather.data[9].temp,hourlyweather.data[10].temp,hourlyweather.data[11].temp,hourlyweather.data[12].temp,hourlyweather.data[13].temp,hourlyweather.data[14].temp,hourlyweather.data[15].temp,hourlyweather.data[16].temp,hourlyweather.data[17].temp,hourlyweather.data[18].temp,hourlyweather.data[19].temp,hourlyweather.data[20].temp,hourlyweather.data[21].temp,hourlyweather.data[22].temp,hourlyweather.data[23].temp],
									label: "Temperature per hour(in C°)"+ closeststation,
									borderColor: "#3e95cd",
									fill: true,
									lineTension: 0.5,
									order: 2
								}]
							},
							options: {
								scales: {
									x: {
										grid: {
											borderColor: currentTheme == "dark" ? "#c9d1d9" : "#666",
											color: currentTheme == "dark" ? "rgba(255, 255, 255, 0.1)" : "#e6e6e6"
										},
										ticks: {
											color: currentTheme == "dark" ? "#c9d1d9" : "#666",
										}
									},
									y: {
										grid: {
											borderColor: currentTheme == "dark" ? "#c9d1d9" : "#666",
											color: currentTheme == "dark" ? "rgba(255, 255, 255, 0.1)" : "#e6e6e6"
										},
										ticks: {
											color: currentTheme == "dark" ? "#c9d1d9" : "#666",
										}
									}
								},
								color: currentTheme == "dark" ? "#c9d1d9" : "#333",
								responsive: true,
								spanGaps: true
							}
						});
					}
				}
				xmlhttp3.open("GET", "https://api.meteostat.net/v2/stations/hourly?station="+ closestactivestationid +"&start="+ recording.date +"&end="+ recording.date +"&tz=CEST", true);
				xmlhttp3.setRequestHeader("x-api-key", "qrTVaDSZR5djogSYK67hIjqFBy1avCTk");
				xmlhttp3.send();
			}
		}
		xmlhttp2.open("GET", "https://api.meteostat.net/v2/stations/nearby?lat="+ recording.latitude +"&lon="+ recording.longitude + "&limit=3", true);
		xmlhttp2.setRequestHeader("x-api-key", "qrTVaDSZR5djogSYK67hIjqFBy1avCTk");
		xmlhttp2.send();
	}
}
xmlhttp.open("GET", "/MindhashApp/rest/recordings/" + id, true);
xmlhttp.setRequestHeader("Accept", "application/json");
xmlhttp.send();

let objects,
    objectType = new Object();
const $selectObj = document.querySelector("#selectObj"),
	$objList = document.querySelector("#objects");

let xmlAddList = new XMLHttpRequest();
xmlAddList.onreadystatechange = function() {
	if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText;
		objects = JSON.parse(response);
		console.log(objects);
		var htmlStr = "";
		for (var i = 0; i < objects.length; i++) {
			if (objectType.hasOwnProperty(objects[i].objectType)) {
				objectType[objects[i].objectType] = objectType[objects[i].objectType] + 1;
			} else {
				objectType[objects[i].objectType] = 1;
			}
			htmlStr += '<li>' + objects[i].objectType + ' ' + objects[i].objectId + '</li>';
		}
		var selectStr = '<option value="0">all objects</option>',
			index = 1;
		for (var o in objectType) {
			selectStr += '<option value="'+ index +'">' + o + '</option>';
			index++;
		}
		$selectObj.innerHTML = selectStr;
		select();
		$selectObj.addEventListener("change", function() {
			updateObjLi($selectObj.options[$selectObj.selectedIndex].text);
		});
		$objList.innerHTML = htmlStr;
	}
}
xmlAddList.open("GET", "/MindhashApp/rest/objects", true);
xmlAddList.setRequestHeader("Accept", "application/json");
xmlAddList.send();

function updateObjLi(val) {
	var htmlStr = "";
	for (var i = 0; i < objects.length; i++) {
		if (objects[i].objectType === val || val === "all objects") {
			htmlStr += '<li>' + objects[i].objectType + ' ' + objects[i].objectId + '</li>';
		}
	}
	$objList.innerHTML = htmlStr;
}

function select() {
	var x, i, j, l, ll, selElmnt, a, b, c;
	/*look for any elements with the class "custom-select":*/
	x = document.querySelectorAll(".custom-select");
	l = x.length;
	for (i = 0; i < l; i++) {
		var $select = x[i];
		selElmnt = x[i].getElementsByTagName("select")[0];
		ll = selElmnt.length;
		/*for each element, create a new DIV that will act as the selected item:*/
		a = document.createElement("DIV");
		a.setAttribute("class", "select-selected");
		a.innerHTML = selElmnt.options[selElmnt.selectedIndex].innerHTML;
		x[i].appendChild(a);
		/*for each element, create a new DIV that will contain the option list:*/
		b = document.createElement("DIV");
		b.setAttribute("class", "select-items select-hide");
		for (j = 0; j < ll; j++) {
			/*for each option in the original select element,
			create a new DIV that will act as an option item:*/
			c = document.createElement("DIV");
			c.innerHTML = selElmnt.options[j].innerHTML;
			c.addEventListener("click", function(e) {
				/*when an item is clicked, update the original select box,
				and the selected item:*/
				var y, i, k, s, h, sl, yl;
				s = this.parentNode.parentNode.getElementsByTagName("select")[0];
				sl = s.length;
				h = this.parentNode.previousSibling;
				for (i = 0; i < sl; i++) {
					if (s.options[i].innerHTML == this.innerHTML) {
						s.selectedIndex = i;
						s.dispatchEvent(new Event('change'));
						h.innerHTML = this.innerHTML;
						//updateObjLi(this.innerHTML);
						y = this.parentNode.getElementsByClassName("same-as-selected");
						yl = y.length;
						for (k = 0; k < yl; k++) {
							y[k].removeAttribute("class");
						}
						this.setAttribute("class", "same-as-selected");
						break;
					}
				}
				h.click();
			});
			b.appendChild(c);
		}
		x[i].appendChild(b);
		a.addEventListener("click", function(e) {
			/*when the select box is clicked, close any other select boxes,
			and open/close the current select box:*/
			e.stopPropagation();
			closeAllSelect(this);
			this.nextSibling.classList.toggle("select-hide");
			this.classList.toggle("select-arrow-active");
		});
	}

	function closeAllSelect(elmnt) {
		/*a function that will close all select boxes in the document,
		except the current select box:*/
		var x, y, i, xl, yl, arrNo = [];
		x = document.getElementsByClassName("select-items");
		y = document.getElementsByClassName("select-selected");
		xl = x.length;
		yl = y.length;
		for (i = 0; i < yl; i++) {
			if (elmnt == y[i]) {
				arrNo.push(i)
			} else {
				y[i].classList.remove("select-arrow-active");
			}
		}
		for (i = 0; i < xl; i++) {
			if (arrNo.indexOf(i)) {
				x[i].classList.add("select-hide");
			}
		}
	}
	/*if the user clicks anywhere outside the select box,
	then close all select boxes:*/
	document.addEventListener("click", closeAllSelect);
}
