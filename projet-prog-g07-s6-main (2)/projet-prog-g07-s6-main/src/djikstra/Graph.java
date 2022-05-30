package djikstra;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.Set;
//updated Version by Mohammed kharbouch and Achraf el Gharib
public class Graph {
	//private Set<Node> nodes = new HashSet<>(); // l'ensemble Des noeuds de graph
	public ArrayList<Node> nodes = new ArrayList<>();
	
	public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }

	private static Double toRad(Double value) {
		 return value * Math.PI / 180;
	}
	public static double calculerLadistance(Double lat1,Double lon1,Double lat2, Double lon2) {
		 final int R = 6371;
		 Double latDistance = toRad(lat2-lat1);
		 Double lonDistance = toRad(lon2-lon1);
		 Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + 
		 Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * 
		 Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		 Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		 Double distance = R * c;
		 return distance;
		
	}
	
	
	public static boolean checkPossibility(long id1,long id2,File file) throws IOException{
		
		//System.out.println("okay");
		String[] arr1 = new String[(getway(id1, file)).length];
		System.arraycopy((getway(id1, file)), 0, arr1, 0, (getway(id1, file)).length);
		Boolean CommonWay;
		
		for (int i=0;i<arr1.length;i++) {
			//System.out.println("arr1["+i+"]= "+arr1[i]);
			}
        
		String[] arr2 = new String[(getway(id2, file)).length];
		//System.arraycopy((getway(id2, file)), 0, arr2, 0, (getway(id2, file)).length);
		for (int i=0;i<arr2.length;i++) {
			//System.out.println("arr2["+i+"]= "+arr2[i]);
			
			}
        
		
		for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr2.length; j++) {
            	//System.out.println("ARR1=  "+arr1[i]+"         ARR2 = "+arr2[j]);
                if (arr1[i].equals(arr2[j])){
                   //System.out.println("Element en commun est "+arr1[i]);
        			return true;
                    
                }else {
                	
                	
                }
            }
         }
		//System.out.println("les deux nodes n'ont pas le même way");
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
				//System.out.println(" : "+ line);
				TabWay[compteur]=tempWay;
				compteur++;
				finalWay=tempWay;
				//System.out.println("j'ai trouvé un "+compteur+" er pour le way "+TabWay[compteur-1]);
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

	
	
	public static Node plusProcheVoisin(Node source, Node Destination, String fxml) throws IOException {
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
	/*private static void getStudentById(Document doc, String textNodeName, String textNodeValue)
	{
	    NodeList studentNodes = doc.getElementsByTagName("way");
	    for(int i=0; i<studentNodes.getLength(); i++)
	    {
	        Node studentNode = studentNodes.item(i);
	        if(studentNode.getNodeType() == Node.ELEMENT_NODE)
	        {
	            Element studentElement = (Element) studentNode;
	            NodeList textNodes = studentElement.getElementsByTagName(textNodeName);
	            if(textNodes.getLength() > 0)
	            {
	                if(textNodes.item(0).getTextContent().equalsIgnoreCase(textNodeValue))
	                {
	                    System.out.println(textNodes.item(0).getTextContent());
	                    System.out.println(studentElement.getElementsByTagName("name").item(0).getTextContent());
	                }
	            }
	        }
	    }
	}*/
	
	/*public static void SearchWay(String id) throws IOException {
		File file = new File("xml//carteItineraire.fxml");
        FileInputStream fileStream = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(fileStream);
        BufferedReader reader = new BufferedReader(input);
        
        String line;
        int List[];
		System.out.println("id= "+id);
		while ((line = reader.readLine()) != null) {
			if(line.contains("ref=\""+id+"\"")) {
				System.out.println("id= "+id);
				//List.append()
				
			}
			}
		
	}*/
		
		
	public Graph initialise(Node node1, Node node2, String fxml) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		
		Node source = node1;
        Node Destination = node2;
		Node plusProche = source;
        
		while(plusProche.lat != Destination.lat && plusProche.lon != Destination.lon) {
			plusProche =  plusProcheVoisin(source,Destination,fxml);
        	LinkedList<Node> shortestPath = new LinkedList<>();
	        shortestPath.add(source);
	        plusProche.setShortestPath(shortestPath);
	        source = plusProche;
        }
		LinkedList<Node> shortestPath = new LinkedList<>();
        shortestPath.add(plusProche);
        Destination.setShortestPath(shortestPath);
		source = node1;
		Graph graph = new Graph();
		graph.addNode(Destination);
		while(Destination.shortestPath.get(0).id != source.id) {
			graph.addNode(Destination);
			Destination = Destination.shortestPath.get(0);
		}
		return graph;
	}
}
