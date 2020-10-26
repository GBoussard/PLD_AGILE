package drawmap.model;

import javafx.util.Pair;

import java.util.*;

public class ComputeTour extends Observable{

    public static final double speed = 15/3.6; //speed is 15km/h so 15/3.6 m/s

    private List<Intersection> cheminInter;
    private List<Segment> chemin;
    private List<Pair<Intersection, Date>> intersectionsDate; //Useful for TextualView
    private boolean computed;

    private CityMap cm;
    private DeliveryTour dt;

    public ComputeTour(CityMap cm, DeliveryTour dt){
        this.cm = cm;
        this.dt = dt;
        this.computed = false;
    }

    public void computeTour(){

        intersectionsDate = new LinkedList<Pair<Intersection, Date>>();
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
        tsp.searchSolution(180000, g,requests);
        System.out.print("Solution of cost "+tsp.getSolutionCost()+" found in "
                +(System.currentTimeMillis() - startTime)+"ms : ");
        for (int i=0; i< allPoints.size(); i++)
            System.out.print(tsp.getSolution(i).getId()+" ");
        System.out.println("0");
        cheminInter = new LinkedList<>();
        chemin = new LinkedList<>();


        for (int i=0; i< allPoints.size()-1; i++){
            LinkedList<Intersection> partialWay = new LinkedList<>();
            Intersection inter = tsp.getSolution(i);
            partialWay = ways.get(new Pair(inter,tsp.getSolution(i+1))).getKey();
            cheminInter.addAll(partialWay);
        }
        LinkedList<Intersection> partialWay = new LinkedList<>();
        Intersection inter = tsp.getSolution(allPoints.size()-1);
        partialWay = ways.get(new Pair(inter,tsp.getSolution(0))).getKey();
        cheminInter.addAll(partialWay);

        intersectionsDate.add(new Pair(cheminInter.get(0), dt.getDepartureTime()));

        for (int i=0; i< cheminInter.size()-1; i++){

            Intersection current = cheminInter.get(i);

            for (Pair<Segment, Intersection> voisin : current.getVoisins()) {
                if(voisin.getValue().getId()==cheminInter.get(i+1).getId()){
 //                   System.out.println("found match");
                    chemin.add(voisin.getKey());
                    int timeToTravel = (int) (voisin.getKey().getLength() / speed); //time is in second
                    long nextDate = intersectionsDate.get(intersectionsDate.size() - 1 ).getValue().getTime() + 1000 * timeToTravel;
                    intersectionsDate.add(new Pair(voisin.getValue(), new Date(nextDate)));
                }
            }
        }

        computed = true;
        setChanged();
        notifyObservers();

    }

    public Iterator<Segment> getPathIterator(){
        if(chemin != null){
            return chemin.iterator();
        }
        else{
            return null;
        }
    }

    public boolean getComputed(){
        return computed;
    }
}
