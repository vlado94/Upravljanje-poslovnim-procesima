package upp.job;

import java.util.HashMap;

import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class JobController {

	@Autowired
	private JobService jobService;

	@Autowired
	private RuntimeService runtimeService;
	
	@PostMapping
	public MockJob add(@RequestBody MockJob obj) {
		HashMap<String, Object> variables=new HashMap<>();
		variables.put("job", obj);
		runtimeService.startProcessInstanceByKey("auctionProcess",variables);

		return obj;
	}	
}
