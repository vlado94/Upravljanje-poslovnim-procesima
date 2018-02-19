$( document ).ready(function() {
	$( "#commonRegistraionForm" ).submit(function( e ) {
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
            }).done(function (data) {
            	showMessage(data);            	
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                showErrors(errorThrown)
            })
        }
		e.preventDefault();
	});
});
	

function showErrors(errors) {
    toastr.error(errors, "Errors");
}

function showMessage(message) {
    toastr.success(message, "Succesfull");
}