class PlayBackground
{
  PlayBackground()
  {
  }
  
  void display()
  {
    p = loadImage("playbackground.png");
    background(p);
  }
  
  void clear()
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
    text(score, width-(3*xSpacing), 9.25*ySpacing);
    
    stroke(5);
    for(int i = 0; i < cbc.length; i++)
    {
      float yMark = liquidyOffset + (i*liquidUnitHeight);
      strokeWeight(2);
      line(liquidxOffset+5, yMark, liquidxOffset+xSpacing-5, yMark);
      strokeWeight(1.5);
      line(liquidxOffset+5, yMark+(liquidUnitHeight/2), liquidxOffset+xSpacing-5, yMark+(liquidUnitHeight/2));
    }
  }
}
