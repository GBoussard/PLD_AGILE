package drawmap.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class TemplateTSP implements TSP {
	private Long[] bestSol;
	protected CompleteGraph g;
	private double bestSolCost;
	private int timeLimit;
	private long startTime;
	private List<Request> requests;
	protected Node depot;
	public void searchSolution(int timeLimit, CompleteGraph g, List<Request> requests, Intersection depot){

		if (timeLimit <= 0) return;
		this.requests = requests;
		this.depot = new Node(depot.getId(), 0.0,NodeType.Depot);
		startTime = System.currentTimeMillis();
		this.timeLimit = timeLimit;
		this.g = g;
		bestSol = new Long[g.getNbVertices()];
		Collection<Node> unvisited = new ArrayList<>(g.getNbVertices()-1);

		for (Request req: requests) {
			unvisited.add(new Node(req.getPickup().getId(),Double.MAX_VALUE,NodeType.Pickup));
			unvisited.add(new Node(req.getDelivery().getId(),Double.MAX_VALUE,NodeType.Delivery));
		}

		Collection<Long> visited = new ArrayList<>(g.getNbVertices());
		visited.add(this.depot.id); // The first visited vertex is 0
		bestSolCost = Double.MAX_VALUE;
		branchAndBound(this.depot, unvisited, visited, 0.0);

	}

	public Long getSolution(int i){
		if (g != null && i>=0 && i<g.getNbVertices())
			return bestSol[i];
		return null;
	}

	public double getSolutionCost(){
		if (g != null)
			return bestSolCost;
		return -1;
	}

	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @return a lower bound of the cost of paths in <code>g</code> starting from <code>currentVertex</code>, visiting
	 * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
	 */
	protected abstract double bound(Node currentVertex, Collection<Node> unvisited);

	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @return an iterator for visiting all vertices in <code>unvisited</code> which are successors of <code>currentVertex</code>
	 */
	protected abstract Iterator<Node> iterator(Node currentVertex, Collection<Node> unvisited);

	/**
	 * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
	 * @param currentVertex the last visited vertex
	 * @param unvisited the set of vertex that have not yet been visited
	 * @param visited the sequence of vertices that have been already visited (including currentVertex)
	 * @param currentCost the cost of the path corresponding to <code>visited</code>
	 */
	private void branchAndBound(Node currentVertex, Collection<Node> unvisited,
								Collection<Long> visited, double currentCost){
		if (System.currentTimeMillis() - startTime > timeLimit) return;
		if (unvisited.size() == 0){
			Node beginning = this.depot;
			if (g.isArc(currentVertex.id,beginning.id)){
				if (currentCost+g.getCost(currentVertex.id,beginning.id) < bestSolCost){
					visited.toArray(bestSol);
					bestSolCost = currentCost+g.getCost(currentVertex.id,beginning.id);
				}
			}
		} else if (currentCost+bound(currentVertex,unvisited) < bestSolCost){
			Iterator<Node> it = iterator(currentVertex, unvisited);

			while (it.hasNext()){
				Node nextVertex = it.next();
				if(isAllowed(nextVertex,visited)){
					visited.add(nextVertex.id);
					unvisited.remove(nextVertex);

					branchAndBound(nextVertex, unvisited, visited,
							currentCost+g.getCost(currentVertex.id, nextVertex.id));

					visited.remove(nextVertex);
					unvisited.add(nextVertex);
				}

			}

		}
	}
	private boolean isAllowed(Node inter, Collection<Long> visited){

		Long associatedPickup;
		if(inter.nodeType==NodeType.Pickup||inter.nodeType==NodeType.Depot){
			return true;
		}
		for (Request req : requests) {
			if(req.getDelivery().getId()==inter.id){
				associatedPickup = req.getPickup().getId();
				return visited.contains(associatedPickup);
			}
		}
		return false;
	}

}
