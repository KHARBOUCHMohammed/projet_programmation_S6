package com.projet.g07;

import java.util.ArrayList;
import java.util.List;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
/** Affiche un point rouge sur la carte */
public class Poly extends MapLayer {

	private final List<MapPoint> mapPoint;
	private final List<Line> cheminCadre;
	/**
	* @param mapPoint le point (latitude et longitude) où afficher le cercle
	* @see com.gluonhq.maps.MapPoint
	*/
	public Poly(List<MapPoint> mapPoint) {
		this.mapPoint = mapPoint;
		this.cheminCadre = new ArrayList<>();
		//this.chemin = new Canvas(100,100);
		/* Ajoute le cercle au MapLayer */
		//this.getChildren().add(chemin);
		
	}
	
	/* La fonction est appelée à chaque rafraichissement de la carte */
	@Override
	protected void layoutLayer() {
	/* Conversion du MapPoint vers Point2D */
		this.getChildren().removeAll(cheminCadre);
		this.cheminCadre.clear();
		for(int i=0; i<mapPoint.size()-1;i++) {
			Point2D point2dA = this.getMapPoint(mapPoint.get(i).getLatitude(), mapPoint.get(i).getLongitude());
			Point2D point2dB = this.getMapPoint(mapPoint.get(i+1).getLatitude(), mapPoint.get(i+1).getLongitude());
			createline(point2dA, point2dB);
		}
		this.getChildren().addAll(cheminCadre);
	}
	
	public void createline(Point2D point2dA, Point2D point2dB) {
		final Line line = new Line();
		line.setTranslateX(point2dA.getX());
		line.setTranslateY(point2dA.getY());
		line.setEndX(point2dB.getX() - point2dA.getX());
		line.setEndY(point2dB.getY() - point2dA.getY());
		line.setStrokeWidth(2);
		line.setStroke(Color.ORANGERED);
		this.cheminCadre.add(line);
	}
}