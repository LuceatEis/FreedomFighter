package freedomFighter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Cruiser implements Alien {
	private Hitbox alien;
	private int dy, dx;
	private BufferedImage imgPad;
	private int health;
	private final int original_health = 200;
	private double armor;
	private int pof;
	private static final int original_pof = 200;
	private BufferedImage[] deathAnimation;
	private double deathFrame;
	private int maxDeathFrames;
	private int move_pattern;
	public static final int score_value = 100;

	public Cruiser (int x, int y, int pattern) {
		try {
			imgPad = GameDriver.addImage("Cruiser.png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), imgPad.getHeight());
		alien = new Hitbox(box);
        health = original_health;
        armor = 0.5;
		dy = 2;
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
			if (alien.getY() > -200 && dy!=0) {
				dy = 0;
			}
		}
		else if (move_pattern == 2) {
			if (alien.getY() > 50 && dy!=0) {
				dy = 0;
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
		return "Cruiser";
	}
	public int getScore() {
		return score_value;
	}
	public boolean isTimeToFire() {
		return (pof <= 0);
	}
	public Projectile[] fireProjectiles() {
		Projectile[] projectile = null;
		projectile = new Projectile[5];
		projectile[0] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 25, (int) (alien.getY() + alien.getHeight() - 5), -Math.PI/3); 
		projectile[1] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 20, (int) (alien.getY() + alien.getHeight() - 3), -Math.PI/6); 
		projectile[2] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 15, (int) (alien.getY() + alien.getHeight()) ); 
		projectile[3] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 10, (int) (alien.getY() + alien.getHeight() - 3), Math.PI/6); 
		projectile[4] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 05, (int) (alien.getY() + alien.getHeight() - 5), Math.PI/3); 
		
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
