package upp.user;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	private final UserRepository repository;

	@Autowired
	public UserServiceImpl(final UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<User> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public User save(User obj) {
		return repository.save(obj);
	}

	@Override
	public User findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}
}
