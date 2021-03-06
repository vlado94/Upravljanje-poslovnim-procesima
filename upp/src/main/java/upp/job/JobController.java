package upp.job;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.activiti.engine.FormService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import upp.offer.Offer;
import upp.offer.OfferService;
import upp.user.User;
import upp.user.UserService;

@RestController
@RequestMapping("/job")
public class JobController {

	@Autowired
	private JobService jobService;

	@Autowired
	private UserService userService;

	@Autowired
	private OfferService offerService;

	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private FormService formService;
	
	@PostMapping("{processId}")
	public String add(@PathVariable String processId,@RequestBody Map<String, String> params) {
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(processId);
		List<Task> tasks= taskService.createTaskQuery().active().list();
		Task t = null;
		for (Task task : tasks) {
			if(task.getProcessInstanceId().equals(processId))
				t = task;
		}		
		variables.put("job", getMock(params));
		variables.put("numberOfRepeting", 0);

		runtimeService.setVariables(t.getProcessInstanceId(), variables);
		formService.submitTaskFormData(t.getId(), params);
		return "";
	}	
	
	private MockJob getMock(Map<String, String> params) {
		MockJob retVal = new MockJob();
		retVal.setCategoryID(Long.parseLong(params.get("categoryIDField")));
		retVal.setDescritpion(params.get("descritpionField"));
		retVal.setOffersLimit(Integer.parseInt(params.get("offersLimitField")));
		retVal.setMaxPrice(Double.parseDouble(params.get("maxPriceField")));
		retVal.setUserID((long)httpSession.getAttribute("userID"));
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date2 = null;
		try {
			date2 = formatter.parse(params.get("auctionLimitField"));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		retVal.setAuctionLimit(new Date(date2.getTime()));
		try {
			date2 = formatter.parse(params.get("jobLimitField"));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		retVal.setJobLimit(new Date(date2.getTime()));
		return retVal;
	}
	
	@PostMapping("statusSet/{taskId}")
	public void statusSet(@PathVariable String taskId,@RequestBody Map<String, String> params) {
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		Task t = getAppropriateTask(u.getId(),taskId);
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());

		if(params.get("accept") == "no") {
			variables.put("howeverSend", 0);
			Job j = jobService.findOne(((Job)variables.get("jobObj")).getId());
			jobService.delete(j.getId());
		}
		else {
			variables.put("howeverSend", 1);			
		}		
		runtimeService.setVariables(t.getProcessInstanceId(), variables);
		formService.submitTaskFormData(taskId, params);
	}
	
	private Task getAppropriateTask(long assignee,String taskID) {
		Task retVal = null;
		List<Task> tasks = taskService.createTaskQuery().active().taskAssignee(Long.toString(assignee)).list();
		for (Task task : tasks) {
			if(task.getId().equals(taskID)) {
				retVal = task;
				break;
			}
		}
		return retVal;		
	}
	
	@PostMapping("moreOffers/{taskId}")
	public void moreOffers(@PathVariable String taskId,@RequestBody Map<String, String> params) {
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		Task t = getAppropriateTask(u.getId(),taskId);
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		if(params.get("acceptOffersField").equals("yes")) {
			variables.put("acceptOffers", 1);
		}
		else {
			Job j = jobService.findOne(((Job)variables.get("jobObj")).getId());
			
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date2 = null;
			try {
				date2 = formatter.parse(params.get("auctionLimitField"));
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			j.setAuctionLimit(new Date(date2.getTime()));
			variables.put("acceptOffers", 0);	
			j = jobService.saveObj(j);
			variables.put("jobObj", j);			
		}
		runtimeService.setVariables(t.getProcessInstanceId(), variables);
		formService.submitTaskFormData(taskId, params);
	}
	
	@PostMapping("zeroOffersDecision/{taskId}")
	public void zeroOffersDecision(@PathVariable String taskId,@RequestBody Map<String, String> params) {
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		Task t = getAppropriateTask(u.getId(),taskId);
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		if(params.get("extendTime").equals("no")) {
			variables.put("noOffersCancelProces", 1);
		}
		else {
			variables.put("noOffersCancelProces", 0);

			Job j = jobService.findOne(((Job)variables.get("jobObj")).getId());
			
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date2 = null;
			try {
				date2 = formatter.parse(params.get("timeForExtendZeroOffers"));
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			j.setAuctionLimit(new Date(date2.getTime()));
			variables.put("acceptOffers", 0);	
			j = jobService.saveObj(j);
		}
		runtimeService.setVariables(t.getProcessInstanceId(), variables);
		formService.submitTaskFormData(taskId, params);
	}
	
	@PostMapping("sendOffer/{taskId}")
	public String sendOffer(@PathVariable String taskId,@RequestBody Map<String, String> params) {
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		Task t = getAppropriateTask(u.getId(),taskId);
		String retVal = "";
		if(t != null) {
			HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
			variables.put("currentOffer",Double.parseDouble(params.get("priceForJob")));
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date2 = null;
			try {
				date2 = formatter.parse(params.get("jobWilBeFinished"));
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			variables.put("currentJobFinisherOffer",date2);
			runtimeService.setVariables(t.getProcessInstanceId(), variables);
			formService.submitTaskFormData(taskId, params);			
			variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
			retVal = (String) variables.get("currentRank");
			retVal += "-" + (String) variables.get("currentProcess");
		}
		return retVal;
	}
	
	@PostMapping("confirmFinalOffer")
	public String confirmFinalOffer(@RequestBody MockJob obj) {
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		Task t = getAppropriateTask(u.getId(),obj.getTaskID());
		String retVal = "";
		if(t != null) {
			HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
			Job job = jobService.findOne(((Job)variables.get("jobObj")).getId());
			Offer o = new Offer();
			o.setCompany(u);
			o.setJobFinished((Date)variables.get("currentJobFinisherOffer"));
			o.setOfferdPrice((double)variables.get("currentOffer"));
			o.setTaskID(t.getId());
 			o = offerService.save(o);
			job.getOffers().add(o);
			job = jobService.saveObj(job);
			variables.put("jobObj",job);
			taskService.complete(t.getId(),variables);
			retVal = (String) variables.get("rank");
		}
		return retVal;
	}
	
	@PostMapping("showOffers/{taskId}")
	public Job showOffers(@PathVariable String taskId) {
		Job retVal = null;
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		Task t = getAppropriateTask(u.getId(),taskId);
		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		retVal = jobService.findOne(((Job)variables.get("jobObj")).getId());
		Offer[] test = (Offer[]) retVal.getOffers().toArray(new Offer[retVal.getOffers().size()]);
		Offer temp = new Offer();  
        for(int i=0; i < test.length; i++){  
             for(int j=1; j < (test.length-i); j++){  
                  if(test[j-1].getOfferdPrice() > test[j].getOfferdPrice()){  
                	 temp = test[j-1];  
                     test[j-1] = test[j];  
                     test[j] = temp;  
                 }                        
             }  
        }  
        temp = new Offer();  
        for(int i=0; i < test.length; i++){  
             for(int j=1; j < (test.length-i); j++){  
            	 if(test[j-1].getJobFinished().after(test[j].getJobFinished()) && test[j-1].getOfferdPrice() == test[j].getOfferdPrice()){  
                	 temp = test[j-1];  
                     test[j-1] = test[j];  
                     test[j] = temp;  
                 }                        
             }  
        }  
        
        temp = new Offer();  
        for(int i=0; i < test.length; i++){  
             for(int j=1; j < (test.length-i); j++){ 
            	 double avgDegree1 = 0;
            	 if(test[j-1].getCompany().getDegreeCount() > 0)
            		 avgDegree1 = test[j-1].getCompany().getDegreeSum()/test[j-1].getCompany().getDegreeCount();
            	 double avgDegree2 = 0;
            	 if(test[j].getCompany().getDegreeCount() > 0)
            		 avgDegree2 = test[j].getCompany().getDegreeSum()/test[j].getCompany().getDegreeCount();
            	 if(test[j-1].getJobFinished().equals(test[j].getJobFinished()) && test[j-1].getOfferdPrice() == test[j].getOfferdPrice() && avgDegree1 > avgDegree2){  
                	 temp = test[j-1];  
                     test[j-1] = test[j];  
                     test[j] = temp;  
                 }                        
             }  
        }
        retVal.setOffers(new ArrayList<Offer>(Arrays.asList(test)));
		return retVal;
	}
	
	@PostMapping("acceptOfferForCompany/{taskId}")
	public void acceptOfferForCompany(@PathVariable String taskId,@RequestBody Map<String, String> params){
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		Task t = getAppropriateTask(u.getId(),taskId);
		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		variables.put("choosenCompanyID", params.get("choosenCompanyIDField"));
		variables.put("repeatProcess",0);
		variables.put("describeProcess",0);
		runtimeService.setVariables(t.getProcessInstanceId(), variables);
		formService.submitTaskFormData(taskId, params);
	}
	
	@PostMapping("submitStartDateJob/{taskId}")
	public void submitStartDateJob(@PathVariable String taskId,@RequestBody Map<String, String> params){
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		Task t = getAppropriateTask(u.getId(),taskId);
		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		Job job = (Job) variables.get("jobObj");
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date2 = null;
		try {
			date2 = formatter.parse(params.get("jobStartOnDate"));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		job.setStartJobDate(new java.sql.Date(date2.getTime()));
		job = jobService.saveObj(job);
		variables.put("jobObj", job);
		runtimeService.setVariables(t.getProcessInstanceId(), variables);
		formService.submitTaskFormData(taskId, params);
	}
	
	@PostMapping("describeJob/{taskId}")
	public MockJob describeJob(@PathVariable String taskId,@RequestBody MockJob obj) {
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		Task t = getAppropriateTask(u.getId(),taskId);
		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		variables.put("jobDescription", obj.getDescritpion());
		taskService.complete(t.getId(),variables);		
		return obj;
	}
	
	@PostMapping("requestForDescribeJob/{taskId}")
	public void requestForDescribeJob(@PathVariable String taskId,@RequestBody Map<String, String> params){
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		Task t = getAppropriateTask(u.getId(),taskId);
		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		variables.put("jobHowToDoDescription", params.get("descriptionField"));
		variables.put("choosenCompanyID", params.get("choosenCompanyIDField2"));
		variables.put("repeatProcess",0);
		variables.put("describeProcess",1);
		runtimeService.setVariables(t.getProcessInstanceId(), variables);
		formService.submitTaskFormData(taskId, params);
	}
	
	@PostMapping("repeatJob/{taskId}")
	public void repeatJob(@PathVariable String taskId,@RequestBody Map<String, String> params){
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		Task t = getAppropriateTask(u.getId(),taskId);
		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		int temp = (int) variables.get("numberOfRepeting");
		if(temp < 3)
			temp++;
		else
			temp = 3;
		if(params.get("repeatJobField") == "no")
			temp = 3;
		Job j = (Job)variables.get("jobObj");
		j.setOffers(new ArrayList<Offer>());
		jobService.saveObj(j);
		variables.put("numberOfRepeting",temp);
		variables.put("repeatProcess",1);
		variables.put("describeProcess",0);
		runtimeService.setVariables(t.getProcessInstanceId(), variables);
		formService.submitTaskFormData(taskId, params);
	}
	
	@PostMapping("acceptWithDescription/{taskId}")
	public void acceptWithDescription(@PathVariable String taskId,@RequestBody Map<String, String> params){
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		Task t = getAppropriateTask(u.getId(),taskId);
		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		variables.put("acceptDescribedJob",params.get("acceptWithDescField2"));
		if(params.get("acceptWithDescField2").equals("1")) {
			Job j = (Job)variables.get("jobObj");
			j.setOwner(userService.findOne(Long.valueOf((String)variables.get("choosenCompanyID")).longValue()));
			variables.put("jobObj",j);
			jobService.saveObj(j);
		}
		runtimeService.setVariables(t.getProcessInstanceId(), variables);
		formService.submitTaskFormData(taskId, params);
	}
	
	@PostMapping("companyToUserDegree/{taskId}")
	public void companyToUserDegree(@PathVariable String taskId,@RequestBody Map<String, String> params){
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		Task t = getAppropriateTask(u.getId(),taskId);
		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		User uu = userService.findOne(Long.valueOf((String)variables.get("choosenCompanyID")).longValue());
		uu.setDegreeCount(uu.getDegreeCount() + 1);
		uu.setDegreeSum(uu.getDegreeSum() + (int)Integer.parseInt(params.get("companyToUserDegreeField")));
		userService.saveObj(uu);

		runtimeService.setVariables(t.getProcessInstanceId(), variables);
		formService.submitTaskFormData(taskId, params);	}
	
	@PostMapping("userToCompanyDegree/{taskId}")
	public void userToCompanyDegree(@PathVariable String taskId,@RequestBody Map<String, String> params){
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		Task t = getAppropriateTask(u.getId(),taskId);
		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		User uu = userService.findOne((long)variables.get("userID"));
		uu.setDegreeCount(uu.getDegreeCount() + 1);
		uu.setDegreeSum(uu.getDegreeSum() + (int)Integer.parseInt(params.get("userToCompanyDegreeField")));
		userService.saveObj(uu);

		runtimeService.setVariables(t.getProcessInstanceId(), variables);
		formService.submitTaskFormData(taskId, params);	}
	
	@PostMapping("completeJobFromUser/{taskId}")
	public void completeJobFromUser(@PathVariable String taskId,@RequestBody Map<String, String> params){
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		Task t = getAppropriateTask(u.getId(),taskId);
		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		runtimeService.setVariables(t.getProcessInstanceId(), variables);
		formService.submitTaskFormData(taskId, params);	
	}
	
	@PostMapping("confirmOfferRank/{processID}")
	public void confirmOfferRank(@PathVariable String processID,@RequestBody Map<String, String> params){
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(processID);
		Task t = null;
		List<Task> tasks = taskService.createTaskQuery().active().taskAssignee(Long.toString(u.getId())).list();
		for (Task task : tasks) {
			if(task.getName().equals("Confirm offer")) {
				t = task;
				break;
			}
		}
		Job job = jobService.findOne(((Job)variables.get("jobObj")).getId());
		Offer o = new Offer();
		o.setCompany(u);
		o.setOfferdPrice(((Double)variables.get("currentOffer")).doubleValue());
		o.setJobFinished(new java.sql.Date(((java.util.Date)variables.get("currentJobFinisherOffer")).getTime()));
		o.setTaskID(t.getId());
		o = offerService.save(o);
		job.getOffers().add(o);
		job = jobService.saveObj(job);
		variables.put("jobObj",job);
		variables.put("jobConfirmWithRang", 1);
		runtimeService.setVariables(processID, variables);
		formService.submitTaskFormData(t.getId(), params);	
	}

	@PostMapping("declineOfferRank/{processID}")
	public void declineOfferRank(@PathVariable String processID,@RequestBody Map<String, String> params){
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(processID);
		Task t = null;
		List<Task> tasks = taskService.createTaskQuery().active().taskAssignee(Long.toString(u.getId())).list();
		for (Task task : tasks) {
			if(task.getName().equals("Confirm offer")) {
				t = task;
				break;
			}
		}
		variables.put("jobConfirmWithRang", 0);
		runtimeService.setVariables(processID, variables);
		formService.submitTaskFormData(t.getId(), params);	
	}
}