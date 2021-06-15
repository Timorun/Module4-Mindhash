const btn = document.querySelector(".logo");
const prefersDarkScheme = window.matchMedia("(prefers-color-scheme: dark)");

let currentTheme = localStorage.getItem("theme");

if (currentTheme == null && prefersDarkScheme.matches) {
	localStorage.setItem("theme", "dark");
	currentTheme = "dark";
}

if (currentTheme == "dark") {
	document.body.classList.remove("light-mode");
	document.body.classList.add("dark-mode");
} else {
	document.body.classList.remove("dark-mode");
	document.body.classList.add("light-mode");
}

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
	localStorage.setItem("theme", currentTheme);
});