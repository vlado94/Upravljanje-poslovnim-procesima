package upp.offer;

import java.util.List;

public interface OfferService {
	List<Offer> findAll();

	Offer findOne(Long id);

	void delete(Long id);

	Offer save(Offer obj);
}
