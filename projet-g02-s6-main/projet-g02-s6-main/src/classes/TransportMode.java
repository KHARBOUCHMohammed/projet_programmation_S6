package classes;

/**
 * @author Dorian Vabre
 * Defines the transport modes the user can choose
 */
public enum TransportMode {

	BUS(12),
	PEDESTRIAN(5),
	CAR(17),
	BICYCLE(15);
	
	private final int avgSpeed;

	TransportMode(int avgSpeed) {
			this.avgSpeed = avgSpeed;
	}

	public int getAvgSpeed() {
		return avgSpeed;
	}
}
