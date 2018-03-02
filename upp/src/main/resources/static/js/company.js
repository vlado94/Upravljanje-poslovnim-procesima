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
    			if(data.jobs[i].taskName == "Company offer for demmand") {
    				$("#tasksTableBody").append('<tr><td>'+ data.jobs[i].categoryName+'</td><td>'+new Date(data.jobs[i].auctionLimit).toString().substring(0,15)+'</td><td>'+new Date(data.jobs[i].jobLimit).toString().substring(0,15)+'</td><td>'+data.jobs[i].maxPrice+'</td><td><button type="button" onclick=showJobForm("'+data.jobs[i].taskID+'") class="btn btn-primary">Accept</button></td></tr>');
    			}  
    			else if(data.jobs[i].taskName == "Define start job") {
    				$("#tasksAcceptableJobs").append('<tr><td>'+ data.jobs[i].categoryName+'</td><td>'+new Date(data.jobs[i].auctionLimit).toString().substring(0,15)+'</td><td>'+new Date(data.jobs[i].jobLimit).toString().substring(0,15)+'</td><td>'+data.jobs[i].maxPrice+'</td><td><button type="button" onclick=showAcceptJobForm("'+data.jobs[i].taskID+'") class="btn btn-primary">Set date</button></td></tr>');
    			}  
    			else if(data.jobs[i].taskName == "Describe process") {
    				$("#tasksForDescribeJobs").append('<tr><td>'+ data.jobs[i].categoryName+'</td><td>'+new Date(data.jobs[i].auctionLimit).toString().substring(0,15)+'</td><td>'+new Date(data.jobs[i].jobLimit).toString().substring(0,15)+'</td><td>'+data.jobs[i].maxPrice+'</td><td><button type="button" onclick=showDescribeJobForm("'+data.jobs[i].taskID+'") class="btn btn-primary">Describe</button></td></tr>');
    			}  
    			else if(data.jobs[i].taskName == "Add degree for user"){
    				$("#tasksGiveDegreeUserBody").append('<tr><td>'+ data.jobs[i].categoryName+'</td><td>'+data.jobs[i].descritpion+'</td><td><button type="button" class="btn btn-primary" onclick=showFormForDegree("'+data.jobs[i].taskID+'")>Show</button></td></tr>');
    			}
    			
    			
    		}
    	}
    })
})

function showJobForm(taskID) {
	$("#taskID").val(taskID);	
	$(".forms").css("display","none");
	$(".offerForm").css("display","block");
}

function sendOffer() {
	job = {}
	job.taskID = $("#taskID").val();
	job.jobLimit = $("#jobEndDate").val();
	job.maxPrice = $("#jobPrice").val();
	$.ajax({
        url: "/job/sendOffer",
        type: 'POST',
        data: JSON.stringify(job),
        contentType: "application/json",
    }).done(function (data) {
    	showMessage("ok");
    })
}

function showAcceptJobForm(taskID) {
	$("#taskStartJobID").val(taskID);	
	$(".forms").css("display","none");
	$(".startDateJobForm").css("display","block");
}

function submitStartJob() {
	job = {}
	job.taskID = $("#taskStartJobID").val();
	job.jobLimit = $("#jobStartDate").val();
	$.ajax({
        url: "/job/submitStartDateJob",
        type: 'POST',
        data: JSON.stringify(job),
        contentType: "application/json",
    }).done(function (data) {
    	window.location.reload();
    })	
}

function showDescribeJobForm(taskID) {
	$("#taskDescribeJobID").val(taskID);	
	$(".forms").css("display","none");
	$(".describeJobForm").css("display","block");
}

function submitJobDescription() {
	job = {}
	job.taskID = $("#taskDescribeJobID").val();
	job.descritpion = $("#jobDescription").val();
	$.ajax({
        url: "/job/describeJob",
        type: 'POST',
        data: JSON.stringify(job),
        contentType: "application/json",
    }).done(function (data) {
    	window.location.reload();
    })	
}

function showFormForDegree(taskID) {
	$("#taskForDegreUserID").val(taskID);
	$(".forms").css("display","none");
	$(".degreeForUser").css("display","block");
}

function giveDegree() {
	task = {}
	task.offersLimit = $("#userDegree").val();	
	task.taskID = $("#taskForDegreUserID").val();
	$.ajax({
        url: "/job/companyToUserDegree",
        type: 'POST',
        data: JSON.stringify(task),
        contentType: "application/json",
        dataType : "json"
    }).done(function (data) {
    	window.location.reload(true);
    })
}
