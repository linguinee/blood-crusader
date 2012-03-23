class Platelet implements Cell
{
  float x, y, nx, ny;
  
  Platelet()
  {
    init();
  }
  
  void display()
  {
    noStroke();
    fill(platelet);
    ellipseMode(CENTER);
    ellipse(x, y, nx, ny);
  }
  
  boolean move()
  {
    y = y + espeed + 5;
    return y >= height-(.2*ySpacing);
  }
  
  void init()
  {
    x = random(5.5*xSpacing, width-(7.5*xSpacing));
    y = random(-height, 0);
    nx = random(.2*xSpacing, .4*xSpacing);
    ny = random(.2*ySpacing, .4*ySpacing);
  }
  
  float returnY()
  {
    return y;
  }
  
  float returnX()
  {
    return x;
  }
}
