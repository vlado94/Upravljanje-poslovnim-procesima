package upp.job;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import upp.category.CategoryRepository;
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
	public Job save(MockJob obj,User u) {
		Job job = new Job(obj);
		job.setCategory(categoryRepository.findOne(obj.getCategoryID()));
		job.setOwner(u);
		if(obj.getCompanyIDS() != null)
			for(long companyID : obj.getCompanyIDS())
				job.getCompanies().add(userRepository.findOne(companyID));
		job = repository.save(job);
		return job;
	}

	@Override
	public Job calculetaRang(Job obj) {
		
		return obj;
	}
}
