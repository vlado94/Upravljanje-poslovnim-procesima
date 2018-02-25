$(document).ready(function() {
	$.ajax({
        url: "/user/checkRole",
        type: 'GET'
    }).done(function (data) {
    	type = data.role.split("-")[0]
    	if(type === "Redirect" || type === "Company") {
    		window.location.href = 'index.html';
    	}
    	else {
    		$(".navbar-brand").text(data.role.split("-")[1]);
    		for(var i =0;i<data.jobs.length;i++) {
    			if(data.jobs[i].taskName == "Demand status") {
    				$("#tasksJobStatusTableBody").append('<tr><td>'+data.jobs[i].taskID+'</td><td>'+data.jobs[i].taskName+'</td><td>'+ data.jobs[i].categoryName+'</td><td>'+data.jobs[i].offersLimit+'</td><td>'+data.jobs[i].sentMail+'</td><td><button type="button" onclick=showJobStatusForm("'+data.jobs[i].taskID+'") class="btn btn-primary">Define status</button></td></tr>');
    			}
    			else if(data.jobs[i].taskName == "Jobs for confirm"){
    				$("#tasksJobForConfirmTableBody").append('<tr><td>'+ data.jobs[i].categoryName+'</td><td>'+new Date(data.jobs[i].auctionLimit).toString().substring(0,15)+'</td><td>'+new Date(data.jobs[i].jobLimit).toString().substring(0,15)+'</td><td>'+data.jobs[i].maxPrice+'</td><td><button type="button" onclick=showOffers("'+data.jobs[i].taskID+'") class="btn btn-primary">Show</button></td></tr>');
    			}    			
    		}
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
            	window.location.reload(true);
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                showErrors(errorThrown)
            })
        }
		e.preventDefault();
	});
})

function showJobStatusForm(taskID) {
	$("#taskJobStatusID").val(taskID);
	$(".jobStatus").css("display","block");
	$("#newJob").css("display","none");	
}

function statusJob(flag) {
	task = {}
	task.offersLimit = flag;
	task.taskID = $("#taskJobStatusID").val();
	$.ajax({
        url: "/job/statusSet",
        type: 'POST',
        data: JSON.stringify(task),
        contentType: "application/json",
        dataType : "json"
    }).done(function (data) {
    	window.location.reload(true);
    })
}

function showOffers(taskID) {
	showMessage("todo")
}