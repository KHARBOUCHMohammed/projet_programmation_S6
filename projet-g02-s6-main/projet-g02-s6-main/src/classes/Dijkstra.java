package classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

// @Author Lou Favre
// Inspired then adapted from https://stackoverflow.com/questions/27454084/dijkstras-algorithm-in-java
// Reason : this dijkstra algorithm seemed appropriated for our project and easy enough for us to understand it and modify it for our use
// Modifications done : Vertex class replaced with our own OsmNode class, used our own variables and methods to get and set variables at the right places


public class Dijkstra {
	
	/**
	 * Update paths and distances for a given node and mode of transport, ie using the path that that mode can use.
	 *
	 * @param sourceNode the source node
	 * @param modeOfTransport the mode of transport
	 */
	public void UpdatePaths(OsmNode sourceNode, TransportMode modeOfTransport, String findMode) {
		sourceNode.setMinDistance(0.);
		PriorityQueue<OsmNode> nodeQueue = new PriorityQueue<>();
		nodeQueue.add(sourceNode);

		// While there is a node in the queue
		//
		while(!nodeQueue.isEmpty()) {
			OsmNode currentNode = nodeQueue.poll();

			Edge[] edges;
			if (modeOfTransport == TransportMode.PEDESTRIAN) {
				edges = currentNode.getAdjacentsForPedestrians();
			} else {
				edges = currentNode.getAdjacentsForVehicles();
			}

			// For each edge (link to another node)
			for(Edge e : edges) {
				if (e == null) continue;
				OsmNode currentTarget = e.getTarget();
				double weight = e.getWeight();
				int maxSpeed = e.getMaxSpeed();
				int avgSpeed = modeOfTransport.getAvgSpeed();

				// Updating the distance
				double distanceThroughCurrentNode = currentNode.getMinDistance();
				if (findMode.equals("Duration")) {
					if (modeOfTransport == TransportMode.PEDESTRIAN || modeOfTransport == TransportMode.BICYCLE) {
						distanceThroughCurrentNode += weight * (double)avgSpeed*1000/3600;
					} else {
						distanceThroughCurrentNode += weight * (double)maxSpeed*1000/3600;
					}
				} else {
					distanceThroughCurrentNode += weight;
				}

				// If the distance is the minimum known, we use it
				if(distanceThroughCurrentNode < currentTarget.getMinDistance()) {
					nodeQueue.remove(currentTarget);

					currentTarget.setMinDistance(distanceThroughCurrentNode);
					currentTarget.setPreviousNode(currentNode);
					nodeQueue.add(currentTarget);
				}
			}
		}
	}

	/**
      * Gets the shortest path to a given node. Requires a previous use of UpdatePaths() from the starting node.
      *
      * @param target the target
      * @return the list of OsmNode that is the shortest path to the given OsmNode
      */
     public List<OsmNode> getShortestPathTo(OsmNode target) {
            List<OsmNode> path = new ArrayList<OsmNode>();
            
            for(OsmNode node = target; node != null; node = node.getPreviousNode()) {
                path.add(node);
            }
            
            Collections.reverse(path);
            return path;
     }

	public double getDurationTo(List<OsmNode> path, TransportMode modeOfTransport) {
		double duration = 0;

		List<OsmNode> pathCopy = new ArrayList<OsmNode>(path);

		for(int i=0; i<pathCopy.size()-1; i++) {
			Edge[] edges;
			OsmNode node = pathCopy.get(i);

			if(modeOfTransport == TransportMode.PEDESTRIAN) {
				edges = node.getAdjacentsForPedestrians();
			}
			else {
				edges = node.getAdjacentsForVehicles();
			}

			for(Edge edge : edges ) {
				if (edge == null) continue;
				if(edge.getTarget() == pathCopy.get(i+1)) {
					if(modeOfTransport == TransportMode.PEDESTRIAN || modeOfTransport == TransportMode.BICYCLE) {
						duration += node.getDistanceBetweenNodes(pathCopy.get(i+1)) / ((double)modeOfTransport.getAvgSpeed()*1000/3600);
					} else {
						duration += node.getDistanceBetweenNodes(pathCopy.get(i+1)) / ((double)edge.getMaxSpeed()*1000/3600);
					}
					break;
				}
			}
		}
		return duration;
	}

}
