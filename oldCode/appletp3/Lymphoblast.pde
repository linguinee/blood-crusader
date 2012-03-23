// acute lymphoblastic leukemia (ALL)

class Lymphoblast implements Cell
{
  float x, y;
  
  Lymphoblast()
  {
    init();
  }
  
  void display()
  {
    noStroke();
    fill(basophilic3);
    ellipseMode(CENTER);
    ellipse(x, y, 2.1*xSpacing, 2*ySpacing);
    strokeWeight(1);
    stroke(darkpurple);
    fill(blastnucleus);
    ellipse(x+2, y+1, 1.7*xSpacing, 1.6*ySpacing);
    fill(platelet);
    ellipse(x+random(-14, 14), y+random(-14, 14), .15*xSpacing, .15*ySpacing);
    ellipse(x+random(-14, 14), y+random(-14, 14), .25*xSpacing, .25*ySpacing);
    ellipse(x+random(-14, 14), y+random(-14, 14), .2*xSpacing, .2*ySpacing);
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
