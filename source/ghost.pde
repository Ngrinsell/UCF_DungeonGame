class ghost{
  
    int rowPosG, colPosG, random, random2;
    int board[][];
    int screenState;
    int squareSize = 80;
    int life = 3;
    PImage boardImages, tileImage;
    boolean gemCollected = false;
    
    ghost(PImage sprite, int gameBoard[][]){
        board = gameBoard;
        boardImages = sprite;
    }//end constructor
    
    void ghostInitial(){
        for(int i = 1; i < 8; i++){
            if(board[i][0] == 6){
                rowPosG = i;
                colPosG = 0; 
            }
        }
        tileImage = boardImages.get(0,240,squareSize,squareSize);
        image(tileImage, colPosG*squareSize, rowPosG*squareSize);
          
    }
    
    void ghostAnimate(int state){
         if(state <= 20){
            tileImage = boardImages.get(0,240,squareSize,squareSize);
            image(tileImage, colPosG*squareSize, rowPosG*squareSize);
         }
         else  if(state <= 40){
            tileImage = boardImages.get(80,240,squareSize,squareSize);
            image(tileImage, colPosG*squareSize, rowPosG*squareSize);
         }
         else if(state <= 60){
           tileImage = boardImages.get(160,240,squareSize,squareSize);
           image(tileImage, colPosG*squareSize, rowPosG*squareSize);
         }
        else{
           tileImage = boardImages.get(80,240,squareSize,squareSize);
           image(tileImage, colPosG*squareSize, rowPosG*squareSize);
         } 
    }
    
    void ghostMovement(int direction){
          if(direction == 1 && board[rowPosG][colPosG-1] != 0)
              colPosG -= 1;
          else if(direction == 2 && board[rowPosG-1][colPosG] != 0) //up
              rowPosG -= 1;
          else if(direction == 3 && board[rowPosG][colPosG+1] != 0) //right
              colPosG += 1;
          else if(direction == 4 && board[rowPosG+1][colPosG] != 0) //down
              rowPosG += 1;
    }
    
    int checkPosition(enemy skull1, enemy skull2, int state){
         int screenState = state;
         //println("ghost: " + rowPosG + " " + colPosG);
         //println("enemy1: " + skull1.getCurrentRow() + " " +
         if((rowPosG == skull1.getCurrentRow() && colPosG == skull1.getCurrentCol()) ||
            (rowPosG == skull2.getCurrentRow() && colPosG == skull2.getCurrentCol())){ //check if skull is hit
            life--;
            
            if(life == 2){
                board[0][13] = 3;  
            }
            else if(life == 1){
               board[0][13] = 4;
            }
            else if(life == 0)
                screenState = 4;   
        }
        else if(board[rowPosG][colPosG] == 8 || board[rowPosG][colPosG] == 12 || board[rowPosG][colPosG] == 13 ||
                board[rowPosG][colPosG] == 14 || board[rowPosG][colPosG] == 15 || board[rowPosG][colPosG] == 16 ||
                board[rowPosG][colPosG] == 17 || board[rowPosG][colPosG] == 18 || board[rowPosG][colPosG] == 19 ||
                board[rowPosG][colPosG] == 20)//check if hit pit or water
                    screenState = 4;
        else if(board[rowPosG][colPosG] == 23 || board[rowPosG][colPosG] == 24 || board[rowPosG][colPosG] == 25){
            //get gem off floor
            board[rowPosG][colPosG] = 7;
            gemCollected = true;
            //add to inventory
            board[0][11] = 5;
        }
        else if(board[rowPosG][colPosG] == 6 && board[0][11] == 5)
            screenState = 5;
    
            
        return screenState;
  
    }
    
    boolean checkGem(){
         return gemCollected; 
    }
}
