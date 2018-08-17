package ax.schnitzel.domain.model;

import lombok.Data;

/** Domain model representing a weekday with a menu in the lunchguide. */
@Data
public class Day {

    /** Date of the weekday's menu */
    private String date;

    /** Day name */
    private String name;

}
