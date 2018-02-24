package upp.job;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Data;
import upp.category.Category;
import upp.user.User;

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
	
	private String key;
	
	private int howeverSend;
	
	@ManyToMany
	@JoinTable(name = "JOB_COMPANY", joinColumns = @JoinColumn(name = "JOB_ID"), inverseJoinColumns = @JoinColumn(name = "COMPANY_ID"))
	private List<User> companies;

	public Job() {
		companies = new ArrayList<User>();
	}
	public Job(MockJob obj) {
		descritpion = obj.getDescritpion();
		maxPrice = obj.getMaxPrice();
		auctionLimit = obj.getAuctionLimit();
		jobLimit = obj.getJobLimit();
		offersLimit = obj.getOffersLimit();
		key = obj.getJobKey();
		companies = new ArrayList<User>();
	}
}
