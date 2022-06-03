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
  int squareSpeed = 1;
  int intScore = 0;
  int intOldX;
  int intOldY;
  int intOldWidth;

  boolean playerAlive = true;
  boolean mouseClicked = false;

  ArrayList<Integer> movingX = new ArrayList<Integer>();
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
    previousX.add((width - beginningWidth) / 2);
    previousY.add(height - intSquareHeight);
    previousWidth.add(beginningWidth);

    for (int i = 0; i < 100; i++){
      movingX.add(0);
    }
  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {

    if (playerAlive == true) {
      background(0);

      textSize(25);
      fill(255, 0, 0);
      text("Score: " + intScore, 15, 30); 

      for (int i = 0; i < roundCount; i++) {
        fill(255, 255, 255);
        rect (previousX.get(i), previousY.get(i), previousWidth.get(i), intSquareHeight);
      }
      
      rect (movingX.get(roundCount - 1), previousY.get(roundCount - 1) - intSquareHeight, previousWidth.get(roundCount - 1), intSquareHeight);
      movingX.set(roundCount - 1, movingX.get(roundCount - 1) + squareSpeed);
      
      if (movingX.get(roundCount - 1) >= (width - previousWidth.get(roundCount - 1))) {
        squareSpeed = -squareSpeed;  
      } 
      else if (movingX.get(roundCount - 1) <= 0) {
        squareSpeed = -squareSpeed;
      }

    }
  }
  
  // define other methods down here.
  
  /**
   * Sets mouseClicked boolean to true 
   */
  public void mousePressed() {
    mouseClicked = true;
    // If clicked to the right of base
    if (movingX.get(roundCount - 1) > previousX.get(roundCount - 1) && movingX.get(roundCount - 1) < previousX.get(roundCount - 1) + previousWidth.get(roundCount - 1)){
      previousX.add(movingX.get(roundCount - 1));
      previousWidth.add(previousWidth.get(roundCount - 1) - movingX.get(roundCount - 1) + previousX.get(roundCount - 1));
      previousY.add(previousY.get(roundCount - 1) - intSquareHeight);
      intScore++;
    }
    // If clicked to the left of base
    else if (movingX.get(roundCount - 1) + previousWidth.get(roundCount - 1) > previousX.get(roundCount - 1) &&  movingX.get(roundCount - 1) < previousX.get(roundCount - 1)){
      previousX.add(previousX.get(roundCount - 1));
      previousWidth.add(previousWidth.get(roundCount - 1) + movingX.get(roundCount - 1) - previousX.get(roundCount - 1));
      previousY.add(previousY.get(roundCount - 1) - intSquareHeight);
      intScore++;
    }
    // If clicked directly on base
    else if (movingX.get(roundCount - 1) == previousX.get(roundCount - 1)){
      previousX.add(movingX.get(roundCount - 1));
      previousWidth.add(previousWidth.get(roundCount - 1));
      previousY.add(previousY.get(roundCount - 1) - intSquareHeight);
      intScore += 3;
    }
    // If box misses the previous box, end game
    else {
        playerAlive = false;
    }

    if (playerAlive == true){
      
      if (squareSpeed < 0){
        squareSpeed = -squareSpeed;
      }

      roundCount++;
      if (roundCount % 2 == 1 && roundCount > 1){
        squareSpeed++;
        movingX.add(roundCount, width - previousWidth.get(roundCount - 1) - 1);
        movingX.remove(roundCount - 1);
      }

      // Move the game back down to the bottom of the screen every 15 rounds
      if ((roundCount - 1) % 15 == 0 && roundCount > 1){
        intOldX = previousX.get(roundCount - 1);
        intOldY = previousY.get(roundCount - 1);
        intOldWidth = previousWidth.get(roundCount - 1);

        previousX.clear();
        previousY.clear();
        previousWidth.clear();

        previousX.add(intOldX);
        previousY.add(height - intSquareHeight);
        previousWidth.add(intOldWidth);
        roundCount = 1;
      }
    }
  }

  /**
   * Sets mouseClicked boolean to false
   */
  public void mouseReleased() {
    mouseClicked = false;
  }
}