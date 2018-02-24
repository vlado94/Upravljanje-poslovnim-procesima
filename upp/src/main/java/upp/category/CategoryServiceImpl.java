package upp.category;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Override
	public List<Category> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Category findOne(Long id) {
		return repository.findOne(id);
	}
}
