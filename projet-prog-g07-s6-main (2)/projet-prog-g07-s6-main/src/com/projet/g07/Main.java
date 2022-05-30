package com.projet.g07;
	
import java.awt.Graphics;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

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
import org.xml.sax.SAXException;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import djikstra.ListMapPoint;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import marqueur.MarqueurArrivee;
import marqueur.MarqueurDepart;
import transport.Bus;
/**
 * Le but du main est de mettre les diffrentes classes en commun et  de produire un logiciel en Java reproduisant une
		partie des fonctionnalits proposes par des services de cartographie
		
 * Variables globales contenant : 
 * Les donnes de la scene : pageEntiere, top, centre
 * Les donnes de la librairie GluonHQ : mapView, mapPoint qui est la localisation par dfaut et taille_zoom_act qui est le zoom par defaut
 * Les donnes qui contiennent soit le cadre, soit l'itinraire : djikstra.ListMapPoint listdepoints
 * Les donnes que l'utilisateur va entrer : localisationDepart et localisationDestination
 * 
 */	
public class Main extends Application implements EventHandler<ActionEvent> {

	/*Les donnes de la scene*/
	private StackPane pageEntiere;
	private  StackPane top;
	private StackPane centre;
	
	
	/*Les donnes de la librairie GluonHQ*/
	MapView mapView;
	MapPoint mapPoint = new MapPoint(43.9464,4.8090);
	int taille_zoom_act=13;
	
	/*Les donnes qui contiennent soit le cadre, soit l'itinraire*/
	static djikstra.ListMapPoint listdepoints = new djikstra.ListMapPoint();
	
	/*Les donnes que l'utilisateur va entrer*/
	static String localisationDepart;
	static String numero;
	static String rue;
	static String ville;
	static String localisationDestination;
	private static final DecimalFormat df = new DecimalFormat("0.0");
	/**/
	static djikstra.Node Depart;
	static djikstra.Node Arrivee;
	
	Scene scene;
	/*
	 * C'est la mthode qui va lancer le logiciel
	 * */
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		launch(args);
	}

	/*
	 * C'est la mthode qui 
	 * */
    @Override
    public void start(Stage stage) throws IOException, ParserConfigurationException, SAXException, TransformerException { 
    		
    	  /* Cration de la mise en page :
    	   * pageEntiere contient tout les Stackpane que ce soit le header (top) ou son contenu (centre)
    	   * le header top contient  les champs de texte et les boutons pour rechercher une ville, faire un itinraire et rgler le zoom
    	   * */
    	  pageEntiere = new StackPane();
    	  
    	  // Debut du header (top)
    	  top = new StackPane();										//On instancie top
          BorderPane cadre = new BorderPane();							//On cre un cadre o mettre les champs et les boutons
         
          HBox header = new HBox(6);									//
          header.setPadding(new Insets(10, 10, 10, 10));
          
          Button connexion = new Button("Se connecter");
          connexion.setPadding(new Insets(10, 10, 10, 10));
          Button inscription = new Button("S'inscrire");
          inscription.setPadding(new Insets(10, 10, 10, 10));
          
          
          //Elements allerA
          /*Tout ce qui concerne une localisation ou un itinraire*/
          
          
          /*Creation d'un Hbox pour mettre tout en ligne*/
          HBox allerA = new HBox(5);
          
          /*Saisir la localisation souhaite*/
          TextField area = new TextField();
          area.setPromptText("Saisir localisation");
          area.setMinWidth(120);
          area.setPadding(new Insets(10, 10, 10, 10));
          
          /*Bouton pour valider la requte*/
          Button goToLocation = new Button("Aller");
          goToLocation.setPadding(new Insets(10, 10, 10, 10));
          
          VBox vboxPopup = new VBox();
          vboxPopup.setStyle(" -fx-background-color: white;");
          
          Label distanceEtTemps = new Label("Distance : " + djikstra.Graph.distanceGlobal);
          Label distanceEtTemps2 = new Label("Durée : ");
          distanceEtTemps.setPadding(new Insets(15,15,15,15));
          HBox hboxDepart = new HBox();
          File fileDepart = new File("images\\marker.png");
  		  Image imageDepart = new Image(fileDepart.toURI().toString(), 30, 30, false, false);
  		  ImageView mapPinImageViewDepart = new ImageView(imageDepart);
  		  
  		  HBox hboxArrivee = new HBox();
          File fileArrivee = new File("images\\marker2.png");
  		  Image imageArrivee = new Image(fileArrivee.toURI().toString(), 30, 30, false, false);
  		  ImageView mapPinImageArrivee = new ImageView(imageArrivee);
  		  
  		  Popup popup = new Popup();
  		  
  		  Label labelDepart = new Label(Main.localisationDepart);
    	  Label labelArrivee = new Label(Main.localisationDestination);
	  
          EventHandler<ActionEvent> event = 
        		  new EventHandler<ActionEvent>() {	
        	  		public void handle(ActionEvent e)
	   	            {
	   	                if (!popup.isShowing())
    	                    popup.show(stage);	    	            
	   	                }
	    	       };
	      goToLocation.setOnAction(event);
          /*goToLocation.setOnAction(e -> { 
        	  Main.localisationDepart = area.getText();
        	  //afficherLocalisation();
        	  try {
        		listdepoints.listofMapPoint.clear();
				searchCadre();
				listdepoints.initialiseCadre();
				MapPoint mapPoint = new MapPoint(listdepoints.listofMapPoint.get(0).getLatitude(), listdepoints.listofMapPoint.get(0).getLongitude());
				Poly p = new Poly(listdepoints.listofMapPoint);
		    	mapView.flyTo(0, mapPoint,3);
		    	mapView.setZoom(10);
		    	mapView.addLayer(p);
		    	mapPoint =	listdepoints.listofMapPoint.get(0);
		    	
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (TransformerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	 if (!popup.isShowing())
        		 popup.show(stage);	
          });*/
          
          /*Changer le mode de pour un itinraire*/
          File fleches = new File("images/arrows.png");
          Image fle = new Image(fleches.toURI().toString());
          ImageView imageFleche = new ImageView(fle);
          imageFleche.setFitHeight(15);
          imageFleche.setFitWidth(15);
          Button changeTo = new Button();
          changeTo.setGraphic(imageFleche);
          changeTo.setPadding(new Insets(10, 10, 10, 10));
          
          changeTo.setOnAction(e -> { changeTop(); });
          
          /*Creation d'un Hbox pour mettre tout en ligne*/
          HBox itineraire = new HBox(5);
          
          /*Saisir la localisation souhaite*/
          TextField depart = new TextField();
          depart.setPromptText("Saisir localisation de dpart");
          depart.setMinWidth(120);
          depart.setPadding(new Insets(10, 10, 10, 10));
          
          /*Bouton pour valider la requte*/
          TextField destination = new TextField();
          destination.setPromptText("Saisir localisation d'arrive");
          destination.setMinWidth(120);
          destination.setPadding(new Insets(10, 10, 10, 10));
          
          Button goToItineraire = new Button("Aller");
          goToItineraire.setPadding(new Insets(10, 10, 10, 10));
          goToItineraire.setOnAction(e -> { 
        	Main.localisationDepart = depart.getText(); 
            Main.localisationDestination= destination.getText(); 	
            labelDepart.setText(Main.localisationDepart);
			labelArrivee.setText(Main.localisationDestination);
            try {
				listdepoints.listofMapPoint.clear();
				afficherItineraire(depart.getText(),destination.getText());
				
				distanceEtTemps.setText("Distance : " + df.format(djikstra.Graph.distanceGlobal) + "Km");
				distanceEtTemps2.setText("Durée : " + df.format(djikstra.Graph.distanceGlobal*2.95) + "min");
				
				if (!popup.isShowing())
		       		 popup.show(stage);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (TransformerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
          });
          
          /*Changer le mode de pour un itinraire*/
          File close = new File("images/close.png");
          Image cross = new Image(close.toURI().toString());
          ImageView imageCross = new ImageView(cross);
          imageCross.setFitHeight(10);
          imageCross.setFitWidth(10);
          Button rechange = new Button();
          rechange.setGraphic(imageCross);
          rechange.setPadding(new Insets(10, 10, 10, 10));
          rechange.setOnAction(e -> { changeTop(); });
          
          /*Attribution des enfants aux Hbox*/
          allerA.getChildren().addAll(area,goToLocation, changeTo);
          itineraire.getChildren().addAll(depart,destination,goToItineraire, rechange);
          
          /*Attribution des enfants aux Hbox*/
          allerA.setVisible(true);
          itineraire.setVisible(false);
          // Button zoom
          File add = new File("images/add.png");
          Image add_c = new Image(add.toURI().toString());
          ImageView imageadd = new ImageView(add_c);
          imageadd.setFitHeight(20);
          imageadd.setFitWidth(20);
          Button zoom = new Button();
          zoom.setPadding(new Insets(10, 10, 10, 10));
          zoom.setGraphic(imageadd);
          zoom.setOnAction(this);
          // button pour dezoomer
          File subt = new File("images/subt.png");
          Image subt_c = new Image(subt.toURI().toString());
          ImageView imagesubt = new ImageView(subt_c);
          imagesubt.setFitHeight(20);
          imagesubt.setFitWidth(20);
          Button dezoom = new Button();
          dezoom.setPadding(new Insets(10, 10, 10, 10));
          dezoom.setGraphic(imagesubt);
          dezoom.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (!popup.isShowing())
                    popup.show(stage);	    	            
	                
				mapView.setZoom(--taille_zoom_act);
				
				
			}
		});
          //Fin header
          
          /*Creation d'un autre Stack*/
          centre = new StackPane();
    	  VBox carte = new VBox();
    	  /* Cration de la carte Gluon JavaFX */
    	  mapView = new MapView();
    	  
    	  top.getChildren().addAll(itineraire,allerA);
          header.getChildren().addAll(top,connexion, inscription,zoom,dezoom);
          cadre.setTop(header);
    	  /* Cration du point avec latitude et longitude */
          
//          MapPoint mapPoint = new MapPoint(44.1170457,4.8760523);
//          MapPoint mapPoint = new MapPoint(listdepoints.listofMapPoint.get(0).getLatitude(), listdepoints.listofMapPoint.get(0).getLongitude());
    	  
    	  mapView.setOnMouseClicked(e -> {
    		  mapView.flyTo(5, mapPoint,10);
    	  });
    	  
    	  /* Cration et ajoute une couche  la carte */
          
          Itineraire it = new Itineraire();
 
//    	  MapLayer mapLayer = new MarqueurDepart(mapPoint);
//    	  mapView.addLayer(mapLayer);
    	 
    	  /* Zoom de 5 */
    	  mapView.setZoom(taille_zoom_act);

//    	  MapPoint arrivee = new MapPoint(listdepoints.listofMapPoint.get(listdepoints.listofMapPoint.size()-1).getLatitude(), listdepoints.listofMapPoint.get(listdepoints.listofMapPoint.size()-1).getLongitude());
//    	  MapLayer arriveeMarqueur = new MarqueurArrivee(arrivee);
//    	  mapView.addLayer(arriveeMarqueur);
    	  
    	  /* Centre la carte sur le point */
    	  mapView.flyTo(0, mapPoint, 0.1);
    	  carte.getChildren().add(mapView);
    	  centre.getChildren().addAll(carte);
    	  cadre.setCenter(centre);
    	  //Fin element centrale
    	  
    	  pageEntiere.getChildren().add(cadre);
    	  /*
    	   * IMPORTANT mettre la taille de la fentre pour viter l'erreur
    	   * java.lang.OutOfMemoryError
    	   */
    	  scene = new Scene(pageEntiere, 1100, 800);
    	  
    	  
    	  
    	  hboxDepart.getChildren().addAll(mapPinImageViewDepart,labelDepart);
    	  hboxDepart.setPadding(new Insets(15, 15, 15, 15));
    	  System.out.println(Main.localisationDepart);
    	  hboxArrivee.getChildren().addAll(mapPinImageArrivee,labelArrivee);
    	  hboxArrivee.setPadding(new Insets(15, 15, 15, 15));
    	  distanceEtTemps.setPadding(new Insets(15, 15, 15, 15));
    	  distanceEtTemps2.setPadding(new Insets(15, 15, 15, 15));
    	  
    	  //imageDepart,depart,imageArrivee,destination,distanceEtTemps
  		  vboxPopup.getChildren().addAll(hboxDepart,hboxArrivee,distanceEtTemps,distanceEtTemps2);
  		  popup.getContent().add(vboxPopup);
          popup.setAutoHide(true);
          //vboxPopup.setMinWidth(1);
          vboxPopup.setMinHeight(cadre.getCenter().getScene().getHeight()-60);
          //cadre.setLeft(distanceEtTemps);
    	  stage.setScene(scene);
    	  stage.show();
    	  popup.setX((stage.getX() + stage.getWidth() / 2 - stage.getWidth() / 2) + 9);
    	  popup.setY((stage.getY() + stage.getHeight() / 2 - stage.getHeight() / 2) + 90);
     }
    
    private void changeTop() {
        ObservableList<Node> childs = this.top.getChildren();

        if (childs.size() > 1) {
            Node goToFront = childs.get(childs.size()-1);
            // Cette node va aller au premier-plan
            Node newGoToFront = childs.get(childs.size()-2);
                  
            goToFront.setVisible(false);
            goToFront.toBack();
          
            newGoToFront.setVisible(true);
        }
    }
    
    private void afficherLocalisation() {
    	System.out.println(localisationDepart);
    }
    
    
    private void afficherItineraire(String Depart, String Arrivee) throws IOException, ParserConfigurationException, SAXException, TransformerException {
    	/*System.out.println(localisationDepart);
    	String splitDepart[] = localisationDepart.split(" ");
    	Main.numero = splitDepart[0];
    	Main.rue = "";
    	for(int i=1; i<splitDepart.length-1; i++) {
    		if(i==splitDepart.length-2) {
    			Main.rue += splitDepart[i];
    		}
    		else {
    			Main.rue += splitDepart[i]+ " ";
    		}
    	}
    	Main.ville = splitDepart[splitDepart.length-1];
    	
    	searchNodeForItineraire();
    	File file = new File("xml//NodeItineraire.fxml");
        FileInputStream fileStream = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(fileStream);
        BufferedReader reader = new BufferedReader(input);
        String line;
        while ((line = reader.readLine()) != null) {
            if(line.contains("<node")) {
                String [] pass2 = line.split("id=\"");
                String idNode1 = pass2[1];
                String[] idNode = idNode1.split("\" lat=\"");
                String [] latitude = idNode[1].split("\" lon=\"");                        
                String [] longitude = latitude[1].split("\"");
            	Main.Depart = new djikstra.Node(Long.parseLong(idNode[0]),Double.parseDouble(latitude[0]), Double.parseDouble(longitude[0]));
            }
        }
        System.out.println(Depart.id + "//" + Depart.lat + "//" + Depart.lon);
        
       
    	System.out.println(localisationDestination);
    	String splitDestination[] = localisationDestination.split(" ");
    	Main.numero = splitDestination[0];
    	Main.rue = "";
    	for(int i=1; i<splitDestination.length-1; i++) {
    		if(i==splitDestination.length-2) {
    			Main.rue += splitDestination[i];
    		}
    		else {
    			Main.rue += splitDestination[i]+ " ";
    		}
    	}
    	Main.ville = splitDestination[splitDestination.length-1];
    	searchNodeForItineraire();
    	file = new File("xml//NodeItineraire.fxml");
        fileStream = new FileInputStream(file);
        input = new InputStreamReader(fileStream);
        reader = new BufferedReader(input);
        line="";
        while ((line = reader.readLine()) != null) {
            if(line.contains("<node")) {
                String [] pass2 = line.split("id=\"");
                String idNode1 = pass2[1];
                String[] idNode = idNode1.split("\" lat=\"");
                String [] latitude = idNode[1].split("\" lon=\"");                        
                String [] longitude = latitude[1].split("\"");
                Main.Arrivee = new djikstra.Node(Long.parseLong(idNode[0]),Double.parseDouble(latitude[0]), Double.parseDouble(longitude[0]));
            }
        }
        System.out.println(Arrivee.id + "//" + Arrivee.lat + "//" + Arrivee.lon);*/
    	//Bus.selectionnerligne(Depart, Arrivee);
    	
    	djikstra.Node source = new djikstra.Node(929040977L,43.9423779,4.8222985);
    	djikstra.Node destination = new djikstra.Node(1113275953L,43.9403469,4.8292711);
		
    	listdepoints.initialiseGraph(source,destination);
		//destination.shortestPath.get(0);
        MapPoint mapPoint = new MapPoint(listdepoints.listofMapPoint.get(0).getLatitude(), listdepoints.listofMapPoint.get(0).getLongitude());
        MapPoint depart = new MapPoint(source.lat, source.lon);
        MapPoint arrivee = new MapPoint(destination.lat, destination.lon);
        MapLayer marqueurDepart = new MarqueurDepart(depart);
        MapLayer marqueurArrivee = new MarqueurArrivee(arrivee);
        Poly p = new Poly(listdepoints.listofMapPoint);
    	mapView.flyTo(0, mapPoint,3);
    	mapView.setZoom(taille_zoom_act);
    	mapView.addLayer(p);
    	mapView.addLayer(marqueurDepart);
    	mapView.addLayer(marqueurArrivee);
    	mapPoint =	listdepoints.listofMapPoint.get(0);

    }

	@Override
	public void handle(ActionEvent event) {
		
		mapView.setZoom(++taille_zoom_act);
		
	}
	
	public static Document getNodesViaOverpass() throws IOException, ParserConfigurationException, SAXException {
		String hostname = "http://www.overpass-api.de/api/interpreter";
		String queryString = "node\r\n"
							+ "   [highway]\r\n"
							+ "  [area!=yes]\r\n"
							+ " (around:100, "+ Main.Depart.lat +","+ Main.Depart.lon +","+ Main.Arrivee.lat+","+ Main.Arrivee.lon+");\r\n"
							+ "(._;>;);\r\n"
							+ "out;";
		URL osm = new URL(hostname);
		HttpURLConnection connection = (HttpURLConnection) osm.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
		printout.writeBytes("data=" + URLEncoder.encode(queryString, "utf-8"));
		printout.flush();
		printout.close();
	
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		return docBuilder.parse(connection.getInputStream());
	}
	
	public void searchItineraire() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(getNodesViaOverpass());
        StreamResult streamResult =  new StreamResult(new File("xml//carteItineraire.fxml"));
        transformer.transform(source, streamResult);
	}
	
	public static Document getCadreOverpass() throws IOException, ParserConfigurationException, SAXException {
        String hostname = "http://www.overpass-api.de/api/interpreter";
        String queryString = "relation\r\n"
                + "  [name="+Main.localisationDepart+"]\r\n"
                + "  [type=boundary]\r\n"
                + "  [admin_level = 8];\r\n"
                + "(._;>;);\r\n"
                + "out;";
        URL osm = new URL(hostname);
        HttpURLConnection connection = (HttpURLConnection) osm.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
        printout.writeBytes("data=" + URLEncoder.encode(queryString, "utf-8"));
        printout.flush();
        printout.close();

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        return docBuilder.parse(connection.getInputStream());
    }
	
	public void searchCadre() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(getCadreOverpass());
        StreamResult streamResult =  new StreamResult(new File("xml//carteCadre.fxml"));
        transformer.transform(source, streamResult);
	}
	
	public static Document getNodesViaOverpassNodeForItineraire() throws IOException, ParserConfigurationException, SAXException {
		String hostname = "http://www.overpass-api.de/api/interpreter";
        String queryString = "area[name= \""+Main.ville+"\"][type = \"boundary\"][admin_level=\"8\"]->.searchArea;\r\n"
                             + "node [\"addr:housenumber\"= \"" +Main.numero+"\"][\"addr:street\" = \"" + Main.rue+"\"](area.searchArea);\r\n"
                             + "out;\r\n"
                             + ">;\r\n"
                             + "out;";
		URL osm = new URL(hostname);
        HttpURLConnection connection = (HttpURLConnection) osm.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
        printout.writeBytes("data=" + URLEncoder.encode(queryString, "utf-8"));
        printout.flush();
        printout.close();

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        return docBuilder.parse(connection.getInputStream());
	}
	
	public void searchNodeForItineraire() throws IOException, ParserConfigurationException, SAXException, TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(getNodesViaOverpassNodeForItineraire());
        StreamResult streamResult =  new StreamResult(new File("xml//NodeItineraire.fxml"));
        transformer.transform(source, streamResult);
	}
}
