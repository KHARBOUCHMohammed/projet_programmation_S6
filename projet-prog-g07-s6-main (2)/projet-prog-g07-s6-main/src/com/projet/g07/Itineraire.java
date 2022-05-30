package com.projet.g07;

import java.util.List;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.util.Pair;

public class Itineraire extends MapLayer {
	
	Canvas chemin = new Canvas(100,100);

	private final ObservableList<Pair<MapPoint, Canvas>> points = FXCollections.observableArrayList();
    
    public Itineraire() {
    	 this.chemin.getGraphicsContext2D().moveTo(0, 0);
    }

    public void addPoint(MapPoint p, Canvas icon) {
        points.add(new Pair<>(p, icon));
        this.getChildren().add(icon);
        this.markDirty();
    }
    
    public void deplacer(double lat, double lon) {
    	this.chemin.getGraphicsContext2D().moveTo(lat, lon);
    }
    
    public Point2D getPoint2D(MapPoint p) {
		Point2D point2d = this.getMapPoint(p.getLatitude(), p.getLongitude());
		return point2d;
	}

    @Override
    protected void layoutLayer() {
        if(!Platform.isFxApplicationThread()) {
            Platform.runLater(()->layoutLayer());
        }
        for (Pair<MapPoint, Canvas> candidate : points) {
            MapPoint point = candidate.getKey();
            Node icon = candidate.getValue();
            Point2D mapPoint = getMapPoint(point.getLatitude(), point.getLongitude());
            icon.setVisible(true);
            icon.setTranslateX(mapPoint.getX());
            icon.setTranslateY(mapPoint.getY());
        }
    }
}