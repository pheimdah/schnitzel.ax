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

	private String url = "http://www.aland.com/se/mat_och_nojen/lunchguiden";
	private String userAgent = "schnitzel.ax";
	private int timeout = 10000;

	public List<Restaurant> get() throws IOException {
		List<Restaurant> restaurants = new ArrayList<Restaurant>(0);

		final Document lunchguiden = Jsoup.connect(url).timeout(timeout).userAgent(userAgent).get();
		LOG.info("Downloading " + url);

		// TODO: Convert to Java 8 Lambdas
		for (Element restaurantElement : lunchguiden.getElementById("restaurants").getElementsByClass("restaurant")) {
			String id = restaurantElement.id();

			Element menuElement = restaurantElement.getElementsByClass("restaurant_menu").get(0);

			if (StringUtils.containsIgnoreCase(menuElement.toString(), "schnitzel")) {

				Restaurant restaurant = new Restaurant();
				restaurant.setId(id);
				restaurant.setName(restaurantElement.getElementsByClass("header_left").get(0).text());

				List<String> highlightedDishes = new ArrayList<String>(0);
				
				for (Element menuItem : menuElement.getElementsByTag("li")) {
					if (StringUtils.containsIgnoreCase(menuItem.toString(), "pasta")) {
						highlightedDishes.add(menuItem.text());
					}
				}
				
				restaurant.setSchnitzelDishes(highlightedDishes);
				restaurants.add(restaurant);
				LOG.info("Found " + restaurant.toString());
			}
		}
		return restaurants;
	}
}
