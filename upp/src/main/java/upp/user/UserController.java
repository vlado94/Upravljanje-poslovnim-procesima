package upp.user;

import java.util.HashMap;
import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private TaskService taskService;
	
	
	@GetMapping
	public ResponseEntity<List<User>> findAll() {
		return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		userService.delete(id);
	}
	
	@PostMapping
	public String add(@RequestBody MockUser obj) {
		String retVal = "";
		HashMap<String, Object> variables=new HashMap<>();
		String key = userService.generateRandomKey();
		variables.put("userKey",key);
		runtimeService.startProcessInstanceByKey("registrationProcess",variables);
		Task t= taskService.createTaskQuery().active().taskAssignee(key).list().get(0);

		variables =(HashMap<String, Object>) runtimeService.getVariables(t.getProcessInstanceId());
		obj.setRandomKey(key);
		variables.put("user", obj);
		taskService.complete(t.getId(),variables);
		
		if(obj.getRole() == 2) {
			retVal = "More data";
		}
		else if(obj.getValid() == 1) {
			retVal = "Registrated";
		} else {
			retVal = "Busy username or email";
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
