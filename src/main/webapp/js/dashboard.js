var token = sessionStorage.getItem("sessionToken");
if (token == null) {
	location.href = "login.html";
}

let queryString = window.location.search,
	urlParams = new URLSearchParams(queryString),
	id = urlParams.get('id'),
	date = urlParams.get('date'),
	lat = urlParams.get('lat'),
	lon = urlParams.get('lon');

let pChart = null,
    bChart = null,
    timeSpeedChart = null,
	weatherChart = null,
	lineChartsArr = new Array(),
	maplayer = null,
	heatmaplayer = null;

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

	if (pChart != null && lineChartsArr.length > 0 && maplayer != null) {
		if (currentTheme == "dark") {
			maplayer.setUrl("https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png");
		    heatmaplayer.setUrl("https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png");
		} else {
			maplayer.setUrl("https://api.mapbox.com/styles/v1/mapbox/streets-v11/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw");
			heatmaplayer.setUrl("https://api.mapbox.com/styles/v1/mapbox/streets-v11/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw");
		}
		pChart.options.color = currentTheme == "dark" ? "#c9d1d9" : "#333";
		pChart.data.datasets[0].backgroundColor = currentTheme == "dark" ? ["#999", "#333", "#336699"] : ["#990066", "#ccc", "#006699"],
		pChart.update();
		timeSpeedChart.data.datasets[0].borderColor = currentTheme == "dark" ? ["#999"] : ["#006699"];
		bChart.data.datasets[0].backgroundColor = currentTheme == "dark" ? ["#333"] : ["#006699"];
		bChart.data.datasets[1].backgroundColor = currentTheme == "dark" ? ["#336699"] : ["#990066"];
		bChart.data.datasets[2].backgroundColor = currentTheme == "dark" ? ["#999"] : ["#ccc"];
		weatherChart.data.datasets[0].backgroundColor = currentTheme == "dark" ? ["#336699"] : ["#990066"];
		weatherChart.data.datasets[1].borderColor = currentTheme == "dark" ? ["#999"] : ["#006699"];
		weatherChart.data.datasets[2].backgroundColor = currentTheme == "dark" ? ["#333"] : ["#ccc"];
		for(var chart of lineChartsArr) {
			chart.options.plugins.title.color = currentTheme == "dark" ? "#c9d1d9" : "#333",
			chart.options.color = currentTheme == "dark" ? "#c9d1d9" : "#333";
			chart.options.scales.x.grid.borderColor = currentTheme == "dark" ? "#aaa" : "#999";
			chart.options.scales.y.grid.borderColor = currentTheme == "dark" ? "#aaa" : "#999";
			chart.options.scales.x.grid.color = currentTheme == "dark" ? "rgba(255, 255, 255, 0.1)" : "#e6e6e6";
			chart.options.scales.y.grid.color = currentTheme == "dark" ? "rgba(255, 255, 255, 0.1)" : "#e6e6e6";
			chart.options.scales.x.ticks.color = currentTheme == "dark" ? "#c9d1d9" : "#333";
			chart.options.scales.y.ticks.color = currentTheme == "dark" ? "#c9d1d9" : "#333";
			chart.update();
		}
	}
	localStorage.setItem("theme", currentTheme);
});

document.querySelector("#date").innerText = date;
let timeStr = "";
for (var i = 0; i < 24; i++) {
	var start = i < 10 ? "0" + i : i,
		end = (i + 1) < 10 ? "0" + (i + 1) : (i + 1);
	timeStr += "<option value='" + start + "'>" + start + ":00:00 - " + end + ":00:00</option>";
}
const $timeInterval = document.querySelector("#timeInterval");
$timeInterval.innerHTML = timeStr;

const $selectObj = document.querySelector("#selectObj"),
	$objList = document.querySelector("#objectsLi");
	
let xmlObjNum = new XMLHttpRequest();
xmlObjNum.onreadystatechange = function() {
	if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText,
			objectNum = JSON.parse(response);
		
		var selectStr = "",
			generalStr = "",
			total = 0,
			index = 0,
			labels = new Array(),
			data = new Array();;
		for (var o in objectNum) {
			labels[index] = o;
			data[index] = objectNum[o];
			index++;
			total += objectNum[o];
			selectStr += '<option value="'+ index +'">' + o + '</option>';
			generalStr += "<div>" + o + ": " + objectNum[o] + "</div>";
		}
		generalStr = "<div>all objects: " + total + "</div>" + generalStr + "<div>recording date: " + date + "</div>";
		document.querySelector("#general-tit").insertAdjacentHTML("afterend", generalStr);
		$selectObj.innerHTML = selectStr;
		select();
		var $interval = document.querySelector("#interval"),
			$selectInterval = $interval.querySelector(".select-items"),
			$time07 = $selectInterval.querySelectorAll("div")[7];
		$time07.dispatchEvent(new Event('click'));
		$selectInterval.classList.add("select-hide");
		$selectObj.addEventListener("change", function() {
			updateObjLi($selectObj.options[$selectObj.selectedIndex].text);
		});
		document.querySelector(".select-selected").classList.remove("select-arrow-active");
		
		var ctx = document.querySelector('#pie-chart').getContext('2d');
		pChart = new Chart(ctx, {
			type: 'pie',
			data: {
				labels: labels,
				datasets: [
					{
						data: data,
						backgroundColor: currentTheme == "dark" ? ["#999", "#333", "#336699"] : ["#990066", "#ccc", "#006699"],
						borderWidth: 0
					},
				]
			},
			options: {
				plugins: {
					tooltip: {
						callbacks: {
							label: function(TooltipItem, object) {
								return TooltipItem.label + ": " + TooltipItem.formattedValue + " (" + ((TooltipItem.formattedValue / total) * 100).toFixed(2) + "%)";
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
	} else if (this.readyState == 4 && this.status == 511) {
		sessionStorage.removeItem("sessionToken");
		location.href = "login.html";
	}
}
xmlObjNum.open("GET", "/mindhash/rest/objects/" + id + "/" + date, true);
xmlObjNum.setRequestHeader("Accept", "application/json");
xmlObjNum.setRequestHeader("Authorization", token);
xmlObjNum.send();
	
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
						backgroundColor: currentTheme == "dark" ? ["#333"] : ["#006699"],
						data: [velocity.pedestrians_max_velocity, velocity.wheelers_max_velocity, velocity.vehicles_max_velocity]
					}, {
						label: "Minimum velocity",
						backgroundColor: currentTheme == "dark" ? ["#336699"] : ["#990066"],
						data: [velocity.pedestrians_min_velocity, velocity.wheelers_min_velocity, velocity.vehicles_min_velocity]
					}, {
						label: "Average velocity",
						backgroundColor: currentTheme == "dark" ? ["#999"] : ["#ccc"],
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
							borderColor: currentTheme == "dark" ? "#aaa" : "#333",
							display: false
						},
						ticks: {
							color: currentTheme == "dark" ? "#aaa" : "#333",
						}
					},
					y: {
						grid: {
							borderColor: currentTheme == "dark" ? "#aaa" : "#333",
							display: false
						},
						ticks: {
							color: currentTheme == "dark" ? "#aaa" : "#333",
						}
					}
				},
				color: currentTheme == "dark" ? "#c9d1d9" : "#333",
				responsive: true,
				maintainAspectRatio: false
			}
		});
		lineChartsArr.push(bChart);
	} else if (this.readyState == 4 && this.status == 511) {
		sessionStorage.removeItem("sessionToken");
		location.href = "login.html";
	}
}
xmlhttp.open("GET", "/mindhash/rest/velocity/" + id + "/" + date, true);
xmlhttp.setRequestHeader("Accept", "application/json");
xmlhttp.setRequestHeader("Authorization", token);
xmlhttp.send();

let measurements = new Array(),
	objects = new Array(),
	heatmap = null,
	heatLayer = null;
$timeInterval.addEventListener("change", function() {
	var time = $timeInterval.options[$timeInterval.selectedIndex].value;
	var xmlheatmap = new XMLHttpRequest();
	xmlheatmap.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = JSON.parse(this.responseText);
			objects = response.objList;
			var objNum = response.objNum;
				infoStr = "",
				total = 0,
				len = 0,
				totalLen = Object.keys(objNum).length;
			for (var o in objNum) {
				len++;
				total += objNum[o];
				if (totalLen != len) {
					infoStr += "<span>" + o + " : "+ objNum[o] + ", </span>"; 
				} else {
					infoStr += "<span>" + o + " : "+ objNum[o] + ".</span>"; 
				}
			}
			infoStr = "<div>(heatmap) all objects : "+ total + ".</div>" + infoStr; 
			document.querySelector("#heatmapInfo").innerHTML = infoStr;
			if (timeSpeedChart == null) {
				var firstId = -1,
					name = "",
					firstTime = new Array(),
					firstSpeed = new Array();
			}
			measurements = response.measureList;
			var coordinates = [];
			var radius = 6378137.0;
			for (var measurement of measurements) {
				var dLat = parseFloat(measurement.y)/radius;
				var dLon = parseFloat(measurement.x)/(radius*Math.cos(Math.PI*parseFloat(lat)/180))
				var latO = parseFloat(lat) +dLat*180.0/Math.PI;
					lonO = parseFloat(lon) + dLon*180/Math.PI;
				coordinates.push([latO, lonO]);
				
				if (timeSpeedChart == null) {
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
			}
			if (timeSpeedChart == null) {
				timeSpeed(firstTime, firstSpeed, name);
			}
			updateObjLi($selectObj.options[$selectObj.options.selectedIndex].text);
			if (heatmap == null) {
				heatmap = L.map('heatmap').setView([lat, lon], 18);
				var mapUrl = currentTheme == "dark" ? "https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png"
						: "https://api.mapbox.com/styles/v1/mapbox/streets-v11/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw";
				heatmaplayer = L.tileLayer(mapUrl, {
					attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
					subdomains: 'abcd',
					maxZoom: 28,
				}).addTo(heatmap);
				heatLayer = L.heatLayer(coordinates, {
						radius: 15,
						minOpacity: 0.2,
						gradient: {
							'0.0': 'blue',
							'1': 'red'
						}
					}).addTo(heatmap);
			} else {
				heatmap.removeLayer(heatLayer);
				heatLayer = L.heatLayer(coordinates, 0.5, {
						radius: 25
					}).addTo(heatmap);
			}
		} else if (this.readyState == 4 && this.status == 511) {
			sessionStorage.removeItem("sessionToken");
			location.href = "login.html";
		}
	}
	xmlheatmap.open("GET", "/mindhash/rest/measurements/" + id + "/" + date + "/" + time, true);
	xmlheatmap.setRequestHeader("Accept", "application/json");
	xmlheatmap.setRequestHeader("Authorization", token);
	xmlheatmap.send();
});

var mymap = L.map('map').setView([lat, lon], 18),
			mapUrl = currentTheme == "dark" ? "https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png"
									: "https://api.mapbox.com/styles/v1/mapbox/streets-v11/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw";
maplayer = L.tileLayer(mapUrl, {
	attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
	subdomains: 'abcd',
	maxZoom: 28
}).addTo(mymap);

/*function LongLatOffset(lon, lat, a, dst) {
	var arc = 6371.393 * 1000;
	lon += dst * Math.sin(a) / (arc * Math.cos(lat) * 2 * Math.PI / 360);
	lat += dst * Math.cos(a) / (arc * 2 * Math.PI / 360);
}*/

function timeSpeed(labels, speed, name) {
	var ctx = document.querySelector('#timeSpeedChart').getContext('2d');
	/*if (timeSpeedChart != null) {
		timeSpeedChart.destroy();
	}*/
	timeSpeedChart = new Chart(ctx, {
		type: "line",
		data: {
			labels: labels,
			datasets: [{
				data: speed,
				fill: false,
				borderColor: currentTheme == "dark" ? ["#999"] : ["#006699"],
				tension: 0.1
			}]
		},
		options: {
			plugins: {
				title: {
					display: true,
					text: "Speed (m/s) of " + name,
					color: currentTheme == "dark" ? "#c9d1d9" : "#333"
				},
				legend: {
					display: false
				}
			},
			scales: {
				x: {
					grid: {
						borderColor: currentTheme == "dark" ? "#aaa" : "#333",
						color: currentTheme == "dark" ? "rgba(255, 255, 255, 0.1)" : "#e6e6e6"
					},
					ticks: {
						color: currentTheme == "dark" ? "#aaa" : "#333",
					}
				},
				y: {
					grid: {
						borderColor: currentTheme == "dark" ? "#aaa" : "#333",
						color: currentTheme == "dark" ? "rgba(255, 255, 255, 0.1)" : "#e6e6e6"
					},
					ticks: {
						color: currentTheme == "dark" ? "#aaa" : "#333",
					}
				}
			},
			color: currentTheme == "dark" ? "#c9d1d9" : "#333",
			responsive: true,
			maintainAspectRatio: false
		}
	});
	lineChartsArr.push(timeSpeedChart);
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
		timeSpeedChart.options.plugins.title.text = "Speed (m/s) of " + name;
		timeSpeedChart.update();
	}
}

function updateObjLi(val) {
	var htmlStr = "";
	for (var i = 0; i < objects.length; i++) {
		if (objects[i].objectType === val) {
			htmlStr += '<li attr-id="' + objects[i].objectId + '">' + objects[i].objectType + ' ' + objects[i].objectId + '</li>';
		}
	}
	$objList.innerHTML = htmlStr;
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
							backgroundColor: currentTheme == "dark" ? ["#336699"] : ["#990066"],
							order: 1
						},{
							//Line chart of temperature per hour
							type: 'line',
							data: temp,
							label: "Temperature(in C°)",
							borderColor: currentTheme == "dark" ? ["#999"] : ["#006699"],
							fill: false,
							lineTension: 0.5,
							order: 2
						},{
							//Bar chart of windspead per hour
							type: 'bar',
							data: wspd,
							label: "Windspeed(in km/h)",
							backgroundColor: currentTheme == "dark" ? ["#333"] : ["#ccc"],
							order: 3
						}]
					},
					options: {
						plugins: {
							title: {
								display: true,
								color: currentTheme == "dark" ? "#c9d1d9" : "#333",
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
				lineChartsArr.push(weatherChart);
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