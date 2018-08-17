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

import ax.schnitzel.infrastructure.LunchguidenRepository;
import ax.schnitzel.domain.model.DayMenu;

@Service
public class SchnitzelService {

	/** Logging utility */
	private final static Logger LOG = LoggerFactory.getLogger(SchnitzelService.class);

	/** Injected dependency used to fetch restaurants and their schnitzel dishes of the day */
	@Autowired
	private LunchguidenRepository lunchguiden;

	/** Local cache of {@link LunchguidenRepository} results */
	private final List<DayMenu> cachedDayMenus = new ArrayList<DayMenu>();

	/** Date when the local cache of restaurants was last successfully updated */
	private Date lastUpdate;

	/** Scheduled method for updating the local cache of day menus. */
	@PostConstruct
	@Scheduled(cron = "0 0 0,9,10,11,12,13 * * MON-FRI")
	public void updateContent() {
		try {
			List<DayMenu> newlist = lunchguiden.getDayMenus();
			synchronized (cachedDayMenus) {
				cachedDayMenus.clear();
				cachedDayMenus.addAll(newlist);
				this.lastUpdate = new Date();
			}
		}
		catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/** @return immutable list of {@link cachedDayMenus} */
	public List<DayMenu> getDayMenus() {
		synchronized (cachedDayMenus) {
			return Collections.unmodifiableList(cachedDayMenus);
		}
	}

	/** @return see {@link lastUpdate} */
	public Date getLastUpdate() {
		return lastUpdate;
	}
}
