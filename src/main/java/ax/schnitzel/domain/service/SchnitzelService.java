package ax.schnitzel.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ax.schnitzel.domain.model.Restaurant;
import ax.schnitzel.infrastructure.LunchguidenRepository;

@Service
public class SchnitzelService {

	/** Logging utility */
	private final static Logger LOG = LoggerFactory.getLogger(SchnitzelService.class);

	/** Injected dependency used to fetch restaurants and their schnitzel dishes of the day */
	@Autowired
	private LunchguidenRepository lunchguiden;

	/** Local cache of {@link LunchguidenRepository} results */
	private List<Restaurant> cachedRestaurants = new ArrayList<Restaurant>(0);

	/** Date when the local cache of restaurants was last successfully updated */
	private Date lastUpdate;

	/** Scheduled method for updating the local cache of restaurants. */
	@PostConstruct
	@Scheduled(cron = "0 0 0,9,10,11,12,13 * * MON-FRI")
	public void updateRestaurants() {
		try {
			List<Restaurant> newlist = lunchguiden.getRestaurants();
			synchronized (cachedRestaurants) {
				cachedRestaurants.clear();
				cachedRestaurants.addAll(newlist);
				this.lastUpdate = new Date();
			}
		}
		catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/** @return immutable list of {@link cachedRestaurants} */
	public List<Restaurant> getRestaurants() {
		synchronized (cachedRestaurants) {
			return Collections.unmodifiableList(cachedRestaurants);
		}
	}

	/** @return see {@link lastUpdate} */
	public Date getLastUpdate() {
		return lastUpdate;
	}
}
