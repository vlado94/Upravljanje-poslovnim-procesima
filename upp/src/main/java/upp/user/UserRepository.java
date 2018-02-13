package upp.user;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository  extends PagingAndSortingRepository<User, Long> {

	User findByUserName(String userName);
	User findByMail(String mail);
}
