package upp.user;

import java.io.Serializable;

import lombok.Data;

@Data
public class MockUser implements Serializable {
	private String firstName;
	private String lastName;
	private String userName;
	private String mail;
	private String city;
	private int type;
	private long longitude;
	private long latitude;
	
	public MockUser(User obj) {
		firstName = obj.getFirstName();
		lastName = obj.getLastName();
		userName = obj.getUserName();
		mail = obj.getMail();
		city = obj.getCity();
		type = obj.getType();
		longitude = obj.getLongitude();
		latitude = obj.getLatitude();
	}
}
