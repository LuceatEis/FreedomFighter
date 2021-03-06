package freedomFighter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Corvette implements Alien {
	private Hitbox alien;
	private int dy, dx;
	private BufferedImage imgPad;
	private int health;
	private final int original_health = 2;
	private double armor;
	private int pof;
	private static final int original_pof = 40;
	private BufferedImage[] deathAnimation;
	private double deathFrame;
	private int maxDeathFrames;
	private int move_pattern;
	public static final int score_value = 1;
	public Corvette (int x, int y, int pattern) {
		try {
			imgPad = GameDriver.addImage("Corvette.png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), imgPad.getHeight());
		alien = new Hitbox(box);
		dy = 0;
        dx = 5;
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
			// left side
			if (alien.getX() + imgPad.getWidth() < 320) {
				if ((alien.getX() < 10 && dx < 0) || (alien.getX() + imgPad.getWidth() > 320 - 5 && dx > 0)) {
					dx = -dx;
				}
			}
			//right side
			else {
				if ((alien.getX() < 320 + 5 && dx < 0) || (alien.getX() + imgPad.getWidth() > 640 - 10 && dx > 0)) {
					dx = -dx;
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
		return "Corvette";
	}
	public int getScore() {
		return score_value;
	}
	public boolean isTimeToFire() {
		return (pof <= 0);
	}
	public Projectile[] fireProjectiles() {
		Projectile[] projectile = new Projectile[1];
		projectile[0] = new LightBlueOrb( (int) (alien.getX() + alien.getWidth() / 2), (int) (alien.getY() + alien.getHeight() / 2) );
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
