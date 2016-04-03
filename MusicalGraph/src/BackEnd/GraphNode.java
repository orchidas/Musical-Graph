package BackEnd;
import java.util.List;
import java.util.ArrayList;

public class GraphNode {
	private String name;
	private List<String> neighbors;
	private List<GraphEdge> edges;
	
	//default constructor
	public GraphNode()
	{
		name ="";
		neighbors = new ArrayList<String>();
		edges = new ArrayList<GraphEdge>();
	}
	//parameterized constructor
	public GraphNode(String name){
		this.name = name;
		neighbors = new ArrayList<String>();
		edges = new ArrayList<GraphEdge>();
	}
	
	public String getName(){
		return this.name;
	}
	
	public List<String> getNeighbors(){
		return this.neighbors;
	}
	
	public List<GraphEdge> getEdges(){
		return this.edges;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void addEdge(GraphEdge edge){
		edges.add(edge);
		this.addNeighbor(edge);
	}
	
	public void addNeighbor(GraphEdge edge){
		if(edge.getStart() == this.name && !neighbors.contains(edge.getEnd()))
			neighbors.add(edge.getEnd());
		else if(edge.getEnd() == this.name && !neighbors.contains(edge.getStart()))
			neighbors.add(edge.getStart());
	}
}
	

