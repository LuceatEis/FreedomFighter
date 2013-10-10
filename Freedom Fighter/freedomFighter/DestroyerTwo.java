package freedomFighter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class DestroyerTwo implements Alien {
	private Hitbox alien;
	private int dy, dx;
	private BufferedImage imgPad;
	private int health;
	private final int original_health = 40;
	private double armor;
	private int pof;
	private static final int original_pof = 300;
	private BufferedImage[] deathAnimation;
	private double deathFrame;
	private int maxDeathFrames;
	private int move_pattern;
	public static final int score_value = 30;

	private int y_dist_travelled;
	private boolean charging = true;
	
	private boolean on_right_edge = false;
	private boolean on_left_edge = false;
	public DestroyerTwo (int x, int y, int pattern) {
		try {
			imgPad = GameDriver.addImage("Destroyer2.png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), imgPad.getHeight());
		alien = new Hitbox(box);
        health = original_health;
        armor = 0.3;
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
				dx = 0;
				y_dist_travelled++;
			}
			else {
				if (charging) {
					if (y_dist_travelled < 20) {
						y_dist_travelled++;
					}
					else {
						dy = 0;
						dx = 0;
						charging = false;
					}
				}
			}
		}
		else if (move_pattern == 2 || move_pattern == 3) {
			if (alien.getY() < 0) {
				dy = 3;
				dx = 0;
				y_dist_travelled++;
			}
			else {
				if (charging) { // heading downward
					if (y_dist_travelled < 20) {
						y_dist_travelled++;
					}
					else {
						dy = 3;
						dx = 0;
						charging = false;
					}
				} else { // the X-shape
					int y_limit = 400;
					if (move_pattern == 3) y_limit = 480; 
					if (alien.getX() < 320) { // left side of screen
						if (alien.getX() < 20 && !on_left_edge) {
							dy = 3; dx = 0;
							on_left_edge = true;
						}
						else if (alien.getY() + alien.getHeight() > y_limit) {
							dy = -1; dx = 2;
							on_left_edge = false;
						}  
					} else { // right side of screen
						if (alien.getX() + alien.getWidth() > 640 - 20 && !on_right_edge) {
							dy = 3; dx = 0;
							on_right_edge = true;
						}
						else if (alien.getY() + alien.getHeight() > y_limit) {
							dy = -1; dx =-2;
							on_right_edge = false;
						}
					}
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
		return "Destroyer Type Two";
	}
	public int getScore() {
		return score_value;
	}
	public boolean isTimeToFire() {
		return (pof <= 0);
	}
	public Projectile[] fireProjectiles() {
		Projectile[] projectile = new Projectile[3];
		projectile[0] = new OrangeLaser( (int) (alien.getX() + alien.getWidth() / 2) - 30, (int) (alien.getY() + alien.getHeight() / 2), -Math.PI/6); //left
		projectile[1] = new OrangeLaser( (int) (alien.getX() + alien.getWidth() / 2) - 15, (int) (alien.getY() + alien.getHeight() / 2)); //middle
		projectile[2] = new OrangeLaser( (int) (alien.getX() + alien.getWidth() / 2), (int) (alien.getY() + alien.getHeight() / 2), Math.PI/6); //right
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
