import processing.core.PApplet;

/**
 * Description: 
 * @author: B. Chan
 */

public class Sketch extends PApplet {
	int intSquareHeight = 30;
  int beginningWidth = 300;
  int currentRound = 1;
  float squareSpeed = 1;
  float movingX = 1;

  boolean playerAlive = true;
  boolean mouseClicked = false;

  float[] previousWidth = new float[currentRound];
  float[] previousX = new float[currentRound];
  float[] previousY = new float[currentRound];


	
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
    previousX[0] = (width - beginningWidth) / 2;
    previousY[0] = height - intSquareHeight;
    previousWidth[0] = beginningWidth;
  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
    if (playerAlive == true) {
      for (int i = 0; i <= currentRound; i++) {
        fill(255, 255, 255);
        rect (previousX[i], previousY[i], previousWidth[i], intSquareHeight);
      }
    
      rect (movingX, previousY[currentRound - 1] - intSquareHeight, previousWidth[currentRound - 1], intSquareHeight);
      movingX += squareSpeed;

      if (movingX >= (500 - previousWidth[currentRound - 1])) {
        squareSpeed = -squareSpeed;
        movingX = 500 - previousWidth[currentRound - 1];   
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
  }

  /**
   * Sets mouseClicked boolean to false
   */
  public void mouseReleased() {
    mouseClicked = false;
  }
}