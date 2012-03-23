// acute promyelocytic leukemia (APL)

class Promyelocyte implements Cell
{
  float x, y;
  
  Promyelocyte()
  {
    init();
  }
  
  void display()
  {
    noStroke();
    fill(basophilic3);
    ellipseMode(CENTER);
    ellipse(x, y, 2.2*xSpacing, 2.1*ySpacing);
    fill(blastnucleus);
    ellipse(x+4, y+6, 1.4*xSpacing, 1.2*ySpacing);
    strokeWeight(1);
    stroke(darkpurple);
    fill(platelet);
    ellipse(x+random(-12, 12)+5, y+random(-12, 12)+5, .2*xSpacing, .2*ySpacing);
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
  
  boolean move()
  {
    y = y + espeed;
    return y >= height;
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
