package marqueur;

import com.gluonhq.maps.MapLayer;

/** Affiche une épingle sur la carte */
public abstract class Marqueur extends MapLayer {

	
	/**
	 * @param mapPoint le point (latitude et longitude) où afficher l'épingle
	 * @see com.gluonhq.maps.MapPoint
	 */
	public Marqueur() {}
	
	/* La fonction est appelée à chaque rafraichissement de la carte */
	@Override
	protected void layoutLayer() {}
}