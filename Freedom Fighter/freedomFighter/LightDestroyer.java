package freedomFighter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class LightDestroyer implements Alien {
	private Hitbox alien;
	private int dy, dx;
	private BufferedImage imgPad;
	private int health;
	private final int original_health = 10;
	private double armor;
	private int pof;
	private static final int original_pof = 250;
	private BufferedImage[] deathAnimation;
	private double deathFrame;
	private int maxDeathFrames;
	private int move_pattern;
	public static final int score_value = 10;
	
	private int y_dist_travelled;
	private boolean charging = true;
	
	private boolean in_square = false;
	private boolean up, down, left, right;
	public LightDestroyer (int x, int y, int pattern) {
		try {
			imgPad = GameDriver.addImage("Light Destroyer.png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), imgPad.getHeight());
		alien = new Hitbox(box);
        health = original_health;
        armor = 0.2;
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
				dy = 4;
				dx = 0;
				y_dist_travelled++;
			}
			else {
				if (charging) {
					if (y_dist_travelled < 130) {
						y_dist_travelled++;
					}
					else {
						dy = 0;
						dx = 4;
						charging = false;
					}
				}
				else {
					if ((alien.getX() < 0 && dx < 0) || (alien.getX() + imgPad.getWidth() > 640 && dx > 0)) {
						dx = -dx;
					}
				}
			}
		}
		else if (move_pattern == 2) {
			if (!in_square) {
				if (alien.getX() >= 20 && alien.getY() >= 20) in_square = true;
				else {
					dy = 4;
					down = true;
				}
			}
			else {
				if (alien.getY() + alien.getHeight() >= 480 && down) {
					dx = 4;
					dy = 0;
					down = false;
					right = true;
				}
				if (alien.getX() + alien.getWidth() >= 640 - 20 && right) {
					dx = 0;
					dy = -4;
					right = false;
					up = true;
				}
				if (alien.getY() <= 20 && up) {
					dx = -4;
					dy = 0;
					up = false;
					left = true;
				}
				if (alien.getX() <= 20 && left) {
					dx = 0;
					dy = 4;
					left = false;
					down = true;
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
		return "LightDestroyer";
	}
	public int getScore() {
		return score_value;
	}
	public boolean isTimeToFire() {
		return (pof <= 0);
	}
	public Projectile[] fireProjectiles() {
		Projectile[] projectile = new Projectile[2];
		projectile[0] = new BlueLaser( (int) (alien.getX() + alien.getWidth() / 2) - 25, (int) (alien.getY() + alien.getHeight() / 2));
		projectile[1] = new BlueLaser( (int) (alien.getX() + alien.getWidth() / 2) + 5, (int) (alien.getY() + alien.getHeight() / 2));
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
