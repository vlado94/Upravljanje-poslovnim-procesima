package upp.user;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
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
	private HttpSession httpSession;
	
	@Autowired
	private RuntimeService runtimeService;
	
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
	public String checkRole() {
		String retVal = "";
		Object id = httpSession.getAttribute("userID");
		if(id == null) {
			retVal = "Redirect";
		}
		else {
			long userID = (long) id;
			User u = userService.findOne(userID);
			if(u.getRole() == 1) {
				retVal = "User-"+u.getName();				
			}
			else if(u.getRole() == 2) {
				retVal = "Company-"+u.getName();
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
