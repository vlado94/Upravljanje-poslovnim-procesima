package upp.user;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

@Component
public class UserComponentService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserService userService;
	
	public MockUser defineGeoLocation(MockUser obj) {
		GeoApiContext context = new GeoApiContext();
		context.setApiKey("AIzaSyDT9LFTvIEcj1GhGxjZkp8_CO2gFjkyBPY");
		GeocodingResult[] results;
		try {
			results = GeocodingApi.geocode(context, obj.getCity()).await();
			obj.setLongitude((long) results[0].geometry.location.lng);
			obj.setLatitude((long) results[0].geometry.location.lat);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public MockUser validateUserForRegistration(MockUser obj) {
		User user = userService.findOneByEmailOrUserName(obj.getEmail(),obj.getUserName());
		if(user == null)
			obj.setValid(1);
		return obj;
	}

	public MockUser sendMailForRegistration(MockUser obj) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom("isarestorani@gmail.com");
		helper.setTo(obj.getEmail());
		helper.setSubject("Registration request");
		String text = "localhost:8080/user/confirmRegistration/"+obj.getRandomKey();
		helper.setText(text);
		mailSender.send(message);
		obj.setSentMail(1);
		User u =userService.save(obj);
		obj.setId(u.getId());
		return obj;
	}
	
	public void disableUser(MockUser obj) {
		userService.delete(obj.getId());
	}
	
	public User confirmRegistration(MockUser obj) {
		User user = userService.findOne(obj.getId());
		if(user != null) {
			obj.setRegistrated(1);
			user = userService.save(obj);
		}
		return user;
	}	
}
