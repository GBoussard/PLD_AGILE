package drawmap.model;

import java.util.Collection;
import java.util.Iterator;

public class SeqIter implements Iterator<Node> {
	private Node[] candidates;
	private int nbCandidates;

	/**
	 * Create an iterator to traverse the set of vertices in <code>unvisited</code>
	 * which are successors of <code>currentVertex</code> in <code>g</code>
	 * Vertices are traversed in the same order as in <code>unvisited</code>
	 * @param unvisited
	 */
	public SeqIter(Collection<Node> unvisited){
		nbCandidates = unvisited.size();
		this.candidates = unvisited.toArray(new Node[nbCandidates]);
	}

	@Override
	public boolean hasNext() {
		return nbCandidates > 0;
	}

	@Override
	public Node next() {
		nbCandidates--;
		return candidates[nbCandidates];
	}

	@Override
	public void remove() {}

}
