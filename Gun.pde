class Gun
{
  float x, y;
  
  Gun()
  {
    init();
  }
  
  void display()
  {
    noStroke();
    fill(0);
    rectMode(CORNER);
    rect(x-(.9*xSpacing), y-(.3*ySpacing), 1.8*xSpacing, .3*ySpacing);
    rect(x-(.8*xSpacing), y-(.35*ySpacing), 1.6*xSpacing, .1*ySpacing);
    rect(x-(.4*xSpacing), y-(.55*ySpacing), .8*xSpacing, .25*ySpacing);
    rect(x-(.1*xSpacing), y-(.65*ySpacing), .2*xSpacing, .15*ySpacing);
  }
  
  void move(float movex)
  {
    if(x+movex >= 5*xSpacing && x+movex <= width-(7*xSpacing))
    {
      x = x + movex;
    }
  }
  
  float returnX()
  {
    return x;
  }
  
  void init()
  {
    x = 14*xSpacing;
    y = height-ySpacing;
  }
}
