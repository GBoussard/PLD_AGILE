package drawmap.model;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Vector;

public class CompleteGraph implements Graph {

	HashMap<Pair<Intersection,Intersection>,Double> cost;
	Vector<Intersection> vertices;

	public CompleteGraph(HashMap<Pair<Intersection,Intersection>,Double> cost, Vector<Intersection> vertices){
		this.cost = cost;
		this.vertices = vertices;
	}

	@Override
	public int getNbVertices() {
		return vertices.size();
	}

	@Override
	public double getCost(Intersection i, Intersection j) {
		Pair test = new Pair(i,j);
		double ecost = cost.get(test);
		return ecost;
	}

	@Override
	public boolean isArc(Intersection i, Intersection j) {
		return i != j;
	}

}
