package node;

import java.util.ArrayList;

public class Node {
	
	private long id;
	private double lat;
	private double lon;
	ArrayList<String> tag;
	
	public Node(long id, double lat, double lon, ArrayList<String> tag) {
		super();
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.tag = tag;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
 
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
	
	public ArrayList<String> getTag() {
		return tag;
	}

	public void setTag(ArrayList<String> tag) {
		this.tag = tag;
	}
	
}
