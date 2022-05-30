package nodeInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class WayInformation extends Information {
	
	ArrayList<way.Way> ListOfWays; 
	
	public WayInformation() {
		// TODO Auto-generated constructor stub
		ListOfWays = new ArrayList<>();
	}
	
	 public void initialise(ArrayList<way.Way> W) throws NumberFormatException, IOException {
		 
		 File file = new File("src/com/projet/g07/Carte.fxml");
		 FileInputStream fileStream = new FileInputStream(file);
		 InputStreamReader input = new InputStreamReader(fileStream);
		 BufferedReader reader = new BufferedReader(input);
		 String line;
		 way.Way elementWay = null;
		 
		 while ((line = reader.readLine()) != null) {
	      	 if(line.contains("<way")) {
	      		String idWay = splitString(line,"id=");
	      		line = reader.readLine();
	      		ArrayList<Long> temporyRefNode = new ArrayList<>();
	      		while(line.contains("<nd")) {
	      			String idNd = splitString(line,"ref=");
	      			temporyRefNode.add(Long.parseLong(idNd));
	      			line = reader.readLine();
	      		}
	      		ArrayList<String> temporyTag = new ArrayList<>();
	      		while(line.contains("<tag")) {
	      			String k = splitString(line,"k=");
	      			temporyTag.add(k);
	      			String v = splitString(line,"v=");
	      			line = reader.readLine();
	      			temporyTag.add(v);
	      		}
	      		elementWay = new way.Way(Long.parseLong(idWay), temporyRefNode, temporyTag);
	      		W.add(elementWay);
	      	}
	     }
		 reader.close();
	 }
	 
	 public static void main(String[] args) throws NumberFormatException, IOException {
		 WayInformation w = new WayInformation();
		 w.initialise(w.ListOfWays);
		 int x = 0;
		 while(x < w.ListOfWays.size()) {
			 System.out.println("id : " + w.ListOfWays.get(x).getId());
			 int y = 0, z = 0;
			 while(y<w.ListOfWays.get(x).getRef().size()) {
				 System.out.println("ref : " + w.ListOfWays.get(x).getRef().get(y));
				 y++;
			 }
			 while(z<w.ListOfWays.get(x).getTag().size()) {
				 if(z%2==0) {
					 System.out.println("tag k : " + w.ListOfWays.get(x).getTag().get(z));
				 }
				 else {
					 System.out.println("tag v : " + w.ListOfWays.get(x).getTag().get(z));
				 }
				 
				 z++;
			 }
			 x++;
		 }
	 }
}


//public static void main(String[] args)
//{ 
//  try {
//      File file = new File("src/com/projet/g07/Carte.fxml");
//      FileInputStream fileStream = new FileInputStream(file);
//      InputStreamReader input = new InputStreamReader(fileStream);
//      BufferedReader reader = new BufferedReader(input);
//      String line;
//      
//      while ((line = reader.readLine()) != null) {
//      	if(line.contains("<way")) {
//      		String idWay = splitString(line,"id=");
//      		System.out.println("id way :" + idWay);
//      		line = reader.readLine();
//      		while(line.contains("<nd")) {
//      			String idNd = splitString(line,"ref=");
//          		System.out.println("id nd :" + idNd);
//      			line = reader.readLine();
//      		}
//      		while(line.contains("<tag")) {
//      			String k = splitString(line,"k=");
//      			System.out.println("tag k :" + k);
//      			String v = splitString(line,"v=");
//      			System.out.println("tag v :" + v);
//      			line = reader.readLine();
//      		}
//      	}
//      }
//      reader.close();
//  }
//  catch(IOException e) {
//      System.out.println(e);
//  } 
//}