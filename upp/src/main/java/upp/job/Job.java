package upp.job;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import upp.category.Category;

@Data
@Entity
public class Job {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CATEGORY_ID")
	private Long id;

	@ManyToOne
	private Category category;
	
	private String descritpion;
	
	private double maxPrice;
	
	private Date auctionLimit;
	
	private Date jobLimit;
	
	private int offersLimit;
	
	public Job(MockJob obj) {
		descritpion = obj.getDescritpion();
		maxPrice = obj.getMaxPrice();
		auctionLimit = obj.getAuctionLimit();
		jobLimit = obj.getJobLimit();
		offersLimit = obj.getOffersLimit();
	}
}
