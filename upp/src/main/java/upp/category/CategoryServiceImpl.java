package upp.category;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository repository;

	@Autowired
	public CategoryServiceImpl(final CategoryRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Category> findAll() {
		return Lists.newArrayList(repository.findAll());
	}
}
