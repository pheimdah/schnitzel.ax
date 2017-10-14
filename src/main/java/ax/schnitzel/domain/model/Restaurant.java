package ax.schnitzel.domain.model;

import java.util.List;

import lombok.Data;

@Data
public class Restaurant {

	private String id;
	private String name;
	private List<String> schnitzelDishes;

}
