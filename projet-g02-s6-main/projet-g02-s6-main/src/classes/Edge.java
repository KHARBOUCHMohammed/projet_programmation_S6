package classes;

// Author Dorian Vabre

/**
 * A link to another OsmNode, with the distance between both
 */
public class Edge {
	
	/** The OsmNode target that the Edge points towards. */
	private OsmNode target;
	
	/** The distance with the target. */
	private double weight;
	
	/** The maximum speed for this road in km/h */
	private int maxSpeed;
	
	public Edge(OsmNode node, double weight, int maxSpeed) {
		this.target = node;
		this.weight = weight;
		this.setMaxSpeed(maxSpeed);
	}

	public String toString() {
		return "Edge [target=" + this.target + ", weight=" + this.weight + "]";
	}

	public OsmNode getTarget() {
		return this.target;
	}

	public void setTarget(OsmNode target) {
		this.target = target;
	}

	public double getWeight() {
		return this.weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
}
