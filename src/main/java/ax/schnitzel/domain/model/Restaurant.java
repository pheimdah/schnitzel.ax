package ax.schnitzel.domain.model;

import java.util.List;

import lombok.Data;

/** Domain model representing a restaurant along with their schnitzel dishes of the day. */
@Data
public class Restaurant {

	/** Restaurant ID, used for front-end magic */
	private String id;

	/** Restaurant name */
	private String name;

	/** Simple list of dishes */
	private List<String> dishes;

}
