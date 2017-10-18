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

	@Autowired
	private LunchguidenRepository lunchguiden;

	private List<Restaurant> cachedRestaurants = new ArrayList<Restaurant>(0);

	private Date lastUpdate;

	public SchnitzelService() {

	}

	@PostConstruct
	@Scheduled(cron = "0 0 0,9,10,11,12,13 * * MON-FRI")
	public void updateRestaurants() {
		try {
			List<Restaurant> newlist = lunchguiden.get();
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

	public List<Restaurant> getRestaurants() {
		synchronized (cachedRestaurants) {
			return Collections.unmodifiableList(cachedRestaurants);
		}
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}
}
