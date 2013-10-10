package freedomFighter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Frigate implements Alien {
	private Hitbox alien;
	private int dy, dx;
	private BufferedImage imgPad;
	private int health;
	private final int original_health = 5;
	private double armor;
	private int pof;
	private static final int original_pof = 200;
	private BufferedImage[] deathAnimation;
	private double deathFrame;
	private int maxDeathFrames;
	private int move_pattern;
	public static final int score_value = 3;
	
	private int y_dist_travelled;
	private boolean charging = true;
	
	private boolean inview = false;
	public Frigate (int x, int y, int pattern) {
		try {
			imgPad = GameDriver.addImage("Frigate.png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), imgPad.getHeight());
		alien = new Hitbox(box);
		dy = 0;
        dx = 4;
        health = original_health;
        armor = 0;
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
			if ((alien.getX() < 10 && dx < 0) || (alien.getX() + imgPad.getWidth() > 640 - 10 && dx > 0)) {
				dx = -dx;
			}
		}
		else if (move_pattern == 2) {
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
						if (alien.getX() < 320) dx = -4;
						else dx = 4;
						charging = false;
					}
				}
				else {
					// left side
					if (alien.getX() + imgPad.getWidth() < 320) {
						if ((alien.getX() < 10 && dx < 0) || (alien.getX() + imgPad.getWidth() > 320 - 10 && dx > 0)) {
							dx = -dx;
						}
					}
					// right side
					else {
						if ((alien.getX() < 320 + 10 && dx < 0) || (alien.getX() + imgPad.getWidth() > 640 - 10 && dx > 0)) {
							dx = -dx;
						}
					}
				}
			}
		}
		else if (move_pattern == 3) {
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
		else if (move_pattern == 4) {
			if (!inview) {
				if (alien.getX() + alien.getWidth() < 100) dx = 4;
				else if (alien.getX() > 640 - 100) dx = -4;
				else inview = true;
			}
			else if ((alien.getX() < 10) || (alien.getX() + imgPad.getWidth() > 640 - 10)) {
				dx = -dx;
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
		return "Frigate";
	}
	public int getScore() {
		return score_value;
	}
	public boolean isTimeToFire() {
		return (pof <= 0);
	}
	public Projectile[] fireProjectiles() {
		Projectile[] projectile = new Projectile[2];
		if (move_pattern == 1) {
			projectile[0] = new LightBlueOrb( (int) (alien.getX() + alien.getWidth() / 2) - 15, (int) (alien.getY() + alien.getHeight() / 2) );
			projectile[1] = new LightBlueOrb( (int) (alien.getX() + alien.getWidth() / 2) + 15, (int) (alien.getY() + alien.getHeight() / 2) );
		}
		else {
			projectile[0] = new LightBlueOrb( (int) (alien.getX() + alien.getWidth() / 2) - 15, (int) (alien.getY() + alien.getHeight() / 2), -Math.PI/8 );
			projectile[1] = new LightBlueOrb( (int) (alien.getX() + alien.getWidth() / 2) + 15, (int) (alien.getY() + alien.getHeight() / 2), Math.PI/8 );
		}
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
