package upp.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import upp.category.CategoryRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository repository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<User> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public User save(MockUser obj) {
		User user = new User(obj);
		if(obj.getCategories() != null)
			for(long categoryID : obj.getCategories())
				user.getCategories().add(categoryRepository.findOne(categoryID));

		return repository.save(user);
	}
	
	@Override
	public User setRegistrated(long id) {
		User u = repository.findOne(id);
		u.setRegistrated(1);
		u = repository.save(u);
		return u;
	}

	@Override
	public User findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	public String generateRandomKey() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

	@Override
	public User findOneByEmailAndPassword(String email, String password) {
		User user = repository.findByEmailAndPassword(email,password);
		return user;
	}
	
	@Override
	public List<User> findByRole(int role) {
		List<User> retVal = repository.findByRole(role);
		return retVal;
	}
	
	@Override
	public User findOneByEmailOrUserName(String email, String userName) {
		User user = repository.findByEmailOrUserName(email,userName);
		return user;
	}

	@Override
	public User findOneByRandomKey(String key) {
		User user = null;
		ArrayList<User> users = Lists.newArrayList(repository.findAll());
		for(int i=0;i<users.size();i++) {
			if(users.get(i).getRandomKey().equals(key))
				user = users.get(i);
		}
		return user;
	}
}
