package upp.job;

import java.io.Serializable;
import java.sql.Date;

import lombok.Data;

@Data
public class MockJob implements Serializable{
	private long categoryID;
	
	private String descritpion;
	
	private double maxPrice;
	
	private Date auctionLimit;
	
	private Date jobLimit;
	
	private int offersLimit;
	
}
