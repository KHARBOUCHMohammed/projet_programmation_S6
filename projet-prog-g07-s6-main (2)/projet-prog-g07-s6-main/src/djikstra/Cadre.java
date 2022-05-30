package djikstra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Cadre {

    static ArrayList<Node> listeNode[] = new ArrayList[1000];
    
    public static void creationCadre() throws IOException {
        
        
        File file = new File("xml\\carteCadre.fxml");
        FileInputStream fileStream = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(fileStream);
        BufferedReader reader = new BufferedReader(input);
        String line,line2;
        int i = 0;
        while ((line = reader.readLine()) != null) {
            if(line.contains("<way")) {
                listeNode[i] = new ArrayList<Node>();
                line = reader.readLine();
                while(line.contains("<nd")) {
                    File file2 = new File("xml\\carteCadre.fxml");
                    FileInputStream fileStream2 = new FileInputStream(file2);
                    InputStreamReader input2 = new InputStreamReader(fileStream2);
                    BufferedReader reader2 = new BufferedReader(input2);
                    String [] pass = line.split("ref=\"");
                    String refNode1 = pass[1];
                    String[] refNode = refNode1.split("\"/");
                    while((line2 = reader2.readLine()) != null){                
                        if(line2.contains("<node id=\""+ refNode[0]) == true) {
                            String [] pass2 = line2.split("id=\"");
                            String idNode1 = pass2[1];
                            String[] idNode = idNode1.split("\" lat=\"");
                            String [] longitude = idNode[1].split("\" lon=\"");                        
                            String [] latitude = longitude[1].split("\"");
                            listeNode[i].add(new Node(Long.parseLong(idNode[0]),Double.parseDouble(longitude[0]),Double.parseDouble(latitude[0])));
                            break;
                        }
                    }
                    line = reader.readLine();
                }
                i++;
            
            }
            
        }
        
    }
    
    public static void main(String [] args) throws IOException {
        Cadre.creationCadre();
        for(int i = 0 ; i < listeNode.length ; i++) {
            if(listeNode[i] == null) { break;}
	            for(int j = 0; j<listeNode[i].size(); j++) {
	                
	                System.out.println(listeNode[i].get(j).id);
	        
	            }
            	System.out.println("----------------------");
           	}
        }    
}