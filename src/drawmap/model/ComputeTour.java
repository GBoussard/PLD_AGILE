package drawmap.model;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class ComputeTour {
    public static LinkedList<Segment> computeTour(CityMap cm, DeliveryTour dt){

        List<Request> requests = dt.getRequests();
        Vector<Intersection> allPoints = new Vector<>();
        requests.forEach(e->{
            allPoints.add(e.getPickup());
            allPoints.add(e.getDelivery());
        });
        HashMap<Pair<Intersection,Intersection>,Double> cost = new HashMap<>();
        HashMap<Pair<Intersection,Intersection>,Pair<LinkedList<Intersection>, Double>> ways = new HashMap<>();
        for (Intersection i1 : allPoints) {
            for (Intersection i2 : allPoints) {
                if(i1!=i2){
                    Pair<LinkedList<Intersection>, Double> way = Algorithm.A_star(i1,i2,cm);
                    ways.put(new Pair(i1,i2),way);
                    cost.put(new Pair(i1,i2), way.getValue());
                }
            }
        }
        CompleteGraph g = new CompleteGraph(cost,allPoints);
        TSP tsp = new TSP1();
        long startTime = System.currentTimeMillis();
        tsp.searchSolution(180000, g);
        System.out.print("Solution of cost "+tsp.getSolutionCost()+" found in "
                +(System.currentTimeMillis() - startTime)+"ms : ");
        for (int i=0; i< allPoints.size(); i++)
            System.out.print(tsp.getSolution(i).getId()+" ");
        System.out.println("0");
        LinkedList<Segment> chemin = new LinkedList<>();
        for (int i=0; i< allPoints.size()-1; i++){
            Intersection inter = tsp.getSolution(i);
            for (Pair<Segment, Intersection> voisin : inter.getVoisins()) {
                if(voisin.getValue().getId()==tsp.getSolution(i+1).getId()){
                    chemin.add(voisin.getKey());
                }
            }
        }
    return chemin;
    }
}
