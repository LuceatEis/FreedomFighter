package freedomFighter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
// The most generic bullet
public class PurplePhaser implements Projectile{
	private Hitbox orb;
	private int dy, dx;
	private BufferedImage imgPad;
	private static final int damage = 50;
	
	private double theta;
	private boolean hasReoriented = false;
	public PurplePhaser (int x, int y) {
		try {
			imgPad = GameDriver.addImage("projectiles\\Alien Phaser Attack (Purple).png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), imgPad.getHeight() );
		orb = new Hitbox(box);
		dy = 15;
	}
	public PurplePhaser (int x, int y, double theta) {
		try {
			if (theta == Math.PI/4) imgPad = GameDriver.addImage("projectiles\\Alien Phaser Attack (Purple) Left.png");
			else imgPad = GameDriver.addImage("projectiles\\Alien Phaser Attack (Purple) Right.png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), imgPad.getHeight() );
		orb = new Hitbox(box);
		dy = (int) (15 * (Math.sin(theta + Math.PI/2)) );
		dx = (int) (15 * (Math.cos(theta + Math.PI/2)) );
		this.theta = theta;
	}
	// pass in the x-coordinate of the player's center; the phaser will reorient itself to move downward toward the player if their centers match
	public void reorient(int playerX) {
		if (theta != 0 && !hasReoriented && Math.abs(playerX - ( orb.getX() + orb.getWidth() )/ 2) < 10) {
			dx = 0;
			dy = 15;
			try {
				imgPad = null;
				imgPad = GameDriver.addImage("projectiles\\Alien Phaser Attack (Purple).png");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			Rectangle[] box = new Rectangle[1];
			box[0] = new Rectangle((int)orb.getX() + 30, (int)orb.getY(), imgPad.getWidth(), imgPad.getHeight() );
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