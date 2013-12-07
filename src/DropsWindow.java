import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.TColor;
import toxi.geom.Vec2D;

import com.hookedup.processing.ExtraWindow;

public class DropsWindow extends ExtraWindow {

	public static final int RIGHT = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int UP = 3;
	public float yCount;
	public float totWidth = 25;
	public float speed = 10;
	public float totHeight = 8;
	public float inc;
	public float[] positions;
	public float[] directions;
	private boolean generated = false;
	private float modifier = 1;
	private int ranCol;
	private int acrossCounter;
	private int downCounter;
	private int direction;
	private Vec2D currPos;
	private float layers = 0;
	private int colorCounter = 0;
	private boolean blackSnake = false;

	
	
	public DropsWindow(PApplet theApplet, String theName, int theWidth,
			int theHeight) {
		super(theApplet, theName, theWidth, theHeight);
		// initWin();
	}

	public void setup() {
		frameRate(40);
		background(0);
	//	ranCol = TColor.newRandom();
		colorMode(HSB, 50, 100, 100);
		setRanCol();

	}

	private void setRanCol() {
		ranCol = color(0, 100, 100);
	//	colorCounter = 0;
		currPos = new Vec2D(layers, layers);
		
		direction = RIGHT;
		

	}

	public void draw() {
		
		noSmooth();
		if (blackSnake){
			stroke(0, 0, 0);
		}else{
			
			stroke(colorCounter, 100, 100);
			
		}
		point(currPos.x, currPos.y);
		colorCounter++;
		if (colorCounter>=50){
			colorCounter = 0;
		}
		
		switch (direction) {
		
		case RIGHT:
			if (currPos.x < totWidth - layers -1) {
				currPos = currPos.add(new Vec2D(1, 0));
			} else {
				direction = DOWN;
			}
			break;
		case DOWN:
			if (currPos.y < totHeight - layers -1) {
				currPos = currPos.add(new Vec2D(0, 1));
			} else {
				direction = LEFT;
			}
			break;
		case LEFT:
			if (currPos.x > 0 + layers ) {
				currPos = currPos.add(new Vec2D(-1, 0));
			} else {
				direction = UP;
			}
			break;
		case UP:
			if (currPos.y > 0 + layers +1) {
				currPos = currPos.add(new Vec2D(0, -1));
			} else {
				if (layers<(totHeight/2)-1){
				layers++;
				}else{
					
					layers = 0;
					int ranTest = (int) random(0,8);
					switch (ranTest) {
					case 0:
					//	background(0);
						blackSnake = false;
						break;
					case 1:
					//	background(255, 0, 100);
						break;
					case 2:
						
						//blackSnake = true;
						break;

					default:
						break;
					}
					
					//colorCounter = 0;
					
				}
				setRanCol();
				
			}
			break;

		default:
			break;
		}
		
		
		

	}

	// set random positions and directions before starting
	private void generateDots() {
		directions = new float[8];
		positions = new float[8];
		int newRan;
		float dirRan;
		for (int i = 0; i < 8; i++) {
			newRan = (int) random(25);
			dirRan = random(1, 5);
			dirRan /= 20;

			if (dirRan == 0)
				dirRan = .5f;

			positions[i] = newRan;
			directions[i] = dirRan;

		}
		generated = true;
		println(positions);
	}

	private void updateFirst() {
		float pos;
		float direction;
		for (int i = 0; i < totHeight; i++) {
			pos = positions[i];
			direction = directions[i];
			// going up
			if (direction > 0) {
				// check if it is at the end
				if (pos >= totWidth) {
					direction = direction * -1;
					directions[i] = direction;
				}
			} else {
				// going down
				if (pos <= 0) {
					// check if it is at the begining
					direction = direction * -1;
					directions[i] = direction;
				}
			}
			pos += directions[i];
			positions[i] = pos;

		}
		println(positions);

	}
}
