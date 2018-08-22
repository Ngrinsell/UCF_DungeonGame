import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class main extends PApplet {

//Lost Life : Nicole Grinsell
//Move with arrow keys
//Collect red life gem and go back to start
//Monsters decrease life by 1
//Pits and water cause immediate death

PImage sprite, start, death, win;
gameBoard board;
ghost hero;
enemy skull1;
enemy skull2;
boolean gemCollection;
int state = 1; //1(start) 2(build board) 3(play) 4(death) 5(win)
int [][] cave = new int [9][16];
int ghostState;
int enemyState;
int waterState;
int gemState;
int direction;

public void setup(){
 size(1280,720);
 sprite = loadImage("data/spriteSheet.png");
 start = loadImage("data/start.png");
 death = loadImage("data/death.png");
 win = loadImage("data/win.png");
}

public void draw(){
  if(state == 1){ //start screen
      
     background(start);
    
  }
  else if(state == 2){ //inital board creation
      for(int i = 0; i < 9; i++){ //reset board
        for(int j = 0; j < 16; j++){
           cave[i][j] = 0;   
        }
      }
      
      gemCollection = false;
      ghostState = 1;
      enemyState = 1;
      waterState = 1;
      gemState = 1;
      direction = 0;
      
      //board creation
      board = new gameBoard(sprite, cave);
      board.createBoard();
 
      hero = new ghost(sprite, cave);
      skull1 = new enemy(sprite, cave);
      skull2 = new enemy(sprite, cave);
 
      hero.ghostInitial();
      skull1.enemyInitial();
      skull2.enemyInitial();
    
      state = 3;  
  }
  else if(state == 3){ //game play
  
      board.showGraphics(cave);
      hero.ghostAnimate(ghostState);
      skull1.enemyAnimate(enemyState);
      skull2.enemyAnimate(enemyState);
      board.waterAnimate(waterState);
      gemCollection = hero.checkGem();
      if(!gemCollection)
          board.gemAnimate(gemState);
  
  
  
      if(ghostState == 80)
          ghostState = 1;
      
      if(enemyState == 70)
          enemyState = 1;
   
      if(waterState == 30)
          waterState = 1;
          
       if(gemState == 90)
           gemState = 1;
      
          ghostState++;
          enemyState++;
          waterState++;
       if(!gemCollection)   
           gemState++;
  }
  else if(state == 4){
        background(death);
  }
  else if(state == 5){
        background(win);
  }
}

public void keyPressed(){ //10 enter 82 r 81 q
    println(keyCode);
    if(state == 1){//beginning screen
      if(keyCode == 10){//start
          state = 2;  
      }
    }
    else if(state == 3){  //gameplay
      if(keyCode == 37){ //left
          direction = 1;
          skull1.enemyMovement();
          skull2.enemyMovement();
      }
          
      else if(keyCode == 38){ //up
          direction = 2;
          skull1.enemyMovement();
          skull2.enemyMovement();
      }
          
      else if(keyCode == 39){ //right
          direction = 3;
          skull1.enemyMovement();
          skull2.enemyMovement();
      }

      else if(keyCode == 40){ //down
          direction = 4;
          skull1.enemyMovement();
          skull2.enemyMovement();
      }
      
      hero.ghostMovement(direction); 
      state = hero.checkPosition(skull1, skull2, state);
    }//end of state
    else if(state == 4 || state == 5){//death or victory
      if(keyCode == 82)//play again
          state = 2;
      else if(keyCode == 81) //quit
          exit();
    }  
}
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
    
    
    public void enemyInitial(){
            boolean placed = false;
            while(!placed){
                random = PApplet.parseInt(random(1,8)); //random row
                random2 = PApplet.parseInt(random(1,14));// random col
                
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
       
       
    public void enemyAnimate(int state){
         if(state <= 35){
            tileImage = boardImages.get(80,160,squareSize,squareSize);
            image(tileImage, colPosE*squareSize, rowPosE*squareSize);
         }
         else{
            tileImage = boardImages.get(160,160,squareSize,squareSize);
            image(tileImage, colPosE*squareSize, rowPosE*squareSize);
         }
    }
    
    public void enemyMovement(){
        boolean moved = false;
        
        
        while(!moved){
             random = PApplet.parseInt(random(1,5));
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
    
    public int getCurrentRow(){
        return rowPosE;
    }
    
    public int getCurrentCol(){
        return colPosE;
    }
  
}
class gameBoard{
   int cols = 16;
   int rows = 9;
   int tileElements = 36;
   int squareSize = 80;
   int [][] board;
   int random2, waterChoice, enter, heightTop, heightMid, heightBot, random, 
   bridgePlacement, bridgePlacement2, horizontalRiver, horizontalRiver2,
   verticalRiver, verticalRiver2;
   
   int gemInitialRow, gemInitialCol, enemyInitialRow, enemyInitialCol;
   PImage boardImages, tileImage;
   
   gameBoard(PImage sprite, int gameBoard[][]){
     boardImages = sprite;
     board = gameBoard;
   }
   
   public void createBoard(){
     
     int[] tiles = new int[tileElements+1];
     
     
     //0 = wall       | 1 = life      | 2 = lifeBars3   | 3 = lifeBars2  | 4 = lifeBars1  | 5 = gemStorage |
     //6 = startFloor | 7 = floor     | 8 = water1      | 9 = water2     | 10 = water3    | 11 = water4    |
     //12 = pitUpLf   | 13 = pitUpMd  | 14 = pitUpRt    | 15 = pitMidLf  | 16 = pitMidMd  | 17 = pitMidRt  |
     //18 = pitBotLf  | 19 = pitBotMd | 20 = pitBotRt   | 21 = bridgeH   | 22 = bridgeV   | 23 = gemUp     |
     //24 = gemLf     | 25 = gemDw    | 26 = skullClose | 27 = skullOpen | 28 = ghost100U | 29 = ghost100M |
     //30 = ghost100D | 31 = ghost75U | 32 = ghost75M   | 33 = ghost75D  | 34 = ghost50U  | 35 = ghost50M  |
     //36 = ghost50D  |
     
     for(int i = 0; i < tileElements; i++){
         tiles[i] = i;
     }
     
     //add start point
     setStart();
     
     //add life bar
     addLifeBar(); 
     
     //add water
     addWater();
     
     //add pit hazard (3x3, 2x3, or 2x2)
     createPit();
     
     //fill in floor
     setFloor();
     
     //place the life gem
     placeGem();

     //print graphics to screen
     showGraphics(board);   
   }//end method 
   
   public void setStart(){
       enter = PApplet.parseInt(random(1, 8));
       board[enter][0] = 6;
   }//end start function
   
   public int getStartRow(){
       return enter;  
   }
   
   public void addLifeBar(){
       board[0][12] = 1;
       board[0][13] = 2;
   }//end life function
   
   
   public void addWater(){
       waterChoice = PApplet.parseInt(random(1, 4)); //gives number 1-3
     
     switch(waterChoice){
         case 1: //two horizontal rivers
           horizontalRiver = PApplet.parseInt(random(3, 6)); // random row between 3 and 5
           while(horizontalRiver == enter){
             horizontalRiver = PApplet.parseInt(random(3, 6));
           }
           horizontalRiver2 = PApplet.parseInt(random(3, 6));
           while(horizontalRiver2 == enter){
             horizontalRiver2 = PApplet.parseInt(random(3, 6));
           }
           bridgePlacement = PApplet.parseInt(random(1, 15));
           bridgePlacement2 = PApplet.parseInt(random(1, 15));
           for(int i = 0; i < cols; i++){ //run through rows
               if(i < 15 && i > 0){
                     board[horizontalRiver][i] = 8; 
                     board[horizontalRiver2][i] = 8;    
               }
           }
               board[horizontalRiver][bridgePlacement] = 22; 
               
               if(horizontalRiver - 1 == horizontalRiver2){
                  board[horizontalRiver-1][bridgePlacement] = 22; 
               }
               else if(horizontalRiver + 1 == horizontalRiver2){
                  board[horizontalRiver+1][bridgePlacement] = 22; 
               }
               else{
                  board[horizontalRiver2][bridgePlacement2] = 22;
               }
           break;
         case 2: //two vertical rivers
           verticalRiver = PApplet.parseInt(random(3, 13)); // random row between 3 and 14
           verticalRiver2 = PApplet.parseInt(random(3, 13));
           bridgePlacement = PApplet.parseInt(random(1, 8));
           bridgePlacement2 = PApplet.parseInt(random(1, 8));
           for(int i = 0; i < rows; i++){ //run through cols
               if(i < 8 && i > 0){
                     board[i][verticalRiver] = 8; 
                     board[i][verticalRiver2] = 8;    
               }
           }
               board[bridgePlacement][verticalRiver] = 21; 
               
               if(verticalRiver - 1 == verticalRiver2){
                  board[bridgePlacement][verticalRiver-1] = 21; 
               }
               else if(verticalRiver + 1 == verticalRiver2){
                  board[bridgePlacement][verticalRiver+1] = 21; 
               }
               else{
                  board[bridgePlacement2][verticalRiver2] = 21;
               }
           break;
         case 3: //connecting veticle and horizontal rivers
           horizontalRiver = PApplet.parseInt(random(3, 6));           //horizontal
           verticalRiver = PApplet.parseInt(random(3,13));          //verticle
           bridgePlacement = PApplet.parseInt(random(verticalRiver+1, 15)); //horizontal
           bridgePlacement2 = PApplet.parseInt(random(1, 8)); //verticle
           
           //create verticle river
           for(int j = 0; j < rows; j++){ //veticle
               if(j < 8 && j > 0){
                     board[j][verticalRiver] = 8;    
               }
           }
           
           //create horizontal river jutting from verticle river
           for(int i = 0; i < cols; i++){ //horizontal
               if(i < 15 && i > verticalRiver){
                     board[horizontalRiver][i] = 8;     
               }
           }
           
           //bridge placement
           board[horizontalRiver][bridgePlacement] = 22;
           if(bridgePlacement2 == horizontalRiver){ //bridge can't be in intersection
               random = PApplet.parseInt(random(1,3));
               if(random == 1)
                   board[bridgePlacement2-1][verticalRiver] = 21;
               else if(random == 2)
                   board[bridgePlacement2+1][verticalRiver] = 21;
           }
           else 
               board[bridgePlacement2][verticalRiver] = 21;
           break;
      }//end switch 
   }//end water function
   
   
   public void createPit(){
       boolean search = true;
       int pass;
       int pitType = 0; //0 = 3x3, 1 = 3x2, 2 = 2x2, 3 = 1x1
       int searchAmt = 0;
       
       
       
       while(search){
           pass = 0;
           random = PApplet.parseInt(random(1, 15)); //random column
           random2 = PApplet.parseInt(random(1, 8)); //random row
           
           
           if(searchAmt < 10 && random <= 12 && random2 <= 5){
                     if(board[random2][random] != 8) //check sqr 1 ! water
                        pass++;
                     if(board[random2][random+1] != 8) //check sqr 2 ! water
                        pass++;
                     if(board[random2][random+2] != 8) //check sqr 3 ! water
                        pass++;
                     if(board[random2+1][random] != 8) //check sqr 4 ! water
                        pass++;
                     if(board[random2+1][random+2] != 8) //check sqr 6 ! water
                        pass++;
                     if(board[random2+2][random] != 8) //check sqr 7 ! water
                        pass++;
                     if(board[random2+2][random+1] != 8) //check sqr 8 ! water
                        pass++;
                     if(board[random2+2][random+2] != 8) //check sqr 9 ! water
                        pass++;
                        
                     if(board[random2-1][random] != 22 && board[random2-1][random] != 8) //check above sqr 1
                        pass++;
                     if(board[random2][random-1] != 21 && board[random2][random-1] != 6 && board[random2][random-1] != 8) //check left sqr 1 
                        pass++;
                     if(board[random2][random-1] != 22 && board[random2][random-1] != 8) //check above sqr 2
                        pass++;
                     if(board[random2][random-1] != 22 && board[random2][random-1] != 8) //check above sqr 3
                        pass++;
                     if(board[random2][random+3] != 21 && board[random2][random+3] != 8) //check right sqr 3
                        pass++;
                     if(board[random2+1][random-1] != 21 && board[random2+1][random-1] != 6 && board[random2+1][random-1] != 8) //check left sqr 4
                        pass++;
                     if(board[random2+1][random+3] != 21 && board[random2+1][random+3] != 8) //check right sqr 6
                        pass++;
                     if(board[random2+2][random-1] != 21 && board[random2+2][random-1] != 6 && board[random2+2][random-1] != 8) //check left sqr 7
                        pass++;
                     if(board[random2+3][random] != 22 && board[random2+3][random] != 8) //check below sqr 7
                        pass++;
                     if(board[random2+3][random+1] != 22 && board[random2+3][random+1] != 8) //check below sqr 8
                        pass++;
                     if(board[random2+3][random+2] != 22 && board[random2+3][random+2] != 8) //check below sqr 9
                        pass++;
                     if(board[random2+3][random+3] != 21 && board[random2+3][random+3] != 8) //check right sqr 9
                        pass++;
                        
                     if(pass == 20){
                         search = false;
                         pitType = 0;
                     }
           }
           else if(searchAmt < 20 && random <= 12 && random2 <= 6){//search for area for 2x3 pit
                     if(board[random2][random] != 8) //check sqr 1 ! water
                        pass++;
                     if(board[random2][random+1] != 8) //check sqr 2 ! water
                        pass++;
                     if(board[random2][random+2] != 8) //check sqr 3 ! water
                        pass++;
                     if(board[random2+1][random] != 8) //check sqr 4 ! water
                        pass++;
                     if(board[random2+1][random+2] != 8) //check sqr 5 ! water
                        pass++;
                     if(board[random2+1][random+3] != 8) //check sqr 6 ! water
                        pass++;
                     
                     if(board[random2-1][random] != 22 && board[random2-1][random] != 8) //check above sqr 1
                        pass++;
                     if(board[random2][random-1] != 21 && board[random2][random-1] != 6 && board[random2][random-1] != 8) //check left sqr 1
                        pass++;
                     if(board[random2-1][random+1] != 22 && board[random2-1][random+1] != 8) //check above sqr 2
                        pass++;
                     if(board[random2-1][random+2] != 22 && board[random2-1][random+2] != 8) //check above sqr 3
                        pass++;
                     if(board[random2][random+3] != 21 && board[random2][random+3] != 8) //check right sqr 3
                        pass++;
                     if(board[random2+1][random-1] != 21 && board[random2+1][random-1] != 6 && board[random2+1][random-1] != 8) //check left sqr 4
                        pass++;
                     if(board[random2+2][random] != 22 && board[random2+2][random] != 8) //check below sqr 4
                        pass++;
                     if(board[random2+2][random+1] != 22 && board[random2+2][random+1] != 8) //check blow sqr 5
                        pass++;
                     if(board[random2+2][random+2] != 22 && board[random2+2][random+2] != 8) //check below sqr 6
                        pass++;
                     if(board[random2+1][random+3] != 21 && board[random2+1][random+3] != 8) //check right sqr 6
                        pass++; 
                      
                      if(pass == 16){
                         search = false;
                         pitType = 1;
                     }  
           }
           else if (searchAmt < 30 && random <= 13 && random2 <= 6){ //search for area for 2x2 pit
                     if(board[random2][random] != 8) //check sqr 1 ! water
                        pass++;
                     if(board[random2][random+1] != 8) //check sqr 2 ! water
                        pass++;
                     if(board[random2+1][random] != 8) //check sqr 3 ! water
                        pass++;
                     if(board[random2+1][random+1] != 8) //check sqr 4 ! water
                        pass++;
           
           
                     if(board[random2-1][random] != 22 && board[random2-1][random] != 8) //check above sqr 1
                        pass++;
                     if(board[random2][random-1] != 21 && board[random2][random-1] != 6 && board[random2][random-1] != 8) //check left sqr 1
                        pass++;
                     if(board[random2-1][random+1] != 22 && board[random2-1][random+1] != 8) //check above sqr 2
                        pass++;
                     if(board[random2][random+2] != 21 && board[random2][random+2] != 8)  //check right sqr2
                        pass++;   
                     if(board[random2+1][random-1] != 21 && board[random2+1][random-1] != 6 && board[random2+1][random-1] != 8) //check left sqr 3
                        pass++;
                     if(board[random2+2][random] != 22 && board[random2+2][random] != 8) //check below sqr 3
                        pass++;
                     if(board[random2+2][random+1] != 22 && board[random2+2][random+1] != 8) //check below sqr 4
                        pass++;
                     if(board[random2+1][random+2] != 21 && board[random2+1][random+2] != 8) //check rigt sqr 4
                        pass++;
                      
                      if(pass == 12){
                         search = false;
                         pitType = 2;
                     }
           }
           else{ //put 1x1 pit
               
               if(board[random2][random-1] != 21 && board[random2][random-1] != 6 && board[random2][random-1] != 8) //check left sqr 1
                        pass++;
               if(board[random2][random+1] != 21 && board[random2][random+1] != 8) //check right sqr 1
                        pass++;
               if(board[random2-1][random] != 22 && board[random2-1][random] != 8) //check above sqr 1
                        pass++; 
               if(board[random2+1][random] != 22 && board[random2+1][random] != 8) //check below sqr 1
                        pass++;
                    
               if(pass == 4){
                         search = false;
                         pitType = 3;
                     }    
           }
               searchAmt++;
           }//end while
          
          int pitNumber = 12;
          
           if(pitType == 0){ //3x3
               for(int i = 0; i < 3; i++){
                   for(int j = 0; j < 3; j++){
                       board[random2][random] = pitNumber;
                       random++;
                       pitNumber ++;
                   }  
                   random2++;
                   random -= 3;
               }
           }
           else if(pitType == 1){ //2x3
               for(int i = 0; i < 2; i++){
                   for(int j = 0; j < 3; j++){
                       board[random2][random] = pitNumber;
                       random++;
                       pitNumber ++;
                   }  
                   random2++;
                   pitNumber += 3;
                   random -= 3;
               }
           }
           else if(pitType == 2){ //2x2
               for(int i = 0; i < 2; i++){
                   for(int j = 0; j < 2; j++){
                       board[random2][random] = pitNumber;
                       random++;
                       
                       if(j == 0)
                           pitNumber += 2;
                       if(j == 1)
                           pitNumber += 4;
                   }  
                   random2++;
                   random -= 2;
               }
           }
           else if(pitType == 3){ //1x1
               board[random2][random] = 16;
           } 
       }//end of pit
       
       public void placeGem(){
            boolean placed = false;
            while(!placed){
                random = PApplet.parseInt(random(1,8)); //random row
                random2 = PApplet.parseInt(random(1,14));// random col
                
                if(board[random][random2] == 7 && board[random][random2-1] != 6){
                   board[random][random2] = 23;
                   gemInitialRow = random;
                   gemInitialCol = random2;
                   placed = true;
                } 
            }
       }
       
       public int getGemRow(){
            return gemInitialRow; 
       }
       
       public int getGemCol(){
            return gemInitialCol; 
       }
       
       
       public void setFloor(){
           for(int i = 1; i < rows-1; i++){
               for(int j = 1; j < cols-1; j++){
                   if(board[i][j] == 0)
                       board[i][j] = 7;
               }
           }
           
       }
       
       public void waterAnimate(int state){
         for(int i = 0; i < rows; i++){
           for(int j = 0; j < cols; j++){
           if(board[i][j] == 8){  
               if(state <= 10){
                  tileImage = boardImages.get(0,80,squareSize,squareSize);
                  image(tileImage, j*squareSize, i*squareSize);
               }
               else  if(state <= 20){
                  tileImage = boardImages.get(80,80,squareSize,squareSize);
                  image(tileImage, j*squareSize, i*squareSize);
               }
               else  if(state <= 30){
                  tileImage = boardImages.get(160,80,squareSize,squareSize);
                  image(tileImage, j*squareSize, i*squareSize);
               }
               else{
                 tileImage = boardImages.get(240,80,squareSize,squareSize);
                 image(tileImage, j*squareSize, i*squareSize);
         }
           }
           }
         }
         
       }
       
       public void gemAnimate(int state){
           if(state <= 30){//up
           board[getGemRow()][getGemCol()] = 23;
            //tileImage = boardImages.get(240,160,squareSize,squareSize);
            //image(tileImage, colPosG*squareSize, rowPosG*squareSize);
           }
           else  if(state <= 60){//left
           board[getGemRow()][getGemCol()] = 24;
            //tileImage = boardImages.get(360,160,squareSize,squareSize);
            //image(tileImage, colPosG*squareSize, rowPosG*squareSize);
           }
           else if(state <= 90){//down
           board[getGemRow()][getGemCol()] = 25;
           //tileImage = boardImages.get(400,160,squareSize,squareSize);
           //image(tileImage, colPosG*squareSize, rowPosG*squareSize);
           }
         
       }
       
       public void showGraphics(int board[][]){
           for(int i = 0; i < rows; i++){
               for(int j = 0; j < cols; j++){
                   switch(board[i][j]){
                       case 0: //wall
                           tileImage = boardImages.get(0,0,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break; 
                       case 1: //life title
                           tileImage = boardImages.get(160,0,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                       case 2: //life bars (3)
                           tileImage = boardImages.get(400,0,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break; 
                        case 3: //life bars (2)
                           tileImage = boardImages.get(320,0,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                         case 4: //life bars (2)
                           tileImage = boardImages.get(240,0,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                         case 5: //gem storage
                           tileImage = boardImages.get(80,0,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                         case 6: //start floor
                           tileImage = boardImages.get(400,80,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                         case 7: //floor
                           tileImage = boardImages.get(0,160,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                         case 8: //water1
                           tileImage = boardImages.get(0,80,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                         case 9: //water2
                           tileImage = boardImages.get(80,80,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break; 
                         case 10: //water3
                           tileImage = boardImages.get(160,80,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                         case 11: //water4
                           tileImage = boardImages.get(240,80,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;  
                         case 12: //pit Up Left
                           tileImage = boardImages.get(240,240,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                         case 13: //pit Up Mid
                           tileImage = boardImages.get(320,240,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                         case 14: //pit Up Right
                           tileImage = boardImages.get(400,240,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                         case 15: //pit Mid Left
                           tileImage = boardImages.get(240,320,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                         case 16: //pit Mid Mid
                           tileImage = boardImages.get(320,320,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                         case 17: //pit Mid Right
                           tileImage = boardImages.get(400,320,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                         case 18: //pit Bot Left
                           tileImage = boardImages.get(240,400,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                        case 19: //pit Bot Mid
                           tileImage = boardImages.get(320,400,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                        case 20: //pit Bot Mid
                           tileImage = boardImages.get(400,400,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                        case 21: //bridge H
                           tileImage = boardImages.get(400,80,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                        case 22: //bridge V
                           tileImage = boardImages.get(320,80,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                        case 23: //Gem Up
                           tileImage = boardImages.get(0,160,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           tileImage = boardImages.get(240,160,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                        case 24: //Gem Left
                           tileImage = boardImages.get(0,160,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           tileImage = boardImages.get(320,160,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break;
                        case 25: //Gem Down
                           tileImage = boardImages.get(0,160,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           tileImage = boardImages.get(400,160,squareSize,squareSize);
                           image(tileImage, j*squareSize, i*squareSize);
                           break; 
                   }
               }   
           }
         
       }//end graphics
       
       public void printBoard(){
           for(int i = 0; i < rows; i++){
               for(int j = 0; j < cols; j++){
                   print(board[i][j]);
               }
               println();
          } 
      }//end print
   
}//end class
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
    
    public void ghostInitial(){
        for(int i = 1; i < 8; i++){
            if(board[i][0] == 6){
                rowPosG = i;
                colPosG = 0; 
            }
        }
        tileImage = boardImages.get(0,240,squareSize,squareSize);
        image(tileImage, colPosG*squareSize, rowPosG*squareSize);
          
    }
    
    public void ghostAnimate(int state){
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
    
    public void ghostMovement(int direction){
          if(direction == 1 && board[rowPosG][colPosG-1] != 0)
              colPosG -= 1;
          else if(direction == 2 && board[rowPosG-1][colPosG] != 0) //up
              rowPosG -= 1;
          else if(direction == 3 && board[rowPosG][colPosG+1] != 0) //right
              colPosG += 1;
          else if(direction == 4 && board[rowPosG+1][colPosG] != 0) //down
              rowPosG += 1;
    }
    
    public int checkPosition(enemy skull1, enemy skull2, int state){
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
    
    public boolean checkGem(){
         return gemCollected; 
    }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "main" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
