// erythrocytes

class Rbc implements Cell
{
  float x, y;
  
  Rbc()
  {
    init();
  }
  
  void display()
  {
    noStroke();
    fill(rbc);
    ellipseMode(CENTER);
    ellipse(x, y, xSpacing, ySpacing);
    fill(190, 0, 0);
    ellipse(x, y, .4*xSpacing, .4*ySpacing);
  }
  
  boolean move()
  {
    y = y + espeed + 2;
    return y >= height-(.5*ySpacing);
  }
  
  void init()
  {
    x = random(5.5*xSpacing, width-(7.5*xSpacing));
    y = random(-height, 0);
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
