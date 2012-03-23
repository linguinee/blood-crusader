class Vial
{
  float hematocrit, plasma;
  
  Vial()
  {
  }
  
  void redFluid(float hematocrit)
  {
    noStroke();
    fill(rbc);
    ellipseMode(CORNER);
    ellipse(liquidxOffset, height-(2.5*ySpacing), xSpacing, ySpacing);
    rectMode(CORNERS);
    rect(liquidxOffset, height-liquidyOffset, liquidxOffset+xSpacing, height-liquidyOffset-(hematocrit*liquidUnitHeight));
  }
  
  void yellowFluid(float plasma)
  {
    noStroke();
    fill(yellow);
    rectMode(CORNERS);
    rect(liquidxOffset, liquidyOffset, liquidxOffset+xSpacing, liquidyOffset+(plasma*liquidUnitHeight));
  }
}
