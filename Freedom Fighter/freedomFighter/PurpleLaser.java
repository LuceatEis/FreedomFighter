package freedomFighter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
// The most generic bullet
public class PurpleLaser implements Projectile{
	private Hitbox orb;
	private int dy, dx;
	private BufferedImage imgPad;
	private static final int damage = 20;
	
	private double theta;
	private boolean hasReoriented = false;
	public PurpleLaser (int x, int y) {
		try {
			imgPad = GameDriver.addImage("projectiles\\Alien Laser (Purple).png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), imgPad.getHeight() );
		orb = new Hitbox(box);
		dy = 10;
	}
	public PurpleLaser(int x, int y, double theta) {
		// "Theta" is the angle from a line drawn downward, perpendicular to the y-axis and parallel to the x-axis, with positive values being toward the right
		try {
			if (theta == Math.PI/3) imgPad = GameDriver.addImage("projectiles\\Alien Laser (Purple) Far Left.png");
			else if (theta == Math.PI/6) imgPad = GameDriver.addImage("projectiles\\Alien Laser (Purple) Left.png");
			else if (theta == -Math.PI/6) imgPad = GameDriver.addImage("projectiles\\Alien Laser (Purple) Right.png");
			else if (theta == -Math.PI/3) imgPad = GameDriver.addImage("projectiles\\Alien Laser (Purple) Far Right.png");
			else imgPad = GameDriver.addImage("projectiles\\Alien Laser (Purple).png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), imgPad.getHeight() );
		orb = new Hitbox(box);
		dy = (int) (10 * (Math.sin(theta + Math.PI/2)) );
		dx = (int) (10 * (Math.cos(theta + Math.PI/2)) );
		this.theta = theta;
	}
	public void reorient(int playerX) {
		if (theta != 0 && !hasReoriented && Math.abs(playerX - ( orb.getX() + orb.getWidth() )/ 2) < 10) {
			dx = 0;
			dy = 10;
			try {
				imgPad = null;
				imgPad = GameDriver.addImage("projectiles\\Alien Laser (Purple).png");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			Rectangle[] box = new Rectangle[1];
			box[0] = new Rectangle((int)orb.getX(), (int)orb.getY(), imgPad.getWidth(), imgPad.getHeight() );
			orb = new Hitbox(box);
			hasReoriented = true;
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