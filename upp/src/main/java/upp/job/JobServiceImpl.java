package upp.job;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import upp.category.CategoryRepository;

@Service
@Transactional
public class JobServiceImpl implements JobService {

	@Autowired
	private JobRepository repository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<Job> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Job findOne(Long id) {
		return repository.findOne(id);
	}
	
	@Override
	public Job findOneByKey(String key) {
		return repository.findOneByKey(key);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	public Job save(MockJob obj) {
		Job job = new Job(obj);
		job.setCategory(categoryRepository.findOne(obj.getCategoryID()));
		job = repository.save(job);
		return job;
	}
}
