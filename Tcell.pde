// adult t-cell leukemia/lymphoma (ATLL)

class Tcell implements Cell
{
  float x, y;
  
  Tcell()
  {
    init();
  }
  
  void display()
  {
    noStroke();
    fill(basophilic3);
    ellipseMode(CENTER);
    ellipse(x, y, 2.1*xSpacing, 2.2*ySpacing);
    strokeWeight(1);
    stroke(darkpurple);
    fill(blastnucleus);
    ellipse(x+16, y+3, .8*xSpacing, ySpacing);
    ellipse(x-13, y-1, 1.1*xSpacing, 1.1*ySpacing);
    ellipse(x, y-13, xSpacing, ySpacing);
    ellipse(x, y+18, .8*xSpacing, .9*ySpacing);
    noStroke();
    ellipse(x+1, y, 1.2*xSpacing, 1.3*ySpacing);
    ellipse(x-13, y-11, .4*xSpacing, .4*ySpacing);
    ellipse(x+11, y+12, .4*xSpacing, .4*ySpacing);
    stroke(darkpurple);
    fill(platelet);
    ellipse(x+random(-14, 14), y+random(-14, 14), .25*xSpacing, .25*ySpacing);
    ellipse(x+random(-14, 14), y+random(-14, 14), .2*xSpacing, .2*ySpacing);
    noStroke();
    fill(bloodstream);
    ellipse(x+random(-15, 15), y+random(-15, 15), .25*xSpacing, .25*ySpacing);
    ellipse(x+random(17, 22), y+random(10, 22), .15*xSpacing, .15*ySpacing);
    ellipse(x-random(17, 22), y+random(8, 18), .25*xSpacing, .25*ySpacing);
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
