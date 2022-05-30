package djikstra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class TestDjikstra {
	public static void main(String[] args) throws IOException {
		System.out.println("On est dans le main ");
		
		File file = new File("xml//carteItineraire.fxml");
        FileInputStream fileStream = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(fileStream);
        BufferedReader reader = new BufferedReader(input);
        
        //getway(1888195584, file);
        if (checkPossibility(2000103031,80448401, file)) {
        	System.out.println("You can");
        }
        else {
        	System.out.println("You can't");
        }
        
		
	}
	
public static boolean checkPossibility(long id1,long id2,File file) throws IOException{
	
		System.out.println("okay");
		String[] arr1 = new String[(getway(id1, file)).length];
		System.arraycopy((getway(id1, file)), 0, arr1, 0, (getway(id1, file)).length);
		Boolean CommonWay;
		
		for (int i=0;i<arr1.length;i++) {
			System.out.println("arr1["+i+"]= "+arr1[i]);
			}
        
		String[] arr2 = new String[(getway(id2, file)).length];
		System.arraycopy((getway(id2, file)), 0, arr2, 0, (getway(id2, file)).length);
		for (int i=0;i<arr2.length;i++) {
			System.out.println("arr2["+i+"]= "+arr2[i]);
			
			}
        
		
		for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr2.length; j++) {
            	System.out.println("ARR1=  "+arr1[i]+"         ARR2 = "+arr2[j]);
                if (arr1[i].equals(arr2[j])){
                   System.out.println("Element en commun est "+arr1[i]);
        			return true;
                    
                }else {
                	
                	
                }
            }
         }
		System.out.println("les deux nodes n'ont pas le même way");
		return false;
	}
	
	
public static String [] getway(long l, File file) throws IOException {
	
		
        FileInputStream fileStream = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(fileStream);
        BufferedReader reader = new BufferedReader(input);
        
        String line;
        String maLat; 
        String maLon;
        String finalWay;
        String tempWay="";
        String TabWay[] = new String[7];
        int compteur=0;
        int wayId=55;
        while ((line = reader.readLine()) != null) {
        	if (line.contains("way id=") ) {
        		String [] idTab = line.split("id=\"");
				tempWay = idTab[1];
				//System.out.println("WAY id= "+tempWay);
        	}
			if (line.contains("ref=\""+l+"\"") ) {
				System.out.println(" : "+ line);
				TabWay[compteur]=tempWay;
				compteur++;
				finalWay=tempWay;
				System.out.println("j'ai trouvé un "+compteur+" er pour le way "+TabWay[compteur-1]);
			}
			//finalWay=temp
			//if
		}
        String TabWayFin[] = new String[compteur];
        for (int i=0;i<compteur;i++) {
			//System.out.println("Tab["+i+"]= "+TabWay[i]);
        	TabWayFin[i]=TabWay[i];

			}
        
        
        return TabWayFin;
	}


	
	public static Node plusProcheVoisin(Node source, Node Destination) throws IOException {
		File file = new File("xml//carteItineraire.fxml");
        FileInputStream fileStream = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(fileStream);
        BufferedReader reader = new BufferedReader(input);
        
        String line;
        Graph graph = new Graph();
        Node destinationSaved;
        Double distanceFromsource = Graph.calculerLadistance(source.lat,source.lon,Destination.lat,Destination.lon);
		
        double distancePrecedent = Double.MAX_VALUE;
        double distanceCurrent = 0.0;
        double distanceCurrentFromDesti;
        String id = null;
        String maLat = null;
        String maLon = null;
		while ((line = reader.readLine()) != null) {
			if(line.contains("<node")) {
				
				String [] pass2 = line.split("id=\"");
				String idNode1 = pass2[1];
				String[] idNode = idNode1.split("\" lat=\"");
				String [] latitude = idNode[1].split("\" lon=\"");
				//System.out.print(latitude[0] +" ");//latitude		
				String [] longitude = latitude[1].split("\"");
				//System.out.println(longitude[0]); // longitude
				distanceCurrent = Graph.calculerLadistance(source.lat,source.lon,Double.parseDouble(latitude[0]),Double.parseDouble(longitude[0]));
				distanceCurrentFromDesti = Graph.calculerLadistance(Destination.lat,Destination.lon,Double.parseDouble(latitude[0]),Double.parseDouble(longitude[0])); 				
				if(distanceCurrent != 0.0 && distanceCurrent < distancePrecedent && distanceCurrentFromDesti < distanceFromsource) {
					//waysource = getway(source)
					//waydestination = getway(destination)
					//if (waysource == waysource-preced)
												
					distancePrecedent = distanceCurrent;
					id = idNode[0];
					maLat = latitude[0];
					maLon = longitude[0];
					//SearchWay(id);
				}
				
			}
		}
		reader.close();
		Node plusProcheVoisin = new Node(Long.parseLong(id),Double.parseDouble(maLat),Double.parseDouble(maLon));
		return plusProcheVoisin;
		}
	
	
}
