package upp.job;

import java.util.List;

public interface JobService {
	List<Job> findAll();

	Job findOne(Long id);

	void delete(Long id);

	Job save(MockJob obj);

	Job findOneByKey(String key);
}
