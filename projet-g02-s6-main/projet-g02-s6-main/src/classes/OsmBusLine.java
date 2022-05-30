package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
* An object representing a bus line
*/
public class OsmBusLine {

	/** The name of the busLine. */
	private String name;
	
	/** The ID of the busLine. */
	private Long id;
	
	/** The list of the ways of the line */
	private List<Long> listRefWays = new ArrayList<Long>();
	
	/** The list of the stops of the line. */
	private List<Long> listRefStops = new ArrayList<Long>();
	
	public OsmBusLine(String theName, Long theId, List<Long> theListRefWays, List<Long> theListRefStops) {
		this.name = theName;
		this.id = theId;
		this.listRefWays = theListRefWays;
		this.listRefStops = theListRefStops;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Long> getListRefWays() {
		return listRefWays;
	}

	public List<Long> getListRefStops() {
		return listRefStops;
	}

}
