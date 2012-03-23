// acute monoblastic leukemia (AML)

class Monoblast implements Cell
{
  float x, y;
  
  Monoblast()
  {
    init();
  }
  
  void display()
  {
    noStroke();
    fill(basophilic1);
    ellipseMode(CENTER);
    ellipse(x, y, 2.4*xSpacing, 2.2*ySpacing);
    strokeWeight(1);
    stroke(darkpurple);
    fill(blastnucleus);
    ellipse(x, y+3, 1.9*xSpacing, 1.7*ySpacing);
    fill(platelet);
    ellipse(x+random(-14, 14), y+random(-14, 14), .25*xSpacing, .25*ySpacing);
    ellipse(x+random(-14, 14), y+random(-14, 14), .2*xSpacing, .2*ySpacing);
    noStroke();
    fill(bloodstream);
    ellipse(x+random(-25, 25), y+random(-15, 15), .1*xSpacing, .1*ySpacing);
    ellipse(x-random(25, 28), y+random(-15, 15), .25*xSpacing, .25*ySpacing);
    ellipse(x+random(-10, 20), y-random(18, 24), .3*xSpacing, .3*ySpacing);
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
