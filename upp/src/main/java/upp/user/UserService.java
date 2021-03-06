package upp.user;

import java.util.List;

public interface UserService {
	List<User> findAll();

	User save(MockUser obj);

	User setRegistrated(long id);

	User findOne(Long id);
	
	User findOneByEmailAndPassword(String email,String password);

	User findOneByEmailOrUserName(String email,String userName);

	User findOneByRandomKey(String key);

	List<User> findByRole(int role);
	
	void delete(Long id);
	
	String generateRandomKey();
	
	User saveObj(User obj);
}
