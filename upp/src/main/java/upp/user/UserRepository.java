package upp.user;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository  extends PagingAndSortingRepository<User, Long> {

	User findByUserName(String userName);
	User findByEmail(String email);
	User findByRandomKey(String randomKey);
	User findByEmailAndPassword(String email,String password);
	User findByEmailOrUserName(String email,String userName);
	List<User> findByRole(int role);
}
