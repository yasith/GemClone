package gemclone;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Polygon;

public class Player{
  
  private Polygon bounds;
  private float x = 340;
  private float y = 240;
  private Animation anims[] = new Animation[4];
  private int dir;
  
  private boolean onJump = false;
  private int vNow;
  private int vIni = 15;
  
  
  public Player(SpriteSheet sheet) {
    for (int dir = 0; dir < 4; dir ++) {
      Animation anim = new Animation();
      for (int frame = 0; frame < 3; frame++) {
        anim.addFrame(sheet.getSprite(frame, dir), 150);
      }
      anim.setAutoUpdate(false);
      this.anims[dir] = anim;
    }
    setBounds(new Polygon(new float[] { x, y, x + 32,
        y, x + 32, y + 32, x, y + 32 }));
    
    this.dir = 0;
  }

  public Polygon getBounds() {
    return bounds;
  }

  public void setBounds(Polygon bounds) {
    this.bounds = bounds;
  }
  
  public void setPos(float x, float y) {
    this.x = x;
    this.y = y;
    resetBounds();
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
  
  public float getX() {
    return x;
  }
  
  public float getY() {
    return y;
  }
  
  public Animation getAnim() {
    return anims[dir];
  }
  
  public void setDir(int dir) {
    this.dir = dir;
  }
  
  public int getDir() {
    return dir;
  }
  
  public void setUpdate(boolean b) {
    anims[dir].setAutoUpdate(b);
  }

  public void draw() {
    anims[dir].draw(x, y);
  }

  public boolean onJump() {
    return onJump;
  }

  public void setJump(boolean onJump) {
    this.onJump = onJump;
  }

  public int vNow() {
    return vNow;
  }

  public void setVNow(int vNow) {
    this.vNow = vNow;
  }

  public int getIni() {
    return vIni;
  }

  public void setIni(int vIni) {
    this.vIni = vIni;
  }
}
