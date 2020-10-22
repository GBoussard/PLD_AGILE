package drawmap.model;

import java.util.Collection;
import java.util.Iterator;

public class SeqIter implements Iterator<Intersection> {
	private Intersection[] candidates;
	private int nbCandidates;

	/**
	 * Create an iterator to traverse the set of vertices in <code>unvisited</code> 
	 * which are successors of <code>currentVertex</code> in <code>g</code>
	 * Vertices are traversed in the same order as in <code>unvisited</code>
	 * @param unvisited
	 * @param currentVertex
	 * @param g
	 */
	public SeqIter(Collection<Intersection> unvisited, Intersection currentVertex, CompleteGraph g){
		this.candidates = new Intersection[unvisited.size()];
		for (Intersection s : unvisited){
			if (g.isArc(currentVertex, s))
				candidates[nbCandidates++] = s;
		}
	}
	
	@Override
	public boolean hasNext() {
		return nbCandidates > 0;
	}

	@Override
	public Intersection next() {
		nbCandidates--;
		return candidates[nbCandidates];
	}

	@Override
	public void remove() {}

}
