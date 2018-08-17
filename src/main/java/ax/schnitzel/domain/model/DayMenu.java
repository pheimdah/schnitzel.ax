package ax.schnitzel.domain.model;

import java.util.List;

import lombok.Data;

import ax.schnitzel.domain.model.Restaurant;
import ax.schnitzel.domain.model.Day;

/** Domain model representing a day's schnitzel restaurants from the Lunchguide. */
@Data
public class DayMenu {

    /** Weekday in the Lunchguide */
    private Day day;

    /** Restaurants from the Lunchguide */
    private List<Restaurant> restaurants;

}
