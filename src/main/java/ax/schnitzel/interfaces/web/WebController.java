package ax.schnitzel.interfaces.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ax.schnitzel.domain.service.SchnitzelService;

@Controller
public class WebController {

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

		return new ModelAndView("home");
	}

}
