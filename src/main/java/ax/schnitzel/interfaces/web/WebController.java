package ax.schnitzel.interfaces.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import ax.schnitzel.domain.service.SchnitzelService;

@RestController
public class WebController {

	@Autowired
	SchnitzelService schnitzelService;

	public WebController() {

	}

	@RequestMapping("/")
	public ModelAndView getRestaurants(final Model model) {
		model.addAttribute("restaurants", schnitzelService.getRestaurants());
		model.addAttribute("lastUpdated", schnitzelService.getLastUpdate());

		return new ModelAndView("home");
	}

	@RequestMapping("/updateRestaurants")
	public void updateRestaurants() {
		schnitzelService.updateRestaurants();
	}
	
}
