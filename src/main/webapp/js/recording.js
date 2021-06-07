let pChart = null,
	bChart = null;
	rainChart = null;
const btn = document.querySelector(".logo");
btn.addEventListener("click", function () {
	if (currentTheme == "dark") {
		document.body.classList.remove("dark-mode");
		document.body.classList.add("light-mode");
		currentTheme = "light";
	} else {
		document.body.classList.remove("light-mode");
		document.body.classList.add("dark-mode");
		currentTheme = "dark";
	}
	if (pChart != null && bChart != null) {
		if (currentTheme == "dark") {
			pChart.options.color = "#c9d1d9";
			pChart.options.borderColor = "#c9d1d9";

			bChart.options.scales.x.grid.borderColor = "#c9d1d9";
			bChart.options.scales.y.grid.borderColor = "#c9d1d9";
			bChart.options.scales.x.ticks.color = "#c9d1d9";
			bChart.options.scales.y.ticks.color = "#c9d1d9";
		} else {
			pChart.options.color = "#333";
			pChart.options.borderColor = "#fff";

			bChart.options.scales.x.grid.borderColor = "#999";
			bChart.options.scales.y.grid.borderColor = "#999";
			bChart.options.scales.x.ticks.color = "#333";
			bChart.options.scales.y.ticks.color = "#333";
		}
		pChart.update();
		bChart.update();
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
			ctx2 = document.querySelector('#bar-chart').getContext('2d');
		ctx3 = document.querySelector('#rainchart').getContext('2d');

		var htmlStr = "<div>Date: " + recording.date + "</div>" +
			"<div>Total: " + recording.totalObjects + "</div>" +
			"<div>Pedestrians: " + recording.totalPedestrians + "</div>" +
			"<div>Vehicles: " + recording.totalVehicles + "</div>" +
			"<div>Two wheelers: " + recording.totalTwoWheelers+ "</div>";
		document.querySelector("#general-tit").insertAdjacentHTML("afterend", htmlStr);

		var percentOfVehicles = ((recording.totalVehicles / recording.totalObjects) * 100).toFixed(2),
			percentOfPedestrians = ((recording.totalPedestrians / recording.totalObjects) * 100).toFixed(2),
			percentOfTwoWheelers = (100 - percentOfVehicles - percentOfPedestrians).toFixed(2);

		pChart = new Chart(ctx, {
			type: 'pie',
			data: {
				labels: ['Vehicles', 'Pedestrians', 'Two wheelers'],
				datasets: [
					{
						backgroundColor: ["#4b77a9", "#5f255f", "#d21243"],
						data: [percentOfVehicles, percentOfPedestrians, percentOfTwoWheelers],
						borderWidth: 0
					},
				]
			},
			options: {
				plugins: {
					tooltip: {
						callbacks: {
							label: function(TooltipItem, object) {
								return TooltipItem.label + " " +TooltipItem.formattedValue + "%";
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
				borderColor: currentTheme == "dark" ? "#c9d1d9" : "#fff",
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
						color: currentTheme == "dark" ? "#c9d1d9" : "#333",
						backgroundColor: ["#4b77a9"],
						data: [recording.pedestrians_max_velocity, recording.wheelers_max_velocity, recording.vehicles_max_velocity]
					}, {
						label: "Minimum velocity",
						color: currentTheme == "dark" ? "#c9d1d9" : "#333",
						backgroundColor: ["#5f255f"],
						data: [recording.pedestrians_min_velocity, recording.wheelers_min_velocity, recording.vehicles_min_velocity]
					}
				]
			},
			options: {
				plugins: {
					legend: {
						display: true,
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
				responsive: true,
				maintainAspectRatio: false
			}
		})


		// get closest station
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
				let closeststation = jsonstations.data[i].name.en
				let closestactivestationid = jsonstations.data[i].id

				//get weather info from that station
				let xmlhttp3 = new XMLHttpRequest();
				xmlhttp3.onreadystatechange = function() {
					if (this.readyState == 4 && this.status == 200) {
						var response = this.responseText
						hourlyweather = JSON.parse(response)
						console.log(hourlyweather.data[17].prcp)
						var raindata = ""
						for (i = 0; i < 24; i++) {
							raindata += hourlyweather.data[i]
						}

						rainChart = new Chart(ctx3, {
							type: 'line',
							data: {
								labels: [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23],
								datasets: [{
									data: [hourlyweather.data[0].prcp,hourlyweather.data[1].prcp,hourlyweather.data[2].prcp,hourlyweather.data[3].prcp,hourlyweather.data[4].prcp,hourlyweather.data[5].prcp,hourlyweather.data[6].prcp,hourlyweather.data[7].prcp,hourlyweather.data[8].prcp,hourlyweather.data[9].prcp,hourlyweather.data[10].prcp,hourlyweather.data[11].prcp,hourlyweather.data[12].prcp,hourlyweather.data[13].prcp,hourlyweather.data[14].prcp,hourlyweather.data[15].prcp,hourlyweather.data[16].prcp,hourlyweather.data[17].prcp,hourlyweather.data[18].prcp,hourlyweather.data[19].prcp,hourlyweather.data[20].prcp,hourlyweather.data[21].prcp,hourlyweather.data[22].prcp,hourlyweather.data[23].prcp],
									label: "Precipitation per hour (in mm) given by source "+ closeststation,
									borderColor: "#3e95cd",
									fill: true
								}
								]
							},
							options: {
								title: {
									display: true,
									text: 'Precipitation per hour (in mm) given by source '+ closeststation
								}
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
		;
	}
}
xmlhttp.open("GET", "/MindhashApp/rest/recordings/" + id, true);
xmlhttp.setRequestHeader("Accept", "application/json");
xmlhttp.send();