import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import processing.core.*;

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
	ArrayList<Ball> ballList;
	// the background image
	PImage bgImg = null;
	private PFont fontA;
	private int totalTweets = 0;
	private String lastTweet = "";

	@Override
	public void setup() {
		background(0);
		fontA = loadFont("Monospaced-48.vlw");
		colorMode(HSB, 360, 1, 1);
		size(1024, 768);
		ballList = new ArrayList<Ball>();
		// creates a first ball
		// createNewBall();
		addTitle();
	}

	private void addTitle() {

		fill(360, 0, 100);
		// Set the font and its size (in units of pixels)
		textFont(fontA, 40);
		text("Realtime Twitter Monitoring", 10, 48);

		fill(200, 20, 100);
		// Set the font and its size (in units of pixels)
		textFont(fontA, 20);
		text(lastTweet, 10, 70);

	}

	@Override
	public void draw() {

	}

	public void printList(ArrayList<Entry<String, Integer>> mapList) {
		totalTweets++;
		int yPos = 100;
		int xPos = 10;
		int fontSize = 0;
		background(0);
		addTitle();
		//

		int maxVal = mapList.get(mapList.size() - 1).getValue();
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
			createNewBall();
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
	private void createNewBall() {
		Ball nBall = new Ball();
		ballList.add(nBall);
	}

	/*
	 * simple inner class Ball balls have a position, speed, size and color the
	 * basic constructor assign random values to all properties
	 * 
	 * balls can move balls can display themselves
	 */
	private class Ball {
		float x;
		float y;
		float size;
		float speedX;
		float speedY;
		Color color;

		private Ball() {
			this.size = random(10, 40);
			this.x = random(this.size, width - this.size);
			this.y = random(this.size, height - this.size);
			this.speedX = random(-2, 2) * 3;
			this.speedY = random(-2, 2) * 3;
			this.color = new Color(random(1), random(1), random(1));
		}

		private void move() {
			if (x + size / 2f > width || x - size / 2f < 0)
				speedX = -speedX;
			if (y + size / 2f > height || y - size / 2f < 0)
				speedY = -speedY;
			x += speedX;
			y += speedY;

		}

		private void display() {
			stroke(color.getRGB());
			fill(color.getRGB(), 120);
			ellipse(x, y, size, size);
		}
	}

	public void setTweet(String string) {
		lastTweet = string;
		// TODO Auto-generated method stub

	}

}
