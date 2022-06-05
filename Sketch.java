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
  boolean RGBMaxed = false;

  ArrayList<Float> movingX = new ArrayList<Float>();
  ArrayList<Float> previousWidth = new ArrayList<Float>();
  ArrayList<Float> previousX = new ArrayList<Float>();
  ArrayList<Float> previousY = new ArrayList<Float>();

  ArrayList<Float> RGBRed = new ArrayList<Float>();
  ArrayList<Float> RGBGreen = new ArrayList<Float>();
  ArrayList<Float> RGBBlue = new ArrayList<Float>();

  int intColourChange = 15;
  float fltMaxColour = 255;
  float fltMinColour = 100;

  float intOldR;
  float intOldG;
  float intOldB;
  float intNewR;
  float intNewG;
  float intNewB;

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

    RGBRed.add(random(fltMinColour, fltMaxColour - 30));
    RGBBlue.add(random(fltMinColour, fltMaxColour - 30));
    RGBGreen.add(random(fltMinColour, fltMaxColour - 30));
    RGBRed.add(RGBRed.get(0) + intColourChange);
    RGBBlue.add(RGBBlue.get(0));
    RGBGreen.add(RGBGreen.get(0));
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
        fill(RGBRed.get(i), RGBBlue.get(i), RGBGreen.get(i));
        rect (previousX.get(i), previousY.get(i), previousWidth.get(i), blockHeight);
      }
      
      fill(RGBRed.get(roundCount), RGBBlue.get(roundCount), RGBGreen.get(roundCount));
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
    if (movingX.get(roundCount - 1) >= previousX.get(roundCount - 1) && movingX.get(roundCount - 1) <= previousX.get(roundCount - 1) + previousWidth.get(roundCount - 1)){
      previousX.add(movingX.get(roundCount - 1));
      previousWidth.add(previousWidth.get(roundCount - 1) - movingX.get(roundCount - 1) + previousX.get(roundCount - 1));
      previousY.add(previousY.get(roundCount - 1) - blockHeight);
      intScore++;
    }
    // If clicked to the left of base
    else if (movingX.get(roundCount - 1) <= previousX.get(roundCount - 1) && movingX.get(roundCount - 1) + previousWidth.get(roundCount - 1) >= previousX.get(roundCount - 1)){
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
      intScore++;
    }
    // If block misses the previous block, end game
    else {
        playerAlive = false;
    }

    if (playerAlive == true){
      
      if (blockSpeed < 0){
        blockSpeed = -blockSpeed;
      }
      
      if (RGBMaxed == false) {
        RGBRed.add(RGBRed.get(roundCount - 1) + intColourChange);
        if (RGBRed.get(roundCount) >= 255){
          RGBRed.remove(roundCount);
          RGBRed.add(fltMaxColour);
          RGBBlue.add(RGBBlue.get(roundCount - 1) + intColourChange);
          RGBGreen.add(RGBGreen.get(roundCount - 1));
          if (RGBBlue.get(roundCount) >= 255){
            RGBBlue.remove(roundCount);
            RGBBlue.add(fltMaxColour);
            RGBGreen.remove(roundCount);
            RGBGreen.add(RGBGreen.get(roundCount - 1) + intColourChange);
            if (RGBGreen.get(roundCount) >= 255){
              RGBGreen.remove(roundCount);
              RGBGreen.add(fltMaxColour);
              RGBMaxed = true;
            }
          }
        }
        else {
          RGBBlue.add(RGBBlue.get(roundCount - 1));
          RGBGreen.add(RGBGreen.get(roundCount - 1));
        }
      }
      else if (RGBMaxed == false) {
        RGBRed.add(RGBRed.get(roundCount - 1) - intColourChange);
        if (RGBRed.get(roundCount) <= 100){
          RGBRed.remove(roundCount);
          RGBRed.add(fltMinColour);
          RGBBlue.add(RGBBlue.get(roundCount - 1) - intColourChange);
          if (RGBBlue.get(roundCount) <= 100){
            RGBBlue.remove(roundCount);
            RGBBlue.add(fltMinColour);
            RGBGreen.add(RGBGreen.get(roundCount - 1) - intColourChange);
            if (RGBGreen.get(roundCount) <= 100){
              RGBGreen.remove(roundCount);
              RGBBlue.remove(roundCount);
              RGBRed.remove(roundCount);
              RGBRed.add(random(100, 230));
              RGBBlue.add(random(100, 230));
              RGBGreen.add(random(100, 230));
              RGBMaxed = false;
            }
          }
        }
        else {
          RGBBlue.add(RGBBlue.get(roundCount - 1));
          RGBGreen.add(RGBGreen.get(roundCount - 1));
        }
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
        intOldR = RGBRed.get(roundCount - 1);
        intOldG = RGBGreen.get(roundCount - 1);
        intOldB = RGBBlue.get(roundCount - 1);
        intNewR = RGBRed.get(roundCount);
        intNewG = RGBGreen.get(roundCount);
        intNewB = RGBBlue.get(roundCount);

        movingX.clear();
        previousX.clear();
        previousY.clear();
        previousWidth.clear();
        RGBRed.clear();
        RGBGreen.clear();
        RGBBlue.clear();
        
        movingX.add(beginningX);
        previousX.add(intOldX);
        previousY.add(height - blockHeight);
        previousWidth.add(intOldWidth);
        RGBRed.add(intOldR);
        RGBGreen.add(intOldG);
        RGBBlue.add(intOldB);
        RGBRed.add(intNewR);
        RGBGreen.add(intNewG);
        RGBBlue.add(intNewB);
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