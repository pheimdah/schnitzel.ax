package ax.schnitzel.infrastructure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ax.schnitzel.domain.model.Restaurant;

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

	/**
	 * @return list of restaurants and their schnitzel dishes of the day
	 * @throws IOException
	 */
	public List<Restaurant> getRestaurants() throws IOException {

		Document lunchguiden = Jsoup.connect(url).timeout(timeout).userAgent(userAgent).get();
		LOG.info("Requesting today's lunch menu from " + url);
		return parse(lunchguiden);
	}

	/**
	 * @param lunchguiden a Lunchguiden jsoup Document
	 * @return list of restaurants and their schnitzel dishes of the day
	 */
	List<Restaurant> parse(Document lunchguiden) {

		List<Restaurant> restaurants = new ArrayList<Restaurant>(0);

		// TODO: Convert to Java 8 Lambdas?
		for (Element restaurantElement : lunchguiden.getElementsByClass("restaurant")) {

			if (StringUtils.containsIgnoreCase(restaurantElement.toString(), "schnitzel")) {

				final Restaurant restaurant = new Restaurant();
				restaurant.setId(restaurantElement.id());
				restaurant.setName(restaurantElement.getElementsByClass("restaurant-name").get(0).text());

				LOG.info("Setting up {}", restaurant.getName());

				List<String> dishes = new ArrayList<String>(0);

				for (Element dish : restaurantElement.getElementsByClass("dish-title")) {

					String title = dish.getElementsByClass("title").get(0).text();

					Elements text = dish.getElementsByClass("text");
					if (text != null) {
						title += " " + text.text();
					}

					Elements attributes = dish.getElementsByClass("attributes");
					if (attributes != null) {
						title += " " + attributes.text();
					}

					if (StringUtils.containsIgnoreCase(title, "schnitzel")) {
						dishes.add(title);
						LOG.info("Adding \"{}\" to {}", title, restaurant.getName());
					}
				}

				restaurant.setDishes(dishes);
				restaurants.add(restaurant);

			}
		}
		return restaurants;
	}

}
