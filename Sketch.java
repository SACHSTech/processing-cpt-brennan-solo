import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;

/**
 * Description: 2D Stack game, where the player can click to stack blocks with the blocks getting progressively smaller and quicker, and the player can attempt to get a high score.
 * @author: B. Chan
 */

public class Sketch extends PApplet {
  // Declare global variables
  float blockHeight = 30;
  float beginningWidth = 300;
  float beginningX = 0;
  int roundCount = 1;
  int blockSpeed = 1;
  int intScore = 0;
  int intBackground;

  float intOldX;
  float intOldY;
  float intOldWidth;

  boolean playerAlive = true;
  boolean RGBMaxed = false;
  boolean gameStarted = false;
  boolean modeSelection = false;
  boolean easyMode = false;
  boolean hardMode = false;

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
    if (gameStarted == false) {
      image (imgStart, 0, 0);

      textSize(60);
      fill(255, 255, 255);
      text("2D STACK!", 95, 140); 

      textSize(20);
      text("Created by: Brennan Chan", 125, 370); 

      // Display mode selection panel after player presses start
      if (modeSelection == true) {
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
    if (gameStarted == true) {
      if (playerAlive == true) {
        image (Backgrounds.get(intBackground), 0, 0);

        stroke(0);
        textSize(25);
        fill(255, 0, 0);
        text("Score: " + intScore, 15, 30); 

        drawPrevious();

        // Blocks move according to the current round's blockSpeed
        movingX.set(roundCount - 1, movingX.get(roundCount - 1) + blockSpeed);
        
        // Collision detection for the left and right edge of the screen for the moving blocks
        if (movingX.get(roundCount - 1) >= (width - previousWidth.get(roundCount - 1))) {
          blockSpeed = -blockSpeed;  
        } 
        else if (movingX.get(roundCount - 1) <= 0) {
          blockSpeed = -blockSpeed;
        }
      }

      // When the game ends after the player misses the previous block
      else if (playerAlive == false) { 
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
   * When Mouse is pressed, the user can press start on the title screen and select the mode difficulty. When playing, the player can click to stack blocks, and click to return back to the title screen or click to play again after they lose. The player's clicks indicate a new round, which results in the block colour changing, and possibly the blockSpeed increasing, or the game moving back down to the bottom with the background changing. 
   */
  public void mousePressed() {    
    if (gameStarted == true) {
      // If the Mouse is clicked when the current block is to the right of the previous
      if (movingX.get(roundCount - 1) >= previousX.get(roundCount - 1) && movingX.get(roundCount - 1) <= previousX.get(roundCount - 1) + previousWidth.get(roundCount - 1)){
        previousX.add(movingX.get(roundCount - 1));
        previousWidth.add(previousWidth.get(roundCount - 1) - movingX.get(roundCount - 1) + previousX.get(roundCount - 1));
        previousY.add(previousY.get(roundCount - 1) - blockHeight);
        intScore = increaseScore(intScore);
      }
      // If the Mouse is clicked when the current block is to the left of the previous
      else if (movingX.get(roundCount - 1) <= previousX.get(roundCount - 1) && movingX.get(roundCount - 1) + previousWidth.get(roundCount - 1) >= previousX.get(roundCount - 1)){
        previousX.add(previousX.get(roundCount - 1));
        previousWidth.add(previousWidth.get(roundCount - 1) + movingX.get(roundCount - 1) - previousX.get(roundCount - 1));
        previousY.add(previousY.get(roundCount - 1) - blockHeight);
        intScore = increaseScore(intScore);
      }
      // If the current block is clicked directly on the previous
      else if (movingX.get(roundCount - 1) == previousX.get(roundCount - 1)){
        previousX.add(movingX.get(roundCount - 1));
        previousWidth.add(previousWidth.get(roundCount - 1));
        previousY.add(previousY.get(roundCount - 1) - blockHeight);
        intScore = increaseScore(intScore);
      }
      // If the block is clicked while not above the previous block at all, end game
      else {
          playerAlive = false;
      }

      // If the game is still continuing after the player successfully stacked the block
      if (playerAlive == true){
        
        // Reset blockspeed to positive if it was heading from right to left
        if (blockSpeed < 0){
          blockSpeed = -blockSpeed;
        }
        
        if (RGBMaxed == false) {
          // Increase Red value by 10 each round
          RGBRed.add(RGBRed.get(roundCount - 1) + intColourChange);
          // When Red value reaches maximum 255 value, keep it at 255 and begin adding 10 to the Blue value
          if (RGBRed.get(roundCount) >= 255){
            RGBRed.remove(roundCount);
            RGBRed.add(fltMaxColour);
            RGBBlue.add(RGBBlue.get(roundCount - 1) + intColourChange);
            RGBGreen.add(RGBGreen.get(roundCount - 1));
            // When Blue value reaches maximum 255 value, keep it at 255 and begin adding 10 to the Green value
            if (RGBBlue.get(roundCount) >= 255){
              RGBBlue.remove(roundCount);
              RGBBlue.add(fltMaxColour);
              RGBGreen.remove(roundCount);
              RGBGreen.add(RGBGreen.get(roundCount - 1) + intColourChange);
              // When Green value reaches maximum 255 value, set to 255 and set RBGMaxed to true
              if (RGBGreen.get(roundCount) >= 255){
                RGBGreen.remove(roundCount);
                RGBGreen.add(fltMaxColour);
                RGBMaxed = true;
              }
            }
          }
          // If Red value has not reached 255 yet, keep Blue and Green the same
          else {
            RGBBlue.add(RGBBlue.get(roundCount - 1));
            RGBGreen.add(RGBGreen.get(roundCount - 1));
          }
        }
        // When Red, Green, and Blue colour values all reach the maximum 255
        else if (RGBMaxed == true) {
          // Subtract from the Red colour value
          RGBRed.add(RGBRed.get(roundCount - 1) - intColourChange);
          // When the Red colour value is reduced down to 100, keep Red at 100 and begin subtracting from Blue value
          if (RGBRed.get(roundCount) <= 100){
            RGBRed.remove(roundCount);
            RGBRed.add(fltMinColour);
            RGBBlue.add(RGBBlue.get(roundCount - 1) - intColourChange);
            RGBGreen.add(RGBGreen.get(roundCount - 1));
            // When the Blue colour value is reduced down to 100, keep Blue at 100 and begin subtracting from Green value
            if (RGBBlue.get(roundCount) <= 100){
              RGBBlue.remove(roundCount);
              RGBBlue.add(fltMinColour);
              RGBGreen.remove(roundCount);
              RGBGreen.add(RGBGreen.get(roundCount - 1) - intColourChange);
              // When the Green colour value is reduced down to 100, restart the cycle by generating new values for Red, Blue, and Green and start adding to them again
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
          // If Red value has not been reduced to 100 yet, keep Blue and Green the same
          else {
            RGBBlue.add(RGBBlue.get(roundCount - 1));
            RGBGreen.add(RGBGreen.get(roundCount - 1));
          }
        } 

        roundCount++;
        // If Easy mode is selected, increase blockSpeed every 4 rounds
        if (easyMode == true) {
          if (roundCount % 4 == 1 && roundCount > 1){
            blockSpeed++;
          }
          
          alternateStart();
        }
        // If Hard mode is selected, increase blockSpeed every round
        else if (hardMode == true) {
          if (roundCount > 1){
            blockSpeed++;
          }
        
          alternateStart();
        }
        // If Normal mode is selected, increase blockSpeed every 2 rounds
        else {
          if (roundCount % 2 == 1 && roundCount > 1){
            blockSpeed++;
          }
          
          alternateStart();
        }

        // Move the game back down to the bottom of the screen every 15 rounds, reset ArrayLists with the most recent numbers as first, and continue game from bottom
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

          clearArrayLists();
          
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

          // Change to random background after the game moves down
          intBackground = (int) random(0,5);
        }
      }

      // When the player ends the game, and the GAME OVER screen appears
      if (playerAlive == false) {
        // If the player clicks the Play Again button, reset game with previous mode still selected
        if (mouseX >= 260 && mouseX <= 260 + 120 && mouseY >= 280 && mouseY <= 280 + 50){
          roundCount = 1;
          blockSpeed = 1;
          intScore = 0;

          clearArrayLists();
        
          beginningVariables();

          playerAlive = true;
          intBackground = (int) random(0,5);
        }
        // If the player clicks the Title Screen button, reset the game + mode variables and return back to title screen
        else if (mouseX >= 120 && mouseX <= 120 + 120 && mouseY >= 280 && mouseY <= 280 + 50) {
          roundCount = 1;
          blockSpeed = 1;
          intScore = 0;

          clearArrayLists();
        
          beginningVariables();

          playerAlive = true;
          intBackground = (int) random(0,5);
          gameStarted = false;
          easyMode = false;
          hardMode = false;
        }
      }
    } 
    
    // Display the mode selection panel if the player presses the start button of title screen, with the ability to close it by clicking on start button again
    if (gameStarted == false) {
      if (mouseX >= 90 && mouseX <= 90 + 315 && mouseY >= 205 && mouseY <= 205 + 70){
        if (modeSelection == false) {
          modeSelection = true;
        }
        else {
          modeSelection = false;
        }
      }
    }
    
    // After the mode selection panel appears, sets the mode variables according to the hitbox the player clicks on
    if (modeSelection == true) {
      // If the player selects Easy mode
      if (mouseX >= 120 && mouseX <= 120 + 70 && mouseY >= 390 && mouseY <= 390 + 70){
        easyMode = true;
        gameStarted = true;
        modeSelection = false;
      }
      // If the player selects Normal mode
      if (mouseX >= 215 && mouseX <= 215 + 70 && mouseY >= 390 && mouseY <= 390 + 70){
        gameStarted = true;
        modeSelection = false;
      }
      // If the player selects Hard mode
      if (mouseX >= 310 && mouseX <= 310 + 70 && mouseY >= 390 && mouseY <= 390 + 70){
        hardMode = true;
        gameStarted = true;
        modeSelection = false;
      }
    }
  }

  /**
   * Draw previous squares from previous rounds to display the continuous stacking of blocks and decreasing size of them
   */
  public void drawPrevious(){
    for (int i = 0; i < roundCount; i++) {
      fill(RGBRed.get(i), RGBBlue.get(i), RGBGreen.get(i));
      rect (previousX.get(i), previousY.get(i), previousWidth.get(i), blockHeight);
    }

    fill(RGBRed.get(roundCount), RGBBlue.get(roundCount), RGBGreen.get(roundCount));
    rect (movingX.get(roundCount - 1), previousY.get(roundCount - 1) - blockHeight, previousWidth.get(roundCount - 1), blockHeight);
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
    previousX.add((width - beginningWidth) / 2);
    previousY.add(height - blockHeight);
    previousWidth.add(beginningWidth);
    movingX.add(beginningX);

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
    if (roundCount % 2 == 1 && roundCount > 1){
      movingX.add(width - previousWidth.get(roundCount - 1) - 1);
    }
    else if (roundCount > 1) {
      movingX.add(beginningX);
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
