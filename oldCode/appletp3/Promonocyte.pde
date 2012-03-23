// acute monocytic leukemia (AML)

class Promonocyte implements Cell
{
  float x, y;
  
  Promonocyte()
  {
    init();
  }
  
  void display()
  {
    noStroke();
    fill(lightpurple);
    ellipseMode(CENTER);
    ellipse(x, y, 2.3*xSpacing, 2.2*ySpacing);
    strokeWeight(2);
    stroke(darkpurple);
    fill(blastnucleus);
    ellipse(x-6, y, 1.6*xSpacing, 1.7*ySpacing);
    fill(darkpurple);
    ellipse(x-6, y, 1.6*xSpacing, .15*ySpacing);
    ellipse(x, y+8, .1*xSpacing, ySpacing);
    ellipse(x+11, y-12, .25*xSpacing, .2*ySpacing);
    curve(x-22, y+22, x-22, y-16, x, y+20, x-10, y+50);
    curve(x-16, y+50, x-16, y+20, x+3, y-10, x+16, y-10);
    curve(x-50, y-15, x-22, y-18, x-5, y, x-3, y+50);
    line(x-2, y-24, x+17, y+5);
    strokeWeight(1);
    fill(platelet);
    ellipse(x+random(-14, 14), y+random(-14, 14), .25*xSpacing, .25*ySpacing);
    ellipse(x+random(-14, 14), y+random(-14, 14), .2*xSpacing, .2*ySpacing);
    noStroke();
    fill(bloodstream);
    ellipse(x-random(25, 28), y+random(-15, 15), .2*xSpacing, .2*ySpacing);
    ellipse(x+random(-10, 20), y-random(18, 24), .25*xSpacing, .25*ySpacing);
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
