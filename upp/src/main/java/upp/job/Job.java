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
import javax.persistence.OneToMany;

import lombok.Data;
import upp.category.Category;
import upp.offer.Offer;
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
	
	private String description;
	
	private double maxPrice;
	
	private Date auctionLimit;
	
	private Date jobLimit;
	
	private int offersLimit;
	
	private int howeverSend;
	
	@ManyToOne
	private User owner;
	
	@ManyToMany
	@JoinTable(name = "JOB_COMPANY", joinColumns = @JoinColumn(name = "JOB_ID"), inverseJoinColumns = @JoinColumn(name = "COMPANY_ID"))
	private List<User> companies;

	@OneToMany
	@JoinTable(name = "JOB_OFFER", joinColumns = @JoinColumn(name = "JOB_ID"), inverseJoinColumns = @JoinColumn(name = "OFFER_ID"))
	private List<Offer> offers;

	
	public Job() {
		companies = new ArrayList<User>();
		offers = new ArrayList<Offer>();
	}
	public Job(MockJob obj) {
		description = obj.getDescritpion();
		maxPrice = obj.getMaxPrice();
		auctionLimit = obj.getAuctionLimit();
		jobLimit = obj.getJobLimit();
		offersLimit = obj.getOffersLimit();
		companies = new ArrayList<User>();
	}
}
