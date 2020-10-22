package drawmap.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class TemplateTSP implements TSP {
	private Intersection[] bestSol;
	protected CompleteGraph g;
	private double bestSolCost;
	private int timeLimit;
	private long startTime;
	
	public void searchSolution(int timeLimit, CompleteGraph g){
		if (timeLimit <= 0) return;
		startTime = System.currentTimeMillis();	
		this.timeLimit = timeLimit;
		this.g = g;
		bestSol = new Intersection[g.getNbVertices()];
		Collection<Intersection> unvisited = new ArrayList<Intersection>(g.getNbVertices()-1);

		for (int i=1; i<g.getNbVertices(); i++) unvisited.add(g.vertices.get(i));

		Collection<Intersection> visited = new ArrayList<Intersection>(g.getNbVertices());
		visited.add(g.vertices.get(0)); // The first visited vertex is 0
		bestSolCost = Double.MAX_VALUE;
		branchAndBound(g.vertices.get(0), unvisited, visited, 0.0);

	}
	
	public Intersection getSolution(int i){
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
	protected abstract double bound(Intersection currentVertex, Collection<Intersection> unvisited);
	
	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @param g
	 * @return an iterator for visiting all vertices in <code>unvisited</code> which are successors of <code>currentVertex</code>
	 */
	protected abstract Iterator<Intersection> iterator(Intersection currentVertex, Collection<Intersection> unvisited, CompleteGraph g);
	
	/**
	 * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
	 * @param currentVertex the last visited vertex
	 * @param unvisited the set of vertex that have not yet been visited
	 * @param visited the sequence of vertices that have been already visited (including currentVertex)
	 * @param currentCost the cost of the path corresponding to <code>visited</code>
	 */	
	private void branchAndBound(Intersection currentVertex, Collection<Intersection> unvisited,
			Collection<Intersection> visited, double currentCost){
		if (System.currentTimeMillis() - startTime > timeLimit) return;
	    if (unvisited.size() == 0){
	    	Intersection beginning = g.vertices.get(0);
	    	if (g.isArc(currentVertex,beginning)){
	    		if (currentCost+g.getCost(currentVertex,beginning) < bestSolCost){
	    			visited.toArray(bestSol);
	    			bestSolCost = currentCost+g.getCost(currentVertex,beginning);
	    		}
	    	}
	    } else if (currentCost+bound(currentVertex,unvisited) < bestSolCost){
	        Iterator<Intersection> it = iterator(currentVertex, unvisited, g);
	        while (it.hasNext()){
				Intersection nextVertex = it.next();
	        	visited.add(nextVertex);
	            unvisited.remove(nextVertex);
	            branchAndBound(nextVertex, unvisited, visited, 
	            		currentCost+g.getCost(currentVertex, nextVertex));
	            visited.remove(nextVertex);
	            unvisited.add(nextVertex);
	        }	    
	    }
	}

}
