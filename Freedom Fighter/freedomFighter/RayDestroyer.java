package freedomFighter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class RayDestroyer implements Alien {
	private Hitbox alien;
	private int dy, dx;
	private BufferedImage imgPad;
	private int health;
	private final int original_health = 50;
	private double armor;
	private int pof;
	private static final int original_pof = 600;
	private BufferedImage[] deathAnimation;
	private double deathFrame;
	private int maxDeathFrames;
	private int move_pattern;
	public static final int score_value = 50;

	private int y_dist_travelled;
	private boolean charging = true;
	private int x_shiver;
	public RayDestroyer (int x, int y, int pattern) {
		try {
			imgPad = GameDriver.addImage("Ray Destroyer.png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), imgPad.getHeight());
		alien = new Hitbox(box);
        health = original_health;
        armor = 0.4;
        pof = original_pof;
        deathFrame = 0;
        maxDeathFrames = 16;
        deathAnimation = new BufferedImage[maxDeathFrames];
		try {
			for (int i = 1; i <= maxDeathFrames; i++) {
				deathAnimation[i-1] = ImageIO.read(new File("explosion\\Frame " + i + ".png"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		move_pattern = pattern;
	}
	public Hitbox getHitbox() {
		return alien; 
	}
	public void moveAndDraw(Graphics2D g2) {
		if (move_pattern == 1) {
			if (alien.getY() < 0) {
				dy = 3;
				y_dist_travelled++;
			}
			else {
				if (charging) {
					if (y_dist_travelled < 100) {
						y_dist_travelled++;
					}
					else {
						dy = 0;
						charging = false;
					}
				}
			}
		}
		else if (move_pattern == 2 || move_pattern == 3) {
			if (charging) {
				int y_limit = 175;
				if (move_pattern == 3) y_limit = 200;
				if (y_dist_travelled < y_limit) {
					dy = 3;
					y_dist_travelled++;
					pof = original_pof;
				}
				else {
					dy = 0; dx = 3;
					charging = false;
					pof = original_pof;
				}
			}
			else { // the dance macabre
				if (x_shiver < 50) x_shiver++;
				else {
					dx = -dx;
					x_shiver = 0;
				}
			}
		}
		alien.translate(dx, dy);
		g2.drawImage(imgPad, (int)alien.getX(), (int)alien.getY(), null);
		if (pof > 0) pof--; // weapon timer decrementer
	}
	public void damage(int damageDealt) {
		health -= damageDealt * (1 - armor);
	}
	public boolean isDead() {
		return (health <= 0);
	}
	public boolean isDoneDying() {
		return (deathFrame > maxDeathFrames && isDead());
	}
	public void drawDeath(Graphics2D g2) {
		if (deathFrame < maxDeathFrames) g2.drawImage(deathAnimation[(int)deathFrame], (int)alien.getX(), (int)alien.getY(), imgPad.getWidth(), imgPad.getHeight(), null);
		deathFrame+=0.25;
	}
	public int getHealth() {
		return health;
	}
	public String toString() {
		return "Ray Destroyer";
	}
	public int getScore() {
		return score_value;
	}
	public boolean isTimeToFire() {
		return (pof <= 0);
	}
	public Projectile[] fireProjectiles() {
		Projectile[] projectile = new Projectile[1];
		projectile[0] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 10, (int) (alien.getY() + alien.getHeight() / 2)); 
		pof = original_pof;
		return projectile;
	}
	public int getMaxHealth() {
		return original_health;
	}
	public int getDX() {
		return dx;
	}
	public int getDY() {
		return dy;
	}
}
