package ax.schnitzel.interfaces.web;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ax.schnitzel.domain.service.SchnitzelService;

@Controller
public class HomeController {

	/** Injected dependency used for all schnitzel related operations */
	@Autowired
	SchnitzelService schnitzelService;

	/**
	 * @param model see {@link org.springframework.ui.Model}
	 * @return see {@link org.springframework.web.servlet.ModelAndView#ModelAndView(String)}
	 */
	@RequestMapping("/")
	public ModelAndView home(final Model model) {

		model.addAttribute("restaurants", schnitzelService.getRestaurants());
		model.addAttribute("lastUpdated", schnitzelService.getLastUpdate());

		DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
		model.addAttribute("isWeekend", (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY));

		return new ModelAndView("home");
	}

}
