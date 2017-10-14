package ax.schnitzel.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ax.schnitzel.domain.model.Restaurant;
import ax.schnitzel.infrastructure.LunchguidenRepository;

@Service
public class SchnitzelService {

	@Autowired
	private LunchguidenRepository lunchguiden;

	private List<Restaurant> cachedRestaurants = new ArrayList<Restaurant>(0);

	private Date lastUpdate;

	public SchnitzelService() {

	}

	// @PostConstruct
	@Scheduled(cron = "0 0 0,9 * * MON-FRI")
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
