package drawmap.model;

import javafx.util.Pair;

import java.util.HashMap;

public class CompleteGraph implements Graph {

	HashMap<Pair<Long,Long>,Double> cost;

	public CompleteGraph(HashMap<Pair<Long,Long>,Double> cost){
		this.cost = cost;
	}

	@Override
	public int getNbVertices() {
		return cost.size();
	}

	@Override
	public double getCost(Long i, Long j) {
		Pair test = new Pair(i,j);
		double ecost = cost.get(test);
		return ecost;
	}

	@Override
	public boolean isArc(Long i, Long j) {
		return i != j;
	}

}
