import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;

/**
 * Description: 2D Stack game, where the player can click to stack blocks with the blocks getting progressively smaller and quicker, and the player can attempt to get a high score.
 * @author: B. Chan
 */

public class Sketch extends PApplet {
  // Declare global variables
  float fltBlockHeight = 30;
  float fltBeginningWidth = 300;
  float fltBeginningX = 0;
  int intRoundCount = 1;
  int intBlockSpeed = 1;
  int intScore = 0;
  int intBackground;

  float fltOldX;
  float fltOldY;
  float fltOldWidth;

  boolean blnPlayerAlive = true;
  boolean blnRGBMaxed = false;
  boolean blnGameStarted = false;
  boolean blnModeSelection = false;
  boolean blnEasyMode = false;
  boolean blnHardMode = false;

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

  float fltOldR;
  float fltOldG;
  float fltOldB;
  float fltNewR;
  float fltNewG;
  float fltNewB;

  PImage imgStart;
  PImage imgBackground1;
  PImage imgBackground2;
  PImage imgBackground3;
  PImage imgBackground4;
  PImage imgBackground5;
  ArrayList<PImage> Backgrounds = new ArrayList<PImage>();

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
    // Load backgrounds into variables, and the variables into an ArrayList
    imgStart = loadImage("start.png");
    imgStart.resize(width, height);
    imgBackground1 = loadImage("background1.png");
    imgBackground1.resize(width, height);
    imgBackground2 = loadImage("background2.png");
    imgBackground2.resize(width, height);
    imgBackground3 = loadImage("background3.png");
    imgBackground3.resize(width, height);
    imgBackground4 = loadImage("background4.png");
    imgBackground4.resize(width, height);
    imgBackground5 = loadImage("background5.png");
    imgBackground5.resize(width, height);

    Backgrounds.add(imgBackground1);
    Backgrounds.add(imgBackground2);
    Backgrounds.add(imgBackground3);
    Backgrounds.add(imgBackground4);
    Backgrounds.add(imgBackground5);
    
    // Generate random background
    intBackground = (int) random(0,5);

    beginningVariables();

  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
    // Display title screen when the player has not started the game or stopped playing
    if (blnGameStarted == false) {
      image (imgStart, 0, 0);

      textSize(60);
      fill(255, 255, 255);
      text("2D STACK!", 95, 140); 

      textSize(20);
      text("Created by: Brennan Chan", 125, 370); 

      // Display mode selection panel after player presses start
      if (blnModeSelection == true) {
        stroke(255, 255, 255);
        fill (0);
        rect ((width - 300) / 2, 300, 300, 190);

        textSize(30);
        fill(255, 255, 255);
        text("SELECT MODE:", 140, 350); 

        fill (0, 255, 0);
        rect (120, 390, 70, 70);

        fill (230, 224, 71);
        rect (215, 390, 70, 70);

        fill (255, 0, 0);
        rect (310, 390, 70, 70);

        textSize(15);
        fill(0);
        text("Easy", 139, 430); 
        text("Normal", 225, 430); 
        text("Hard", 329, 430); 
      }
    }

    // Start game after player selects mode
    if (blnGameStarted == true) {
      if (blnPlayerAlive == true) {
        image (Backgrounds.get(intBackground), 0, 0);

        stroke(0);
        textSize(25);
        fill(255, 0, 0);
        text("Score: " + intScore, 15, 30); 

        drawPrevious();

        // Blocks move according to the current round's intBlockSpeed
        movingX.set(intRoundCount - 1, movingX.get(intRoundCount - 1) + intBlockSpeed);
        
        // Collision detection for the left and right edge of the screen for the moving blocks
        if (movingX.get(intRoundCount - 1) >= (width - previousWidth.get(intRoundCount - 1))) {
          intBlockSpeed = -intBlockSpeed;  
        } 
        else if (movingX.get(intRoundCount - 1) <= 0) {
          intBlockSpeed = -intBlockSpeed;
        }
      }

      // When the game ends after the player misses the previous block
      else if (blnPlayerAlive == false) { 
        image (Backgrounds.get(intBackground), 0, 0);

        drawPrevious();

        // Display GAME OVER screen, with option to return to title screen or play again
        stroke(255, 255, 255);
        fill (0);
        rect ((width - 300) / 2, (height - 200) / 2, 300, 200);

        textSize(30);
        fill(255, 255, 255);
        text("GAME OVER!", 155, 200); 

        fill (0);
        rect (260, 280, 120, 50);
        rect (120, 280, 120, 50);

        textSize(20);
        fill (255, 255, 255);
        text("Play Again", 275, 310); 
        text("Title Screen", 127, 310); 

        // Move Score display depending on if the score is double digits or single digits to center it properly
        if (intScore < 10) {
          textSize(35);
          text("Score: " + intScore, 180, 250); 
        }
        else if (intScore >= 10){
          textSize(35);
          text("Score: " + intScore, 172, 250); 
        }

        stroke(0);
      }
    }
  }
  
  // define other methods down here.
  
  /**
   * When Mouse is pressed, the user can press start on the title screen and select the mode difficulty. When playing, the player can click to stack blocks, and click to return back to the title screen or click to play again after they lose. The player's clicks indicate a new round, which results in the block colour changing, and possibly the intBlockSpeed increasing, or the game moving back down to the bottom with the background changing. 
   */
  public void mousePressed() {    
    if (blnGameStarted == true) {
      // If the Mouse is clicked when the current block is to the right of the previous
      if (movingX.get(intRoundCount - 1) >= previousX.get(intRoundCount - 1) && movingX.get(intRoundCount - 1) <= previousX.get(intRoundCount - 1) + previousWidth.get(intRoundCount - 1)){
        previousX.add(movingX.get(intRoundCount - 1));
        previousWidth.add(previousWidth.get(intRoundCount - 1) - movingX.get(intRoundCount - 1) + previousX.get(intRoundCount - 1));
        previousY.add(previousY.get(intRoundCount - 1) - fltBlockHeight);
        intScore = increaseScore(intScore);
      }
      // If the Mouse is clicked when the current block is to the left of the previous
      else if (movingX.get(intRoundCount - 1) <= previousX.get(intRoundCount - 1) && movingX.get(intRoundCount - 1) + previousWidth.get(intRoundCount - 1) >= previousX.get(intRoundCount - 1)){
        previousX.add(previousX.get(intRoundCount - 1));
        previousWidth.add(previousWidth.get(intRoundCount - 1) + movingX.get(intRoundCount - 1) - previousX.get(intRoundCount - 1));
        previousY.add(previousY.get(intRoundCount - 1) - fltBlockHeight);
        intScore = increaseScore(intScore);
      }
      // If the current block is clicked directly on the previous
      else if (movingX.get(intRoundCount - 1) == previousX.get(intRoundCount - 1)){
        previousX.add(movingX.get(intRoundCount - 1));
        previousWidth.add(previousWidth.get(intRoundCount - 1));
        previousY.add(previousY.get(intRoundCount - 1) - fltBlockHeight);
        intScore = increaseScore(intScore);
      }
      // If the block is clicked while not above the previous block at all, end game
      else {
          blnPlayerAlive = false;
      }

      // If the game is still continuing after the player successfully stacked the block
      if (blnPlayerAlive == true){
        
        // Reset intBlockSpeed to positive if it was heading from right to left
        if (intBlockSpeed < 0){
          intBlockSpeed = -intBlockSpeed;
        }
        
        if (blnRGBMaxed == false) {
          // Increase Red value by 10 each round
          RGBRed.add(RGBRed.get(intRoundCount - 1) + intColourChange);
          // When Red value reaches maximum 255 value, keep it at 255 and begin adding 10 to the Blue value
          if (RGBRed.get(intRoundCount) >= 255){
            RGBRed.remove(intRoundCount);
            RGBRed.add(fltMaxColour);
            RGBBlue.add(RGBBlue.get(intRoundCount - 1) + intColourChange);
            RGBGreen.add(RGBGreen.get(intRoundCount - 1));
            // When Blue value reaches maximum 255 value, keep it at 255 and begin adding 10 to the Green value
            if (RGBBlue.get(intRoundCount) >= 255){
              RGBBlue.remove(intRoundCount);
              RGBBlue.add(fltMaxColour);
              RGBGreen.remove(intRoundCount);
              RGBGreen.add(RGBGreen.get(intRoundCount - 1) + intColourChange);
              // When Green value reaches maximum 255 value, set to 255 and set RGBMaxed to true
              if (RGBGreen.get(intRoundCount) >= 255){
                RGBGreen.remove(intRoundCount);
                RGBGreen.add(fltMaxColour);
                blnRGBMaxed = true;
              }
            }
          }
          // If Red value has not reached 255 yet, keep Blue and Green the same
          else {
            RGBBlue.add(RGBBlue.get(intRoundCount - 1));
            RGBGreen.add(RGBGreen.get(intRoundCount - 1));
          }
        }
        // When Red, Green, and Blue colour values all reach the maximum 255
        else if (blnRGBMaxed == true) {
          // Subtract from the Red colour value
          RGBRed.add(RGBRed.get(intRoundCount - 1) - intColourChange);
          // When the Red colour value is reduced down to 100, keep Red at 100 and begin subtracting from Blue value
          if (RGBRed.get(intRoundCount) <= 100){
            RGBRed.remove(intRoundCount);
            RGBRed.add(fltMinColour);
            RGBBlue.add(RGBBlue.get(intRoundCount - 1) - intColourChange);
            RGBGreen.add(RGBGreen.get(intRoundCount - 1));
            // When the Blue colour value is reduced down to 100, keep Blue at 100 and begin subtracting from Green value
            if (RGBBlue.get(intRoundCount) <= 100){
              RGBBlue.remove(intRoundCount);
              RGBBlue.add(fltMinColour);
              RGBGreen.remove(intRoundCount);
              RGBGreen.add(RGBGreen.get(intRoundCount - 1) - intColourChange);
              // When the Green colour value is reduced down to 100, restart the cycle by generating new values for Red, Blue, and Green and start adding to them again
              if (RGBGreen.get(intRoundCount) <= 100){
                RGBGreen.remove(intRoundCount);
                RGBBlue.remove(intRoundCount);
                RGBRed.remove(intRoundCount);
                RGBRed.add(random(100, 230));
                RGBBlue.add(random(100, 230));
                RGBGreen.add(random(100, 230));
                blnRGBMaxed = false;
              }
            }
          }
          // If Red value has not been reduced to 100 yet, keep Blue and Green the same
          else {
            RGBBlue.add(RGBBlue.get(intRoundCount - 1));
            RGBGreen.add(RGBGreen.get(intRoundCount - 1));
          }
        } 

        intRoundCount++;
        // If Easy mode is selected, increase intBlockSpeed every 4 rounds
        if (blnEasyMode == true) {
          if (intRoundCount % 4 == 1 && intRoundCount > 1){
            intBlockSpeed++;
          }
          
          alternateStart();
        }
        // If Hard mode is selected, increase intBlockSpeed every round
        else if (blnHardMode == true) {
          if (intRoundCount > 1){
            intBlockSpeed++;
          }
        
          alternateStart();
        }
        // If Normal mode is selected, increase intBlockSpeed every 2 rounds
        else {
          if (intRoundCount % 2 == 1 && intRoundCount > 1){
            intBlockSpeed++;
          }
          
          alternateStart();
        }

        // Move the game back down to the bottom of the screen every 15 rounds, reset ArrayLists with the most recent numbers as first, and continue game from bottom
        if ((intRoundCount - 1) % 15 == 0 && intRoundCount > 1){
          fltOldX = previousX.get(intRoundCount - 1);
          fltOldY = previousY.get(intRoundCount - 1);
          fltOldWidth = previousWidth.get(intRoundCount - 1);
          fltOldR = RGBRed.get(intRoundCount - 1);
          fltOldG = RGBGreen.get(intRoundCount - 1);
          fltOldB = RGBBlue.get(intRoundCount - 1);
          fltNewR = RGBRed.get(intRoundCount);
          fltNewG = RGBGreen.get(intRoundCount);
          fltNewB = RGBBlue.get(intRoundCount);

          clearArrayLists();
          
          movingX.add(fltBeginningX);
          previousX.add(fltOldX);
          previousY.add(height - fltBlockHeight);
          previousWidth.add(fltOldWidth);
          RGBRed.add(fltOldR);
          RGBGreen.add(fltOldG);
          RGBBlue.add(fltOldB);
          RGBRed.add(fltNewR);
          RGBGreen.add(fltNewG);
          RGBBlue.add(fltNewB);
          intRoundCount = 1;

          // Change to random background after the game moves down
          intBackground = (int) random(0,5);
        }
      }

      // When the player ends the game, and the GAME OVER screen appears
      if (blnPlayerAlive == false) {
        // If the player clicks the Play Again button, reset game with previous mode still selected
        if (mouseX >= 260 && mouseX <= 260 + 120 && mouseY >= 280 && mouseY <= 280 + 50){
          intRoundCount = 1;
          intBlockSpeed = 1;
          intScore = 0;

          clearArrayLists();
        
          beginningVariables();

          blnPlayerAlive = true;
          intBackground = (int) random(0,5);
        }
        // If the player clicks the Title Screen button, reset the game + mode variables and return back to title screen
        else if (mouseX >= 120 && mouseX <= 120 + 120 && mouseY >= 280 && mouseY <= 280 + 50) {
          intRoundCount = 1;
          intBlockSpeed = 1;
          intScore = 0;

          clearArrayLists();
        
          beginningVariables();

          blnPlayerAlive = true;
          intBackground = (int) random(0,5);
          blnGameStarted = false;
          blnEasyMode = false;
          blnHardMode = false;
        }
      }
    } 
    
    // Display the mode selection panel if the player presses the start button of title screen, with the ability to close it by clicking on start button again
    if (blnGameStarted == false) {
      if (mouseX >= 90 && mouseX <= 90 + 315 && mouseY >= 205 && mouseY <= 205 + 70){
        if (blnModeSelection == false) {
          blnModeSelection = true;
        }
        else {
          blnModeSelection = false;
        }
      }
    }
    
    // After the mode selection panel appears, sets the mode variables according to the hitbox the player clicks on
    if (blnModeSelection == true) {
      // If the player selects Easy mode
      if (mouseX >= 120 && mouseX <= 120 + 70 && mouseY >= 390 && mouseY <= 390 + 70){
        blnEasyMode = true;
        blnGameStarted = true;
        blnModeSelection = false;
      }
      // If the player selects Normal mode
      if (mouseX >= 215 && mouseX <= 215 + 70 && mouseY >= 390 && mouseY <= 390 + 70){
        blnGameStarted = true;
        blnModeSelection = false;
      }
      // If the player selects Hard mode
      if (mouseX >= 310 && mouseX <= 310 + 70 && mouseY >= 390 && mouseY <= 390 + 70){
        blnHardMode = true;
        blnGameStarted = true;
        blnModeSelection = false;
      }
    }
  }

  /**
   * Draw previous squares from previous rounds to display the continuous stacking of blocks and decreasing size of them
   */
  public void drawPrevious(){
    for (int i = 0; i < intRoundCount; i++) {
      fill(RGBRed.get(i), RGBBlue.get(i), RGBGreen.get(i));
      rect (previousX.get(i), previousY.get(i), previousWidth.get(i), fltBlockHeight);
    }

    fill(RGBRed.get(intRoundCount), RGBBlue.get(intRoundCount), RGBGreen.get(intRoundCount));
    rect (movingX.get(intRoundCount - 1), previousY.get(intRoundCount - 1) - fltBlockHeight, previousWidth.get(intRoundCount - 1), fltBlockHeight);
  }

  /**
   * Clear all ArrayLists containing the previous game's information to reset the game, or to move the game back down to the bottom while playing
   */
  public void clearArrayLists(){
    previousX.clear();
    previousY.clear();
    previousWidth.clear();
    movingX.clear();
    RGBRed.clear();
    RGBGreen.clear();
    RGBBlue.clear();
  }

  /**
   * The beginning variables that are set at the beginning of a new game which reset the game when declared again
   */
  public void beginningVariables(){
    previousX.add((width - fltBeginningWidth) / 2);
    previousY.add(height - fltBlockHeight);
    previousWidth.add(fltBeginningWidth);
    movingX.add(fltBeginningX);

    RGBRed.add(random(fltMinColour, fltMaxColour - (intColourChange * 2)));
    RGBBlue.add(random(fltMinColour, fltMaxColour - (intColourChange * 2)));
    RGBGreen.add(random(fltMinColour, fltMaxColour - (intColourChange * 2)));
    RGBRed.add(RGBRed.get(0) + intColourChange);
    RGBBlue.add(RGBBlue.get(0));
    RGBGreen.add(RGBGreen.get(0));
  }

  /**
   * Have the moving blocks alternate from starting from the left and starting from the right each time when they appear
   */
  public void alternateStart(){
    if (intRoundCount % 2 == 1 && intRoundCount > 1){
      movingX.add(width - previousWidth.get(intRoundCount - 1) - 1);
    }
    else if (intRoundCount > 1) {
      movingX.add(fltBeginningX);
    }
  }

  /**
    * Increases the score by 1
    *
    * @param n  The orginal score before the increase
    * @return The score increased by 1
    * 
    */
  private int increaseScore(int n){
    return n + 1;
  }
}
