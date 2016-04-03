package BackEnd;

import java.util.Random;


public class CreateGraph {
	String[] minorChords;
	String[] majorChords;
	Graph g;
	Random r;
	
	public CreateGraph(){}
	
	public CreateGraph(Graph graph){
		majorChords = new String[]{"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
		minorChords = new String[]{"Cm","C#m","Dm","D#m","Em","Fm","F#m","Gm","G#m","Am","A#m","Bm"};
		g = graph;
		r = new Random();
	}
	
   public void addVertices(){
	   for(int i=0;i<12;i++){
		   g.addVertex(majorChords[i]);
		   g.addVertex(minorChords[i]);
	   }
   }
   
   public void addEdges(){
	   //add edges for major and minor chords
	   for(int i=0;i<12;i++){
		   g.addEdge(majorChords[i],majorChords[(i+5)%12], generateRandomNumber());
		   g.addEdge(minorChords[i],minorChords[(i+5)%12], generateRandomNumber());
		   g.addEdge(majorChords[i],majorChords[(i+7)%12], generateRandomNumber());
		   g.addEdge(minorChords[i],minorChords[(i+7)%12], generateRandomNumber());
		   g.addEdge(majorChords[i],minorChords[(i+2)%12], generateRandomNumber());
		   g.addEdge(majorChords[i],minorChords[(i+4)%12], generateRandomNumber());
		   g.addEdge(majorChords[i],minorChords[(i+9)%12], generateRandomNumber());
		   g.addEdge(minorChords[i],majorChords[(i+8)%12], generateRandomNumber());
		   g.addEdge(minorChords[i],majorChords[(i+10)%12], generateRandomNumber());
	   }
   }
  
   //generate a random number between 0.8 and 0.2 that will represent the edge length
   public double generateRandomNumber(){
	   return 0.2+(0.8-0.2)*r.nextDouble();
   }
   
}


