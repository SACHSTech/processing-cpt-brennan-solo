[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-c66648af7eb3fe8bc4f294546bfd86ef473780cde1dea487d3c4ff354943c9ae.svg)](https://classroom.github.com/online_ide?assignment_repo_id=7940250&assignment_repo_type=AssignmentRepo)
# 2D STACK! by Brennan Chan

## Objective of the Game
The objective of 2D STACK is to stack the blocks (rectangles) as high as you can, trying to get the highest score. It is a game based on timing and reaction time, as the game increases in difficulty over time since the blocks will speed up and the platform will gradually get smaller.

## Gameplay Mechanics / User interaction
The user is presented with the title screen, where the user presses the mouse within the start button, and the MODE SELECTION menu will appear. Then the user can select the mode they desire by clicking their mouse with the cursor within the desired hitbox. Then, the game will begin, and the user clicks their mouse (anywhere) to try and get the block as close as possible to align perfectly with the previous block below. The player loses and the game ends once the player completely misses the previous block when the mouse is clicked. The GAME OVER screen will appear, with a button that the user can click on with their mouse to return to the title screen and select a new mode, or click play again, restarting the game with the current mode selected.

## Scoring System
The player increases their score by successfully stacking the block on the previous block without missing, increasing their score by one and increasing the height of their stack. When the player misses the stack, the game ends without increasing the score for that round. Essentially, the height of their successful stack is directly correlated to their score. After every game after it ends, the player’s score for that round is presented on the GAME OVER screen. 

## Limitations
My program does not store the previous high scores of the player if they decide to continue trying to beat their score. It will only output the score of the player after every round, and if the player decides to make it a competition to try and beat their previous score, they have to keep a personal note of it since my program does not do it for them.


