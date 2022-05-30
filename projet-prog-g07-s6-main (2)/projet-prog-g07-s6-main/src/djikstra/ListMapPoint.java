package djikstra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.gluonhq.maps.MapPoint;


public class ListMapPoint {
	
	public List<MapPoint> listofMapPoint = new ArrayList<>();
	public Graph graph = new Graph();
	public Cadre cadre = new Cadre();
	
	public ListMapPoint() {
	}
	
	public void initialiseGraph(Node node1, Node node2, String fxml) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		this.graph = graph.initialise(node1,node2,fxml);
		int i = 0;
        while(i != graph.nodes.size()-1) {
        	listofMapPoint.add(new MapPoint(graph.nodes.get(i).lat, graph.nodes.get(i).lon));
        	i++;
        }
	}
	
	public void initialiseCadre() throws IOException, ParserConfigurationException, SAXException, TransformerException {
		Cadre.creationCadre();
		ArrayList<Node> listeNodeArranged[] = new ArrayList[10000];
		listeNodeArranged[0] = Cadre.listeNode[0];
		for (int i = 1; Cadre.listeNode[i] != null ; i++) {
			if(isTrouver(listeNodeArranged[i-1].get(listeNodeArranged[i-1].size()-1).id ,listeNodeArranged[i-1].get(0).id) == true) {
            	listeNodeArranged[i] = Cadre.listeNode[trouver(listeNodeArranged[i-1].get(listeNodeArranged[i-1].size()-1).id ,listeNodeArranged[i-1].get(0).id)];
            }
            else {
            	listeNodeArranged[i] = inverser(retrouver(listeNodeArranged[i-1].get(listeNodeArranged[i-1].size()-1).id));
            }
		}
		for(int x = 0 ; x < listeNodeArranged.length ; x++) {
            if(listeNodeArranged[x] == null) { break;}
            for(int y = 0; y<listeNodeArranged[x].size(); y++) {
            	listofMapPoint.add(new MapPoint(listeNodeArranged[x].get(y).lat, listeNodeArranged[x].get(y).lon));
            }
        }
	}
	
	public boolean isTrouver(long id, long id2) {
		boolean dansLaListe = false;
		for(int i = 0 ; Cadre.listeNode[i] != null && dansLaListe==false; i++) {
            if(Cadre.listeNode[i].get(0).id == id && Cadre.listeNode[i].get(Cadre.listeNode[i].size()-1).id != id2) {
            	dansLaListe = true;
            }
        }
		return dansLaListe;
	}
	
	public int trouver(long id, long id2) {
		boolean dansLaListe = false;
		int index=0;
		for(int i = 0 ; Cadre.listeNode[i] != null && dansLaListe==false ; i++) {
            if(Cadre.listeNode[i].get(0).id == id && Cadre.listeNode[i].get(Cadre.listeNode[i].size()-1).id != id2) {
            	dansLaListe = true;
            	index=i;
            }
        }
		return index;
	}
	
	public int retrouver(long id) {
		boolean dansLaListe = false;
		int index=0;
		for(int i = 0 ; Cadre.listeNode[i] != null && dansLaListe==false ; i++) {
            if(Cadre.listeNode[i].get(Cadre.listeNode[i].size()-1).id == id) {
            	dansLaListe = true;
            	index=i;
            }
        }
		return index;
	}
	
	public ArrayList<Node> inverser(int index) {
		ArrayList<Node> listeNodeInversed = new ArrayList<>();
        for(int y = Cadre.listeNode[index].size()-1; y>=0; y--) {
        	listeNodeInversed.add(Cadre.listeNode[index].get(y));
        }
		return listeNodeInversed;
	}
	
	public static void main (String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		ListMapPoint l = new ListMapPoint();
		l.initialiseCadre();
		for(int i=0; i<l.listofMapPoint.size(); i++) {
			System.out.println(l.listofMapPoint.get(0).getLatitude());
			System.out.println(l.listofMapPoint.get(0).getLongitude());
		}
	}
}
