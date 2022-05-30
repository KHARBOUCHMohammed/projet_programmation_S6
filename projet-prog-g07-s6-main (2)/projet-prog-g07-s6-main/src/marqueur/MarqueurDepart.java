package marqueur;

import java.io.File;

import com.gluonhq.maps.MapPoint;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MarqueurDepart extends Marqueur {
	private final MapPoint mapPoint;
	private final ImageView mapPinImageView;
	
	private static final int PIN_WIDTH = 30, PIN_HEIGHT = 30;
	
	public MarqueurDepart(MapPoint mapPoint) {
		super();
		this.mapPoint = mapPoint;
		File file = new File("images\\marker.png");
		Image image = new Image(file.toURI().toString(), PIN_WIDTH, PIN_HEIGHT, false, false);
		this.mapPinImageView = new ImageView(image);
		/* Ajoute l'épingle au MapLayer */
		this.getChildren().add(this.mapPinImageView);
	}
	
	protected void layoutLayer() {
		/* Conversion du MapPoint vers Point2D */
		Point2D point2d = this.getMapPoint(mapPoint.getLatitude(), mapPoint.getLongitude());
	
		/* Déplace l'épingle selon les coordonnées du point */
		mapPinImageView.setTranslateX(point2d.getX() - (PIN_WIDTH / 2));
		mapPinImageView.setTranslateY(point2d.getY() - PIN_HEIGHT);
	 }

}
