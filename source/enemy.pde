class enemy{
  
    int rowPosE, colPosE, random, random2;
    int board[][];
    boolean dead = false;
    int squareSize = 80;
    PImage boardImages, tileImage;
    
    enemy(PImage sprite, int gameBoard[][]){
        board = gameBoard;
        boardImages = sprite;
    }//end constructor
    
    
    void enemyInitial(){
            boolean placed = false;
            while(!placed){
                random = int(random(1,8)); //random row
                random2 = int(random(1,14));// random col
                
                if((board[random][random2] == 7 || board[random][random2] == 21 ||
                    board[random][random2] == 22) && board[random][random2-1] != 6){
                   rowPosE = random;
                   colPosE = random2;
                   placed = true;
                } 
            }
            
            tileImage = boardImages.get(80,160,squareSize,squareSize);
            image(tileImage, random2*squareSize, random*squareSize);
       }//end enemy placement
       
       
    void enemyAnimate(int state){
         if(state <= 35){
            tileImage = boardImages.get(80,160,squareSize,squareSize);
            image(tileImage, colPosE*squareSize, rowPosE*squareSize);
         }
         else{
            tileImage = boardImages.get(160,160,squareSize,squareSize);
            image(tileImage, colPosE*squareSize, rowPosE*squareSize);
         }
    }
    
    void enemyMovement(){
        boolean moved = false;
        
        
        while(!moved){
             random = int(random(1,5));
             //check if can move left (floor, horizontal bridge only, no starting point)
             if(random == 1 && (board[rowPosE][colPosE-1] == 7 || board[rowPosE][colPosE-1] == 21) &&
                board[rowPosE][colPosE-1] != 6){
                    colPosE -= 1;
                    moved = true;    
              }
             //check if can move up
             else if(random == 2 && (board[rowPosE-1][colPosE] == 7 || board[rowPosE-1][colPosE] == 22)){
                    rowPosE -= 1;
                    moved = true;      
             }
             //check if can move right
             else if(random == 3 && (board[rowPosE][colPosE+1] == 7 || board[rowPosE][colPosE+1] == 21)){
               
                    colPosE += 1;
                    moved = true;      
             }
             //check if can move down
             else if(random == 4 && (board[rowPosE+1][colPosE] == 7 || board[rowPosE+1][colPosE] == 22)){
                    rowPosE += 1;
                    moved = true;      
             }
               
        }    
    }
    
    int getCurrentRow(){
        return rowPosE;
    }
    
    int getCurrentCol(){
        return colPosE;
    }
  
}
