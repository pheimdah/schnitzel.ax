package ax.schnitzel.infrastructure;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import ax.schnitzel.domain.model.Restaurant;
import ax.schnitzel.domain.model.Day;

public class LunchguidenRepositoryTest extends LunchguidenRepository {

	LunchguidenRepository lunchguidenRepository = new LunchguidenRepository();

	private String url = "http://www.aland.com/se/mat_och_nojen/lunchguiden";

	@Test
	public void test_20171019() throws IOException {

		Document doc = Jsoup.parse(this.getClass().getClassLoader().getResourceAsStream("ax/schnitzel/lunchguiden_2017-10-19.html"), "UTF-8", url);
		List<Restaurant> restaurants = lunchguidenRepository.parseRestaurants(doc);
		List<Day> days = lunchguidenRepository.parseDays(doc);
		assertTrue(restaurants.size() == 2);

		// Validate Bistro Savoy
		assertTrue(StringUtils.equals(restaurants.get(0).getId(), "restaurant_7"));
		assertTrue(StringUtils.equals(restaurants.get(0).getName(), "Bistro Savoy"));
		assertTrue(restaurants.get(0).getDishes().size() == 1);
		assertTrue(StringUtils.equals(restaurants.get(0).getDishes().get(0), "Fläskschnitzel med champinjonsås och pommes frites"));

		// Validate Sittkoffska
		assertTrue(StringUtils.equals(restaurants.get(1).getId(), "restaurant_53"));
		assertTrue(StringUtils.equals(restaurants.get(1).getName(), "Restaurang Sittkoffska Gården"));
		assertTrue(restaurants.get(1).getDishes().size() == 1);
		assertTrue(StringUtils.equals(restaurants.get(1).getDishes().get(0), "Schnitzel med vitlökssmör, citron och röstipotatis (H)"));

		// Validate number of days
		assertTrue(days.size() == 5);
	}
	
	@Test
	public void test_20171023() throws IOException {

		Document doc = Jsoup.parse(this.getClass().getClassLoader().getResourceAsStream("ax/schnitzel/lunchguiden_2017-10-23.html"), "UTF-8", url);
		List<Restaurant> restaurants = lunchguidenRepository.parseRestaurants(doc);
		assertTrue(restaurants.isEmpty());
	}
}
