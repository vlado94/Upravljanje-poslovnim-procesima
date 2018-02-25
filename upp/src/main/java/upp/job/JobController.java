package upp.job;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@PostMapping
	public MockJob add(@RequestBody MockJob obj) {
		HashMap<String, Object> variables=new HashMap<>();
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("auctionProcess",variables);
		List<Task> tasks= taskService.createTaskQuery().active().list();
		Task t = null;
		for (Task task : tasks) {
			if(task.getProcessInstanceId().equals(pi.getId()))
				t = task;
		}
		obj.setUserID((long)httpSession.getAttribute("userID"));		
		variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());

		variables.put("userID",obj.getUserID());
		variables.put("job", obj);
		taskService.complete(t.getId(),variables);

		return obj;
	}	
	
	@PostMapping("statusSet")
	public void statusSet(@RequestBody MockJob obj) {
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		List<Task> tasks = taskService.createTaskQuery().active().taskAssignee(u.getId().toString()).list();
		
		Task t = null;
		for (Task task : tasks) {
			if(task.getId().equals(obj.getTaskID())) {
				t = task;
				break;
			}
		}
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		if(obj.getOffersLimit() == 2) {
			variables.put("howeverSend", 0);
			Job j = jobService.findOne(((Job)variables.get("jobObj")).getId());
			jobService.delete(j.getId());
		}
		else {
			variables.put("howeverSend", 1);			
		}
		taskService.complete(t.getId(),variables);
	}
	
	@PostMapping("sendOffer")
	public MockJob sendOffer(@RequestBody MockJob obj) {
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		
		List<Task> tasks = taskService.createTaskQuery().active().taskAssignee(u.getId().toString()).list();
		
		Task t = null;
		for (Task task : tasks) {
			if(task.getId().equals(obj.getTaskID())) {
				t = task;
				break;
			}
		}
		if(t != null) {
			HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
			Job job = jobService.findOne(((Job)variables.get("jobObj")).getId());
			Offer o = new Offer();
			o.setCompany(u);
			o.setJobFinished(obj.getJobLimit());
			o.setOfferdPrice(obj.getMaxPrice());
			o.setTaskID(t.getId());
 			o = offerService.save(o);
			job.getOffers().add(o);
			job = jobService.calculetaRang(job);
			job = jobService.saveObj(job);
			variables.put("jobObj",job);
			taskService.complete(t.getId(),variables);
		}
		return obj;
	}
}
