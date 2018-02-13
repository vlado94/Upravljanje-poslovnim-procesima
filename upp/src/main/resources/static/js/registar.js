$( document ).ready(function() {
	$( "#commonRegistraionForm" ).submit(function( e ) {
        if (e.isDefaultPrevented() === false) {
        	dataToAdd = {}
        	dataToAdd.userName = $("#userName").val();
        	$.ajax({
                url: "/user",
                type: 'POST',
                data: JSON.stringify(dataToAdd),
                contentType: "application/json",
                dataType: 'json'
            }).done(function (data) {
            	showMessage("Ok");            	
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