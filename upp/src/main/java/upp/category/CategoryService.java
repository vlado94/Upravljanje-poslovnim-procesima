package upp.category;

import java.util.List;

public interface CategoryService {
	List<Category> findAll();

	Category findOne(Long id);
}
