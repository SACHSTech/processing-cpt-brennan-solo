import processing.core.PApplet;
import java.util.*;

/**
 * Description: 
 * @author: B. Chan
 */

public class Sketch extends PApplet {
	int intSquareHeight = 30;
  int beginningWidth = 300;
  int roundCount = 1;
  int movingX = 0;
  int squareSpeed = 1;
  int intScore = 0;

  boolean playerAlive = true;
  boolean mouseClicked = false;

  ArrayList<Integer> previousWidth = new ArrayList<Integer>();
  ArrayList<Integer> previousX = new ArrayList<Integer>();
  ArrayList<Integer> previousY = new ArrayList<Integer>();

  /**
   * Called once at the beginning of execution, put your size all in this method
   */
  public void settings() {
	// put your size call here
    size(500, 500);
  }

  /** 
   * Called once at the beginning of execution.  Add initial set up
   * values here i.e background, stroke, fill etc.
   */
  public void setup() {
    background(0);
    previousX.add((width - beginningWidth) / 2);
    previousY.add(height - intSquareHeight);
    previousWidth.add(beginningWidth);
  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {

    if (playerAlive == true) {
      for (int i = 0; i < roundCount; i++) {
        fill(255, 255, 255);
        rect (previousX.get(i), previousY.get(i), previousWidth.get(i), intSquareHeight);
      }
      
      rect (movingX, previousY.get(roundCount - 1) - intSquareHeight, previousWidth.get(roundCount - 1), intSquareHeight);
      movingX += squareSpeed;
      
      if (movingX >= (500 - previousWidth.get(roundCount - 1))) {
        squareSpeed = -squareSpeed;
        movingX = 500 - previousWidth.get(roundCount - 1);   
      } 
      else if (movingX <= 0) {
        squareSpeed = -squareSpeed;
        movingX = 0;
      }

    }
  }
  
  // define other methods down here.
  
  /**
   * Sets mouseClicked boolean to true 
   */
  public void mousePressed() {
    mouseClicked = true;
    if (movingX > previousX.get(roundCount - 1) && movingX < previousX.get(roundCount - 1) + previousWidth.get(roundCount - 1)){
      previousX.add(movingX);
      previousWidth.add(previousWidth.get(roundCount - 1) - movingX + previousX.get(roundCount - 1));
      previousY.add(previousY.get(roundCount - 1) - intSquareHeight);
    }
    else if (movingX + previousWidth.get(roundCount - 1) > previousX.get(roundCount - 1) &&  movingX + previousWidth.get(roundCount - 1) < previousX.get(roundCount - 1) + previousWidth.get(roundCount - 1)){
      previousX.add(previousX.get(roundCount - 1));
      previousWidth.add(previousWidth.get(roundCount - 1) + movingX - previousX.get(roundCount - 1));
      previousY.add(previousY.get(roundCount - 1) - intSquareHeight);
    }
    else if (movingX == previousX.get(roundCount - 1)){
      previousX.add(movingX);
      previousWidth.add(previousWidth.get(roundCount - 1));
      previousY.add(previousY.get(roundCount - 1) - intSquareHeight);
    }
    else {
        playerAlive = false;
    }

    roundCount++;
  }

  /**
   * Sets mouseClicked boolean to false
   */
  public void mouseReleased() {
    mouseClicked = false;
  }
}