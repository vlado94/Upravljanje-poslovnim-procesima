package upp.user;

import java.util.List;

public interface UserService {
	List<User> findAll();

	User save(User obj);

	User findOne(Long id);

	void delete(Long id);
	
	String generateRandomKey();
}
