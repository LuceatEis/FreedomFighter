package freedomFighter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Boss implements Alien {
	private Hitbox alien;
	private int dy, dx;
	private BufferedImage imgPad;
	private int health;
	private final int original_health = 25000;
	private double armor;
	private int pof;
	private static final int original_pof = 200;
	private BufferedImage[] deathAnimation;
	private double deathFrame;
	private int maxDeathFrames;
	public static final int score_value = 10000;
	public Boss (int x, int y) {
		try {
			imgPad = GameDriver.addImage("Boss.png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Rectangle[] box = new Rectangle[2];
		box[0] = new Rectangle(x, y, imgPad.getWidth(), 310);
		box[1] = new Rectangle(x + 112, y + 310, 289, 289);
		alien = new Hitbox(box);
        health = original_health;
        armor = 0.8;
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
	}
	public Hitbox getHitbox() {
		return alien; 
	}
	public void moveAndDraw(Graphics2D g2) {
		if (dy != 0 && alien.getY() > -60) {
			dy = 0;
			dx = 1;
		}
		if (alien.getX() < -100 || alien.getX() + imgPad.getWidth() > 640 + 10) {
			dx = -dx;
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
		return "Boss";
	}
	public int getScore() {
		return score_value;
	}
	public boolean isTimeToFire() {
		return (pof <= 0);
	}
	public Projectile[] fireProjectiles() {
		Projectile[] projectile = null;
		int yposition = (int) alien.getY();
		if (health < original_health / 4) {
			projectile = new Projectile[18];
			projectile[0] = new PurplePhaser((int) (alien.getX() + alien.getWidth() / 2) - 15, (int) (alien.getY() + 160), Math.PI/4);
			projectile[1] = new PurplePhaser((int) (alien.getX() + alien.getWidth() / 2) - 15, (int) (alien.getY() + 160), - Math.PI/4);
			
			// left flank
			projectile[2] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 95, (yposition) + 60, Math.PI/4);
			projectile[3] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 95, (yposition) + 120, Math.PI/4);
			projectile[4] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 95, (yposition) + 180, Math.PI/4);
			projectile[5] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 95, (yposition) + 240, Math.PI/4);
			projectile[6] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 95, (yposition) + 300, Math.PI/4);
	
			projectile[7] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 05, yposition + 120, Math.PI/3); 
			projectile[8] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 05, yposition + 240, Math.PI/3); 
			projectile[9] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 05, yposition + 360, Math.PI/3); 
			// right flank
			projectile[10] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 65, (yposition) + 60, - Math.PI/4);
			projectile[11] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 65, (yposition) + 120, - Math.PI/4);
			projectile[12] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 65, (yposition) + 180, - Math.PI/4);
			projectile[13] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 65, (yposition) + 240, - Math.PI/4);
			projectile[14] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 65, (yposition) + 300, - Math.PI/4);
			
			projectile[15] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 25, yposition + 120, -Math.PI/3); 
			projectile[16] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 25, yposition + 240, -Math.PI/3); 
			projectile[17] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 25, yposition + 360, -Math.PI/3); 
		} else if (health < original_health / 2) {
			projectile = new Projectile[16];
			projectile[0] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 95, (yposition) + 60, Math.PI/4);
			projectile[1] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 95, (yposition) + 120, Math.PI/4);
			projectile[2] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 95, (yposition) + 180, Math.PI/4);
			projectile[3] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 95, (yposition) + 240, Math.PI/4);
			projectile[4] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 95, (yposition) + 300, Math.PI/4);

			projectile[5] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 05, yposition + 120, Math.PI/3); 
			projectile[6] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 05, yposition + 240, Math.PI/3); 
			projectile[7] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 05, yposition + 360, Math.PI/3); 
			
			projectile[8] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 65, (yposition) + 60, - Math.PI/4);
			projectile[9] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 65, (yposition) + 120, - Math.PI/4);
			projectile[10] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 65, (yposition) + 180, - Math.PI/4);
			projectile[11] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 65, (yposition) + 240, - Math.PI/4);
			projectile[12] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 65, (yposition) + 300, - Math.PI/4);
			
			projectile[13] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 25, yposition + 120, -Math.PI/3); 
			projectile[14] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 25, yposition + 240, -Math.PI/3); 
			projectile[15] = new PurpleLaser( (int) (alien.getX() + alien.getWidth() / 2) - 25, yposition + 360, -Math.PI/3); 
		} else {
			projectile = new Projectile[10];
			projectile[0] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 95, (yposition) + 60, Math.PI/4);
			projectile[1] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 95, (yposition) + 120, Math.PI/4);
			projectile[2] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 95, (yposition) + 180, Math.PI/4);
			projectile[3] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 95, (yposition) + 240, Math.PI/4);
			projectile[4] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 95, (yposition) + 300, Math.PI/4);
			
			projectile[5] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 65, (yposition) + 60, - Math.PI/4);
			projectile[6] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 65, (yposition) + 120, - Math.PI/4);
			projectile[7] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 65, (yposition) + 180, - Math.PI/4);
			projectile[8] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 65, (yposition) + 240, - Math.PI/4);
			projectile[9] = new LightBlueOrb((int) (alien.getX() + alien.getWidth() / 2) - 65, (yposition) + 300, - Math.PI/4);
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
