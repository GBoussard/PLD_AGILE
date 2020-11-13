package drawmap.model;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

/**
 * Helper algorithmic static methods
 */
public class Algorithm {
    final static double radius = 6371.01;

    /**
     * @param a : First intersection
     * @param b : Second intersection
     * @return : euclidian distance between a and b
     */
    public static double getDistance(Intersection a, Intersection b){

        double lon1 = a.getLongitude();
        double lat1 = a.getLatitude();

        double lon2 = b.getLongitude();
        double lat2 = b.getLatitude();

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double temp = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(temp), Math.sqrt(1 - temp));
        double distance = radius * c * 1000; // convert to meters
        return Math.sqrt(distance);
    }

    /**
     * @param predecessors : Predecessors map, associating Intersections to their predecessors
     * @param current : endpoint of the path
     * @return Ordered path arriving to current
     */
    public static LinkedList<Intersection> reconstruct_path(HashMap<Intersection,Intersection> predecessors, Intersection current){
        double total = 0.0;
        LinkedList<Intersection> result = new LinkedList<>();
        result.add(current);

        while (current!=null) {
            current = predecessors.get(current);
            if(current!=null) {
                result.addFirst(current);
            }
        }
        return result;
    }

    /**
     * @param fScores
     * @param openSet
     * @return Intersection from openSet of minimum fScore
     */
    public static Intersection getMinElement(HashMap<Intersection,Double> fScores, Vector<Intersection> openSet){
        double minScore = Double.MAX_VALUE;
        Intersection inter = null;
        for (Intersection i: openSet) {
            if(fScores.get(i)<minScore){
                inter = i;
                minScore = fScores.get(i);
            }
        }
        return inter;
    }


    /**
     * @param start
     * @param end
     * @param map
     * @return shortest path from start to end following the streets of map
     */
    public static Pair<LinkedList<Intersection>,Double> A_star(Intersection start, Intersection end, CityMap map){
        // cheapest known cost from start to node
        HashMap<Intersection,Double> gScores = new HashMap<>(); // initialize with infinite cost except for starting node : 0

        // cheapest known cost from start to node + heuristic cost from node to end node
        HashMap<Intersection,Double> fScores = new HashMap<>(); // initialize with infinite cost except for starting node : h(node)

        LinkedList<Intersection> resultList = new LinkedList<>();
        Pair<LinkedList<Intersection>,Double> result = new Pair<LinkedList<Intersection>,Double>(resultList,0.0);
        Vector<Intersection> openSet = new Vector<>();

        openSet.add(start);
        HashMap<Intersection,Intersection> predecessors = new HashMap<>();


        Iterator iter = map.getListeIntersections().values().iterator();

        while (iter.hasNext()) {
            Intersection next = (Intersection)iter.next();
            gScores.put(next,Double.MAX_VALUE);
            fScores.put(next,Double.MAX_VALUE);
        }
        gScores.put(start,0.0);
        fScores.put(start,getDistance(start,end));

        while(!openSet.isEmpty()){
            Intersection current = getMinElement(fScores,openSet);
            openSet.remove(current);
            if(current==null){
                System.out.println("fatal error");
                return result;
            }else{
                if(current==end){
                    System.out.println("reached end");
                    return new Pair(reconstruct_path(predecessors,current),fScores.get(current));
                }else{

                    for (Pair i: current.getNeighbours()) {
                        double possibleScore = gScores.get(current) + ((Pair<Segment, Intersection>)i).getKey().getLength();
                        Intersection neighbor = (Intersection)i.getValue();
                        if(possibleScore<gScores.get(neighbor)){
                            predecessors.put(neighbor,current);
                            gScores.put(neighbor,possibleScore);
                            fScores.put(neighbor,possibleScore+getDistance(neighbor,end));
                            if(!openSet.contains(neighbor)){
                                openSet.add(neighbor);
                            }
                        }
                    }

                }
            }
        }

        return result;
    }
}

