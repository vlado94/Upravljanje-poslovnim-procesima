package upp.offer;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import upp.user.User;

@Data
@Entity
public class Offer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CATEGORY_ID")
	private Long id;
	
	@ManyToOne
	private User company;
	
	private double offerdPrice;
	
	private int rank;
	
	private Date jobFinished;
	
	private String taskID;
	
}
