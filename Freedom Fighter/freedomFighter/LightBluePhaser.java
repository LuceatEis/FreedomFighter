package freedomFighter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
// The most generic bullet
public class LightBluePhaser implements Projectile{
	private Hitbox orb;
	private int dy, dx;
	private BufferedImage imgPad;
	private static final int damage = 20;
	public LightBluePhaser (int x, int y) {
		try {
			imgPad = GameDriver.addImage("projectiles\\Alien Phaser Attack (Light Blue).png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), imgPad.getHeight() );
		orb = new Hitbox(box);
		dy = 15;
	}
	public void moveAndDraw(Graphics2D g2) {
		orb.translate(dx, dy);
		g2.drawImage(imgPad, (int)orb.getX(), (int)orb.getY(), null);
	}
	public Hitbox getHitbox() {
		return orb;
	}
	public int getDamage() {
		return damage;
	}
	public boolean isPenetrating() {
		return false;
	}

}