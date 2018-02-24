$(document).ready(function() {   
    $.ajax({
        url: "/user/checkRole",
        type: 'GET'
    }).done(function (data) {
    	type = data.role.split("-")[0];
    	if(type === "Redirect" || type === "User") {
    		window.location.href = 'index.html';
    	}
    	else {
    		$(".navbar-brand").text(data.role.split("-")[1]);
    		for(var i =0;i<data.jobs.length;i++) {
    			$("#tasksTableBody").append('<tr><td>'+ data.jobs[i].category.name+'</td><td>'+new Date(data.jobs[i].auctionLimit).toString().substring(0,15)+'</td><td>'+new Date(data.jobs[i].jobLimit).toString().substring(0,15)+'</td><td>'+data.jobs[i].maxPrice+'</td><td><button type="button" onclick="sendOffer('+data.jobs[i].id+')" class="btn btn-primary">Accept</button></td></tr>');
    		}
    		
    	}
    })
})

function sendOffer(jobID) {
	$("#jobID").val(jobID);
	
	$(".offerForm").css("display","block");
}