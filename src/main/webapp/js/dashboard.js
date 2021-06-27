let queryString = window.location.search,
	urlParams = new URLSearchParams(queryString),
	id = urlParams.get('id'),
	date = urlParams.get('date'),
	lat = urlParams.get('lat'),
	lon = urlParams.get('lon'),
	lineChartsArr = new Array(),
	token = sessionStorage.getItem("sessionToken");

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
	
let xmlObjNum = new XMLHttpRequest(),
    pChart = null,
    totalObjNum = {},
	totalObj = 0;
xmlObjNum.onreadystatechange = function() {
	if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText;
		totalObjNum = JSON.parse(response);
		
		var selectStr = "",
			generalStr = "",
			index = 0,
			labels = new Array(),
			data = new Array();;
		for (var o in totalObjNum) {
			labels[index] = o;
			data[index] = totalObjNum[o];
			index++;
			totalObj += totalObjNum[o];
			selectStr += '<option value="'+ index +'">' + o + '</option>';
			generalStr += "<div>" + o + ": " + totalObjNum[o] + "</div>";
		}
		generalStr = "<div>all objects: " + totalObj + "</div>" + generalStr + "<div>recording date: " + date + "</div>";
		document.querySelector("#general-tit").insertAdjacentHTML("afterend", generalStr);
		$selectObj.innerHTML = selectStr;
		document.querySelector("#date").innerText = date;
		select();
		var $interval = document.querySelector("#interval"),
			$selectInterval = $interval.querySelector(".select-items"),
			$time07 = $selectInterval.querySelectorAll("div")[7];
		$time07.dispatchEvent(new Event('click'));
		$selectInterval.classList.add("select-hide");
		$selectObj.addEventListener("change", function() {
			var type = $selectObj.options[$selectObj.selectedIndex].text;
			updateObjLi(type);
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
								return TooltipItem.label + ": " + TooltipItem.formattedValue + " (" + ((TooltipItem.formattedValue / totalObj) * 100).toFixed(2) + "%)";
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
xmlObjNum.open("GET", "/mindhash/rest/objects?rid=" + id + "&date=" + date, true);
xmlObjNum.setRequestHeader("Accept", "application/json");
xmlObjNum.setRequestHeader("Authorization", token);
xmlObjNum.send();

let xmlhttp = new XMLHttpRequest(),
    bChart = null;
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
						label: "Maximum",
						backgroundColor: currentTheme == "dark" ? ["#333"] : ["#006699"],
						data: [velocity.pedestrians_max_velocity, velocity.wheelers_max_velocity, velocity.vehicles_max_velocity]
					}, {
						label: "Minimum",
						backgroundColor: currentTheme == "dark" ? ["#336699"] : ["#990066"],
						data: [velocity.pedestrians_min_velocity, velocity.wheelers_min_velocity, velocity.vehicles_min_velocity]
					}, {
						label: "Average",
						backgroundColor: currentTheme == "dark" ? ["#999"] : ["#ccc"],
						data: [velocity.pedestriansAvgVelocity, velocity.wheelersAvgVelocity, velocity.vehiclesAvgVelocity]
					}
				]
			},
			options: {
				plugins: {
					title: {
						display: true,
						text: "Velocity of objects",
						color: currentTheme == "dark" ? "#c9d1d9" : "#333"
					},
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
xmlhttp.open("GET", "/mindhash/rest/velocity?rid=" + id + "&date=" + date, true);
xmlhttp.setRequestHeader("Accept", "application/json");
xmlhttp.setRequestHeader("Authorization", token);
xmlhttp.send();

let measurements = new Array(),
	objects = new Array(),
	heatmap = null,
	heatmaplayer = null,
	heatLayer = null,
	intervalChart = null,
	timeSpeedChart = null,
	/*perChart = null,*/
	scatterChart = null,
	objNum = {},
	typeheatmap = null,
	typeheatmaplayer = null,
	typeheatLayer = null;
	
$timeInterval.addEventListener("change", function() {
	var time = $timeInterval.options[$timeInterval.selectedIndex].value;
	var xmlheatmap = new XMLHttpRequest();
	xmlheatmap.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = JSON.parse(this.responseText);
			objects = response.objList;
			objNum = response.objNum;
			if (intervalChart == null) {
				var ctx = document.querySelector('#intervalChart').getContext('2d');
				intervalChart = new Chart(ctx, {
					type: 'bar',
					data: {
						labels: Object.keys(objNum),
						datasets: [
							{
								label: "one-hour time slot",
								backgroundColor: ["#555"],
								data: Object.values(objNum)
							},
							{
								label: "entire peroid",
								backgroundColor: ["#888"],
								data: Object.values(totalObjNum)
							}
						]
					},
					options: {
						plugins: {
							title: {
								display: true,
								text: "Number of objects",
								color: currentTheme == "dark" ? "#c9d1d9" : "#333"
							},
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
				lineChartsArr.push(intervalChart);
			} else {
				intervalChart.data.datasets[0].data = Object.values(objNum);
				intervalChart.update();
			}
			if (timeSpeedChart == null) {
				var firstId = -1,
					name = "",
					firstTime = new Array(),
					firstSpeed = new Array();
			}
			measurements = response.measureList;
			var coordinates = [],
				radius = 6378137.0,
				xy = new Array();
			for (var measurement of measurements) {
				var dLat = parseFloat(measurement.y) / radius,
					dLon = parseFloat(measurement.x) / (radius * Math.cos(Math.PI * parseFloat(lat) / 180)),
					latO = parseFloat(lat) + dLat * 180.0 / Math.PI;
					lonO = parseFloat(lon) + dLon * 180 / Math.PI;
				coordinates.push([latO, lonO]);
				
				if (timeSpeedChart == null || scatterChart == null) {
					var obj = {x: 0, y: 0};
					if (firstTime.length === 0 && firstId === -1) {
						firstId = measurement.objectId;
						firstSpeed.push(measurement.velocity);
						firstTime.push(measurement.timeWithoutDate.slice(0, 8));
						
						obj.x = measurement.x;
						obj.y = measurement.y;
						xy.push(obj);
						name = measurement.objectType + ' ' + measurement.objectId;
					} else if (firstId === measurement.objectId) {
						firstSpeed.push(measurement.velocity);
						firstTime.push(measurement.timeWithoutDate.slice(0, 8));
						obj.x = measurement.x;
						obj.y = measurement.y;
						xy.push(obj);
					}
				}
			}
			if (timeSpeedChart == null) {
				timeSpeed(firstTime, firstSpeed, name);
			}
			var type = $selectObj.options[$selectObj.options.selectedIndex].text;
			updateObjLi(type);
			/*if (perChart == null) {
				var ctx = document.querySelector('#perChart').getContext('2d');
				perChart = new Chart(ctx, {
					type: 'pie',
					data: {
						labels: [type + " (" + objNum[type] + ")", "other objects (" + (totalObj - objNum[type]) + ")"],
						datasets: [
							{
								data: [objNum[type], totalObj - objNum[type]],
								backgroundColor: ["#555", "#888"],
								borderWidth: 0
							},
						]
					},
					options: {
						plugins: {
							tooltip: {
								callbacks: {
									label: function(TooltipItem, object) {
										return TooltipItem.label + ": " + ((TooltipItem.formattedValue / totalObj) * 100).toFixed(2) + "%";
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
			} else {
				perChart.data.labels = [type + " (" + objNum[type] + ")", "other objects (" + (totalObj - objNum[type]) + ")"];
				perChart.data.datasets[0].data = [objNum[type], totalObj - objNum[type]];
				perChart.update();
			}*/
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
			if (scatterChart == null) {
				var ctx = document.querySelector('#scatterChart').getContext('2d');
				scatterChart = new Chart(ctx, {
					type: 'scatter',
					data: {
						datasets: [{
										label: 'object position',
										data: xy,
										backgroundColor: '#888'
									},
									{
										label: 'sensor',
										data: [{x: 0, y:0}],
										backgroundColor: '#555'
									}
								]
					},
					options: {
						plugins : {
							title: {
								display: true,
								text: "Position relative to sensor",
								color: currentTheme == "dark" ? "#c9d1d9" : "#333"
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
						maintainAspectRatio: false,
					}
				});
				lineChartsArr.push(scatterChart);
			}
		} else if (this.readyState == 4 && this.status == 511) {
			sessionStorage.removeItem("sessionToken");
			location.href = "login.html";
		} else if (this.readyState == 4 && this.status == 401) {
			location.href = "access-denied.html";
		}
	}
	xmlheatmap.open("GET", "/mindhash/rest/measurements?rid=" + id + "&date=" + date + "&time=" + time, true);
	xmlheatmap.setRequestHeader("Accept", "application/json");
	xmlheatmap.setRequestHeader("Authorization", token);
	xmlheatmap.send();
});

let mymap = L.map('map').setView([lat, lon], 18),
			mapUrl = currentTheme == "dark" ? "https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png"
									: "https://api.mapbox.com/styles/v1/mapbox/streets-v11/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw";
let maplayer = L.tileLayer(mapUrl, {
	attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
	subdomains: 'abcd',
	maxZoom: 28
}).addTo(mymap);

function timeSpeed(labels, speed, name) {
	var ctx = document.querySelector('#timeSpeedChart').getContext('2d');
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
			//match = 0,
			time = new Array(),
			speed = new Array(),
			xy = new Array();
		for (var m of measurements) {
			//if (m.objectId === id && match !== 2) {
			if (m.objectId === id) {
				time.push(m.timeWithoutDate.slice(0, 8));
				speed.push(m.velocity);
				var position = {x: 0, y: 0};
				position.x = m.x;
				position.y = m.y;
				xy.push(position);
			}
				/*match = 1;
			} else if (m.objectId !== id && match === 1) {
				match = 2;
			} else if (m.objectId !== id && match === 2) {
				break;
			}*/
		}
		timeSpeedChart.data.labels = time;
		timeSpeedChart.data.datasets[0].data = speed;
		timeSpeedChart.options.plugins.title.text = "Speed (m/s) of " + name;
		timeSpeedChart.update();
		scatterChart.data.datasets[0].data = xy;
		scatterChart.update();
	}
}

function updateObjLi(type) {
	var htmlStr = "",
		coordinates = new Array(),
	    radius = 6378137.0;
	for (var i = 0; i < objects.length; i++) {
		if (objects[i].objectType === type) {
			htmlStr += '<li attr-id="' + objects[i].objectId + '">' + objects[i].objectType + ' ' + objects[i].objectId + '</li>';
		    var dLat = parseFloat(objects[i].y) / radius,
		    	dLon = parseFloat(objects[i].x) / (radius * Math.cos(Math.PI * parseFloat(lat) / 180)),
		    	latO = parseFloat(lat) + dLat * 180.0 / Math.PI;
		    	lonO = parseFloat(lon) + dLon * 180 / Math.PI;
		    coordinates.push([latO, lonO]);
		}
	}
	$objList.innerHTML = htmlStr;
	/*if (perChart != null) {
		perChart.data.labels = [type + " (" + objNum[type] + ")", "other objects (" + (totalObj - objNum[type]) + ")"];
		perChart.data.datasets[0].data = [objNum[type], totalObj - objNum[type]];
		perChart.update();
	}*/
	if (typeheatmap == null) {
		typeheatmap = L.map('typeheatmap', { zoomControl: false }).setView([lat, lon], 18);
		var mapUrl = currentTheme == "dark" ? "https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png"
				: "https://api.mapbox.com/styles/v1/mapbox/streets-v11/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw";
		typeheatmaplayer = L.tileLayer(mapUrl, {
			attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
			subdomains: 'abcd',
			maxZoom: 28,
		}).addTo(typeheatmap);
		typeheatLayer = L.heatLayer(coordinates, {
				radius: 15,
				minOpacity: 0.2,
				gradient: {
					'0.0': 'blue',
					'1': 'red'
				}
			}).addTo(typeheatmap);
	} else {
		typeheatmap.removeLayer(typeheatLayer);
		typeheatLayer = L.heatLayer(coordinates, 0.5, {
				radius: 25
			}).addTo(typeheatmap);
	}
}

// get closest station based on coordinates
let xmlhttp2 = new XMLHttpRequest(),
    weatherChart = null;
xmlhttp2.onreadystatechange = function() {
	if (this.readyState == 4 && this.status == 200) {
		var response = this.responseText,
			jsonstations = JSON.parse(response).data,
			i;
		if (jsonstations == null) {
			return;
		}
		var len = jsonstations.length;
		for (i = 0; i < len; i++) {
			if (jsonstations[i].active === true) {
				break;
			}
		}
		let closeststation = jsonstations[i].name.en,
			closestactivestationid = jsonstations[i].id;

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
			typeheatmaplayer.setUrl("https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png");
		} else {
			maplayer.setUrl("https://api.mapbox.com/styles/v1/mapbox/streets-v11/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw");
			heatmaplayer.setUrl("https://api.mapbox.com/styles/v1/mapbox/streets-v11/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw");
			typeheatmaplayer.setUrl("https://api.mapbox.com/styles/v1/mapbox/streets-v11/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw");
		}
		pChart.options.color = currentTheme == "dark" ? "#c9d1d9" : "#333";
		pChart.data.datasets[0].backgroundColor = currentTheme == "dark" ? ["#999", "#333", "#336699"] : ["#990066", "#ccc", "#006699"],
		pChart.update();
		/*perChart.options.color = currentTheme == "dark" ? "#c9d1d9" : "#333";
		perChart.update();*/
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