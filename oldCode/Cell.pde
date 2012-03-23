// Rbc, Platelet, Lymphoblast, Monoblast, Myeloblast, Promonocyte, Promyelocyte, and Tcell all implement Cell

interface Cell
{
  void display();
  boolean move();
  float returnY();
  float returnX();
  void init();
}
