package upp.job;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface JobRepository  extends PagingAndSortingRepository<Job, Long> {
}
