// Ling-Yi Kung
// Section C
// lkung@andrew.cmu.edu
// Copyright © Ling-Yi Kung January 2010 Pittsburgh, PA 15221 All Rights Reserved
// Jim Roberts
// Sang Tian, Alex Yoo, Jitu Das
// Facts and statistics from the Leukemia and Lymphoma Society and Wikipedia

PlayBackground playBackground;
Gun gun;
Vial vial;

final int TITLE = 0;
final int INTRODUCTION = 1;
final int INSTRUCTIONS1 = 2;
final int INSTRUCTIONS2 = 3;
final int INSTRUCTIONS3 = 4;
final int LEVELS = 5;
final int PLAY = 6;
final int LOSE = 7;
final int WIN = 8;

int phase, score, lives, countdown, numberOfCells, cellNumber, counter, espeed, minCellsKilled, level;

float xSpacing, ySpacing, liquidyOffset, liquidUnitHeight, liquidxOffset, plasma, hematocrit, plasmaIncrement, hematocritIncrement;

int [] cbc = new int [10];
color [] clr = new color[10];

Cell [] cells;
Chemo [] chemo = new Chemo [10];

color yellow = color(250, 250, 155);
color rbc = color(225, 0, 0);
color platelet = color(190, 140, 190);
color darkpurple = color(130, 40, 175);
color lightpurple = color(205, 130, 245);
color blastnucleus = color(175, 80, 225);
color bloodstream = color(250, 240, 250);
color basophilic5 = color(30, 50, 215);
color basophilic3 = color(110, 140, 255);
color basophilic1 = color(170, 180, 255);
color c = color(10, 240, 30);

PImage p;
PFont f;


void setup()
{
  size(900, 510);
  smooth();
  frameRate(10);
  
  variables();
  
  playBackground = new PlayBackground();
  gun = new Gun();
  vial = new Vial();
  
  phase = TITLE;
  level = 0;
  score = 0;
  lives = 4;
}

void draw()
{
  if(phase == TITLE)
  {
    drawTitleBackground();
  }
  else if(phase == LEVELS)
  {
    drawLevelsBackground();
  }
  else if(phase == PLAY)
  {
    playBackground.display();
    drawLives();
    vial.redFluid(hematocrit);
    vial.yellowFluid(plasma);
    drawChemo();
    gun.display();
    drawCells();
    checkForKill();
    checkForCollision();
    checkForWin();
    checkForLose();
    playBackground.clear();
  }
  else if(phase == INTRODUCTION)
  {
    drawIntroductionBackground();
  }
  else if(phase == INSTRUCTIONS1)
  {
    drawInstructions1Background();
  }
  else if(phase == INSTRUCTIONS2)
  {
    drawInstructions2Background();
  }
  else if(phase == INSTRUCTIONS3)
  {
    drawInstructions3Background();
  }
  else if(phase == WIN)
  {
    drawWinBackground();
  }
  else if(phase == LOSE)
  {
    drawLoseBackground();
  }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                              WHEN WE'RE IN PLAY                                                                            //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void checkForLose()
{
  if(hematocrit <= 1.5 || 10-plasma-hematocrit >= 3.0)
  {
    lives = lives - 1;
  }
  
  if(lives < 1)
  {
    phase = LOSE;
  }
}

void checkForWin()
{
  if(10-plasma-hematocrit <= .1)
  {
    phase = WIN;
  }
}

void drawLives()
{
  noStroke();
  fill(rbc);
  rectMode(CENTER);
  for(int i = 0; i < lives; i++)
  {
    rect(width-(5*xSpacing)+(4*i*(xSpacing/3)), 3.2*ySpacing, xSpacing, ySpacing);
  }
}

// check to see if a cell collided with a cell - if it did, subtract a life and semi-start over
void checkForCollision()
{
  for(int i = 0; i < cells.length; i++)
  {
    if(cells[i] != null)
    {
      if(!(cells[i] instanceof Platelet) && !(cells[i] instanceof Rbc) && cells[i].returnY() > (height-ySpacing) && cells[i].returnX() >= (gun.returnX()-xSpacing) && cells[i].returnX() <= (gun.returnX()+xSpacing))
      {
        lives = lives - 1;
        variables();
      }
      if(cells[i] instanceof Rbc && cells[i].returnY() > (height-ySpacing) && cells[i].returnX() >= (gun.returnX()-xSpacing) && cells[i].returnX() <= (gun.returnX()+xSpacing))
      {
        lives = lives - 1;
        variables();
      }
    }
  }
}

// check to see if the chemotherapy hit a cell - if it hit a cancer cell, +100 points, but if it hit a RBC, -25 points and reduce the hematocrit
void checkForKill()
{
  if(level == 0)
  {
    for(int i = 0; i < cells.length; i++)
    {
      if(cells[i] != null)
      {
        for(int j = 0; j < chemo.length; j++)
        {
          if(chemo[j] != null && cells[i] != null)
          {
            if(!(cells[i] instanceof Platelet) && !(cells[i] instanceof Rbc) && chemo[j].returnY() <= (cells[i].returnY()+ySpacing) && chemo[j].returnX() >= (cells[i].returnX()-xSpacing) && chemo[j].returnX() <= (cells[i].returnX()+xSpacing))
            {
              cells[i] = null;
              chemo[j] = null;
              counter = counter - 1;
              score = score + 100;
              plasma = plasma - plasmaIncrement;
            }
            if(cells[i] instanceof Rbc && chemo[j].returnY() <= (cells[i].returnY()+ySpacing) && chemo[j].returnX() >= (cells[i].returnX()-(.5*xSpacing)) && chemo[j].returnX() <= (cells[i].returnX()+(.5*xSpacing)))
            {
              cells[i] = null;
              chemo[j] = null;
              counter = counter - 1;
              score = score - 25;
              hematocrit = hematocrit - hematocritIncrement;
            }
          }
        }
      }
    }
  }
  else if(level == 1)
  {
    for(int i = 0; i < cells.length; i++)
    {
      if(cells[i] != null)
      {
        for(int j = 0; j < chemo.length; j++)
        {
          if(chemo[j] != null && cells[i] != null)
          {
            if(!(cells[i] instanceof Platelet) && !(cells[i] instanceof Rbc) && !(cells[i] instanceof Lymphoblast) && !(cells[i] instanceof Tcell) &&
            chemo[j].returnY() <= (cells[i].returnY()+ySpacing) && chemo[j].returnX() >= (cells[i].returnX()-xSpacing) && chemo[j].returnX() <= (cells[i].returnX()+xSpacing))
            {
              if(clr[j] == color(10, 250, 130))
              {
                cells[i] = null;
                chemo[j] = null;
                counter = counter - 1;
                score = score + 100;
                plasma = plasma - plasmaIncrement;
              }
            }
            if(!(cells[i] instanceof Platelet) && !(cells[i] instanceof Rbc) && cells[i] instanceof Lymphoblast || cells[i] instanceof Tcell &&
            chemo[j].returnY() <= (cells[i].returnY()+ySpacing) && chemo[j].returnX() >= (cells[i].returnX()-xSpacing) && chemo[j].returnX() <= (cells[i].returnX()+xSpacing))
            {
              if(clr[j] == color(10, 250, 210))
              {
                cells[i] = null;
                chemo[j] = null;
                counter = counter - 1;
                score = score + 100;
                plasma = plasma - plasmaIncrement;
              }
            }
            if(cells[i] instanceof Rbc && chemo[j].returnY() <= (cells[i].returnY()+ySpacing) && chemo[j].returnX() >= (cells[i].returnX()-(.5*xSpacing)) && chemo[j].returnX() <= (cells[i].returnX()+(.5*xSpacing)))
            {
              cells[i] = null;
              chemo[j] = null;
              counter = counter - 1;
              score = score - 25;
              hematocrit = hematocrit - hematocritIncrement;
            }
          }
        }
      }
    }
  }
  else if(level == 2)
  {
    for(int i = 0; i < cells.length; i++)
    {
      if(cells[i] != null)
      {
        for(int j = 0; j < chemo.length; j++)
        {
          if(chemo[j] != null && cells[i] != null)
          {
            if(!(cells[i] instanceof Platelet) && !(cells[i] instanceof Rbc) && !(cells[i] instanceof Lymphoblast) && !(cells[i] instanceof Tcell) && !(cells[i] instanceof Promyelocyte) &&
            chemo[j].returnY() <= (cells[i].returnY()+ySpacing) && chemo[j].returnX() >= (cells[i].returnX()-xSpacing) && chemo[j].returnX() <= (cells[i].returnX()+xSpacing))
            {
              if(clr[j] == color(10, 250, 130))
              {
                cells[i] = null;
                chemo[j] = null;
                counter = counter - 1;
                score = score + 100;
                plasma = plasma - plasmaIncrement;
              }
            }
            if(!(cells[i] instanceof Platelet) && !(cells[i] instanceof Rbc) && cells[i] instanceof Lymphoblast &&
            chemo[j].returnY() <= (cells[i].returnY()+ySpacing) && chemo[j].returnX() >= (cells[i].returnX()-xSpacing) && chemo[j].returnX() <= (cells[i].returnX()+xSpacing))
            {
              if(clr[j] == color(10, 250, 210))
              {
                cells[i] = null;
                chemo[j] = null;
                counter = counter - 1;
                score = score + 100;
                plasma = plasma - plasmaIncrement;
              }
            }
            if(!(cells[i] instanceof Platelet) && !(cells[i] instanceof Rbc) && cells[i] instanceof Tcell &&
            chemo[j].returnY() <= (cells[i].returnY()+ySpacing) && chemo[j].returnX() >= (cells[i].returnX()-xSpacing) && chemo[j].returnX() <= (cells[i].returnX()+xSpacing))
            {
              if(clr[j] == color(250, 10, 90))
              {
                cells[i] = null;
                chemo[j] = null;
                counter = counter - 1;
                score = score + 100;
                plasma = plasma - plasmaIncrement;
              }
            }
            if(!(cells[i] instanceof Platelet) && !(cells[i] instanceof Rbc) && cells[i] instanceof Promyelocyte &&
            chemo[j].returnY() <= (cells[i].returnY()+ySpacing) && chemo[j].returnX() >= (cells[i].returnX()-xSpacing) && chemo[j].returnX() <= (cells[i].returnX()+xSpacing))
            {
              if(clr[j] == color(240, 10, 230))
              {
                cells[i] = null;
                chemo[j] = null;
                counter = counter - 1;
                score = score + 100;
                plasma = plasma - plasmaIncrement;
              }
              else
              {
                cells[i] = null;
                chemo[j] = null;
                counter = counter - 1;
                lives = lives - 1;
                variables();
                gun.init();
              }
            }
            if(cells[i] instanceof Rbc && chemo[j].returnY() <= (cells[i].returnY()+ySpacing) && chemo[j].returnX() >= (cells[i].returnX()-(.5*xSpacing)) && chemo[j].returnX() <= (cells[i].returnX()+(.5*xSpacing)))
            {
              cells[i] = null;
              chemo[j] = null;
              counter = counter - 1;
              score = score - 25;
              hematocrit = hematocrit - hematocritIncrement;
            }
          }
        }
      }
    }
  }
}

void drawChemo()
{
  // when a certain key is pressed, insert the correct color chemotherapy into a spot in the array
  for(int i = 0; i < chemo.length; i++)
  {
    if(keyPressed)
    {
      // spacebar (green) = general chemotherapy
      if(key == 32 && level == 0)
      {
        if(chemo[i] == null)
        {
          clr[i] = color(10, 240, 30);
          chemo[i] = new Chemo(gun.returnX());
          break;
        }
      }
      // m (green-blue) = "7+3" cytarabine + anthracycline for AML
      else if(key == 109 && level != 0)
      {
        if(chemo[i] == null)
        {
          clr[i] = color(10, 250, 130);
          chemo[i] = new Chemo(gun.returnX());
          break;
        }
      }
      // l (blue-green) = vincristine + prednisolone + daunorubicin + asparaginase for ALL
      else if(key == 108 && level != 0)
      {
        if(chemo[i] == null)
        {
          clr[i] = color(10, 250, 210);
          chemo[i] = new Chemo(gun.returnX());
          break;
        }
      }
      // a (purple) = ATRA therapy for APL
      else if(key == 97 && level == 2)
      {
        if(chemo[i] == null)
        {
          clr[i] = color(240, 10, 230);
          chemo[i] = new Chemo(gun.returnX());
          break;
        }
      }
      // t (red) = alemtuzumab for T-PLL
      else if(key == 116 && level == 2)
      {
        if(chemo[i] == null)
        {
          clr[i] = color(250, 10, 90);
          chemo[i] = new Chemo(gun.returnX());
          break;
        }
      }
    }
  }
  
  // traverse the array to display and move any elements that have chemotherapy in them - if a beam in an element has moved offscreen, make that element null
  for(int i = 0; i < chemo.length; i++)
  {
    if(chemo[i]!= null)
    {
      chemo[i].display(clr[i]);
      if(chemo[i].shoot())
      {
        chemo[i] = null;
      }
    }
  }
}

void drawCells()
{
  // when countdown is zero and there are less than 15 cells on the screen, insert a new type of cell into a spot in the array
  if(countdown == 0)
  {
    countdown = int(random(0, 30));
    while(counter < numberOfCells)
    {
      for(int i = 0; i < cells.length; i++)
      {
        if(cells[i] == null)
        {
          cellNumber = int(random(16));
          if(cellNumber <= 4)
          {
            cells[i] = new Rbc();
          }
          else if(cellNumber > 4 && cellNumber <= 7)
          {
            cells[i] = new Platelet();
          }
          else if(cellNumber > 7 && cellNumber <= 9)
          {
            cells[i] = new Lymphoblast();
          }
          else if(cellNumber > 9 && cellNumber <= 11)
          {
            cells[i] = new Myeloblast();
          }
          else if(cellNumber == 12)
          {
            cells[i] = new Promonocyte();
          }
          else if(cellNumber == 13)
          {
            cells[i] = new Monoblast();
          }
          else if(cellNumber == 14)
          {
            cells[i] = new Promyelocyte();
          }
          else if(cellNumber == 15)
          {
            cells[i] = new Tcell();
          }
          cells[i].init();
          counter = counter + 1;
          break;
        }
      }
    }
  }
  
  if(countdown > 0)
  {
    countdown = countdown - 1;
  }
  
  // traverse the array to display and move any elements that have cells in them - if a cell in an element has moved offscreen, make that element null
  for(int i = 0; i < cells.length; i++)
  {
    if(cells[i] != null)
    {
      cells[i].display();
      if(cells[i].move())
      {
        if(!(cells[i] instanceof Rbc) && !(cells[i] instanceof Platelet))
        {
          score = score - 50;
          plasma = plasma + plasmaIncrement;
        }
        cells[i] = null;
        counter = counter - 1;
      }
    }
  }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                      IF WE'RE IN PLAY, ARE WE MOVING?                                                                      //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void keyPressed()
{
  if(phase == PLAY)
  {
    int movex = 30;
    
    if(key == CODED)
    {
      if(keyCode == LEFT)
      {
        gun.move(-movex);
      }
      else if(keyCode == RIGHT)
      {
        gun.move(movex);
      }
      else
      {
        gun.move(0);
      }
    }
  }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                          DRAWING THE DIFFERENT PHASES                                                                      //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void drawLoseBackground()
{
  p = loadImage("losebackground.png");
  background(p);
  f = loadFont("text.vlw");
  textFont(f);
  textAlign(CENTER);
  fill(253);
  text("SCORE:  " + score, width/2, .72*height);
}

void drawWinBackground()
{
  p = loadImage("winbackground.png");
  background(p);
  f = loadFont("text.vlw");
  textFont(f);
  textAlign(CENTER);
  fill(253);
  text("SCORE:  " + score, width/2, .72*height);
}

void drawLevelsBackground()
{
  p = loadImage("levelsbackground.png");
  background(p);
}

void drawTitleBackground()
{
  p = loadImage("titlebackground.png");
  background(p);
}

void drawIntroductionBackground()
{
  p = loadImage("introductionbackground.png");
  background(p);
}

void drawInstructions1Background()
{
  p = loadImage("cellinformation.png");
  background(p);
}

void drawInstructions2Background()
{
  p = loadImage("vialinformation.png");
  background(p);
}

void drawInstructions3Background()
{
  p = loadImage("chemoinformation.png");
  background(p);
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                          WHICH PHASE ARE WE IN?                                                                            //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void mousePressed()
{
  if(phase == TITLE)
  {
    if(mouseX > .358*width && mouseX < (.358*width)+(8.4*xSpacing) && mouseY > .585*height && mouseY < (.585*height)+(.85*ySpacing))
    {
      phase = INTRODUCTION;
    }
    else if(mouseX > .358*width && mouseX < (.358*width)+(8.4*xSpacing) && mouseY > .655*height && mouseY < (.655*height)+(.85*ySpacing))
    {
      phase = INSTRUCTIONS1;
    }
    else if(mouseX > .385*width && mouseX < (.385*width)+(6.9*xSpacing) && mouseY > .725*height && mouseY < (.725*height)+(.85*ySpacing))
    {
      phase = LEVELS;
    }
  }
  else if(phase == INTRODUCTION)
  {
    if(mouseX > 0 && mouseX < 4*xSpacing && mouseY > height-ySpacing && mouseY < height)
    {
      phase = TITLE;
    }
    else if(mouseX > .455*width && mouseX < (.455*width)+(2.6*xSpacing) && mouseY > height-ySpacing && mouseY < height)
    {
      phase = LEVELS;
    }
    else if(mouseX > width-(6.3*xSpacing) && mouseX < width && mouseY > height-ySpacing && mouseY < height)
    {
      phase = INSTRUCTIONS1;
    }
  }
  else if(phase == INSTRUCTIONS1)
  {
    if(mouseX > 0 && mouseX < 4*xSpacing && mouseY > height-ySpacing && mouseY < height)
    {
      phase = TITLE;
    }
    if(mouseX > 26.75*xSpacing && mouseX < width && mouseY > 16*ySpacing && mouseY < height)
    {
      phase = INSTRUCTIONS2;
    }
  }
  else if(phase == INSTRUCTIONS2)
  {
    if(mouseX > 0 && mouseX < 4*xSpacing && mouseY > height-ySpacing && mouseY < height)
    {
      phase = INSTRUCTIONS1;
    }
    if(mouseX > 26.75*xSpacing && mouseX < width && mouseY > 16*ySpacing && mouseY < height)
    {
      phase = INSTRUCTIONS3;
    }
  }
  else if(phase == INSTRUCTIONS3)
  {
    if(mouseX > 0 && mouseX < 4*xSpacing && mouseY > height-ySpacing && mouseY < height)
    {
      phase = INSTRUCTIONS2;
    }
    if(mouseX > 26.75*xSpacing && mouseX < width && mouseY > 16*ySpacing && mouseY < height)
    {
      phase = LEVELS;
    }
  }
  else if(phase == LEVELS)
  {
    if(mouseX > .3*width && mouseX < (.3*width)+(11.8*xSpacing) && mouseY > .15*height && mouseY < (.15*height)+(3*ySpacing))
    {
      level = 0;
      variables();
      gun.init();
      phase = PLAY;
    }
    else if(mouseX > .22*width && mouseX < (.22*width)+(16.8*xSpacing) && mouseY > .35*height && mouseY < (.35*height)+(3*ySpacing))
    {
      level = 1;
      variables();
      gun.init();
      phase = PLAY;
    }
    else if(mouseX > .305*width && mouseX < (.305*width)+(11.6*xSpacing) && mouseY > .55*height && mouseY < (.55*height)+(3*ySpacing))
    {
      level = 2;
      variables();
      gun.init();
      phase = PLAY;
    }
  }
  else if(phase == PLAY)
  {
    if(mouseX > width-(3.8*xSpacing) && mouseX < width-(.5*xSpacing) && mouseY > height-(1.5*ySpacing) && mouseY < height-(.9*ySpacing))
    {
      variables();
      gun.init();
      score = 0;
      lives = 4;
      phase = TITLE;
    }
  }
  else if(phase == WIN)
  {
    if(mouseX > .385*width && mouseX < (.385*width)+(6.9*xSpacing) && mouseY > .725*height && mouseY < (.725*height)+(.85*ySpacing))
    {
      variables();
      gun.init();
      score = 0;
      lives = 4;
      phase = TITLE;
    }
  }
  else if(phase == LOSE)
  {
    if(mouseX > .385*width && mouseX < (.385*width)+(6.9*xSpacing) && mouseY > .725*height && mouseY < (.725*height)+(.85*ySpacing))
    {
      variables();
      gun.init();
      score = 0;
      lives = 4;
      phase = TITLE;
    }
  }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                        INITIALIZING VARIABLES                                                                              //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void variables()
{
  xSpacing = width/30;
  ySpacing = height/17;
  liquidyOffset = 2*ySpacing;
  liquidUnitHeight = (13*ySpacing)/cbc.length;
  liquidxOffset = (1.5)*xSpacing;
  
  if(level == 0)
  {
    minCellsKilled = 40;
    plasma = 7.0;
    hematocrit = 2.5;
    plasmaIncrement = (5.7-plasma)/minCellsKilled;
    hematocritIncrement = (4.2-hematocrit)/minCellsKilled;
    espeed = 10;
    numberOfCells = 10;
  }
  else if(level == 1)
  {
    minCellsKilled = 45;
    plasma = 6.75;
    hematocrit = 2.25;
    plasmaIncrement = (5.7-plasma)/minCellsKilled;
    hematocritIncrement = (4.2-hematocrit)/minCellsKilled;
    espeed = 8;
    numberOfCells = 10;
  }
  else if(level == 2)
  {
    minCellsKilled = 50;
    plasma = 6.5;
    hematocrit = 2.0;
    plasmaIncrement = (5.7-plasma)/minCellsKilled;
    hematocritIncrement = (4.2-hematocrit)/minCellsKilled;
    espeed = 6;
    numberOfCells = 10;
  }
  
  countdown = 0;
  counter = 0;
  
  cells = new Cell [numberOfCells];
}
