package upp.user;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import upp.job.Job;

@Data
public class LogInResponse {
	private String role;
	private List<Job> jobs;
	
	public LogInResponse() {
		role = "";
		jobs = new ArrayList<Job>();
	}
}
