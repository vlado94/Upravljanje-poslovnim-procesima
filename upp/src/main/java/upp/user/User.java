package upp.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USER_ID")
	private Long id;

	private String name;
	private String email;
	private String userName;
	private String password;
	private String address;
	private String city;
	private String postNumber;
	private int registrated;
	
	private int distance;
	private String companyName;
	
	//1 for clasic users 
	//2 for companies
	private int role;
	
	private double longitude;
	private double latitude;
	
	private String randomKey;	

	public User() {}
	public User(MockUser obj) {
		name = obj.getName();
		email = obj.getEmail();
		userName = obj.getUserName();
		password = obj.getPassword();
		address = obj.getAddress();
		city = obj.getCity();
		postNumber = obj.getPostNumber();
		randomKey = obj.getRandomKey();
		role = obj.getRole();
		latitude = obj.getLatitude();
		longitude = obj.getLongitude();
		registrated = obj.getRegistrated();
		distance = obj.getDistance();
		companyName = obj.getCompanyName();
	}
}

