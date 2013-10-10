package freedomFighter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
// The most generic bullet
public class Bullet implements Projectile{
	private Hitbox bullet;
	private int dy;
	private BufferedImage imgPad;
	private static final int bulletstrength = WorldEngine.damage;
	public Bullet (int x, int y) {
		try {
			imgPad = GameDriver.addImage("bullet.png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), imgPad.getHeight() );
		bullet = new Hitbox(box);
		dy = -20;
	}
	public void moveAndDraw(Graphics2D g2) {
		bullet.translate(0, dy);
		g2.drawImage(imgPad, (int)bullet.getX(), (int)bullet.getY(), null);
	}
	public Hitbox getHitbox() {
		return bullet;
	}
	public int getDamage() {
		return bulletstrength;
	}
	public boolean isPenetrating() {
		return false;
	}

}