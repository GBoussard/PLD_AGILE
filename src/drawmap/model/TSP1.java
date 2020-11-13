package drawmap.model;

import javafx.util.Pair;

import java.util.*;

public class TSP1 extends TemplateTSP {

	public  double MSTcost(Collection<Node> graph, Node start) {
		double cost = 0;
		Node startNode = null;
		for (Node node: graph) {
			if(startNode==null){
				startNode = node;
			}else{
				node.cost = g.getCost(startNode.id, node.id);
			}
		}
		PriorityQueue<Node> SP = new PriorityQueue<>(new  NodeComparator());
		SP.addAll(graph);
		Node vertex;
		while (!SP.isEmpty()) {
			vertex = SP.poll();
			cost += vertex.cost;
			for (Node neighbor: SP) {
				if(g.getCost(vertex.id, neighbor.id)<neighbor.cost){
					neighbor.cost = g.getCost(vertex.id, neighbor.id);
				}
			}
		}
		return cost;
	}

	class NodeComparator implements Comparator<Node>{

		public int compare(Node lhs, Node rhs) {
			if (lhs.cost > rhs.cost)
				return 1;
			else if (lhs.cost < rhs.cost)
				return -1;
			return 0;
		}
	}
	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @return a lower bound of the cost of paths in <code>g</code> starting from <code>currentVertex</code>, visiting
	 * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
	 */
	@Override
	//possibilité d'enrichir en utilisant l'inégalité triangulaire
	protected double bound(Node currentVertex, Collection<Node> unvisited) {
		int size = unvisited.size();
		if(size==0){
			return g.cost.get(new Pair(currentVertex.id,depot.id));
		}else if(size==1){
			Node left = unvisited.iterator().next();
			return g.cost.get(new Pair(currentVertex.id,left.id))+g.cost.get(new Pair(left.id,depot.id));
		}else{
			double MST = MSTcost(unvisited,unvisited.iterator().next());
			double minCurrentCollec = Double.MAX_VALUE;
			double minCollecDepot = Double.MAX_VALUE;


			for (Node i: unvisited) {
				double distance1 = g.cost.get(new Pair(currentVertex.id,i.id));
				double distance2 = g.cost.get(new Pair(i.id,depot.id));

				if(distance1<minCurrentCollec) {

					minCurrentCollec = distance1;
				}

				if(distance2<minCollecDepot) {

					minCollecDepot = distance2;
				}

			}

				return MST+minCollecDepot+minCurrentCollec;


		}


	}

	@Override
	protected Iterator<Node> iterator(Node currentVertex, Collection<Node> unvisited) {
	/*	ArrayList nodes = new ArrayList<Node>();
		nodes.addAll(unvisited);
		Comparator<Node> nodeComparator
				= Comparator.comparing( (s1) -> {
					return g.cost.get(new Pair(currentVertex.id,s1.id));
				});
		Collections.sort(nodes,nodeComparator);
		return nodes.iterator();*/

		return new SeqIter(unvisited);
	}


}
