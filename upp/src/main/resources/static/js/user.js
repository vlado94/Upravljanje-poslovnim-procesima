$(document).ready(function() {
	$.ajax({
        url: "/user/checkRole",
        type: 'GET'
    }).done(function (data) {
    	if(data === "Redirect" || data === "Company") {
    		window.location.href = 'index.html';
    	}
    })
    
    $.ajax({
        url: "/category",
        type: 'GET',
        dataType : "json"
    }).done(function (data) {
    	for(i=0;i<data.length;i++) {
    		$('#category').append($('<option>', {
    		    value: data[i].id,
    		    text: data[i].name
    		}));
    	}
    })
    
    $("#jobForm").submit(function( e ) {
        if (e.isDefaultPrevented() === false) {
        	dataToAdd = {}
        	dataToAdd.categoryID = $("#category").val();
        	dataToAdd.description = $("#description").val();
        	dataToAdd.auctionLimit = $("#auctionLimit").val();
        	dataToAdd.jobLimit = $("#jobLimit").val();
        	dataToAdd.offersLimit = $("#offersLimit").val();
        	dataToAdd.maxPrice = $("#maxPrice").val();
        	$.ajax({
                url: "/job",
                type: 'POST',
                data: JSON.stringify(dataToAdd),
                contentType: "application/json",
                dataType : "json"
            }).done(function (data) {
            	toastr.success("next");
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                showErrors(errorThrown)
            })
        }
		e.preventDefault();
	});
})