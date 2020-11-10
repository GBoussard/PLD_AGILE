package drawmap.model;

public class Node{
    public Long id;
    public Double cost;
    public NodeType nodeType;
    public Node(Long id, Double cost, NodeType nodeType){
        this.id = id;
        this.cost = cost;
        this.nodeType = nodeType;
    }

    public Long getId(){
        return id;
    }
}
