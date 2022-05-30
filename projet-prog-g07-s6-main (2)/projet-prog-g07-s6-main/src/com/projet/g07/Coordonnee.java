package com.projet.g07;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Coordonnee {
	public static String localisation;
	
	public Coordonnee(String local) {
		localisation=local;
	}

	public static Document getNodesViaOverpass() throws IOException, ParserConfigurationException, SAXException {
		String hostname = "http://www.overpass-api.de/api/interpreter";
		String queryString = "relation\r\n"
							+ "  [name="+localisation+"]\r\n"
							+ "  [type=boundary];\r\n"
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
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		Coordonnee.localisation = "Paris";
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(getNodesViaOverpass());
		StreamResult streamResult =  new StreamResult(new File("xml\\Carte.fxml"));
		transformer.transform(source, streamResult);
		System.out.println("ok bon");
	}
	
	public static String getLocalisation() {
		return localisation;
	}

	public static void setLocalisation(String localisation) {
		Coordonnee.localisation = localisation;
	}
}
