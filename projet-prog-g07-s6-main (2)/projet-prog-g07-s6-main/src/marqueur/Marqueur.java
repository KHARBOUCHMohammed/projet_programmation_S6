package marqueur;

import com.gluonhq.maps.MapLayer;

/** Affiche une �pingle sur la carte */
public abstract class Marqueur extends MapLayer {

	
	/**
	 * @param mapPoint le point (latitude et longitude) o� afficher l'�pingle
	 * @see com.gluonhq.maps.MapPoint
	 */
	public Marqueur() {}
	
	/* La fonction est appel�e � chaque rafraichissement de la carte */
	@Override
	protected void layoutLayer() {}
}