$(document).ready(function() {
	$("#jobForm").css("display","block");
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
    				$("#tasksJobStatusTableBody").append('<tr><td>'+data.jobs[i].taskID+'</td><td>'+data.jobs[i].taskName+'</td><td>'+ data.jobs[i].categoryName+'</td><td>'+data.jobs[i].sentMail+'</td><td>'+data.jobs[i].offersLimit+'</td><td><button type="button" onclick=showJobStatusForm("'+data.jobs[i].taskID+'") class="btn btn-primary">Define status</button></td></tr>');
    			}
    			else if(data.jobs[i].taskName == "Jobs for confirm"){
    				$("#tasksJobForConfirmTableBody").append('<tr><td>'+data.jobs[i].taskName+'</td><td>'+ data.jobs[i].categoryName+'</td><td>'+new Date(data.jobs[i].auctionLimit).toString().substring(0,15)+'</td><td>'+new Date(data.jobs[i].jobLimit).toString().substring(0,15)+'</td><td>'+data.jobs[i].maxPrice+'</td><td><button type="button" onclick=showOffers("'+data.jobs[i].taskID+'") class="btn btn-primary">Show</button></td></tr>');
    			}    			
    			else if(data.jobs[i].taskName == "More offer request"){
    				$("#tasksNoEnoughOffersTableBody").append('<tr><td>'+data.jobs[i].taskName+'</td><td>'+ data.jobs[i].categoryName+'</td><td>'+new Date(data.jobs[i].auctionLimit).toString().substring(0,15)+'</td><td>'+new Date(data.jobs[i].jobLimit).toString().substring(0,15)+'</td><td>'+data.jobs[i].maxPrice+'</td><td><button type="button" onclick=showMoreOffersForm("'+data.jobs[i].taskID+'") class="btn btn-primary">Show</button></td></tr>');
    			}
    			else if(data.jobs[i].taskName == "No offers decision"){
    				$("#tasksZeroOffersTableBody").append('<tr><td>'+data.jobs[i].taskName+'</td><td>'+ data.jobs[i].categoryName+'</td><td>'+new Date(data.jobs[i].auctionLimit).toString().substring(0,15)+'</td><td>'+new Date(data.jobs[i].jobLimit).toString().substring(0,15)+'</td><td>'+data.jobs[i].maxPrice+'</td><td><button type="button" onclick=showZeroForm("'+data.jobs[i].taskID+'") class="btn btn-primary">Show</button></td></tr>');
    			}
    			else if(data.jobs[i].taskName == "Decide job status with description"){
    				$("#tasksToDescribeBody").append('<tr><td>'+data.jobs[i].taskName+'</td><td>'+ data.jobs[i].categoryName+'</td><td>'+new Date(data.jobs[i].auctionLimit).toString().substring(0,15)+'</td><td>'+new Date(data.jobs[i].jobLimit).toString().substring(0,15)+'</td><td>'+data.jobs[i].maxPrice+'</td><td><button type="button" class="btn btn-primary" onclick=showFormWithDescritpion("'+data.jobs[i].taskID+'","'+data.jobs[i].descritpion+'")>Show</button></td></tr>');
    			}
    			else if(data.jobs[i].taskName == "Add degree for company"){
    				$("#tasksGiveDegreeCompanyBody").append('<tr><td>'+ data.jobs[i].categoryName+'</td><td>'+data.jobs[i].descritpion+'</td><td><button type="button" class="btn btn-primary" onclick=showFormForDegree("'+data.jobs[i].taskID+'")>Show</button></td></tr>');
    			}
    			else if(data.jobs[i].taskName == "Complete job"){
    				$("#tasksCompleteJobBody").append('<tr><td>'+ data.jobs[i].categoryName+'</td><td>'+data.jobs[i].descritpion+'</td><td><button type="button" class="btn btn-primary" onclick=completeJob("'+data.jobs[i].taskID+'")>Finish</button></td></tr>');
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
        	dataToAdd.descritpion = $("#description").val();
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
	$(".divToHide").css("display","none");	
	$("#newOfferForm").css("display","block");
	
}

function showMoreOffersForm(taskID) {
	$("#taskMoreOffersStatusID").val(taskID);
	$(".divToHide").css("display","none");	
	$("#moreOffers").css("display","block");
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
	$(".divToHide").css("display","none");	
	$("#activeOffers").css("display","block")
	task = {}
	task.taskID = taskID
	$("#showOffersForTaskID").val(taskID);
	$.ajax({
        url: "/job/showOffers",
        type: 'POST',
        data: JSON.stringify(task),
        contentType: "application/json",
        dataType : "json"
    }).done(function (data) {
    	$("#offersForTaskTableBody").empty();
    	for(i = 0;i<data.offers.length;i++)
    		$("#offersForTaskTableBody").append('<tr><td>'+data.offers[i].company.name+'</td><td>'+data.offers[i].offerdPrice+'</td><td>'+ data.offers[i].jobFinished+'</td><td><button type="button" onclick=acceptOfferFormOpen("'+data.offers[i].company.id+'") class="btn btn-primary">Accept</button></td></tr>');
    })
}

function moreOffers(flag) {
	task = {}
	task.offersLimit = flag;
	task.taskID = $("#taskMoreOffersStatusID").val();
	task.auctionLimit = $("#auctionLimitForNewOffers").val();
	$.ajax({
        url: "/job/moreOffers",
        type: 'POST',
        data: JSON.stringify(task),
        contentType: "application/json",
        dataType : "json"
    }).done(function (data) {
    	window.location.reload(true);
    })
}

function showZeroForm(taskID) {
	$("#taskZeroOffersID").val(taskID);
	$(".divToHide").css("display","none");	
	$("#zeroOffersForm").css("display","block");
}

function cancelZeroOffersJob(flag) {
	task = {}
	task.offersLimit = flag;
	task.taskID = $("#taskZeroOffersID").val();
	task.auctionLimit = $("#auctionLimitForZeroOffers").val();
	$.ajax({
        url: "/job/zeroOffersDecision",
        type: 'POST',
        data: JSON.stringify(task),
        contentType: "application/json",
        dataType : "json"
    }).done(function (data) {
    	window.location.reload(true);
    })
}

function acceptOfferFormOpen(companyID) {
	$("#companyForAccept").val(companyID);
	$(".divToHide").css("display","none");	
	$("#acceptOfferForm").css("display","block");
}


function acceptOffer() {
	task = {}
	task.sentMail = $("#companyForAccept").val();
	task.taskID = $("#showOffersForTaskID").val();
	$.ajax({
        url: "/job/acceptOfferForCompany",
        type: 'POST',
        data: JSON.stringify(task),
        contentType: "application/json",
        dataType : "json"
    }).done(function (data) {
    	window.location.reload(true);
    })
}

function moreInfoForAccept() {
	task = {}
	task.taskID = $("#showOffersForTaskID").val();
	task.description = $("#howWillDoJob").val();	
	task.sentMail = $("#companyForAccept").val();
	$.ajax({
        url: "/job/requestForDescribeJob",
        type: 'POST',
        data: JSON.stringify(task),
        contentType: "application/json",
        dataType : "json"
    }).done(function (data) {
    	window.location.reload(true);
    })
}


function repeatJob(flag) {
	task = {}
	task.maxPrice = flag;	
	task.taskID = $("#showOffersForTaskID").val();
	$.ajax({
        url: "/job/repeatJob",
        type: 'POST',
        data: JSON.stringify(task),
        contentType: "application/json",
        dataType : "json"
    }).done(function (data) {
    	window.location.reload(true);
    })
}

function showFormWithDescritpion(taskID,desc) {
	$("#acceptJobWithDescritpionTaskID").val(taskID);
	$("#descriptionDone").val(desc)
	$(".divToHide").css("display","none");	
	$("#decriptionForm").css("display","block");
}


function defineStatusAfterDesc(flag) {
	task = {}
	task.offersLimit = flag;	
	task.taskID = $("#acceptJobWithDescritpionTaskID").val();
	$.ajax({
        url: "/job/acceptWithDescription",
        type: 'POST',
        data: JSON.stringify(task),
        contentType: "application/json",
        dataType : "json"
    }).done(function (data) {
    	window.location.reload(true);
    })
}

function showFormForDegree(taskID) {
	$("#taskForDegreCompanyID").val(taskID);
	$(".divToHide").css("display","none");	
	$("#degreeForCompany").css("display","block");
}

function giveDegree() {
	task = {}
	task.offersLimit = $("#companyDegree").val();	
	task.taskID = $("#taskForDegreCompanyID").val();
	$.ajax({
        url: "/job/userToCompanyDegree",
        type: 'POST',
        data: JSON.stringify(task),
        contentType: "application/json",
        dataType : "json"
    }).done(function (data) {
    	window.location.reload(true);
    })
}

function completeJob(taskID) {
	task = {}
	task.taskID = taskID
	$.ajax({
        url: "/job/completeJobFromUser",
        type: 'POST',
        data: JSON.stringify(task),
        contentType: "application/json",
        dataType : "json"
    }).done(function (data) {
    	window.location.reload(true);
    })
}