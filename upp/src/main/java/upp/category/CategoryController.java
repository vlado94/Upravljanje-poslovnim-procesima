package upp.category;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import upp.user.User;
import upp.user.UserService;

@RestController
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private HttpSession httpSession;
	

	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<List<Category>> findAll() {
		
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		HashMap<String, Object> variables=new HashMap<>();
		variables.put("userID",u.getId().longValue());
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("auctionProcess",variables);

		List<Category> cats = categoryService.findAll();
		Category cat = new Category();
		cat.setName(pi.getId());
		cats.add(cat);
		
		return new ResponseEntity<>(cats, HttpStatus.OK);
	}	
	
	@GetMapping("second")
	public ResponseEntity<List<Category>> findAll2() {
		return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("startReg")
	public String startRegistration() {
		HashMap<String, Object> variables=new HashMap<>();
		String key = userService.generateRandomKey();
		variables.put("userKey",key);
		runtimeService.startProcessInstanceByKey("registrationProcess",variables);

		return key;
	}	
}
