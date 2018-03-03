package upp.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import upp.category.Category;
import upp.category.CategoryService;
import upp.user.User;
import upp.user.UserService;

@Component
public class JobComponentService {

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private JobService jobService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private RuntimeService runtimeService;
	

	@Autowired
	private HttpSession httpSession;
	
	public MockJob defineCompanies(MockJob job, String executionId) {
		List<User> companies = userService.findByRole(2);
		List<User> validCompanies = new ArrayList<>();
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(executionId);
		User u = userService.findOne(job.getUserID());
		for(int i=0;i<companies.size();i++) {
			double distance = getDistance(companies.get(i).getLatitude(),u.getLatitude(), companies.get(i).getLongitude(), u.getLongitude(), 0, 0);
			for(int j=0;j<companies.get(i).getCategories().size();j++) {
				if(companies.get(i).getCategories().get(j).getId() == job.getCategoryID() && distance < companies.get(i).getDistance()) {
					validCompanies.add(companies.get(i));
					break;
				}
			}
		}

		if(validCompanies.size() > 0)
			job.setCompanyIDS(getRandomCompanies(validCompanies,job.getOffersLimit(),job));
		else if (validCompanies.size() == 0)
			job.setCompanyIDS(new ArrayList<Long>());
		else 
			for(int i=0;i<validCompanies.size();i++)
				job.getCompanyIDS().add(validCompanies.get(i).getId());

		Job j = (Job)variables.get("jobObj");
		Job jobObj = jobService.save(job,u,j);
		
		variables.put("jobObj", jobObj);
		runtimeService.setVariables(executionId, variables);
		return job;
	}
	
	private List<Long> getRandomCompanies(List<User> foundedCompanies, int numberOfOffers, MockJob job) {
		List<Long> retVal = new ArrayList<Long>();
		List <User> companyForRemove = new ArrayList<User>();
		/*for(int i=0;i<job.getCompanyIDS().size();i++)
			for(int j=0;j<foundedCompanies.size();j++)
				if((long)job.getCompanyIDS().get(i) == (long)foundedCompanies.get(j).getId())
					companyForRemove.add(foundedCompanies.get(j));*/
		for(int i=0;i<job.getSendMailsToCompanies().size();i++)
			for(int j=0;j<foundedCompanies.size();j++)
				if((long)job.getCompanyIDS().get(i) == (long)foundedCompanies.get(j).getId())
					companyForRemove.add(foundedCompanies.get(j));
		foundedCompanies.removeAll(companyForRemove);
		/*for(int i=0;i<foundedCompanies.size();i++)
			if(!job.getCompanyIDS().contains(foundedCompanies.get(i).getId()) && numberOfOffers != retVal.size())
				retVal.add(foundedCompanies.get(i).getId());
		*/
		Collections.shuffle(foundedCompanies);

		for(int i =0;i<foundedCompanies.size();i++) {
			if(numberOfOffers != retVal.size())
				retVal.add(foundedCompanies.get(i).getId());
		}
		return retVal;
	}
	
	private double getDistance(double lat1, double lat2, double lon1,
	        double lon2, double el1, double el2) {

	    final int R = 6371; // Radius of the earth
	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters
	    double height = el1 - el2;
	    distance = Math.pow(distance, 2) + Math.pow(height, 2);
	    double distanceMeters = Math.sqrt(distance);
	    return distanceMeters/1000;
	}
	
	public MockJob sendMailForNotEnough(MockJob obj, String processID) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom("isarestorani@gmail.com");
		User u = userService.findOne(obj.getUserID());
		Category c = categoryService.findOne(obj.getCategoryID());
		helper.setTo(u.getEmail());
		helper.setSubject("Not enough company for category "+c.getName()+" , choose next step on link.");
		String text = "Visit your profile, no enough offers";
		helper.setText(text);
		//mailSender.send(message);
		obj.setSentMail(1);
		return obj;
	}
	
	public List<User> prepareCompaniesForNotify(String processID) {
		List<User> retVal = new ArrayList<User>();
		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(processID);
		MockJob mock = (MockJob)variables.get("job");
		Job j  = (Job)variables.get("jobObj");
		for(int i=0;i<j.getCompanies().size();i++) {
			if(!mock.getSendMailsToCompanies().contains(j.getCompanies().get(i).getId())) {
				mock.getSendMailsToCompanies().add(j.getCompanies().get(i).getId());
				retVal.add(j.getCompanies().get(i));
			}
		}
		variables.put("job", mock);
		variables.put("expectOffers", retVal.size());
		runtimeService.setVariables(processID, variables);
		return retVal;
	}
	
	public String sendCompanyMail(User firm) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom("sepftn2017@gmail.com");
		helper.setTo(firm.getEmail());
		helper.setSubject("One more demmand");
		String text = "You get new demmand, visit your profile.";
		helper.setText(text);
		//mailSender.send(message);
		return firm.getId().toString();		
	}
	
	public String calculateRangForOffer(String processID,Job jobObj) {
		User u = userService.findOne((Long)httpSession.getAttribute("userID"));
		HashMap<String, Object> variables =(HashMap<String, Object>) runtimeService.getVariables(processID);
		int rank = jobService.calculetaRang(jobObj.getId(),u.getId(),(double)variables.get("currentOffer"));
		variables.put("currentRank",""+ rank);
		variables.put("currentProcess", processID);
		runtimeService.setVariables(processID, variables);
		return u.getId().toString();
	}
}
