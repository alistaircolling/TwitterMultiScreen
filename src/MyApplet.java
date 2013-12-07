

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import processing.core.*;

/**
* A simple Processing Demo Applet
*
* Used to demonstrate the combination of JFrame, JButton, JFileChooser
* and PApplet
*
* Moves and displays a list of balls on the applet's screen
* A background image can be loaded
*
* This applet can be used as ActionListener for Java Applications.
* @author georg munkel
*
*/
public class MyApplet extends PApplet implements ActionListener{

        //list of all balls
        ArrayList <Ball> ballList;
        //the background image
        PImage bgImg = null;

        @Override
        public void setup() {
                size(400, 300);
                ballList = new ArrayList<Ball>();
                //creates a first ball
                createNewBall();
        }

        @Override
        public void draw() {

                //check if the background image is already loaded
                //if not, the background is painted white
                if (bgImg == null) {
                        background(255);
                } else {
                        image(bgImg,0,0, width, height);
                }

                //move and display all balls
                for (int i=0; i<ballList.size(); i++) {
                        Ball ball = ballList.get(i);
                        ball.move();
                        ball.display();
                }
        }

        /**
        * implementation from interface ActionListener
        * method is called from the Application
        * the String being compared is the ActionCommand from the button
        */
        public void actionPerformed(ActionEvent evt) {
                if (evt.getActionCommand().equals("create ball")) {
                        createNewBall();
                } else {
                        println("actionPerformed(): can't handle " +evt.getActionCommand());
                }
        }

        /**
        * this method is called by the ActionListener asigned to
        * the JButton buttonLoad in Application
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
        * simple inner class Ball
        * balls have a position, speed, size and color
        * the basic constructor assign random values to all properties
        *
        * balls can move
        * balls can display themselves
        */
        private  class Ball {
                float x;
                float y;
                float size;
                float speedX;
                float speedY;
                Color color;

                private Ball() {
                        this.size = random(10, 40);
                        this.x = random(this.size, width-this.size);
                        this.y = random(this.size, height-this.size);
                        this.speedX = random(-2, 2) *3;
                        this.speedY = random(-2, 2) *3;
                        this.color = new Color(random(1), random(1), random(1));
                }

                private void move() {
                        if (x+size/2f > width || x-size/2f < 0) speedX = -speedX;
                        if (y+size/2f > height || y-size/2f < 0) speedY = -speedY;
                        x += speedX;
                        y += speedY;

                }

                private void display() {
                        stroke(color.getRGB());
                        fill(color.getRGB(), 120);
                        ellipse(x, y, size, size);
                }
        }


}
