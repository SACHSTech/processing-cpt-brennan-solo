import processing.core.PApplet;
import java.util.*;

/**
 * Description: 
 * @author: B. Chan
 */

public class Sketch extends PApplet {
  float blockHeight = 30;
  float beginningWidth = 300;
  float beginningX = 0;
  int roundCount = 1;
  int blockSpeed = 1;
  int intScore = 0;

  float intOldX;
  float intOldY;
  float intOldWidth;

  boolean playerAlive = true;
  boolean mouseClicked = false;

  ArrayList<Float> movingX = new ArrayList<Float>();
  ArrayList<Float> previousWidth = new ArrayList<Float>();
  ArrayList<Float> previousX = new ArrayList<Float>();
  ArrayList<Float> previousY = new ArrayList<Float>();

  ArrayList<Float> RBGRed = new ArrayList<Float>();
  ArrayList<Float> RBGBlue = new ArrayList<Float>();
  ArrayList<Float> RBGGreen = new ArrayList<Float>();
  int intColourChange = 10;
  float fltMaxColour = 255;
  float fltMinColour = 255;

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
    previousY.add(height - blockHeight);
    previousWidth.add(beginningWidth);
    movingX.add(beginningX);

    RBGRed.add(random(100, 240));
    RBGBlue.add(random(100, 240));
    RBGGreen.add(random(100, 240));
    RBGRed.add(RBGRed.get(0) + intColourChange);
    RBGBlue.add(RBGBlue.get(0));
    RBGGreen.add(RBGGreen.get(0));
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
        fill(RBGRed.get(i), RBGBlue.get(i), RBGGreen.get(i));
        rect (previousX.get(i), previousY.get(i), previousWidth.get(i), blockHeight);
      }
      
      fill(RBGRed.get(roundCount), RBGBlue.get(roundCount), RBGGreen.get(roundCount));
      rect (movingX.get(roundCount - 1), previousY.get(roundCount - 1) - blockHeight, previousWidth.get(roundCount - 1), blockHeight);
      movingX.set(roundCount - 1, movingX.get(roundCount - 1) + blockSpeed);
      
      if (movingX.get(roundCount - 1) >= (width - previousWidth.get(roundCount - 1))) {
        blockSpeed = -blockSpeed;  
      } 
      else if (movingX.get(roundCount - 1) <= 0) {
        blockSpeed = -blockSpeed;
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
      previousY.add(previousY.get(roundCount - 1) - blockHeight);
      intScore++;
    }
    // If clicked to the left of base
    else if (movingX.get(roundCount - 1) + previousWidth.get(roundCount - 1) > previousX.get(roundCount - 1) &&  movingX.get(roundCount - 1) < previousX.get(roundCount - 1)){
      previousX.add(previousX.get(roundCount - 1));
      previousWidth.add(previousWidth.get(roundCount - 1) + movingX.get(roundCount - 1) - previousX.get(roundCount - 1));
      previousY.add(previousY.get(roundCount - 1) - blockHeight);
      intScore++;
    }
    // If clicked directly on base
    else if (movingX.get(roundCount - 1) == previousX.get(roundCount - 1)){
      previousX.add(movingX.get(roundCount - 1));
      previousWidth.add(previousWidth.get(roundCount - 1));
      previousY.add(previousY.get(roundCount - 1) - blockHeight);
      intScore += 3;
    }
    // If block misses the previous block, end game
    else {
        playerAlive = false;
    }

    if (playerAlive == true){
      
      if (blockSpeed < 0){
        blockSpeed = -blockSpeed;
      }

      RBGRed.add(RBGRed.get(roundCount - 1) + intColourChange);
      if (RBGRed.get(roundCount) >= 255){
        RBGRed.remove(roundCount);
        RBGRed.add(fltMaxColour);
        RBGBlue.add(RBGBlue.get(roundCount - 1) + intColourChange);
        if (RBGBlue.get(roundCount) >= 255){
          RBGBlue.remove(roundCount);
          RBGBlue.add(fltMaxColour);
          RBGGreen.add(RBGGreen.get(roundCount - 1) + intColourChange);
        }
      }
      else {
        RBGBlue.add(RBGBlue.get(roundCount - 1));
        RBGGreen.add(RBGGreen.get(roundCount - 1));
      }

      roundCount++;
      if (roundCount % 2 == 1 && roundCount > 1){
        blockSpeed++;
        movingX.add(width - previousWidth.get(roundCount - 1) - 1);
      }
      else if (roundCount > 1) {
        movingX.add(beginningX);
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
        previousY.add(height - blockHeight);
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