package classes;

import java.util.ArrayList;
import java.util.List;


public class InfoItinerary {

	private ArrayList<String> infos = new ArrayList<>();
	private ArrayList<String> listStreetName = new ArrayList<String>();
	private String destinationStreetName;
	private String startStreetName;
	private double distanceTotal = 0;
	private double distanceRue = 0;
	private String streetName = " ";
	
	public void setListInfo(List<OsmNode> path, OsmGraph osmGraph, String destinationStreetName, String startStreetName) {
		this.destinationStreetName = destinationStreetName;
		this.startStreetName = startStreetName;
		double angle = 0;
		double preAngle = 0;
		for(int i=0; i<path.size()-1; i++) {
			for (OsmWay way: osmGraph.getOsmWayMap().values()) {
				if( way.getChildOsmNodeList().contains(path.get(i).getId()) && way.getChildOsmNodeList().contains(path.get(i+1).getId()) ) {
					this.distanceRue += getDistance(path.get(i), path.get(i+1));
					if(way.getName()!=null && way.getName().equals(streetName)==false && this.distanceRue>10) {
						angle = getAngle(path.get(i), path.get(i+1));
						streetName = way.getName();
						this.infos.add(getDirection(angle, preAngle, i) + " sur " + streetName + " (" + String.format("%.1f", this.distanceRue) + "m) 	");
						this.distanceTotal += this.distanceRue;
						this.distanceRue = 0;
						preAngle = angle;
					}
					
				}
			}
		}
	}
	
	public ArrayList<String> getListInfo(){
		return this.infos;
	}
	
	public double getDistanceTotal() {
		return this.distanceTotal;
	}
	
	public void resetListInfo() {
		this.infos = new ArrayList<>();
	}
	
	
	
	private double getAngle(OsmNode node1, OsmNode node2) {
		Double y1 = node1.getLat();
		Double x1 = node1.getLon();
		
		Double y2 = node2.getLat();
		Double x2 = node2.getLon();

		Double dy = y1 - y2;
		Double dx = x1 - x2;
		
		Double theta = Math.atan2(dy, dx);
		theta *= 180 / Math.PI;
		
		theta -= 180;
		
		if (theta < 0) theta = 360 + theta;
		
		return theta;
	}
	
	/** 
     * Calculate the distance between two coordinates
     * @param start 
     * @param end 
     * @return metre
     */
	private double getDistance(OsmNode start, OsmNode end) {
		double lat1 = (Math.PI/180)*start.getLat();  
		double lat2 = (Math.PI/180)*end.getLat();
		double lon1 = (Math.PI/180)*start.getLon();  
        double lon2 = (Math.PI/180)*end.getLon();
        //Earth radius
        double R = 6371; 
        double distance =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*R;
		distance *= 1000;
		return distance;
	}
	
	private String getDirection(double angle, double preAngle, int index) {
//		System.out.println("getDirection:  " + angle);
		if(index == 0) {
			System.out.println("angle="+angle);
			if((angle>=0 && angle<45) || (angle>=315 && angle<360)) {
				return " ↑ Prendre la direction Sud";
			}
			else if((angle>=45 && angle<135)) {
				return " ↑ Prendre la direction Ouest";
			}
			else if((angle>=135 && angle<225)) {
				return " ↑ Prendre la direction Est";
			}
			else if((angle>=225 && angle<315)) {
				return " ↑ Prendre la direction Nord";
			}
		}
//		System.out.println("angle: "+ angle);
//		System.out.println("preAngle: "+preAngle);
		else {
			if(angle > 180) { angle = angle - 180; }
			if(preAngle > 180) { preAngle = preAngle - 180; }
			if(angle < preAngle) {
				return " → Prendre à gauche ";
			}
			else if(angle > preAngle) {
				return " → Prendre à droit ";
			}
			else {
				return " Avancer tout droite ";
			}
		}
		return " ";
	}
	
}
