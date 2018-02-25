package upp.offer;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
@Transactional
public class OfferServiceImpl implements OfferService {

	@Autowired
	private OfferRepository repository;

	@Override
	public List<Offer> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Offer findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	public Offer save(Offer obj) {
		return repository.save(obj);
	}
}
