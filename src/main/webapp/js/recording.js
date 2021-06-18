let pChart = null,
	bChart = null,
	weatherChart = null,
	maplayer = null,
	timeSpeedChart = null;

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
			bChart.options.scales.x.grid.borderColor = "#aaa";
			bChart.options.scales.y.grid.borderColor = "#aaa";
			bChart.options.scales.x.ticks.color = "#aaa";
			bChart.options.scales.y.ticks.color = "#aaa";

			weatherChart.options.color = "#c9d1d9";
			weatherChart.options.scales.x.grid.borderColor = "#aaa";
			weatherChart.options.scales.y.grid.borderColor = "#aaa";
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
	id = urlParams.get('id'),
	date = urlParams.get('date'),
	lat = urlParams.get('lat'),
	lon = urlParams.get('lon');

var mymap = L.map('map').setView([lat, lon], 18),
			mapUrl = currentTheme == "dark" ? "https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png"
									: "https://api.mapbox.com/styles/v1/mapbox/streets-v11/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw";
maplayer = L.tileLayer(mapUrl, {
	attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
	subdomains: 'abcd',
	maxZoom: 28
}).addTo(mymap);


let xmlhttp = new XMLHttpRequest();
xmlhttp.onreadystatechange = function() {
	if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText,
			velocity = JSON.parse(response),
			ctx = document.querySelector('#bar-chart').getContext('2d');

		bChart = new Chart(ctx, {
			type: 'bar',
			data: {
				labels: ['Pedestrians', 'Two Wheelers', 'Vehicles'],
				datasets: [
					{
						label: "Maximum velocity",
						backgroundColor: ["#4b77a9"],
						data: [velocity.pedestrians_max_velocity, velocity.wheelers_max_velocity, velocity.vehicles_max_velocity]
					}, {
						label: "Minimum velocity",
						backgroundColor: ["#5f255f"],
						data: [velocity.pedestrians_min_velocity, velocity.wheelers_min_velocity, velocity.vehicles_min_velocity]
					}, {
						label: "Average velocity",
						backgroundColor: ["#d21243"],
						data: [velocity.pedestriansAvgVelocity, velocity.wheelersAvgVelocity, velocity.vehiclesAvgVelocity]
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
							borderColor: currentTheme == "dark" ? "#aaa" : "#666",
							display: false
						},
						ticks: {
							color: currentTheme == "dark" ? "#aaa" : "#666",
						}
					},
					y: {
						grid: {
							borderColor: currentTheme == "dark" ? "#aaa" : "#666",
							display: false
						},
						ticks: {
							color: currentTheme == "dark" ? "#aaa" : "#666",
						}
					}
				},
				color: currentTheme == "dark" ? "#c9d1d9" : "#333",
				responsive: true,
				maintainAspectRatio: false
			}
		});
	}
}
xmlhttp.open("GET", "/mindhash/rest/velocity/" + id + "/" + date, true);
xmlhttp.setRequestHeader("Accept", "application/json");
xmlhttp.send();

let measurements = new Array();
let xmlHeatMap = new XMLHttpRequest();
xmlHeatMap.onreadystatechange = function() {
	if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText;
		measurements = JSON.parse(response).measureList;
		console.log(measurements)
		var coordinates = [],
			firstTime = new Array(),
			firstSpeed = new Array(),
			firstId = -1,
			name = "";
		for (var measurement of measurements) {
			var latx = parseFloat(lat) + parseFloat(measurement.x),
				lony = parseFloat(lon) + parseFloat(measurement.y);
			coordinates.push([latx, lony]);
			
			if (firstTime.length === 0 && firstId === -1) {
				firstId = measurement.objectId;
				firstSpeed.push(measurement.velocity);
				firstTime.push(measurement.timeWithoutDate.slice(0, 8));
				name = measurement.objectType + ' ' + measurement.objectId;
			} else if (firstId === measurement.objectId) {
				firstSpeed.push(measurement.velocity);
				firstTime.push(measurement.timeWithoutDate.slice(0, 8));
			}
		}
		timeSpeed(firstTime, firstSpeed, name);
		var heatMap = L.map('heatMap').setView([lat, lon], 1),
			mapUrl = currentTheme == "dark" ? "https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png"
				: "https://api.mapbox.com/styles/v1/mapbox/streets-v11/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw";
		var baseLayer = L.tileLayer(mapUrl, {
			attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
			subdomains: 'abcd',
			maxZoom: 28,
		}).addTo(heatMap);

		var heat = L.heatLayer(coordinates, 0.5, {
				radius: 25
			}).addTo(heatMap)
	}
}
xmlHeatMap.open("GET", "/mindhash/rest/measurements/" + id + "/" + date, true);
xmlHeatMap.setRequestHeader("Accept", "application/json");
xmlHeatMap.send();

let objects,
    objectType = new Object();
const $selectObj = document.querySelector("#selectObj"),
	$objList = document.querySelector("#objects");
	
let xmlAddList = new XMLHttpRequest();
xmlAddList.onreadystatechange = function() {
	if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText;
		objects = JSON.parse(response);
		var htmlStr = "";
		for (var i = 0; i < objects.length; i++) {
			if (objectType.hasOwnProperty(objects[i].objectType)) {
				objectType[objects[i].objectType] = objectType[objects[i].objectType] + 1;
			} else {
				objectType[objects[i].objectType] = 1;
			}
			htmlStr += '<li attr-id="' + objects[i].objectId + '">' + objects[i].objectType + ' ' + objects[i].objectId + '</li>';
		}
		var selectStr = '<option value="0">all objects</option>',
		    generalStr = "<div>total objects: " + objects.length + "</div>";
			index = 0,
			labels = new Array(),
			data = new Array();
		for (var o in objectType) {
			labels[index] = o;
			data[index] = objectType[o];
			index++;
			selectStr += '<option value="'+ index +'">' + o + '</option>';
			generalStr += "<div>" + o + ": " + objectType[o] + "</div>";
		}
		generalStr += "<div>recording Date: " + date + "</div>";
		document.querySelector("#general-tit").insertAdjacentHTML("afterend", generalStr);
		
		var ctx = document.querySelector('#pie-chart').getContext('2d');
		pChart = new Chart(ctx, {
			type: 'pie',
			data: {
				labels: labels,
				datasets: [
					{
						backgroundColor: ["#4b77a9", "#5f255f", "#d21243"],
						data: data,
						borderWidth: 0
					},
				]
			},
			options: {
				plugins: {
					tooltip: {
						callbacks: {
							label: function(TooltipItem, object) {
								return TooltipItem.label + ": " + TooltipItem.formattedValue + " (" + ((TooltipItem.formattedValue / objects.length) * 100).toFixed(2) + "%)";
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
		
		$selectObj.innerHTML = selectStr;
		select();
		$selectObj.addEventListener("change", function() {
			updateObjLi($selectObj.options[$selectObj.selectedIndex].text);
		});
		$objList.innerHTML = htmlStr;
	}
}
xmlAddList.open("GET", "/mindhash/rest/objects", true);
xmlAddList.setRequestHeader("Accept", "application/json");
xmlAddList.send();

function timeSpeed(labels, speed, name) {
	var ctx = document.querySelector('#timeSpeedChart').getContext('2d');
	timeSpeedChart = new Chart(ctx, {
		type: "line",
		data: {
			labels: labels,
			datasets: [{
				data: speed,
				fill: false,
				borderColor: "#3e95cd",
				tension: 0.1
			}]
		},
		options: {
			plugins: {
				title: {
					display: true,
					text: "Speed per second of " + name
				},
				legend: {
					display: false
				}
			},
			scales: {
				x: {
					grid: {
						borderColor: currentTheme == "dark" ? "#aaa" : "#666",
						color: currentTheme == "dark" ? "rgba(255, 255, 255, 0.1)" : "#e6e6e6"
					},
					ticks: {
						color: currentTheme == "dark" ? "#aaa" : "#666",
					}
				},
				y: {
					grid: {
						borderColor: currentTheme == "dark" ? "#aaa" : "#666",
						color: currentTheme == "dark" ? "rgba(255, 255, 255, 0.1)" : "#e6e6e6"
					},
					ticks: {
						color: currentTheme == "dark" ? "#aaa" : "#666",
					}
				}
			},
			color: currentTheme == "dark" ? "#c9d1d9" : "#333",
			responsive: true,
			maintainAspectRatio: false
		}
	});
}

$objList.onclick = function(e) {
	var target = e.target;
	if(target.tagName.toLowerCase() === "li"){
		var id = parseInt(target.getAttribute("attr-id")),
			name = target.innerHTML,
			match = 0,
			time = new Array(),
			speed = new Array();
		for (var m of measurements) {
			if (m.objectId === id && match !== 2) {
				time.push(m.timeWithoutDate.slice(0, 8));
				speed.push(m.velocity);
				match = 1;
			} else if (m.objectId !== id && match === 1) {
				match = 2;
			} else if (m.objectId !== id && match === 2) {
				break;
			}
		}
		timeSpeedChart.data.labels = time;
		timeSpeedChart.data.datasets[0].data = speed;
		timeSpeedChart.options.plugins.title.text = "Speed per second of " + name;
		timeSpeedChart.update();
	}
}


function updateObjLi(val) {
	var htmlStr = "";
	for (var i = 0; i < objects.length; i++) {
		if (objects[i].objectType === val || val === "all objects") {
			htmlStr += '<li attr-id="' + objects[i].objectId + '">' + objects[i].objectType + ' ' + objects[i].objectId + '</li>';
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

// get closest station based on coordinates
let xmlhttp2 = new XMLHttpRequest();
xmlhttp2.onreadystatechange = function() {
	if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText,
			jsonstations = JSON.parse(response);
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
				var response = this.responseText,
				    hourlyweather = JSON.parse(response),
					ctx = document.querySelector('#weatherChart').getContext('2d');
				//Chart mixing multiple weather charts
				var prcp = new Array(),
					temp = new Array(),
					wspd = new Array(),
					labels = new Array();
				for (var i = 0; i < 24; i++) {
					prcp[i] = hourlyweather.data[i].prcp;
					temp[i] = hourlyweather.data[i].temp;
					wspd[i] = hourlyweather.data[i].wspd;
					labels[i] = i;
				}
				weatherChart = new Chart(ctx, {
					data: {
						labels: labels,
						datasets: [{
							//Bar chart of precipitation per hour
							type: 'bar',
							data: prcp,
							label: "Precipitation(in mm)",
							borderColor: "#3e95cd",
							backgroundColor: "rgba(83, 120, 158, 0.8)",
							order: 1
						},{
							//Line chart of temperature per hour
							type: 'line',
							data: temp,
							label: "Temperature(in C°)",
							borderColor: "#3e95cd",
							fill: false,
							lineTension: 0.5,
							order: 2
						},{
							//Bar chart of windspead per hour
							type: 'bar',
							data: wspd,
							label: "Windspeed(in km/h)",
							borderColor: "#3e95cd",
							order: 3
						}]
					},
					options: {
						plugins: {
							title: {
								display: true,
								text: "Hourly weather information provided by closest active station: " + closeststation
							}
						},
						scales: {
							x: {
								grid: {
									borderColor: currentTheme == "dark" ? "#aaa" : "#666",
									color: currentTheme == "dark" ? "rgba(255, 255, 255, 0.1)" : "#e6e6e6"
								},
								ticks: {
									color: currentTheme == "dark" ? "#aaa" : "#666",
								}
							},
							y: {
								grid: {
									borderColor: currentTheme == "dark" ? "#aaa" : "#666",
									color: currentTheme == "dark" ? "rgba(255, 255, 255, 0.1)" : "#e6e6e6"
								},
								ticks: {
									color: currentTheme == "dark" ? "#aaa" : "#666",
								}
							}
						},
						color: currentTheme == "dark" ? "#c9d1d9" : "#333",
						responsive: true,
						maintainAspectRatio: false
					}
				});
			}
		}
		xmlhttp3.open("GET", "https://api.meteostat.net/v2/stations/hourly?station="+ closestactivestationid +"&start="+ date +"&end="+ date +"&tz=CEST", true);
		xmlhttp3.setRequestHeader("x-api-key", "qrTVaDSZR5djogSYK67hIjqFBy1avCTk");
		xmlhttp3.send();
	}
}
xmlhttp2.open("GET", "https://api.meteostat.net/v2/stations/nearby?lat="+ lat +"&lon="+ lon + "&limit=3", true);
xmlhttp2.setRequestHeader("x-api-key", "qrTVaDSZR5djogSYK67hIjqFBy1avCTk");
xmlhttp2.send();