package nodeInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import node.Node;


public class NodeInformation extends Information {
	
	ArrayList<node.Node> ListOfNodes;
	
    public NodeInformation() {
		super();
		ListOfNodes = new ArrayList<>();
	}

	public void initialise(ArrayList<node.Node> nI) throws NumberFormatException, IOException {
		
        File file = new File("src/com/projet/g07/Carte.fxml");
        FileInputStream fileStream = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(fileStream);
        BufferedReader reader = new BufferedReader(input);
        String line;
        node.Node elementNode = null;

        while ((line = reader.readLine()) != null) {
        	if(line.contains("<node")) {
        		String idNode = splitString(line,"id=");
            	String latitudeNode = splitString(line,"lat=");
                String longitudeNode = splitString(line,"lon=");
                ArrayList<String> temporyTag = new ArrayList<>();
                line = reader.readLine();
	      		while(line.contains("<tag")) {
	      			String k = splitString(line,"k=");
	      			temporyTag.add(k);
	      			String v = splitString(line,"v=");
	      			line = reader.readLine();
	      			temporyTag.add(v);
	      		}
            	elementNode = new node.Node(Long.parseLong(idNode), Double.parseDouble(latitudeNode), Double.parseDouble(longitudeNode), temporyTag);
            	nI.add(elementNode);
        	}
        }
        reader.close();
    }
    
	public static void main(String[] args) throws IOException {
		NodeInformation nI = new NodeInformation();
		nI.initialise(nI.ListOfNodes);
		int i = 0;
		
		while(i < nI.ListOfNodes.size()){
			System.out.println("id :"+ nI.ListOfNodes.get(i).getId());
			int j=0;
			while(j<nI.ListOfNodes.get(i).getTag().size()) {
				 if(j%2==0) {
					 System.out.println("tag k : " + nI.ListOfNodes.get(i).getTag().get(j));
				 }
				 else {
					 System.out.println("tag v : " + nI.ListOfNodes.get(i).getTag().get(j));
				 }
				 
				 j++;
			 }
			i++;
		}
	}
}
