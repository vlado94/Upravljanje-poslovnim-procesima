function showErrors(errors) {
    toastr.error(errors, "Errors");
}

function showMessage(message) {
    toastr.success(message, "Succesfull");
}

function logOut() {
	$.ajax({
        url: "/user/logOut",
        type: 'GET',
    }).done(function (data) {
   		window.location.href = 'index.html';
    })
}
