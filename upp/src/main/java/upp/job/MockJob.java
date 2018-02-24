package upp.job;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MockJob implements Serializable{
	private long categoryID;
	
	private String descritpion;
	
	private double maxPrice;
	
	private Date auctionLimit;
	
	private Date jobLimit;
	
	private int offersLimit;
	
	private List<Long> companyIDS;
	
	private long userID;
	
	private int sentMail;
	
	private String jobKey;
	
	public MockJob() {
		companyIDS = new ArrayList<Long>();
	}
	
}
