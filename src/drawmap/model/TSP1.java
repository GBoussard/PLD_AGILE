package drawmap.model;

import java.util.Collection;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {
	@Override
	protected double bound(Intersection currentVertex, Collection<Intersection> unvisited) {
		return 0.0;
	}

	@Override
	protected Iterator<Intersection> iterator(Intersection currentVertex, Collection<Intersection> unvisited, CompleteGraph g) {
		return new SeqIter(unvisited, currentVertex, g);
	}

}
