package upp.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MockUser implements Serializable {
	private long id;
	private String name;
	private String email;
	private String userName;
	private String password;
	private String address;
	private String city;
	private String postNumber;
	private int role;
	private double longitude;
	private double latitude;
	private int registrated;
	private int distance;
	private String companyName;

	private String randomKey;
	private int valid;
	private int sentMail;
	private List<Long> categories;
	
	public MockUser() {
		longitude = 0;
		latitude = 0;
		valid = 0;
		sentMail = 0;
		categories = new ArrayList<Long>();
	}
	public MockUser(User obj) {
		id = obj.getId();
		name = obj.getName();
		email = obj.getEmail();
		userName = obj.getUserName();
		password = obj.getPassword();
		address = obj.getAddress();
		city = obj.getCity();
		postNumber = obj.getPostNumber();
		role = obj.getRole();
		obj.setLatitude(0);
		obj.setLongitude(0);
		randomKey = obj.getRandomKey();
		distance = obj.getDistance();
		companyName = obj.getCompanyName();
		randomKey = obj.getRandomKey();
	}
}
