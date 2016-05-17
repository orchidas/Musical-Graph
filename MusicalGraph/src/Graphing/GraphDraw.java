package Graphing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import processing.core.*;
import BackEnd.*;
import oscP5.*;
import netP5.*;
//this package is used for GUIs - adding text fields and drop down menus etc
import controlP5.*;

public class GraphDraw extends PApplet {
	//object of type graph
	Graph g = new Graph();
	private HashMap<String,GraphNode> vertices;
	private int numVertices;
	HashMap<String,Location> vertexLocation = new HashMap<String,Location>();
	List<String> chords;
	//BFS or DFS, start and stop nodes
	String searchType = "",start ="", goal = "";
	//array of 24 buttons representing each ellipse, ie, vertex (chord)
	boolean buttons[];
	//keeps track of whether chucK has finished playing audio
	int flag = 1;
	//radius of circles representing nodes
	int radius = 40;
	//set up OSC objects
	OscP5 oscP5;
	NetAddress myBroadcastLocation;
	String oscServer = "localhost";
	int oscServerPortSend = 6449;
	int oscServerPortReceive = 6450;
	//set up controlp5 objects
	ControlP5 cp5;
	//add drop down menu to select method of graph traversal
	DropdownList dlist;
	
		
	// Run this project as Java application and this
    // method will launch the sketch
    public static void main(String[] args) {
        String[] a = {"MAIN"};
        PApplet.runSketch( a, new GraphDraw());
    }
	
    public void settings(){
    	size(560,560);
    }
	
	public void setup(){	
		frameRate(25);
		CreateGraph create = new CreateGraph(g);
		create.addVertices();
		create.addEdges();
		getGraphInfo();
		//Instantiation of a new oscP5 object - listens for incoming messages at port
		oscP5 = new OscP5(this, oscServerPortReceive);
		//used for sending osc messages
		myBroadcastLocation = new NetAddress(oscServer, oscServerPortSend);
	    //controlp5 object - for adding interactivity
		cp5 = new ControlP5(this);
		dlist = cp5.addDropdownList("traverseGraph").setPosition(40,20);
		customize(dlist);
		//text fields and button for start and stop nodes
		cp5.addTextfield("startNode").setPosition(220, 20).setSize(50, 30).setAutoClear(false);
		cp5.addTextfield("stopNode").setPosition(320, 20).setSize(50, 30).setAutoClear(false);
		cp5.addBang("Start").setPosition(450, 30).setSize(20,20); 	
	}
	
	
	public void getGraphInfo(){
		numVertices = g.getNumVertices();
		vertices = new HashMap<String, GraphNode>(g.getVertices());
		Set<String> set = new HashSet<String>(vertices.keySet());
		chords = new ArrayList<String>(set);
		buttons = new boolean[numVertices];	
	}
	
	//customize dropdown list
	public void customize(DropdownList d){   
	  d.setItemHeight(20);
	  d.setBarHeight(20);
	  d.addItem("BFS",0);
	  d.addItem("DFS",1);
	  d.addItem("",2);
	  d.setColorBackground(color(60));
	  d.setColorActive(color(255, 128));
	}
	
	//control event generated when we use DDlist/ textfield
	public void controlEvent(ControlEvent theEvent) {
		  if (theEvent.isGroup()) {
		    // check if the Event was triggered from a ControlGroup
			println("event from group : "+theEvent.getGroup().getValue()+" from "+theEvent.getGroup());
				
		  } 
		  else if (theEvent.isController()) {
			 //figure out what kind of search user wants
			println("event from controller : "+theEvent.getController().getValue()+" from "+theEvent.getController());
		    if(theEvent.getController().getName() == "traverseGraph"){
		    	int index = (int)theEvent.getController().getValue();
		    	switch(index){
		    		case 0 : searchType = "BFS";break;
		    		case 1: searchType = "DFS"; break;
		    		default:searchType =""; break;
		    	}
		    	println(searchType);
		    }
		  }
		}
	
	public void draw(){	
		background(128);
		int xBase = 90;
		int yBase = 275;
		int step = 70;
		drawNodes(xBase,yBase,step);
		drawEdges();
		executeSearch();
	}
	
	//draw vertices
	public void drawNodes(int xBase, int yBase, int step){
	    String chordName = "";
	    textSize(12);
		for(int i=0;i<numVertices;i++){
			chordName = chords.get(i);
			if(!buttons[i])
				fill(255,143,143);
			else
				fill(153,0,153);
			ellipse(xBase, yBase, radius, radius);
			fill(0);
			text(chordName, xBase-12, yBase+5);
			//create Location specific to each node
			vertexLocation.put(chordName,new Location(xBase,yBase));
		    xBase += (int) ((int)50*sin((float) (TWO_PI/24.0*i)));
			yBase +=(int) ((int)50*cos((float) (TWO_PI/24.0*i)));
		}
	}
	
	//draw edges
	public void drawEdges(){
		int xPos, yPos;
		for(String chord:chords){
			xPos = vertexLocation.get(chord).getXPos();
			yPos = vertexLocation.get(chord).getYPos();
			List<String> neighbors = vertices.get(chord).getNeighbors();
			for(String neighbor:neighbors){
				int xPosTo = vertexLocation.get(neighbor).getXPos();
				int yPosTo = vertexLocation.get(neighbor).getYPos();
				if(buttons[chords.indexOf(chord)] && buttons[chords.indexOf(neighbor)]){
					stroke(0,0,153);
					strokeWeight(1.0f);
				}
				else{
					stroke(255);
					strokeWeight(0.25f);
				}
				line(xPos,yPos,xPosTo,yPosTo);
			}	
		}
	}
	
	//add interactivity
	public void mousePressed(){
		for(int i=0;i< chords.size();i++){
			//make sure with each new click no button is selected
			if(buttons[i]) buttons[i] = false;
			String chord = chords.get(i);
			int x = vertexLocation.get(chord).getXPos();
			int y = vertexLocation.get(chord).getYPos();
			buttons[i] = overVertex(x,y,radius,radius);
			if(buttons[i]){
				println("Sending to ChucK " + chord);
				playSound(chord);
			}
		}	
	}

   public boolean overVertex(int x, int y, int width, int height){
	   if(mouseX >= x-width/2 && mouseX <= x+width/2
				&& mouseY >= y-height/2 && mouseY <= y+height/2 && flag == 1)
		   return true;
	   else
		   return false;
   }
   
   //listens or osc event(incoming messages) - chucK sends 1 when it finishes playing audio
   public void oscEvent(OscMessage theOscMessage) {
 		String pattern = theOscMessage.addrPattern();
 		flag = theOscMessage.get(0).intValue();
 		String chordPlayed = theOscMessage.get(1).stringValue();
 		//should be "/chuckSends" + chordName
 		println(pattern + " " + chordPlayed);
 		// disable all buttons, clear text fields too
 	    int pos = chords.indexOf(chordPlayed);
 	    buttons[pos] = true;
   }   
			
  public void playSound(String chords){
	    if(flag == 1){
			 OscMessage myOscMessage = new OscMessage("/chuckReceives");
		     // add a value to the OscMessage - name of chord clicked
			 myOscMessage.add(chords);
		     // send the OscMessage to a remote location specified in myNetAddress
		     oscP5.send(myOscMessage, myBroadcastLocation);
		     //change flag to 0 till positive message is received from chuck
		     //this ensures no new sound is played till current audio is over
		     flag = 0;   
		}
	}
    
   //gets executed when "Start" button is pressed
   public void Start(){
	   print("the following text was submitted :");
	   start = cp5.get(Textfield.class,"startNode").getText();
	   goal = cp5.get(Textfield.class,"stopNode").getText();
	   print(" Start Chord = " + start);
	   print(" Stop Chord = " + goal);
	   println();
   }
   
   //this executes the search
   public void executeSearch(){
	   if(chords.contains(start) && chords.contains(goal) && searchType != ""){
		    println("Valid arguments, starting search");
	   	    List<String> exploredNodes = new ArrayList<String>();
	        if(searchType == "BFS")
		   		exploredNodes = g.bfs(start, goal);
	   		else if(searchType == "DFS")
		   		exploredNodes = g.dfs(start,  goal);
	   		println("The search returned");
	   		String toPlay ="";
	   		for(String node:exploredNodes){
		   		print(node + "\t");
		   		toPlay += node + " ";	   
	   		}
	   		playSound(toPlay);
	  		//reset parameters so function is not called continuously by draw()
	   		searchType ="";
	   		goal ="";
	   		start = "";
   	   }
   }
}
