import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class BloodCrusader extends PApplet {

// Ling-Yi Kung
// Section C
// lkung@andrew.cmu.edu
// Copyright \u00a9 Ling-Yi Kung January 2010 Pittsburgh, PA 15221 All Rights Reserved
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
int [] clr = new int[10];

Cell [] cells;
Chemo [] chemo = new Chemo [10];

int yellow = color(250, 250, 155);
int rbc = color(225, 0, 0);
int platelet = color(190, 140, 190);
int darkpurple = color(130, 40, 175);
int lightpurple = color(205, 130, 245);
int blastnucleus = color(175, 80, 225);
int bloodstream = color(250, 240, 250);
int basophilic5 = color(30, 50, 215);
int basophilic3 = color(110, 140, 255);
int basophilic1 = color(170, 180, 255);
int c = color(10, 240, 30);

PImage p;
PFont f;


public void setup()
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

public void draw()
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

public void checkForLose()
{
  if(hematocrit <= 1.5f || 10-plasma-hematocrit >= 3.0f)
  {
    lives = lives - 1;
  }
  
  if(lives < 1)
  {
    phase = LOSE;
  }
}

public void checkForWin()
{
  if(10-plasma-hematocrit <= .1f)
  {
    phase = WIN;
  }
}

public void drawLives()
{
  noStroke();
  fill(rbc);
  rectMode(CENTER);
  for(int i = 0; i < lives; i++)
  {
    rect(width-(5*xSpacing)+(4*i*(xSpacing/3)), 3.2f*ySpacing, xSpacing, ySpacing);
  }
}

// check to see if a cell collided with a cell - if it did, subtract a life and semi-start over
public void checkForCollision()
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
public void checkForKill()
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
            if(cells[i] instanceof Rbc && chemo[j].returnY() <= (cells[i].returnY()+ySpacing) && chemo[j].returnX() >= (cells[i].returnX()-(.5f*xSpacing)) && chemo[j].returnX() <= (cells[i].returnX()+(.5f*xSpacing)))
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
            if(cells[i] instanceof Rbc && chemo[j].returnY() <= (cells[i].returnY()+ySpacing) && chemo[j].returnX() >= (cells[i].returnX()-(.5f*xSpacing)) && chemo[j].returnX() <= (cells[i].returnX()+(.5f*xSpacing)))
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
            if(cells[i] instanceof Rbc && chemo[j].returnY() <= (cells[i].returnY()+ySpacing) && chemo[j].returnX() >= (cells[i].returnX()-(.5f*xSpacing)) && chemo[j].returnX() <= (cells[i].returnX()+(.5f*xSpacing)))
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

public void drawChemo()
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

public void drawCells()
{
  // when countdown is zero and there are less than 15 cells on the screen, insert a new type of cell into a spot in the array
  if(countdown == 0)
  {
    countdown = PApplet.parseInt(random(0, 30));
    while(counter < numberOfCells)
    {
      for(int i = 0; i < cells.length; i++)
      {
        if(cells[i] == null)
        {
          cellNumber = PApplet.parseInt(random(16));
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

public void keyPressed()
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

public void drawLoseBackground()
{
  p = loadImage("losebackground.png");
  background(p);
  f = loadFont("text.vlw");
  textFont(f);
  textAlign(CENTER);
  fill(253);
  text("SCORE:  " + score, width/2, .72f*height);
}

public void drawWinBackground()
{
  p = loadImage("winbackground.png");
  background(p);
  f = loadFont("text.vlw");
  textFont(f);
  textAlign(CENTER);
  fill(253);
  text("SCORE:  " + score, width/2, .72f*height);
}

public void drawLevelsBackground()
{
  p = loadImage("levelsbackground.png");
  background(p);
}

public void drawTitleBackground()
{
  p = loadImage("titlebackground.png");
  background(p);
}

public void drawIntroductionBackground()
{
  p = loadImage("introductionbackground.png");
  background(p);
}

public void drawInstructions1Background()
{
  p = loadImage("cellinformation.png");
  background(p);
}

public void drawInstructions2Background()
{
  p = loadImage("vialinformation.png");
  background(p);
}

public void drawInstructions3Background()
{
  p = loadImage("chemoinformation.png");
  background(p);
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                          WHICH PHASE ARE WE IN?                                                                            //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public void mousePressed()
{
  if(phase == TITLE)
  {
    if(mouseX > .358f*width && mouseX < (.358f*width)+(8.4f*xSpacing) && mouseY > .585f*height && mouseY < (.585f*height)+(.85f*ySpacing))
    {
      phase = INTRODUCTION;
    }
    else if(mouseX > .358f*width && mouseX < (.358f*width)+(8.4f*xSpacing) && mouseY > .655f*height && mouseY < (.655f*height)+(.85f*ySpacing))
    {
      phase = INSTRUCTIONS1;
    }
    else if(mouseX > .385f*width && mouseX < (.385f*width)+(6.9f*xSpacing) && mouseY > .725f*height && mouseY < (.725f*height)+(.85f*ySpacing))
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
    else if(mouseX > .455f*width && mouseX < (.455f*width)+(2.6f*xSpacing) && mouseY > height-ySpacing && mouseY < height)
    {
      phase = LEVELS;
    }
    else if(mouseX > width-(6.3f*xSpacing) && mouseX < width && mouseY > height-ySpacing && mouseY < height)
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
    if(mouseX > 26.75f*xSpacing && mouseX < width && mouseY > 16*ySpacing && mouseY < height)
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
    if(mouseX > 26.75f*xSpacing && mouseX < width && mouseY > 16*ySpacing && mouseY < height)
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
    if(mouseX > 26.75f*xSpacing && mouseX < width && mouseY > 16*ySpacing && mouseY < height)
    {
      phase = LEVELS;
    }
  }
  else if(phase == LEVELS)
  {
    if(mouseX > .3f*width && mouseX < (.3f*width)+(11.8f*xSpacing) && mouseY > .15f*height && mouseY < (.15f*height)+(3*ySpacing))
    {
      level = 0;
      variables();
      gun.init();
      phase = PLAY;
    }
    else if(mouseX > .22f*width && mouseX < (.22f*width)+(16.8f*xSpacing) && mouseY > .35f*height && mouseY < (.35f*height)+(3*ySpacing))
    {
      level = 1;
      variables();
      gun.init();
      phase = PLAY;
    }
    else if(mouseX > .305f*width && mouseX < (.305f*width)+(11.6f*xSpacing) && mouseY > .55f*height && mouseY < (.55f*height)+(3*ySpacing))
    {
      level = 2;
      variables();
      gun.init();
      phase = PLAY;
    }
  }
  else if(phase == PLAY)
  {
    if(mouseX > width-(3.8f*xSpacing) && mouseX < width-(.5f*xSpacing) && mouseY > height-(1.5f*ySpacing) && mouseY < height-(.9f*ySpacing))
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
    if(mouseX > .385f*width && mouseX < (.385f*width)+(6.9f*xSpacing) && mouseY > .725f*height && mouseY < (.725f*height)+(.85f*ySpacing))
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
    if(mouseX > .385f*width && mouseX < (.385f*width)+(6.9f*xSpacing) && mouseY > .725f*height && mouseY < (.725f*height)+(.85f*ySpacing))
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

public void variables()
{
  xSpacing = width/30;
  ySpacing = height/17;
  liquidyOffset = 2*ySpacing;
  liquidUnitHeight = (13*ySpacing)/cbc.length;
  liquidxOffset = (1.5f)*xSpacing;
  
  if(level == 0)
  {
    minCellsKilled = 40;
    plasma = 7.0f;
    hematocrit = 2.5f;
    plasmaIncrement = (5.7f-plasma)/minCellsKilled;
    hematocritIncrement = (4.2f-hematocrit)/minCellsKilled;
    espeed = 10;
    numberOfCells = 10;
  }
  else if(level == 1)
  {
    minCellsKilled = 45;
    plasma = 6.75f;
    hematocrit = 2.25f;
    plasmaIncrement = (5.7f-plasma)/minCellsKilled;
    hematocritIncrement = (4.2f-hematocrit)/minCellsKilled;
    espeed = 8;
    numberOfCells = 10;
  }
  else if(level == 2)
  {
    minCellsKilled = 50;
    plasma = 6.5f;
    hematocrit = 2.0f;
    plasmaIncrement = (5.7f-plasma)/minCellsKilled;
    hematocritIncrement = (4.2f-hematocrit)/minCellsKilled;
    espeed = 6;
    numberOfCells = 10;
  }
  
  countdown = 0;
  counter = 0;
  
  cells = new Cell [numberOfCells];
}
// Rbc, Platelet, Lymphoblast, Monoblast, Myeloblast, Promonocyte, Promyelocyte, and Tcell all implement Cell

interface Cell
{
  public void display();
  public boolean move();
  public float returnY();
  public float returnX();
  public void init();
}
class Chemo
{
  float x, y, ybeam;
  
  Chemo(float gunX)
  {
    x = gunX;
    y = height-ySpacing;
    ybeam = y-(.5f*ySpacing);
  }
  
  public void display(int c)
  {
    strokeWeight(2);
    stroke(color(c));
    line(x, ybeam, x, ybeam-(.5f*ySpacing));
  }
  
  public boolean shoot()
  {
    ybeam = ybeam - 60;
    return ybeam < ySpacing;
  }
  
  public float returnY()
  {
    return ybeam;
  }
  
  public float returnX()
  {
    return x;
  }
}
class Gun
{
  float x, y;
  
  Gun()
  {
    init();
  }
  
  public void display()
  {
    noStroke();
    fill(0);
    rectMode(CORNER);
    rect(x-(.9f*xSpacing), y-(.3f*ySpacing), 1.8f*xSpacing, .3f*ySpacing);
    rect(x-(.8f*xSpacing), y-(.35f*ySpacing), 1.6f*xSpacing, .1f*ySpacing);
    rect(x-(.4f*xSpacing), y-(.55f*ySpacing), .8f*xSpacing, .25f*ySpacing);
    rect(x-(.1f*xSpacing), y-(.65f*ySpacing), .2f*xSpacing, .15f*ySpacing);
  }
  
  public void move(float movex)
  {
    if(x+movex >= 5*xSpacing && x+movex <= width-(7*xSpacing))
    {
      x = x + movex;
    }
  }
  
  public float returnX()
  {
    return x;
  }
  
  public void init()
  {
    x = 14*xSpacing;
    y = height-ySpacing;
  }
}
// acute lymphoblastic leukemia (ALL)

class Lymphoblast implements Cell
{
  float x, y;
  
  Lymphoblast()
  {
    init();
  }
  
  public void display()
  {
    noStroke();
    fill(basophilic3);
    ellipseMode(CENTER);
    ellipse(x, y, 2.1f*xSpacing, 2*ySpacing);
    strokeWeight(1);
    stroke(darkpurple);
    fill(blastnucleus);
    ellipse(x+2, y+1, 1.7f*xSpacing, 1.6f*ySpacing);
    fill(platelet);
    ellipse(x+random(-14, 14), y+random(-14, 14), .15f*xSpacing, .15f*ySpacing);
    ellipse(x+random(-14, 14), y+random(-14, 14), .25f*xSpacing, .25f*ySpacing);
    ellipse(x+random(-14, 14), y+random(-14, 14), .2f*xSpacing, .2f*ySpacing);
  }
  
  public boolean move()
  {
    y = y + espeed;
    return y >= height;
  }
  
  public void init()
  {
    x = random(5.5f*xSpacing, width-(7.5f*xSpacing));
    y = random(-height, 0);
  }
  
  public float returnY()
  {
    return y;
  }
  
  public float returnX()
  {
    return x;
  }
}
// acute monoblastic leukemia (AML)

class Monoblast implements Cell
{
  float x, y;
  
  Monoblast()
  {
    init();
  }
  
  public void display()
  {
    noStroke();
    fill(basophilic1);
    ellipseMode(CENTER);
    ellipse(x, y, 2.4f*xSpacing, 2.2f*ySpacing);
    strokeWeight(1);
    stroke(darkpurple);
    fill(blastnucleus);
    ellipse(x, y+3, 1.9f*xSpacing, 1.7f*ySpacing);
    fill(platelet);
    ellipse(x+random(-14, 14), y+random(-14, 14), .25f*xSpacing, .25f*ySpacing);
    ellipse(x+random(-14, 14), y+random(-14, 14), .2f*xSpacing, .2f*ySpacing);
    noStroke();
    fill(bloodstream);
    ellipse(x+random(-25, 25), y+random(-15, 15), .1f*xSpacing, .1f*ySpacing);
    ellipse(x-random(25, 28), y+random(-15, 15), .25f*xSpacing, .25f*ySpacing);
    ellipse(x+random(-10, 20), y-random(18, 24), .3f*xSpacing, .3f*ySpacing);
  }
  
  public boolean move()
  {
    y = y + espeed;
    return y >= height;
  }
  
  public void init()
  {
    x = random(5.5f*xSpacing, width-(7.5f*xSpacing));
    y = random(-height, 0);
  }
  
  public float returnY()
  {
    return y;
  }
  
  public float returnX()
  {
    return x;
  }
}
// acute myeloid leukemia (AML)

class Myeloblast implements Cell
{
  float x, y;
  
  Myeloblast()
  {
    init();
  }
  
  public void display()
  {
    noStroke();
    fill(basophilic3);
    ellipseMode(CENTER);
    ellipse(x, y, 2.1f*xSpacing, 2.2f*ySpacing);
    strokeWeight(1);
    fill(blastnucleus);
    ellipse(x-2, y+2, 1.7f*xSpacing, 1.8f*ySpacing);
    stroke(darkpurple);
    fill(platelet);
    ellipse(x+random(-15, 15)-2, y+random(-15, 15)+1, .15f*xSpacing, .15f*ySpacing);
    ellipse(x+random(-15, 15)-2, y+random(-15, 15)+1, .25f*xSpacing, .25f*ySpacing);
    ellipse(x+random(-15, 15)-2, y+random(-15, 15)+1, .2f*xSpacing, .2f*ySpacing);
    line(x-14, y-26, x, y-29);
    line(x+27, y-3, x+23, y+14);
  }
  
  public boolean move()
  {
    y = y + espeed;
    return y >= height;
  }
  
  public void init()
  {
    x = random(5.5f*xSpacing, width-(7.5f*xSpacing));
    y = random(-height, 0);
  }
  
  public float returnY()
  {
    return y;
  }
  
  public float returnX()
  {
    return x;
  }
}
class Platelet implements Cell
{
  float x, y, nx, ny;
  
  Platelet()
  {
    init();
  }
  
  public void display()
  {
    noStroke();
    fill(platelet);
    ellipseMode(CENTER);
    ellipse(x, y, nx, ny);
  }
  
  public boolean move()
  {
    y = y + espeed + 5;
    return y >= height-(.2f*ySpacing);
  }
  
  public void init()
  {
    x = random(5.5f*xSpacing, width-(7.5f*xSpacing));
    y = random(-height, 0);
    nx = random(.2f*xSpacing, .4f*xSpacing);
    ny = random(.2f*ySpacing, .4f*ySpacing);
  }
  
  public float returnY()
  {
    return y;
  }
  
  public float returnX()
  {
    return x;
  }
}
class PlayBackground
{
  PlayBackground()
  {
  }
  
  public void display()
  {
    p = loadImage("playbackground.png");
    background(p);
  }
  
  public void clear()
  {
    noStroke();
    fill(0);
    rectMode(CORNER);
    rect(0, 0, 30*xSpacing, ySpacing);
    rect(0, height-ySpacing, 30*xSpacing, ySpacing);
    
    textAlign(CENTER);
    f = loadFont("text.vlw");
    textFont(f);
    fill(rbc);
    text(score, width-(3*xSpacing), 9.25f*ySpacing);
    
    stroke(5);
    for(int i = 0; i < cbc.length; i++)
    {
      float yMark = liquidyOffset + (i*liquidUnitHeight);
      strokeWeight(2);
      line(liquidxOffset+5, yMark, liquidxOffset+xSpacing-5, yMark);
      strokeWeight(1.5f);
      line(liquidxOffset+5, yMark+(liquidUnitHeight/2), liquidxOffset+xSpacing-5, yMark+(liquidUnitHeight/2));
    }
  }
}
// acute monocytic leukemia (AML)

class Promonocyte implements Cell
{
  float x, y;
  
  Promonocyte()
  {
    init();
  }
  
  public void display()
  {
    noStroke();
    fill(lightpurple);
    ellipseMode(CENTER);
    ellipse(x, y, 2.3f*xSpacing, 2.2f*ySpacing);
    strokeWeight(2);
    stroke(darkpurple);
    fill(blastnucleus);
    ellipse(x-6, y, 1.6f*xSpacing, 1.7f*ySpacing);
    fill(darkpurple);
    ellipse(x-6, y, 1.6f*xSpacing, .15f*ySpacing);
    ellipse(x, y+8, .1f*xSpacing, ySpacing);
    ellipse(x+11, y-12, .25f*xSpacing, .2f*ySpacing);
    curve(x-22, y+22, x-22, y-16, x, y+20, x-10, y+50);
    curve(x-16, y+50, x-16, y+20, x+3, y-10, x+16, y-10);
    curve(x-50, y-15, x-22, y-18, x-5, y, x-3, y+50);
    line(x-2, y-24, x+17, y+5);
    strokeWeight(1);
    fill(platelet);
    ellipse(x+random(-14, 14), y+random(-14, 14), .25f*xSpacing, .25f*ySpacing);
    ellipse(x+random(-14, 14), y+random(-14, 14), .2f*xSpacing, .2f*ySpacing);
    noStroke();
    fill(bloodstream);
    ellipse(x-random(25, 28), y+random(-15, 15), .2f*xSpacing, .2f*ySpacing);
    ellipse(x+random(-10, 20), y-random(18, 24), .25f*xSpacing, .25f*ySpacing);
  }
  
  public boolean move()
  {
    y = y + espeed;
    return y >= height;
  }
  
  public void init()
  {
    x = random(5.5f*xSpacing, width-(7.5f*xSpacing));
    y = random(-height, 0);
  }
  
  public float returnY()
  {
    return y;
  }
  
  public float returnX()
  {
    return x;
  }
}
// acute promyelocytic leukemia (APL)

class Promyelocyte implements Cell
{
  float x, y;
  
  Promyelocyte()
  {
    init();
  }
  
  public void display()
  {
    noStroke();
    fill(basophilic3);
    ellipseMode(CENTER);
    ellipse(x, y, 2.2f*xSpacing, 2.1f*ySpacing);
    fill(blastnucleus);
    ellipse(x+4, y+6, 1.4f*xSpacing, 1.2f*ySpacing);
    strokeWeight(1);
    stroke(darkpurple);
    fill(platelet);
    ellipse(x+random(-12, 12)+5, y+random(-12, 12)+5, .2f*xSpacing, .2f*ySpacing);
    noFill();
    stroke(darkpurple);
    line(x-32, y+6, x-20, y+5);
    line(x-28, y-4, x-27, y+8);
    line(x-5, y-28, x-3, y-15);
    line(x-10, y-20, x+1, y-17);
    line(x, y, x+10, y+11);
    line(x+18, y-20, x+20, y-10);
    line(x+9, y+3, x+4, y+10);
    line(x-7, y+28, x-5, y+15);
    line(x-18, y+12, x-9, y);
    line(x-25, y+15, x-14, y+12);
    line(x-19, y-10, x-8, y-7);
    line(x-25, y-15, x-20, y-23);
    line(x+15, y+20, x+20, y+10);
    line(x+2, y+8, x+5, y+20);
    line(x+5, y-2, x+19, y-5);
    line(x+28, y+3, x+17, y-2);
    line(x-1, y+25, x-13, y+22);
    line(x, y-28, x+11, y-25);
    line(x+10, y-15, x+15, y-27);
    line(x+4, y-10, x+8, y-2);
    line(x+12, y+5, x+12, y-10);
    line(x-27, y-8, x-18, y-18);
    line(x-20, y-5, x-28, y-11);
    line(x-1, y+15, x-7, y+4);
  }
  
  public boolean move()
  {
    y = y + espeed;
    return y >= height;
  }
  
  public void init()
  {
    x = random(5.5f*xSpacing, width-(7.5f*xSpacing));
    y = random(-height, 0);
  }
  
  public float returnY()
  {
    return y;
  }
  
  public float returnX()
  {
    return x;
  }
}
// erythrocytes

class Rbc implements Cell
{
  float x, y;
  
  Rbc()
  {
    init();
  }
  
  public void display()
  {
    noStroke();
    fill(rbc);
    ellipseMode(CENTER);
    ellipse(x, y, xSpacing, ySpacing);
    fill(190, 0, 0);
    ellipse(x, y, .4f*xSpacing, .4f*ySpacing);
  }
  
  public boolean move()
  {
    y = y + espeed + 2;
    return y >= height-(.5f*ySpacing);
  }
  
  public void init()
  {
    x = random(5.5f*xSpacing, width-(7.5f*xSpacing));
    y = random(-height, 0);
  }
  
  public float returnY()
  {
    return y;
  }
  
  public float returnX()
  {
    return x;
  }
}
// adult t-cell leukemia/lymphoma (ATLL)

class Tcell implements Cell
{
  float x, y;
  
  Tcell()
  {
    init();
  }
  
  public void display()
  {
    noStroke();
    fill(basophilic3);
    ellipseMode(CENTER);
    ellipse(x, y, 2.1f*xSpacing, 2.2f*ySpacing);
    strokeWeight(1);
    stroke(darkpurple);
    fill(blastnucleus);
    ellipse(x+16, y+3, .8f*xSpacing, ySpacing);
    ellipse(x-13, y-1, 1.1f*xSpacing, 1.1f*ySpacing);
    ellipse(x, y-13, xSpacing, ySpacing);
    ellipse(x, y+18, .8f*xSpacing, .9f*ySpacing);
    noStroke();
    ellipse(x+1, y, 1.2f*xSpacing, 1.3f*ySpacing);
    ellipse(x-13, y-11, .4f*xSpacing, .4f*ySpacing);
    ellipse(x+11, y+12, .4f*xSpacing, .4f*ySpacing);
    stroke(darkpurple);
    fill(platelet);
    ellipse(x+random(-14, 14), y+random(-14, 14), .25f*xSpacing, .25f*ySpacing);
    ellipse(x+random(-14, 14), y+random(-14, 14), .2f*xSpacing, .2f*ySpacing);
    noStroke();
    fill(bloodstream);
    ellipse(x+random(-15, 15), y+random(-15, 15), .25f*xSpacing, .25f*ySpacing);
    ellipse(x+random(17, 22), y+random(10, 22), .15f*xSpacing, .15f*ySpacing);
    ellipse(x-random(17, 22), y+random(8, 18), .25f*xSpacing, .25f*ySpacing);
  }
  
  public boolean move()
  {
    y = y + espeed;
    return y >= height;
  }
  
  public void init()
  {
    x = random(5.5f*xSpacing, width-(7.5f*xSpacing));
    y = random(-height, 0);
  }
  
  public float returnY()
  {
    return y;
  }
  
  public float returnX()
  {
    return x;
  }
}
class Vial
{
  float hematocrit, plasma;
  
  Vial()
  {
  }
  
  public void redFluid(float hematocrit)
  {
    noStroke();
    fill(rbc);
    ellipseMode(CORNER);
    ellipse(liquidxOffset, height-(2.5f*ySpacing), xSpacing, ySpacing);
    rectMode(CORNERS);
    rect(liquidxOffset, height-liquidyOffset, liquidxOffset+xSpacing, height-liquidyOffset-(hematocrit*liquidUnitHeight));
  }
  
  public void yellowFluid(float plasma)
  {
    noStroke();
    fill(yellow);
    rectMode(CORNERS);
    rect(liquidxOffset, liquidyOffset, liquidxOffset+xSpacing, liquidyOffset+(plasma*liquidUnitHeight));
  }
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "BloodCrusader" });
  }
}
