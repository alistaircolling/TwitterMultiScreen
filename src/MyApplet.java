import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Map.Entry;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import toxi.color.TColor;
import controlP5.ControlP5;
import controlP5.Slider;
import controlP5.Textlabel;

/**
 * A simple Processing Demo Applet
 * 
 * Used to demonstrate the combination of JFrame, JButton, JFileChooser and
 * PApplet
 * 
 * Moves and displays a list of balls on the applet's screen A background image
 * can be loaded
 * 
 * This applet can be used as ActionListener for Java Applications.
 * 
 * @author georg munkel
 * 
 */
public class MyApplet extends PApplet implements ActionListener {

	// list of all balls

	private static final float GRAPH_WIDTH = 700;
	private static final float GRAPH_HEIGHT = 400;
	private static final int MAX_SNAPSHOTS_TO_SHOW = 300;
	//how many are we having in the graph?
	private static final int MAXIMUM_VALUES_TO_TRACK = 5;
	private static final int TOTAL_SLIDERS = 5;
	// the background image
	PImage bgImg = null;
	private PFont fontA;
	private int totalTweets = 0;
	private String lastTweet = "";
	private ArrayList<Snapshot> snapShots;
	private ArrayList<Entry<String, Integer>> mapList;
	private int maxVal;
	private ControlP5 cp5;
	private TColor[] colors;
	private Object myTextlabelA;
	private Textlabel myT;
	private Slider slider1;

	@Override
	public void setup() {
		setupColors();
		background(0);
		fontA = loadFont("Monospaced-48.vlw");
		colorMode(HSB, 360, 1, 1);
		size(1024, 768);

		// creates a first ball
		// createNewBall();
		addTitle();
		setupUI();
	}

	private void setupColors() {
		colors = new TColor[7];
		
		colors[0] = TColor.newHex("595959");
		colors[1] = TColor.newHex("bababa");
		colors[2] = TColor.newHex("A5F4FA");
		colors[3] = TColor.newHex("B7FAB4");
		colors[4] = TColor.newHex("F6FFA8");
		colors[5] = TColor.newHex("FFDB66");
		colors[6] = TColor.newHex("FF87EF");
		
		
	}

	private void setupUI() {
		cp5 = new ControlP5(this);
			myT = cp5.addTextlabel("label","label", 20,20);
			myT.setFont(3);
			
			myT.setSize(300, 300);
			
			setupSliders();
			  
			  
			     
			
			  
			  // repo
//		  myTextlabelA = cp5.addTextlabel(
//		                    .setText("A single ControlP5 textlabel, in yellow.")
//		                    .setPosition(100,50)
//		                    .setColorValue(0xffffff00)
//		                    .setFont(createFont("Georgia",20))
//		                    ;

		
		
	}

	private void setupSliders() {
	//	slider1 = cp5.addSlider("slider1", 0, arg2bnm,)
		// TODO Auto-generated method stub
		
	}

	private void addTitle() {

		fill(360, 0, 100);
		// Set the font and its size (in units of pixels)
		textFont(fontA, 40);
		text("Realtime Twitter Monitoring", 10, 48);

		fill(200, 20, 100);
		// Set the font and its size (in units of pixels)
		textFont(fontA, 20);
		String noFormat = lastTweet.replace("\n", "").replace("\r", "");
		text(noFormat, 10, 70);
		textFont(fontA, 20);
		fill(200, 0, 100);
		text("TOTALS", 10, 90);

	}

	@Override
	public void draw() {
	

	}
	
	public void myDraw() {
		background(0);
		
		drawGraph();
		addTitle();
		if (mapList != null) {
			printList();
		}
		
	}
	
	

	public void printList() {

		totalTweets++;
		int yPos = 100;
		int xPos = 10;
		int fontSize = 0;

		//

		int maxVal = mapList.get(0).getValue();
		float colorDiff = 360 / maxVal;

		for (Entry<String, Integer> entry : mapList) {

			fill(colorDiff * entry.getValue(), 30, 100);
			// Set the font and its size (in units of pixels)
			fontSize = 4 * entry.getValue();
			if (fontSize > 30)
				fontSize = 30;
			textFont(fontA, fontSize);
			text(entry.getKey() + ":" + entry.getValue(), xPos, yPos
					+ (fontSize * .5f));
			// already exists
			// System.out.println(entry.getKey() + ":" + entry.getValue());

			yPos += fontSize;
			if (yPos > height) {
				return;
			}
		}

	}

	/**
	 * implementation from interface ActionListener method is called from the
	 * Application the String being compared is the ActionCommand from the
	 * button
	 */
	public void actionPerformed(ActionEvent evt) {
		println("action performed");
		if (evt.getActionCommand().equals("create ball")) {
			// put method here
		} else {
			println("actionPerformed(): can't handle " + evt.getActionCommand());
		}
	}

	/**
	 * this method is called by the ActionListener asigned to the JButton
	 * buttonLoad in Application
	 */
	public void loadBgImage(File selectedFile) {
		bgImg = loadImage(selectedFile.getAbsolutePath());
	}

	/*
	 * creates a new Ball instance and adds it to ballList
	 */

	public void setTweet(String string) {
		lastTweet = string;
		// TODO Auto-generated method stub

	}

	public void setNewSnapShotList(ArrayList<Snapshot> theSnapShots,
			int theMaxVal) {
		snapShots = theSnapShots;
		maxVal = theMaxVal;
	}

	public void drawGraph() {
		// move in from the side etc
		pushMatrix();
		translate(200, 250);
		fill(200, 0, 100);
		// rect(0, 0, GRAPH_WIDTH, GRAPH_HEIGHT);

		// draw axis
		stroke(255);
		line(0, 0, 0, GRAPH_HEIGHT);
		line(0, GRAPH_HEIGHT, GRAPH_WIDTH, GRAPH_HEIGHT);
		int top = 0;
		if (snapShots != null) {
			if (snapShots.size() > 0) {
				top = snapShots.size() - 1;
			}
		}

		// // draw dots - make sure we only get the last XX snapshots
		// int top = snapShots.size()-1; //get the highest value
		// if (top<0) return;
		int bottom = 0;
		float xPos = 0;
		float yPos = 0;
		float lastX = 0;
		float lastY= 0;
		float diffX = 0; //used to store how far back left to start drawingt he line
		int counter = 0;// used to track our x progress
		String keyName;//used for string labels
		//
		float incPerSnapShot = GRAPH_WIDTH / MAX_SNAPSHOTS_TO_SHOW;
		float yInc = GRAPH_HEIGHT / maxVal;
		
		// if the top value is longer than the maxomim number we can show, move
		// the bottom up accordingly
		if (top > MAX_SNAPSHOTS_TO_SHOW)
			bottom = top - MAX_SNAPSHOTS_TO_SHOW;

		// iterate from the bottom value to the top
		for (int i = bottom; i < top; i++) {
			pushMatrix();
				// get the current snapshot
				Snapshot snap = snapShots.get(i);
				xPos = counter * incPerSnapShot;
				diffX = xPos - lastX;
				// move to the correct xpos
				translate(xPos, GRAPH_HEIGHT);
				fill(100,0,0);
				//ellipse(0, 0, 30, 30);
				int topVal = snap.getArray().size();
				if (topVal>MAXIMUM_VALUES_TO_TRACK) topVal = MAXIMUM_VALUES_TO_TRACK; 
				//iterate through top X values
				for (int j = 0; j < topVal; j++) {
					Entry<String, Integer> entry =  snap.getArray().get(j);
					keyName = entry.getKey();
					yPos = entry.getValue()*yInc;
					noStroke();
					fill((360/maxVal)*entry.getValue(), 100, 100);
					ellipse(0, 0-yPos, 4, 4);
					
					//write text on the last one
					if (i>top-2){
						//write the name
						textFont(fontA, 16);
						int textX = 10;
						//flip left and right of dots
						if (j%2==0){
							//textX = (int) (0-(textWidth(keyName)+10));
						}
						text(keyName, textX, 0-yPos);
					}
	
				}
				lastX = xPos;
				
				
			
			
			popMatrix();
			counter++;
		}
		popMatrix();
	

	}

	public void setList(ArrayList<Entry<String, Integer>> mapList2) {
		mapList = mapList2;

	}

	

}
