package ax.schnitzel.interfaces.web;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ax.schnitzel.domain.service.SchnitzelService;
import ax.schnitzel.domain.model.Day;
import ax.schnitzel.domain.model.DayMenu;

@Controller
public class HomeController {

	/** Injected dependency used for all schnitzel related operations */
	@Autowired
	SchnitzelService schnitzelService;
	String chosenDay = "";

	/**
	 * @param model see {@link org.springframework.ui.Model}
	 * @return see {@link org.springframework.web.servlet.ModelAndView#ModelAndView(String)}
	 */
	@RequestMapping("/")
	public ModelAndView home(final Model model, @RequestParam(value  = "day", defaultValue = "") String day) {
		chosenDay = "";
		int dayIndex = 0;

		List<DayMenu> dayMenus = schnitzelService.getDayMenus();

		// If the query parameter "day" has been requested
		if(!day.equals("")) {
			dayIndex = findDayIndex(dayMenus, day);
		}

		prepareData(model, dayMenus, dayIndex);

		return new ModelAndView("home");
	}

	/**
	 * Find index of the requested day
	 * @param dayMenus
	 * @param day The requested date
	 * @return The day index if a valid day was found
	 */
	private int findDayIndex(List<DayMenu> dayMenus, String day) {
		int dayIndex = 0;
		boolean daySet = false;

		for(final DayMenu dayMenu : dayMenus) {
			if(dayMenu.getDay().getDate().equals(day)) {
				chosenDay = day;
				daySet = true;
				break;
			}

			dayIndex++;
		}

		return daySet ? dayIndex : 0;
	}

	/**
	 * Prepare the model with data to be rendered
	 * @param model
	 * @param dayMenus
	 * @param dayIndex
	 */
	private void prepareData(final Model model, List<DayMenu> dayMenus, int dayIndex) {
		List<Day> days = new ArrayList<Day>();

		for(final DayMenu dayMenu : dayMenus) {
			days.add(dayMenu.getDay());
		}

		model.addAttribute("days", days);
		model.addAttribute("chosenDay", chosenDay);
		model.addAttribute("restaurants", dayMenus.get(dayIndex).getRestaurants());

		Date lastUpdate = schnitzelService.getLastUpdate();
		model.addAttribute("lastUpdated", lastUpdate);
		model.addAttribute("updatedToday", lastUpdate != null && DateUtils.isSameDay(lastUpdate, new Date()));

		DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
		model.addAttribute("isWeekend", (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY));
	}

}
