class Chemo
{
  float x, y, ybeam;
  
  Chemo(float gunX)
  {
    x = gunX;
    y = height-ySpacing;
    ybeam = y-(.5*ySpacing);
  }
  
  void display(color c)
  {
    strokeWeight(2);
    stroke(color(c));
    line(x, ybeam, x, ybeam-(.5*ySpacing));
  }
  
  boolean shoot()
  {
    ybeam = ybeam - 60;
    return ybeam < ySpacing;
  }
  
  float returnY()
  {
    return ybeam;
  }
  
  float returnX()
  {
    return x;
  }
}
