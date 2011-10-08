package gemclone;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;

/**
 * @author Yasith Vidanaarachchi
 * 
 */
public class Unit {

  private int hp;
  private String name;
  private float vx, vy;
  private int width, height;
  private float jumpv;

  protected Animation anims[] = new Animation[4];

  private Polygon bounds;

  private float x, y;
  private int dir;

  public Unit() {
    
  }

  public boolean onJump() {
    return vy != 0; // if vy is not zero then the unit is on a jump
  }
  
  public void resetBounds() {
    bounds.setX(x);
    bounds.setY(y);
  }
  
  public void setPos(float x, float y) {
    this.x = x;
    this.y = y;
    resetBounds();
  }
  
  public void move(float x, float y) {
    this.x += x;
    this.y += y;
    resetBounds();
  }
  
  public Animation getAnim() {
    return anims[dir];
  }
  
  public void setUpdate(boolean b) {
    anims[dir].setAutoUpdate(b);
  }
  
  public void draw() {
    anims[dir].draw(x, y);
  }
  
  /**
   * @return the bounding Polygon of the Unit
   */
  public Polygon getBounds() {
    return bounds;
  }

  /**
   * @param bounds
   *          the bounding Polygon of the Unit
   */
  public void setBounds(Polygon bounds) {
    this.bounds = bounds;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public int getDir() {
    return dir;
  }

 public void setDir(int dir) {
    this.dir = dir;
  }

  public int getHp() {
    return hp;
  }

  public void setHp(int hp) {
    this.hp = hp;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the velocity on the X axis
   */
  public float getVx() {
    return vx;
  }

  /**
   * @param vx the velocity on the Y axis
   */
  public void setVy(float vy) {
    this.vy = vy;
  }

  /**
   * @return the velocity on the Y axis
   */
  public float getVy() {
    return vy;
  }

  /**
   * @param vx the velocity on the X axis
   */
  public void setVx(float vx) {
    this.vx = vx;
  }
  
  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * @return the Jumping Velocity of the Unit
   */
  public float getJumpv() {
    return jumpv;
  }

  /**
   * @param jumpv the Jumping Velocity of the Unit
   */
  public void setJumpv(float jumpv) {
    this.jumpv = jumpv;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }
}
