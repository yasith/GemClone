package gemclone;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

class Bullet{
  
  public Polygon bounds;
  public float x;
  public float y;
  public int dir;
  private Animation anim = new Animation();
  public int id;
  
  public Bullet(String filename, float x, float y, int dir) throws SlickException {
    Image img = new Image(filename);
    this.dir = dir;
    this.x = x;
    this.y = y;
    
    anim.addFrame(img, 100);
    Image img2 = img.copy();
    img2.rotate((float) 180);
    anim.addFrame(img2, 100);
    
    bounds = new Polygon(new float[] { x, y, x + img.getWidth(),
        y, x + img.getWidth(), y + img.getHeight(), x, y + img.getHeight() });
    
    //System.out.println("Reached!"); 
  }
  
  public Polygon getBounds() {
    return bounds;
  }
  
  private void resetBounds() {
    bounds.setX(x);
    bounds.setY(y);
  }

  public void move(float x, float y) {
    this.x += x;
    this.y += y;
    resetBounds();
  }
  
  public void draw() {
    anim.draw(x, y);
  }
}