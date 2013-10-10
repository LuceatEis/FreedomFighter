package freedomFighter;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
public class Nyancat {
	private Hitbox ship;
	private int dy, dx;
	private BufferedImage[] imgPad;
	private int whichFrame, frameTimer;
	private int health;
	private double armor;
	private double shield, maxShield, shieldRechargeRate;
	private int shieldRechargeTimer, shieldRechargeTime;
	private BufferedImage[] deathAnimation;
	private double deathFrame;
	private int maxDeathFrames;
	
	private boolean hit;
	private int hitTimer = 20;
	private BufferedImage[] shields;
	public Nyancat (int x, int y, BufferedImage[] aimg) {
		imgPad = aimg;
		Rectangle[] box = new Rectangle[1];
		box[0] = new Rectangle(x, y, imgPad[0].getWidth(), imgPad[0].getHeight());
		ship = new Hitbox(box);
		dy = WorldEngine.dy;
        dx = WorldEngine.dx;
        health = WorldEngine.health;
        armor = WorldEngine.armor;
        shield = WorldEngine.maxShield;
        maxShield = WorldEngine.maxShield;
        shieldRechargeRate = WorldEngine.shieldRechargeRate;
        shieldRechargeTimer = 0;
        shieldRechargeTime = WorldEngine.shieldRechargeTime;
        deathFrame = 0;
        maxDeathFrames = 16;
        deathAnimation = new BufferedImage[maxDeathFrames];
        shields = new BufferedImage[2];
		try {
			for (int i = 1; i <= maxDeathFrames; i++) {
				deathAnimation[i-1] = ImageIO.read(new File("explosion\\Frame " + i + ".png"));
			}
			shields[0] = GameDriver.addImage("Shield (Neutral).png");
			shields[1] = GameDriver.addImage("Shield (Hit).png");
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
	}
	public Hitbox getHitbox() {
		return ship; 
	}
	public void moveAndDraw(Graphics2D g2, boolean isUp, boolean isDown, boolean isLeft, boolean isRight) {
		if(isUp && ship.getY() >= 480) {
			ship.translate(0, -dy);
		}	
		if(isDown && ship.getY()+imgPad[0].getHeight() <= 720) {
			ship.translate(0, dy);
		}
        if(isLeft && ship.getX() >= 20) {
        	ship.translate(-dx,0);
        }
        if(isRight && ship.getX()+imgPad[0].getWidth() <= 620) {
        	ship.translate(dx,0);
        }
        if (frameTimer < 7) frameTimer++;
        else {
        	frameTimer = 0;
        	if (whichFrame < 4) whichFrame++;
        	else whichFrame = 0;
        }
        g2.drawImage(imgPad[whichFrame], null, (int)ship.getX(), (int)ship.getY());
        if (shield > 0) {
        	if (!hit) {
        		g2.drawImage(shields[0], (int)ship.getX() + 4, (int)ship.getY() - 4, null);
        	}
        	else {
        		if (hitTimer > 0) {
        			g2.drawImage(shields[1], (int)ship.getX() + 4, (int)ship.getY() - 4, null);
            		hitTimer--;
        		}
        		else hit = false;
        	}
        }
        // Handles shield recharging, which happens on a frame by frame basis
        if (shieldRechargeTimer >= 1) shieldRechargeTimer--; 
        else {
        	if (shield < maxShield) {
        		if (shield + shieldRechargeRate > maxShield) shield = maxShield;
        		else shield += shieldRechargeRate;
        	}
        }
	}
	public void damage(int damageDealt) {
		if (shield > 0) {
			if (shield - damageDealt > 0) shield-= damageDealt;
			else {
				health -= (damageDealt - shield) * (1 - armor);
				shield = 0;
			}
		}
		else health -= damageDealt * (1 - armor);
		if (health < 0) health = 0;
		// Resets shield recharge timer due to damage being taken
		shieldRechargeTimer = shieldRechargeTime;
		// Notifies moveAndDraw to flash shields
		hit = true;
		hitTimer = 20;
	}
	public boolean isDead() {
		return (health <= 0);
	}
	public boolean isDoneDying() {
		return (deathFrame > maxDeathFrames && isDead());
	}
	public void drawDeath(Graphics2D g2) {
		if (deathFrame < maxDeathFrames) g2.drawImage(deathAnimation[(int)deathFrame], null, (int)ship.getX(), (int)ship.getY());
		deathFrame+=0.25;
	}
	public int getHealth() {
		return health;
	}
	public double getShield() {
		return shield;
	}
	public int getShieldRechargeTimer() {
		return shieldRechargeTimer;
	}
}
