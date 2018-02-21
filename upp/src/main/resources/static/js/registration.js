$(document).ready(function() {
	$.ajax({
        url: "/category",
        type: 'GET',
        dataType : "json"
    }).done(function (data) {
    	for(i=0;i<data.length;i++) {
    		$('#categories').append($('<option>', {
    		    value: data[i].id,
    		    text: data[i].name
    		}));
    	}
    })
	
	$("#commonRegistraionForm").submit(function( e ) {
        if (e.isDefaultPrevented() === false) {
        	dataToAdd = {}
        	dataToAdd.name = $("#name").val();
        	dataToAdd.userName = $("#userName").val();
        	dataToAdd.password = $("#password").val();
        	dataToAdd.email = $("#email").val();
        	dataToAdd.address = $("#address").val();
        	dataToAdd.city = $("#city").val();
        	dataToAdd.postNumber = $("#postNumber").val();
        	dataToAdd.role = $("#role").val();
        	$.ajax({
                url: "/user",
                type: 'POST',
                data: JSON.stringify(dataToAdd),
                contentType: "application/json",
                dataType : "json"
            }).done(function (data) {
            	if(data.message === "More data") {
            		$("#assignedKey").val(data.user.randomKey);
            		$("#latitude").val(data.user.latitude);
            		$("#longitude").val(data.user.longitude);
            		showMessage("Insert data for company");
            		$("#registeaCompanyForm").css("display","block");
            		$("#commonRegistraionForm button").css("display","none");
            	}
            	else if(data.message === "Registrated") {
            		showMessage(data.message + ", confirm registration");
            		setTimeout(redirectOnLogin, 2000);
            	}
            	else if(data.message === "Busy username or email") {
            		showErrors(data.message);
            	}
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                showErrors(errorThrown)
            })
        }
		e.preventDefault();
	});
});
	
function addCompany() {
	dataToAdd = {}
	dataToAdd.name = $("#name").val();
	dataToAdd.userName = $("#userName").val();
	dataToAdd.password = $("#password").val();
	dataToAdd.email = $("#email").val();
	dataToAdd.address = $("#address").val();
	dataToAdd.city = $("#city").val();
	dataToAdd.postNumber = $("#postNumber").val();
	dataToAdd.role = $("#role").val();
	dataToAdd.companyName = $("#companyName").val();
	dataToAdd.distance = $("#distance").val();
	dataToAdd.randomKey = $("#assignedKey").val();
	dataToAdd.latitude = $("#latitude").val();
	dataToAdd.categories = $("#categories").val();
	$.ajax({
        url: "/user/addCompany",
        type: 'POST',
        data: JSON.stringify(dataToAdd),
        contentType: "application/json",
        dataType : "json"
    }).done(function (data) {
    	if(data.message === "Registrated") {
    		showMessage(data.message + ", confirm registration");
    		setTimeout(redirectOnLogin, 2000);
    	}
    	else if(data.message === "Busy username or email") {
    		showErrors(data.message);
    	}
    })
}

function redirectOnLogin() {
	window.location.href = 'index.html';
}
