package ax.schnitzel.infrastructure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ax.schnitzel.domain.model.Restaurant;
import ax.schnitzel.domain.model.Day;
import ax.schnitzel.domain.model.DayMenu;

@Repository
public class LunchguidenRepository {

	/** Logging utility */
	private final static Logger LOG = LoggerFactory.getLogger(LunchguidenRepository.class);

	/** Lunchguiden URI */
	private String url = "https://www.aland.com/lunch/";

	/** Jsoup user-agent header */
	private String userAgent = "schnitzel.ax";

	/** 60 second HTTP request and read timeout */
	private int timeout = 60000;

	private Document lunchguiden;

	/**
	 * @return lunchguiden HTML document
	 * @throws IOException
	 */
	private Document getLunchguidenDocument(String date) throws IOException {
		lunchguiden = Jsoup.connect(url + date).timeout(timeout).userAgent(userAgent).get();
		return lunchguiden;
	}

	/**
	 * @return Lunchguiden schnitzel menu for each available day
	 */
	public List<DayMenu> getDayMenus() throws IOException {
		List<DayMenu> dayMenuList = new ArrayList<DayMenu>();
		List<Day> availableDays = getDays();

		for(final Day day : availableDays) {
			DayMenu dayMenu = new DayMenu();
			dayMenu.setDay(day);
			dayMenu.setRestaurants(getRestaurants(day.getDate()));
			dayMenuList.add(dayMenu);
		}

		return dayMenuList;
	}

	/**
	 * @return list of available days to look for schnitzels
	 * @throws IOException
	 */
	private List<Day> getDays() throws IOException {
		LOG.info("Requesting all available weekdays " + url);
		return parseDays(getLunchguidenDocument(""));
	}

	/**
	 * @return list of restaurants and their schnitzel dishes of the day
	 */
	private List<Restaurant> getRestaurants(String date) throws IOException {
		LOG.info("Requesting today's lunch menu from " + url + date);
		return parseRestaurants(getLunchguidenDocument(date));
	}

	/**
	 * @param lunchguiden a Lunchguiden jsoup Document
	 * @return List of weekdays with possible schnizels and URLs to them
	 */
	public List<Day> parseDays(Document lunchguiden) {
		List<Day> days = new ArrayList<Day>(0);

		for (Element dayElement : lunchguiden.getElementById("lunch_days").getElementsByTag("a")) {
			final Day day = new Day();

			day.setName(dayElement.ownText​());

			day.setDate(dayElement.attr("href").replace("https://www.aland.com/lunch/", ""));

			days.add(day);
		}

		return days;
	}

	/**
	 * @param lunchguiden a Lunchguiden jsoup Document
	 * @return list of restaurants and their schnitzel dishes of the day
	 */
	public List<Restaurant> parseRestaurants(Document lunchguiden) {
		List<Restaurant> restaurants = new ArrayList<Restaurant>(0);

		// TODO: Convert to Java 8 Lambdas?
		for (Element restaurantElement : lunchguiden.getElementById("restaurants").getElementsByClass("restaurant")) {
			Element menuElement = restaurantElement.getElementsByClass("restaurant_menu").get(0);

			if (StringUtils.containsIgnoreCase(menuElement.toString(), "schnitzel")) {

				final Restaurant restaurant = new Restaurant();
				restaurant.setId(restaurantElement.id());
				restaurant.setName(restaurantElement.getElementsByClass("header_left").get(0).text());

				LOG.info("Setting up {}", restaurant.getName());

				List<String> dishes = new ArrayList<String>(0);

				for (Element menuItem : menuElement.getElementsByTag("li")) {

					// TODO: Use fuzzy searching in case "schnitzel" is misspelled in the menu?
					// See https://en.wikipedia.org/wiki/Approximate_string_matching

					if (StringUtils.containsIgnoreCase(menuItem.toString(), "schnitzel")) {
						dishes.add(menuItem.text());
						LOG.info("Adding \"{}\" to {}", menuItem.text(), restaurant.getName());
					}
				}

				restaurant.setDishes(dishes);
				restaurants.add(restaurant);

			}
		}
		return restaurants;
	}

}
