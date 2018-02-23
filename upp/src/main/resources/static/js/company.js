$(document).ready(function() {
	$.ajax({
        url: "/user/checkRole",
        type: 'GET'
    }).done(function (data) {
    	type = data.split("-")[0]
    	if(type === "Redirect" || type === "User") {
    		window.location.href = 'index.html';
    	}
    	else {
    		$(".navbar-brand").text(data.split("-")[1]);
    	}
    })
})