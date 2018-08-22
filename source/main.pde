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

void setup(){
 size(1280,720);
 sprite = loadImage("data/spriteSheet.png");
 start = loadImage("data/start.png");
 death = loadImage("data/death.png");
 win = loadImage("data/win.png");
}

void draw(){
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

void keyPressed(){ //10 enter 82 r 81 q
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
