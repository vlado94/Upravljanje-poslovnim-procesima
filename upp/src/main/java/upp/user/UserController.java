package upp.user;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import upp.job.Job;
import upp.job.MockJob;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
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
	
	@PostMapping("logIn")
	public String logIn(@RequestBody MockUser obj) {
		String retVal = "";
		User u = userService.findOneByEmailAndPassword(obj.getEmail(), obj.getPassword());
		if(u == null) {
			retVal = "Wrong inputs";
		}
		else if(u.getRegistrated() == 0) {
			retVal = "Please confirm registration first";			
		}
		else if(u.getRole() == 1) {
			httpSession.setAttribute("userID", u.getId());
			retVal = "User";			
		}
		else if(u.getRole() == 2) {
			httpSession.setAttribute("userID", u.getId());
			retVal = "Company";			
		}
		return retVal;
	}

	@GetMapping("logOut")
	public String logOut() {
		String retVal = "";
		httpSession.invalidate();
		return retVal;
	}
	
	@GetMapping("checkRole")
	public LogInResponse checkRole() {
		LogInResponse retVal = new LogInResponse();
		Object id = httpSession.getAttribute("userID");
		if(id == null) {
			retVal.setRole("Redirect");
		}
		else {
			long userID = (long) id;
			User u = userService.findOne(userID);
			if(u.getRole() == 1) {
				List<Task> tasks = taskService.createTaskQuery().active().taskAssignee(u.getId().toString()).list();				
				for (Task t : tasks) {
					HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
					Job job = (Job)variables.get("jobObj");
					MockJob mockJob = (MockJob)variables.get("job");
					MockJob mock = new MockJob();
					if(t.getName().equals("Demand status")) {
						mock.setCategoryName(job.getCategory().getName());
						mock.setOffersLimit(mockJob.getOffersLimit());
						mock.setSentMail(mockJob.getCompanyIDS().size());
					}
					else if(t.getName().equals("Jobs for confirm")  || t.getName().equals("More offer request")|| t.getName().equals("No offers decision")){
						mock.setJobLimit(job.getJobLimit());
						mock.setAuctionLimit(job.getAuctionLimit());
						mock.setCategoryName(job.getCategory().getName());
						mock.setMaxPrice(job.getMaxPrice());
					}
					else if(t.getName().equals("Decide job status with description")) {
						mock.setJobLimit(job.getJobLimit());
						mock.setAuctionLimit(job.getAuctionLimit());
						mock.setCategoryName(job.getCategory().getName());
						mock.setMaxPrice(job.getMaxPrice());
						mock.setDescritpion((String)variables.get("jobDescription"));
						
					}
					else if(t.getName().equals("Complete job")) {
						mock.setDescritpion(userService.findOne((long)Long.valueOf((String)variables.get("choosenCompanyID"))).getName());
						mock.setCategoryName(job.getCategory().getName());						
					}
					else if(t.getName().equals("Add degree for company")) {
						mock.setDescritpion(userService.findOne((long)Long.valueOf((String)variables.get("choosenCompanyID"))).getName());
						mock.setCategoryName(job.getCategory().getName());						
					}
					else if(t.getName().equals("Complete job")) {
						mock.setDescritpion(userService.findOne((long)Long.valueOf((String)variables.get("choosenCompanyID"))).getName());
						mock.setCategoryName(job.getCategory().getName());						
					}
					
					mock.setTaskID(t.getId());
					mock.setTaskName(t.getName());
					retVal.getJobs().add(mock);
				}
				retVal.setRole("User-"+u.getName());				
			}
			else if(u.getRole() == 2) {
				retVal.setRole("Company-"+u.getName());
				List<Task> tasks = taskService.createTaskQuery().active().taskAssignee(u.getId().toString()).list();
				for (Task t : tasks) {
					
					HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
					Job job = (Job)variables.get("jobObj");
					MockJob mock = new MockJob();
					mock.setTaskID(t.getId());
					mock.setTaskName(t.getName());
					if(t.getName().equals("Company offer for demmand")) {
						mock.setJobLimit(job.getJobLimit());
						mock.setAuctionLimit(job.getAuctionLimit());
						mock.setCategoryName(job.getCategory().getName());
						mock.setMaxPrice(job.getMaxPrice());
					}
					else if(t.getName().equals("Define start job") || t.getName().equals("Describe process")) {
						mock.setJobLimit(job.getJobLimit());
						mock.setAuctionLimit(job.getAuctionLimit());
						mock.setCategoryName(job.getCategory().getName());
						mock.setMaxPrice(job.getMaxPrice());
					}
					else if(t.getName().equals("Add degree for user")) {
						mock.setDescritpion(job.getOwner().getName());
						mock.setCategoryName(job.getCategory().getName());						
					}
					retVal.getJobs().add(mock);
				}
			}
		}
		return retVal;
	}
	
	
	@PostMapping
	public UserResponse add(@RequestBody MockUser obj) {
		UserResponse retVal = new UserResponse();
		HashMap<String, Object> variables=new HashMap<>();
		String key = userService.generateRandomKey();
		variables.put("userKey",key);
		runtimeService.startProcessInstanceByKey("registrationProcess",variables);

		Task t= taskService.createTaskQuery().active().taskAssignee(key).list().get(0);
		variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		obj.setRandomKey(key);
		variables.put("user", obj);
		taskService.complete(t.getId(),variables);
		
		retVal.setUser(obj);
		if(obj.getRole() == 2) {
			retVal.setMessage("More data");
		}
		else if(obj.getValid() == 1) {
			retVal.setMessage("Registrated");
		} else {
			retVal.setMessage("Busy username or email");
		}
		return retVal;
	}	
	
	@PostMapping("/addCompany")
	public UserResponse addCompany(@RequestBody MockUser obj) {
		UserResponse retVal = new UserResponse();
		Task t= taskService.createTaskQuery().active().taskAssignee(obj.getRandomKey()).list().get(0);

		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		variables.put("user", obj);
		taskService.complete(t.getId(),variables);
		retVal.setUser(obj);
		if(obj.getValid() == 1) {
			retVal.setMessage("Registrated");
		} else {
			retVal.setMessage("Busy username or email");
		}
		return retVal;
	}
	@GetMapping("/confirmRegistration/{key}")
	public void confirmRegistration(@PathVariable String key) {
		HashMap<String, Object> variables=new HashMap<>();
		User u = userService.findOneByRandomKey(key);
		if(u != null) {
			variables.put("user",new MockUser(u));		
			runtimeService.signalEventReceived("alert",variables);
		}
	}
}
