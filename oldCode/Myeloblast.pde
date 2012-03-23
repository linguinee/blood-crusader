// acute myeloid leukemia (AML)

class Myeloblast implements Cell
{
  float x, y;
  
  Myeloblast()
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
    fill(blastnucleus);
    ellipse(x-2, y+2, 1.7*xSpacing, 1.8*ySpacing);
    stroke(darkpurple);
    fill(platelet);
    ellipse(x+random(-15, 15)-2, y+random(-15, 15)+1, .15*xSpacing, .15*ySpacing);
    ellipse(x+random(-15, 15)-2, y+random(-15, 15)+1, .25*xSpacing, .25*ySpacing);
    ellipse(x+random(-15, 15)-2, y+random(-15, 15)+1, .2*xSpacing, .2*ySpacing);
    line(x-14, y-26, x, y-29);
    line(x+27, y-3, x+23, y+14);
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
