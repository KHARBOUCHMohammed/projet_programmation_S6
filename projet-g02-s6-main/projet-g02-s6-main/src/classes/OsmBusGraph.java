package classes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OsmBusGraph {

	/** The factory to create a document builder. */
	private DocumentBuilderFactory factory;
	
	/** The document builder used to parse the XML file. */
	private DocumentBuilder builder;
	
	/** The document got from the XML file. */
	private Document document;

	/**
	 * The parser for the file containing the maximum speeds for each country
	 */
	private DefaultSpeedJSONParser dsjp;

	/** The number of OsmNodes. */
	private int nbNodes;
	
	/** The number of OsmWays. */
	private int nbWays;
	
	/** The number of relations. */
	private int nbRelation;
	
	/** The map of BusLines */
	private Map<Long, OsmBusLine> osmBusLineMap;
	
	/** The map linking each OsmNode Id with the associated OsmNode */
	private Map<Long, OsmNode> osmNodeMap;

	/** The map linking a way Id to the associated OsmWay */
	private Map<Long, OsmWay> osmWayMap;
	
	public OsmBusGraph(String pathToFile) {
    	initializeDocumentBuilder(pathToFile);

		this.dsjp = new DefaultSpeedJSONParser();

    	initializeOsmWayMap();
    	
    	initializeOsmRelationMap();
    	
    	initializeOsmNodeMap();
	}
	
	
	/**
	 * Initialize document builder.
	 *
	 * @param pathToFile the path to file
	 */
	public void initializeDocumentBuilder(String pathToFile) {
    	this.factory = DocumentBuilderFactory.newInstance();
		try {
			this.builder = this.factory.newDocumentBuilder();
			this.document = this.builder.parse(new File(pathToFile));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	

    /**
     * Initialize osm way map.
     */
    public void initializeOsmWayMap() {
    	NodeList listOfOsmWays = this.document.getDocumentElement().getElementsByTagName("way");
    	
    	this.osmWayMap = new HashMap<Long, OsmWay>();
    	this.nbWays = listOfOsmWays.getLength();
    	
    	for (int tempWayIndex = 0; tempWayIndex < this.nbWays; tempWayIndex++) {
            Node way = listOfOsmWays.item(tempWayIndex);

            if (way.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) way;
                
                long id = Long.parseLong(element.getAttribute("id"));
                String visible = element.getAttribute("visible");
                NodeList osmNodeList = element.getChildNodes();
                
                List<Long> childOsmNodeList = new ArrayList<Long>();
                List<OsmTag> childOsmTagList = new ArrayList<OsmTag>();
                String name = null;
				int maxSpeed = 50;
                
                // For each child, adding it to the list of Nodes or Tags of that Way
            	for (int tempChildIndex = 0; tempChildIndex < osmNodeList.getLength(); tempChildIndex++) {
                    Node child = osmNodeList.item(tempChildIndex);

					if (child.getNodeName().equals("tag")) {
						String type = child.getAttributes().getNamedItem("k").getNodeValue();
						String value = child.getAttributes().getNamedItem("v").getNodeValue();

						if (type.equals("name")) {
							name = value;
						} else if (type.equals("maxspeed")) {
							if (isNumeric(value)) {
								maxSpeed = Integer.parseInt(value);
							} else {
								try {
									maxSpeed = this.dsjp.getMaxSpeed(value);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}

						childOsmTagList.add(new OsmTag(type, value));
					} else if (child.getNodeName().equals("nd")) {
            			Long ref = Long.parseLong(child.getAttributes().getNamedItem("ref").getNodeValue());
            			childOsmNodeList.add(ref);
            		}
            	}
            	
                OsmWay osmWay = new OsmWay(id, visible, childOsmNodeList, childOsmTagList, name, maxSpeed);

                this.osmWayMap.put(id, osmWay);
            }
        }
    }

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch(NumberFormatException e){
			return false;
		}
	}

    public void initializeOsmRelationMap() {
    	NodeList listOfRelations = this.document.getDocumentElement().getElementsByTagName("relation");
    	
    	this.osmBusLineMap = new HashMap<Long, OsmBusLine>();
    	this.nbRelation = listOfRelations.getLength();
    	
    	for (int tempRelationIndex = 0; tempRelationIndex < this.nbRelation; tempRelationIndex++) {
            Node relation = listOfRelations.item(tempRelationIndex);

            if (relation.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) relation;
                
                long id = Long.parseLong(element.getAttribute("id"));
                NodeList osmNodeList = element.getChildNodes();
                
                List<Long> listRefWays = new ArrayList<Long>();
                List<Long> listRefNodes = new ArrayList<Long>();
                String name = null;
                
                // For each child, adding it to the list of Nodes or Tags of that line
            	for (int tempChildIndex = 0; tempChildIndex < osmNodeList.getLength(); tempChildIndex++) {
                    Node child = osmNodeList.item(tempChildIndex);
                    
                    if (child.getNodeName().equals("member")) {
                    	String type = child.getAttributes().getNamedItem("type").getNodeValue();
                    	String ref = child.getAttributes().getNamedItem("ref").getNodeValue();
                    	String role = child.getAttributes().getNamedItem("role").getNodeValue();
                    	
                    	if (type.equals("way")) {
                    		Long way = Long.parseLong(ref);
                    		listRefWays.add(way);
                    	}
                    	else if (type.equals("node")) {
                    		if (role.equals("stop") || role.equals("stop_exit_only") || role.equals("platform_exit_only") 
                    		|| (role.equals("platform_entry_only")) || (role.equals("stop_entry_only"))){
                    			Long busStop = Long.parseLong(ref);
                    			listRefNodes.add(busStop);
                    		}
                    	}
            		} else if (child.getNodeName().equals("tag")) {
            			String type = child.getAttributes().getNamedItem("k").getNodeValue();
                    	String value = child.getAttributes().getNamedItem("v").getNodeValue();
                    	
                    	if (type.equals("name")) {
                    		name = value;
                    	}
            		}
            	}
            	
                OsmBusLine osmBusLine = new OsmBusLine(name, id, listRefWays, listRefNodes);

                this.osmBusLineMap.put(id, osmBusLine);
            }
        }
	}
    
    /**
     * Initialize the OsmNode map.
     */
    public void initializeOsmNodeMap() {
    	NodeList listOfOsmNodes = this.document.getDocumentElement().getElementsByTagName("node");
    	
    	this.osmNodeMap = new HashMap<Long, OsmNode>();
    	this.nbNodes = listOfOsmNodes.getLength();

		String nameStreet = "";

    	for (int temp = 0; temp < this.nbNodes; temp++) {
            Node node = listOfOsmNodes.item(temp);
            
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                long id = Long.parseLong(element.getAttribute("id"));
                String visible = element.getAttribute("visible");
                double lat = Double.parseDouble(element.getAttribute("lat"));
                double lon = Double.parseDouble(element.getAttribute("lon"));
				nameStreet = element.getAttribute("addr:street");

                NodeList osmNodeList = element.getChildNodes();
                List<OsmTag> childOsmTagList = new ArrayList<OsmTag>();
                
                // For each child, adding it to the list of Tags of that Node
            	for (int tempChildIndex = 0; tempChildIndex < osmNodeList.getLength(); tempChildIndex++) {
                    Node child = osmNodeList.item(tempChildIndex);
                    
                    if (child.getNodeName().equals("tag")) {
                    	String type = child.getAttributes().getNamedItem("k").getNodeValue();
                    	String value = child.getAttributes().getNamedItem("v").getNodeValue();

                    	childOsmTagList.add(new OsmTag(type, value));
            		}
            	}

				// todo put the street name ?
                this.osmNodeMap.put(id, new OsmNode(id, visible, lat, lon, childOsmTagList, nameStreet));
            }
        }
    }
    
    /*
     * browses through the bus_stopsFile and retrieve the names of the lines to put them on the menuitem
     */
    public List<String> fillBusLinesNames() {
	 	NodeList listOfNames = this.document.getDocumentElement().getElementsByTagName("tag");
	 	List<String> listOfLinesToReturn = new ArrayList<>();
	 	String itemOfAttributeName = "name";
	 	
	 	for (int currentName = 0; currentName < listOfNames.getLength(); currentName++) {
	 		Node node = listOfNames.item(currentName);
	 		if (node.getNodeType() == Node.ELEMENT_NODE) {
	 			Node child = listOfNames.item(currentName);
     			if (child.getNodeName().equals("tag")) {
     				if(child.getAttributes().getNamedItem("k").getNodeValue().equals(itemOfAttributeName)) {
	     				String name = child.getAttributes().getNamedItem("v").getNodeValue();
	     				listOfLinesToReturn.add(name);	
     				}
     			}
	 		}
	 	}
		return listOfLinesToReturn;
    }


	public DocumentBuilderFactory getFactory() {
		return factory;
	}


	public void setFactory(DocumentBuilderFactory factory) {
		this.factory = factory;
	}


	public DocumentBuilder getBuilder() {
		return builder;
	}


	public void setBuilder(DocumentBuilder builder) {
		this.builder = builder;
	}


	public Document getDocument() {
		return document;
	}


	public void setDocument(Document document) {
		this.document = document;
	}


	public int getNbNodes() {
		return nbNodes;
	}


	public void setNbNodes(int nbNodes) {
		this.nbNodes = nbNodes;
	}


	public int getNbWays() {
		return nbWays;
	}


	public void setNbWays(int nbWays) {
		this.nbWays = nbWays;
	}


	public int getNbRelation() {
		return nbRelation;
	}


	public void setNbRelation(int nbRelation) {
		this.nbRelation = nbRelation;
	}


	public Map<Long, OsmBusLine> getOsmBusLineMap() {
		return osmBusLineMap;
	}


	public void setOsmBusLineMap(Map<Long, OsmBusLine> osmBusLineMap) {
		this.osmBusLineMap = osmBusLineMap;
	}


	public Map<Long, OsmNode> getOsmNodeMap() {
		return osmNodeMap;
	}


	public void setOsmNodeMap(Map<Long, OsmNode> osmNodeMap) {
		this.osmNodeMap = osmNodeMap;
	}


	public Map<Long, OsmWay> getOsmWayMap() {
		return osmWayMap;
	}


	public void setOsmWayMap(Map<Long, OsmWay> osmWayMap) {
		this.osmWayMap = osmWayMap;
	}
    
}
