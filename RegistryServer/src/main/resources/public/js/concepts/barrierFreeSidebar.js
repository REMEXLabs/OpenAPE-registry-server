function handleSidebarMenuButtonClick(event) {
	console.log("handleSidebarMenuButtonClick")
	//toggleSidebarMenuButton(event.target);
}

function handleSidebarMenuButtonKeyPress(event) {
	// Check to see if space or enter were pressed
	if (event.keyCode === 32 || event.keyCode === 13) {
		// Prevent the default action to stop scrolling when space is pressed
		event.preventDefault();
		toggleSidebarMenuButton(event.target);
	}
}

function toggleSidebarMenuButton(sidebarMenuButton) {
	console.log("toggleSidebarMenuButton")
	// Check to see if the button is pressed
	var pressed = (sidebarMenuButton.getAttribute("aria-pressed") === "true");
	// Change aria-pressed to the opposite state
	sidebarMenuButton.setAttribute("aria-pressed", !pressed);
	if (sidebarMenuButton.getAttribute("class") === "opener") {
		sidebarMenuButton.setAttribute("class", "opener active");
	} else if (sidebarMenuButton.getAttribute("class") === "opener active") {
		sidebarMenuButton.setAttribute("class", "opener");
	}

}