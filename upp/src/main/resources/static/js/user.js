$(document).ready(function() {
	$.ajax({
        url: "/user/checkRole",
        type: 'GET'
    }).done(function (data) {
    	if(data === "Redirect" || data === "Company") {
    		window.location.href = 'index.html';
    	}
    })
})