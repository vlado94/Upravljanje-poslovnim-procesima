package upp.job;

import java.util.List;

import upp.user.User;

public interface JobService {
	List<Job> findAll();

	Job findOne(Long id);

	void delete(Long id);

	Job save(MockJob obj,User u,Job j);

	Job saveObj(Job obj);
	
	int calculetaRang(long jID,long uID,double offerdPrice);
}
