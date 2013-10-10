package freedomFighter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
// The most generic bullet
public class LightBlueOrb implements Projectile{
	private Hitbox orb;
	private int dy, dx;
	private BufferedImage imgPad;
	private static final int damage = 1;
	
	private double theta;
	private boolean hasReoriented = false;
	public LightBlueOrb (int x, int y) {
		try {
			imgPad = GameDriver.addImage("projectiles\\Alien Orb Attack (Light Blue).png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), imgPad.getHeight() );
		orb = new Hitbox(box);
		dy = 5;
	}
	public LightBlueOrb(int x, int y, double theta) {
		// "Theta" is the angle from a line drawn downward, perpendicular to the y-axis and parallel to the x-axis, with positive values being toward the right
		try {
			imgPad = GameDriver.addImage("projectiles\\Alien Orb Attack (Light Blue).png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), imgPad.getHeight() );
		orb = new Hitbox(box);
		dy = (int) (5 * (Math.sin(theta + Math.PI/2)) );
		dx = (int) (5 * (Math.cos(theta + Math.PI/2)) );
		this.theta = theta;
	}
	public void reorient(int playerX) {
		if (theta != 0 && !hasReoriented && Math.abs(playerX - ( orb.getX() + orb.getWidth() )/ 2) < 10) {
			dx = 0;
			dy = 5;
		}
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