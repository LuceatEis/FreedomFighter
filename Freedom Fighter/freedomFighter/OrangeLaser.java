package freedomFighter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
// The most generic bullet
public class OrangeLaser implements Projectile{
	private Hitbox orb;
	private int dy, dx;
	private BufferedImage imgPad;
	private static final int damage = 10;
	public OrangeLaser (int x, int y) {
		try {
			imgPad = GameDriver.addImage("projectiles\\Alien Laser (Orange).png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), imgPad.getHeight() );
		orb = new Hitbox(box);
		dy = 10;
	}
	public OrangeLaser(int x, int y, double theta) {
		// "Theta" is the angle from a line drawn downward, perpendicular to the y-axis and parallel to the x-axis, with positive values being toward the right
		try {
			if (theta == Math.PI/6) imgPad = GameDriver.addImage("projectiles\\Alien Laser (Orange) Left.png");
			else imgPad = GameDriver.addImage("projectiles\\Alien Laser (Orange) Right.png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), imgPad.getHeight() );
		orb = new Hitbox(box);
		dy = (int) (10 * (Math.sin(theta + Math.PI/2)) );
		dx = (int) (10 * (Math.cos(theta + Math.PI/2)) );
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