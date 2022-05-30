package nodeInfo;

import java.io.IOException;
import java.util.ArrayList;

public class LinkedInformation {
	
	WayInformation way = new WayInformation();
	NodeInformation node = new NodeInformation();
	
	ArrayList<Long> identifiant;
	ArrayList<Double> coordonnee;

	public LinkedInformation() {
		this.identifiant = new ArrayList<>();
		this.coordonnee = new ArrayList<>();
	}
	
	public LinkedInformation initialise(LinkedInformation linked) throws NumberFormatException, IOException {
		int x=0,y,z;
		
		this.way.initialise(this.way.ListOfWays);
		this.node.initialise(this.node.ListOfNodes);
		
		while(x < this.way.ListOfWays.size()) {
			 y = 0; z = 0;
			 while(y< this.way.ListOfWays.get(x).getRef().size()) {
				 System.out.println("ref : " + this.way.ListOfWays.get(x).getRef().get(y));
				 y++;
			 }
			 x++;
		 }
		
		return linked;
		
	}
	
	public static void main(String[] args) {
		
	}

}
