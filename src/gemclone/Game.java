package gemclone;

import java.util.ArrayList;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Polygon;

public class Game extends BasicGame {

  final String BG_IMAGE = "data/bg0.png";
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
  private ArrayList<Creep> creeps = new ArrayList<Creep>();
  private Sound BSound;
  private Sound ShootSound;
  private Sound BGSound;

  private long t; // For printing the message for walking out of the screen.

  public Game() {
    super("Gem Clone");
  }

  private void addCreeps(int count) throws SlickException {
    for (int i = 0; i < count; i++) {
      Creep c = new Creep(new SpriteSheet("data/sprites.png", 31, 32));
      // c.setPos(Math.random(), y)
      float cx = (float) Math.random() * map.getWidth();
      float cy = (float) Math.random() * map.getHeight();
      Polygon poly = new Polygon(new float[] { cx, cy, cx + 32, cy, cx + 32,
          cy + 32, cx, cy + 32 });

      while (collidesBlocks(poly) || collideWithCreep(poly)) {
        cx = (float) Math.random() * map.getWidth();
        cy = (float) Math.random() * map.getHeight();
        poly = new Polygon(new float[] { cx, cy, cx + 32, cy, cx + 32, cy + 32,
            cx, cy + 32 });
      }
      c.setPos(cx, cy);
      creeps.add(c);
    }
  }

  public void init(GameContainer container) throws SlickException {
    container.setVSync(true);
    SpriteSheet sheet = new SpriteSheet("data/sprites.png", 31, 32);
    map = new BlockMap("data/map01.tmx");
    BSound = new Sound("data/bullet.ogg"); // TODO: REMOVE THIS BLOCK FROM THIS
                                           // PLACE TO BULLET CLASS
    ShootSound = new Sound("data/shoot.ogg"); // TODO: REMOVE THIS BLOCK FROM
                                              // THIS PLACE ORGANIZE THIS!
    BGSound = new Sound("data/bg.ogg");
    BGSound.loop();

    player = new Player(sheet);
    player.setPos(1, 1);

    addCreeps(4);
  }

  public void update(GameContainer container, int delta) throws SlickException {
    player.setUpdate(false);

    updateCreeps();

    if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
      player.setDir(LEFT);
      player.move(-1, 0);
      player.setUpdate(true);
      if (player.onJump()) {
        player.move(-2, 0);
      }
      while (entityCollisionWith(player)) {
        player.move(1, 0);
      }
    }

    if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
      player.setDir(RIGHT);
      player.move(1, 0);
      player.setUpdate(true);
      if (player.onJump()) {
        player.move(2, 0);
      }
      while (entityCollisionWith(player)) {
        player.move(-1, 0);
      }
    }

    if (container.getInput().isKeyPressed(Input.KEY_UP)) {
      if (!player.onJump()) {
        player.setDir(UP);
        player.setVNow(-player.getIni());
        player.setJump(true);
      }
    }

    for (Player p : creeps) {
      if (!entityCollisionWith(p)) {
        p.setVNow(p.vNow() + GRAVITY);
        p.move(0, p.vNow());
        if (entityCollisionWith(p)) {
          p.move(0, -p.vNow());
          p.setVNow(0);
          p.setJump(false);
        }
      }
    }

    if (!entityCollisionWith(player)) {
      player.setVNow(player.vNow() + GRAVITY);
      player.move(0, player.vNow());
      if (entityCollisionWith(player)) {
        player.move(0, -player.vNow());
        player.setVNow(0);
        player.setJump(false);
      }
    } else {
      System.out.println("Happened");
    }

    if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
      if (bullets.size() >= BULLET_LIMIT) {
        System.out.println("No more bullets for you!");
      } else {
        Bullet b = new Bullet("data/star.png", player.getX(), player.getY(),
            player.getDir());
        b.id = bullets.size();
        System.out.println("Fired Bullet " + b.id + " from " + b.x + ", " + b.y
            + ". Direction " + b.dir);
        bullets.add(b);
        ShootSound.play();
      }
    }

    for (int i = 0; i < bullets.size(); i++) {
      Bullet b = bullets.get(i);

      if (b.dir == LEFT) {
        b.move(-1 * BULLET_SPEED, 0);
      }
      if (b.dir == RIGHT) {
        b.move(1 * BULLET_SPEED, 0);
      }
      if (b.dir == UP) {
        b.move(0, -1 * BULLET_SPEED);
      }

      if (b.x < 0 || b.y < 0 || b.x > map.getWidth() || b.y > map.getHeight()) {
        System.out.println("Bullet " + b.id + " going out of bounds");
        bullets.remove(b);
      }

      if (bulletHit(b)) {
        System.out.println("Bullet " + b.id + " hit something!");
        bullets.remove(b);
        BSound.play();
      }
    }
  }

  private void updateCreeps() {
    for (int i = 0; i < creeps.size(); i++) {
      Creep c = creeps.get(i);

      float dirdif = player.getX() - c.getX();

      if (dirdif < 0) {
        movePlayer(c, -1, 0);
        c.setDir(LEFT);
      }else {
        movePlayer(c, 1, 0);
        c.setDir(RIGHT);
      }

    }
  }

  private void movePlayer(Player p, int x, int y) {
    try {
      p.move(x, y);
      p.setUpdate(true);
      if (p.onJump()) {
        p.move(-2, 0);
      }
      while (entityCollisionWith(p)) {
        p.move(1, 0);
      }
    } catch (SlickException ex) {
      ex.printStackTrace();
    }
  }

  public boolean hasGround(Player p) throws SlickException {
    for (int i = 0; i < BlockMap.entities.size(); i++) {
      Block entity = (Block) BlockMap.entities.get(i);
      if (p.getBounds().getMaxY() <= entity.poly.getMinY()
          && p.getBounds().intersects(entity.poly)) {
        return true;
      }
    }
    return false;
  }

  public boolean bulletHit(Bullet b) throws SlickException {
    for (Object obj : BlockMap.entities) {
      Block block = (Block) obj;
      if (b.bounds.intersects(block.poly)) {
        return true;
      }
    }

    for (int i = 0; i < creeps.size(); i++) {
      Player p = creeps.get(i);
      if (p.getBounds().intersects(b.bounds)) {
        creeps.remove(p);
        return true;
      }
    }

    return false;
  }

  public boolean entityCollisionWith(Player p) throws SlickException {
    if (collidesBlocks(p.getBounds())) {
      return true;
    }

    if (p.getX() <= 0 || p.getX() + 25 >= map.getWidth()) {
      long td = Math.abs(t - System.currentTimeMillis());
      if (td > 1000) {
        t = System.currentTimeMillis();
        System.out.println("Tsk tsk. Don't try to escape off the map.");
      }
      return true;
    }
    return false;
  }

  public boolean collidesBlocks(Polygon poly) {
    for (int i = 0; i < BlockMap.entities.size(); i++) {
      Block entity1 = (Block) BlockMap.entities.get(i);
      if (poly.intersects(entity1.poly)) {
        return true;
      }
    }

    return false;
  }

  public boolean collideWithCreep(Polygon poly) {
    for (int i = 0; i < creeps.size(); i++) {
      Player p = creeps.get(i);
      if (poly.intersects(p.getBounds())) {
        return true;
      }
    }
    return false;
  }

  public void render(GameContainer container, Graphics g) {

    try {
      Image bg = new Image(BG_IMAGE);
      bg.draw(0, 0);
    } catch (SlickException e) {
      e.printStackTrace();
    }

    BlockMap.tmap.render(0, 0);
    // g.drawAnimation(player.getAnim(), player.getX(), player.getY());
    player.draw();

    for (Bullet b : bullets) {
      b.draw();
    }
    for (Creep c : creeps) {
      c.draw();
    }

  }

  public static void main(String[] argv) throws SlickException {
    AppGameContainer container = new AppGameContainer(new Game(), 640, 480,
        false);
    container.start();
  }
}