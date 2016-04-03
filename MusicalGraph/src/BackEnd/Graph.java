package BackEnd;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Queue;
import java.util.Stack;


public class Graph {

	private HashMap<String, GraphNode> vertices;
	private List<GraphEdge> edges;
	private int numVertices;
	private int numEdges;
	
	public Graph(){
		numVertices = 0;
		numEdges = 0;
		vertices = new HashMap<String, GraphNode>();
		edges = new ArrayList<GraphEdge>();
	}
	
	public int getNumVertices(){
		return numVertices;
	}
	
	public int getNumEdges(){
		return numEdges;
	}
	
	public void addVertex(String name){
		//increment number of vertices first
		numVertices++;
		GraphNode newNode = new GraphNode(name);
		vertices.put(name,  newNode);
	}
	
	public void addEdge(String start, String end, double length){
		//increment number of edges first
		numEdges++;
		GraphEdge newEdge = new GraphEdge(start, end, length);
		edges.add(newEdge);
		vertices.get(start).addEdge(newEdge);
		vertices.get(end).addEdge(newEdge);	
	}
	
	
	public HashMap<String,GraphNode> getVertices(){
		return this.vertices;
	}
	
	public List<GraphEdge> getEdges(){
		return this.edges;
	}
	
	//print graph for debugging
	public void printGraph(){
		System.out.println("Number of vertices = " + numVertices);
		System.out.println("Number of edges = " + numEdges);
		for(GraphNode vertex: vertices.values()){
				String chord = vertex.getName();
				System.out.println("Chord : " + chord);
				List<GraphEdge> edges = new ArrayList<GraphEdge>(vertex.getEdges());
				for(GraphEdge edge:edges){
					System.out.println("Start Chord = " + edge.getStart());
					System.out.println("End Chord = " + edge.getEnd());
					System.out.println("Chord Length = " + edge.getLength());	
				}
				List<String> neighbors = new ArrayList<String>(vertex.getNeighbors());
				System.out.println("The neighbors are ");
				for(String n: neighbors)
					System.out.println("Chord : " + n);
				System.out.println();
		}
	}
	
	//do BFS on graph
	public List<String> bfs(String start, String goal){
		List<String> explored = new ArrayList<String>();
		Queue<String> queue = new LinkedList<String>();
		
		queue.add(start);
		explored.add(start);
		while(!queue.isEmpty()){
			String current = queue.remove();
			List<String> neighbors = new ArrayList<String>
			(vertices.get(current).getNeighbors());		
			for(String neighbor: neighbors){
				if(!explored.contains(neighbor)){
					explored.add(neighbor);
					queue.add(neighbor);
				}
				if(neighbor.equals(goal))
					return explored;
			}	
	    }
		return explored;
	}
	
	//do DFS on graph
	public List<String> dfs(String start, String goal){
		List<String> explored = new ArrayList<String>();
		Stack<String> stack = new Stack<String>();
		
		stack.push(start);
		explored.add(start);
		while(!stack.isEmpty()){
			String current = stack.pop();
			List<String> neighbors = new ArrayList<String>
			(vertices.get(current).getNeighbors());
			for(String neighbor:neighbors){
				if(!explored.contains(neighbor)){
					explored.add(neighbor);
					stack.push(neighbor);
				}
				if(neighbor.equals(goal)){
					return explored;
				}
			}
		}
		return explored;
	}
	
	public static void main(String args[]){
		Graph g = new Graph();
		CreateGraph create = new CreateGraph(g);
		create.addVertices();
		create.addEdges();
		//g.printGraph();
		List<String> exploreBFS = new ArrayList<String>(g.bfs("D#m", "A"));
		System.out.println("BFS results:");
		for(String s: exploreBFS)
			System.out.print(s+ "\t");
		List<String> exploreDFS = new ArrayList<String>(g.dfs("D#m", "A"));
		System.out.println("\nDFS results:");
		for(String s: exploreDFS)
			System.out.print(s + "\t");
	}
}
	
	
