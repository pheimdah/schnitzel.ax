package ax.schnitzel.domain.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ax.schnitzel.domain.model.Restaurant;
import ax.schnitzel.infrastructure.LunchguidenRepository;

@Service
public class SchnitzelService {

	/** Logging utility */
	private final static Logger LOG = LoggerFactory.getLogger(SchnitzelService.class);

	/**
	 * Injected dependency used to fetch restaurants and their schnitzel dishes of
	 * the day
	 */
	@Autowired
	private LunchguidenRepository lunchguiden;

	/** @return today's restaurants and their dishes */
	public List<Restaurant> getRestaurants() {
		try {
			return lunchguiden.getRestaurants();
		}
		catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}
}
