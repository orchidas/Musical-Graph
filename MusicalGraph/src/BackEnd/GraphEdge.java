package BackEnd;

public class GraphEdge {
	private String to;
	private String from;
	private double duration;
	
	public GraphEdge(){
		to ="";
		from ="";
		duration = 0.0;
	}
	
	public GraphEdge(String from, String to, double dur){
		this.to = to;
		this.from = from;
		this.duration = dur;
	}
	
	public void setEnd(String to){
		this.to = to;
	}
	
	public String getEnd(){
		return this.to;
	}
	
	public void setStart(String from){
		this.from = from;
	}
	
	public String getStart(){
		return this.from;
	}
	
	public void setLength(double dur){
		this.duration = dur;
	}
	
	public double getLength(){
		return this.duration;
	}

}
