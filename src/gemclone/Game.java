package gemclone;

import java.util.ArrayList;

import org.newdawn.slick.*;

public class Game extends BasicGame {
  
  final int GRAVITY = 1;
  final int BULLET_SPEED = 3;
  final int BULLET_LIMIT = 3;
  final int LEFT = 1;
  final int RIGHT = 2;
  final int DOWN = 3;
  final int UP = 0;

  private Player player;
  public BlockMap map;
  
  private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
  
  private long t; // For printing the message for walking out of the screen.

  public Game() {
    super("Gem Clone");
  }

  public void init(GameContainer container) throws SlickException {
    container.setVSync(true);
    SpriteSheet sheet = new SpriteSheet("data/sprites.png", 31, 32);
    map = new BlockMap("data/map01.tmx");
    
    player = new Player(sheet);
    player.setPos(1, 1);
  
  }

  public void update(GameContainer container, int delta) throws SlickException {
    player.setUpdate(false);
    
    if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
      player.setDir(LEFT);
      player.move(-1, 0);
      player.setUpdate(true);
      if(player.onJump) {
        player.move(-2, 0);
      }
      while (entityCollisionWith()) {
        player.move(1, 0);
      }
    }
    
    if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
      player.setDir(RIGHT);
      player.move(1, 0);
      player.setUpdate(true);
      if(player.onJump) {
        player.move(2, 0);
      }
      while (entityCollisionWith()) {
        player.move(-1, 0);
      }
    }
    
    if(container.getInput().isKeyPressed(Input.KEY_UP)) {
      if(!player.onJump) {
        player.setDir(UP);
        player.vNow = -player.vIni;
        player.onJump = true;
      }
    }
    
    if(!hasGround()) {
      player.vNow += GRAVITY;
      player.move(0, player.vNow);
      if(entityCollisionWith()) {
        player.move(0, -player.vNow);
        player.vNow = 0;
        player.onJump = false;
      }
    }
    
    if(container.getInput().isKeyPressed(Input.KEY_SPACE)) {
      if(bullets.size() >= BULLET_LIMIT) {
        System.out.println("No more bullets for you!");
      }else {
        Bullet b = new Bullet("data/star.png", player.getX(), player.getY(), player.getDir());
        b.id = bullets.size();
        System.out.println("Fired Bullet " + b.id + " from " + b.x + ", " + b.y + ". Direction " + b.dir);
        bullets.add(b);
      }
    }
    
    for(int i = 0; i < bullets.size(); i++) {
      Bullet b = bullets.get(i);
      
      if(b.dir == LEFT) {
        b.move(-1 * BULLET_SPEED, 0);
      }
      if(b.dir == RIGHT) {
        b.move(1 * BULLET_SPEED, 0);
      }
      if(b.dir == UP) {
        b.move(0, -1 * BULLET_SPEED);
      }
      
      if(b.x < 0 || b.y < 0 || b.x > map.getWidth() || b.y > map.getHeight()) {
        System.out.println("Bullet " + b.id + " going out of bounds");
        bullets.remove(b);
      }
      
      if(bulletHit(b)) {
        System.out.println("Bullet " + b.id + " hit something!");
        bullets.remove(b);
      }
    }
  }
  
  public boolean hasGround() throws SlickException {
    for (int i = 0; i < BlockMap.entities.size(); i++) {
      Block entity = (Block) BlockMap.entities.get(i);
      if(player.getBounds().getMaxY() <= entity.poly.getMinY() && player.getBounds().intersects(entity.poly)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean bulletHit(Bullet b) throws SlickException {
    for(Object obj: BlockMap.entities) {
      Block block = (Block)obj;
      if(b.bounds.intersects(block.poly)) {
        return true;
      }
    }
    
    return false;
  }

  public boolean entityCollisionWith() throws SlickException {
    for (int i = 0; i < BlockMap.entities.size(); i++) {
      Block entity1 = (Block) BlockMap.entities.get(i);
      if (player.getBounds().intersects(entity1.poly)) {
        return true;
      }
    }
    
    if(player.getX() <= 0 || player.getX()+25 >= map.getWidth()) {
      long td = Math.abs(t - System.currentTimeMillis());
      if(td > 1000) {
       t = System.currentTimeMillis();
       System.out.println("Tsk tsk. Don't try to escape off the map.");
      }
      return true;
    }
    return false;
  }

  public void render(GameContainer container, Graphics g) {
    
    try {
      Image bg = new Image("data/bg.jpg");
      bg.draw(0,0);
    } catch (SlickException e) {
      e.printStackTrace();
    }
    
    BlockMap.tmap.render(0, 0);
    g.drawAnimation(player.getAnim(), player.getX(), player.getY());
    
    for(Bullet b: bullets) {
      b.draw();
    }

  }

  public static void main(String[] argv) throws SlickException {
    AppGameContainer container = new AppGameContainer(new Game(), 640, 480, false);
    container.start();
  }
}