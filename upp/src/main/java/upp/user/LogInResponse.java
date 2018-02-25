package upp.user;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import upp.job.MockJob;

@Data
public class LogInResponse {
	private String role;
	private List<MockJob> jobs;
	
	public LogInResponse() {
		role = "";
		jobs = new ArrayList<MockJob>();
	}
}
