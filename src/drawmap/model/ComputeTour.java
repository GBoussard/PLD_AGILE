package drawmap.model;

import javafx.util.Pair;

import java.util.*;

/**
 * Computed tour model class
 */
public class ComputeTour extends Observable{

    public static final double speed = 15/3.6; //speed is 15km/h so 15/3.6 m/s

    private List<Intersection> cheminInter;
    private List<Segment> chemin;
    private List<Pair<Intersection, Date>> intersectionsDate; //Useful for TextualView
    private boolean computed;

    private CityMap cm;
    private DeliveryTour dt;

    /**
     * Constructor for ComputeTour
     * @param cm : map model used for computing
     * @param dt : requests to be fulfilled
     */
    public ComputeTour(CityMap cm, DeliveryTour dt){
        this.cm = cm;
        this.dt = dt;
        this.computed = false;
    }

    /**
     * Computes tour with A* algorithm
     */
    public void computeTour(){

        intersectionsDate = new LinkedList<Pair<Intersection, Date>>();
        List<Request> requests = dt.getRequests();
        Vector<Intersection> allPoints = new Vector<>();
        requests.forEach(e->{
            allPoints.add(e.getPickup());
            allPoints.add(e.getDelivery());
        });
        HashMap<Pair<Long,Long>,Double> cost = new HashMap<>();
        HashMap<Pair<Long,Long>,Pair<LinkedList<Intersection>, Double>> ways = new HashMap<>();
        HashMap<Intersection,Segment> edges = new HashMap<>();
        Intersection depot = dt.getOrigin();
        for (Intersection i1 : allPoints) {

            Pair<LinkedList<Intersection>, Double> way = Algorithm.A_star(depot,i1,cm);
            ways.put(new Pair(depot.getId(),i1.getId()),way);
            cost.put(new Pair(depot.getId(),i1.getId()), way.getValue());
            Segment seg = new Segment(way.getValue(),way.getKey().getFirst(),way.getKey().getLast(),"");
            edges.put(depot, seg);

            Pair<LinkedList<Intersection>, Double> way2 = Algorithm.A_star(i1,depot,cm);
            ways.put(new Pair(i1.getId(),depot.getId()),way2);
            cost.put(new Pair(i1.getId(),depot.getId()), way2.getValue());
            Segment seg2 = new Segment(way2.getValue(),way2.getKey().getFirst(),way2.getKey().getLast(),"");
            edges.put(i1, seg2);

            for (Intersection i2 : allPoints) {
                if(i1!=i2){
                    Pair<LinkedList<Intersection>, Double> way3 = Algorithm.A_star(i1,i2,cm);
                    ways.put(new Pair(i1.getId(),i2.getId()),way3);
                    cost.put(new Pair(i1.getId(),i2.getId()), way3.getValue());

                    Segment seg3 = new Segment(way3.getValue(),way3.getKey().getFirst(),way3.getKey().getLast(),"");
                    edges.put(i1, seg3);
                }
            }
        }


        CompleteGraph g = new CompleteGraph(cost);
        TSP tsp = new TSP1();
        long startTime = System.currentTimeMillis();
        long average =0;
        tsp.searchSolution(180000, g,requests, dt.getOrigin());

        average = System.currentTimeMillis() - startTime;
        System.out.println("Solution of cost "+tsp.getSolutionCost()+" found in "
                +(average)+"ms : ");

        for (int i=0; i< allPoints.size(); i++)
            System.out.print(tsp.getSolution(i)+" ");

        System.out.println("0");
        LinkedList<Intersection> cheminInter = new LinkedList<>();
        LinkedList<Segment> chemin = new LinkedList<>();

        for (int i=0; i< allPoints.size(); i++){
            LinkedList<Intersection> partialWay = new LinkedList<>();
            Long inter = tsp.getSolution(i);
            partialWay = ways.get(new Pair(inter,tsp.getSolution(i+1))).getKey();
            cheminInter.addAll(partialWay);
        }
        LinkedList<Intersection> partialWay = new LinkedList<>();
        Long inter = tsp.getSolution(allPoints.size());
        partialWay = ways.get(new Pair(inter,tsp.getSolution(0))).getKey();
        cheminInter.addAll(partialWay);
        intersectionsDate.add(new Pair(depot, dt.getDepartureTime()));
        for (int i=0; i< cheminInter.size()-1; i++){
            Intersection current = cheminInter.get(i);
            for (Pair<Segment, Intersection> voisin : current.getNeighbours()) {
                if(voisin.getValue().getId()==cheminInter.get(i+1).getId()){

                    chemin.add(voisin.getKey());
                    int timeToTravel = (int) (voisin.getKey().getLength() / speed); //time is in second
                    long nextDate = intersectionsDate.get(intersectionsDate.size() - 1 ).getValue().getTime() + 1000 * timeToTravel;
                    intersectionsDate.add(new Pair(voisin.getValue(), new Date(nextDate)));
                }
            }
        }
        this.chemin = chemin;
        computed = true;
        setChanged();
        notifyObservers();
    }

    /**
     * Gets an iterator to iterate through the computed path
     * @return Iterator
     */
    public Iterator<Segment> getPathIterator(){
        if(chemin != null){
            return chemin.iterator();
        }
        else{
            return null;
        }
    }

    /**
     * Gets an iterator to iterate through the list of dates by intersection
     * @return Iterator
     */
    public Iterator<Pair<Intersection, Date>> getIntersectionsDateIterator(){
        if(intersectionsDate != null){
            return intersectionsDate.iterator();
        }
        else{
            return null;
        }
    }

    /**
     * Gets path
     * @return chemin
     */
    public List<Segment> getPath() {
        return chemin;
    }


    /**
     * Return <pre>true</pre> if computation has been made
     * @return
     */
    public boolean getComputed(){
        return computed;
    }

    public CityMap getCityMap(){
        return this.cm;
    }

    public DeliveryTour getDeliveryTour(){
        return this.dt;
    }


    /**
     * Sets computed status
     * @param value : new value
     */
    public void setComputed(boolean value){ this.computed = value;}
}
