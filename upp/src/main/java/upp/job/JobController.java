package upp.job;

import java.util.HashMap;

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

import upp.user.MockUser;
import upp.user.UserService;

@RestController
@RequestMapping("/job")
public class JobController {

	@Autowired
	private JobService jobService;

	@Autowired
	private UserService userService;

	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;
	
	@PostMapping
	public MockJob add(@RequestBody MockJob obj) {
		HashMap<String, Object> variables=new HashMap<>();
		runtimeService.startProcessInstanceByKey("auctionProcess",variables);
		String key = userService.generateRandomKey();

		Task t= taskService.createTaskQuery().active().list().get(0);
		obj.setUserID((long)httpSession.getAttribute("userID"));		
		obj.setJobKey(key);
		variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());

		variables.put("jobKey",key);
		variables.put("job", obj);
		taskService.complete(t.getId(),variables);

		return obj;
	}	
	
	@GetMapping("decideStatusTrue/{key}")
	public void decideStatusTrue(@PathVariable String key) {
		Task t= taskService.createTaskQuery().active().taskAssignee(key).list().get(0);
		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		variables.put("howeverSend", 1);
		taskService.complete(t.getId(),variables);
	}

	@GetMapping("decideStatusFalse/{key}")
	public void decideStatusFalse(@PathVariable String key) {
		Task t= taskService.createTaskQuery().active().taskAssignee(key).list().get(0);
		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		Job job = jobService.findOneByKey(key);
		variables.put("howeverSend", 0);
		jobService.delete(job.getId());
		taskService.complete(t.getId(),variables);
	}
}
