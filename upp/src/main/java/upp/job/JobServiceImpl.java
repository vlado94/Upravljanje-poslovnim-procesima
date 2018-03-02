package upp.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import upp.category.CategoryRepository;
import upp.offer.Offer;
import upp.user.User;
import upp.user.UserRepository;

@Service
@Transactional
public class JobServiceImpl implements JobService {

	@Autowired
	private JobRepository repository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<Job> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Job findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}	

	@Override
	public Job saveObj(Job obj) {
		return repository.save(obj);
	}

	@Override
	public Job save(MockJob obj,User u,Job j) {
		Job job = new Job();
		if(j != null)
			job = repository.findOne(j.getId());
		else {
			job = new Job(obj);
			job.setCategory(categoryRepository.findOne(obj.getCategoryID()));
			job.setOwner(u);
		}
		if(obj.getCompanyIDS() != null)
			for(long companyID : obj.getCompanyIDS())
				if(!job.getCompanies().contains(userRepository.findOne(companyID)))
					job.getCompanies().add(userRepository.findOne(companyID));
		job = repository.save(job);
		return job;
	}

	@Override
	public int calculetaRang(long jID,long uID,double offerdPrice) {
		
		int retVal = 0;
		Job job = repository.findOne(jID);
		Offer newOffer = new Offer();
		newOffer.setCompany(userRepository.findOne(uID));
		newOffer.setOfferdPrice(offerdPrice);
		job.getOffers().add(newOffer);
		Offer[] test = (Offer[]) job.getOffers().toArray(new Offer[job.getOffers().size()]);
		Offer temp = new Offer();  
		for(int i=0; i < test.length; i++){  
            for(int j=1; j < (test.length-i); j++){  
                 if(test[j-1].getOfferdPrice() > test[j].getOfferdPrice()){  
               	 temp = test[j-1];  
                    test[j-1] = test[j];  
                    test[j] = temp;  
                }                        
            }  
       }  
       job.setOffers(new ArrayList<Offer>(Arrays.asList(test)));
       for(int i=0; i < job.getOffers().size(); i++){  
    	   if(job.getOffers().get(i).getCompany().getId() == uID) 
    		   retVal = i;
       }
       for(int i=0; i < job.getOffers().size(); i++){  
    	   if(job.getOffers().get(i).getId() == null) 
    		   job.getOffers().remove(i);
       }
       
       retVal += 1;
       return retVal;
	}
}
