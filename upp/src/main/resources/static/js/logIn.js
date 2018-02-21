$(document).ready(function() {
	$("#logInForm").submit(function( e ) {
        if (e.isDefaultPrevented() === false) {
        	dataToAdd = {}
        	dataToAdd.email = $("#email").val();
        	dataToAdd.password = $("#password").val();
        	$.ajax({
                url: "/user/logIn",
                type: 'POST',
                data: JSON.stringify(dataToAdd),
                contentType: "application/json",
            }).done(function (data) {
            	if(data === "Company") {
            		window.location.href = 'company.html';
            	}
            	else if(data === "User") {
            		window.location.href = 'userr.html';
            	}
            	else if(data === "Wrong inputs") {
            		showErrors(data);
            	}
            	else if(data === "Please confirm registration first") {
            		showErrors(data);
            	}
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                showErrors(errorThrown)
            })
        }
		e.preventDefault();
	});	
	$("#logInForm").submit();
})