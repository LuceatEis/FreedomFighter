package freedomFighter;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Rectangle;
public class Laser {
	private int lasertimer;
	private double laserframe;
	private BufferedImage[] laser; 
	private Rectangle hitbox, oldHitbox;
	private static final int damage = 5;
	public Laser(int x, int y) {
		laser = new BufferedImage[34];
		for (int i = 0; i < laser.length; i++) {
			laser[i] = GameDriver.addImage("Laser\\Laser "+(i+1)+".png");
		}
		lasertimer = 500;
		laserframe = 0;
		hitbox = new Rectangle(x, y, laser[0].getWidth(), laser[0].getHeight());
		oldHitbox = (Rectangle) hitbox.clone();
	}
	public void draw(Graphics2D g2, Alien boss) {
		int bottomY = (int)(boss.getHitbox().getY() + boss.getHitbox().getHeight() - 80);
		int bottomX = (int)boss.getHitbox().getX() + 140;
		if (lasertimer == 0 && laserframe == 0) {
			g2.drawImage(laser[(int)laserframe], bottomX, bottomY, null);
			laserframe+=0.50d;
			lasertimer = 500;
			
			hitbox = oldHitbox;
			hitbox.setLocation(bottomX, bottomY);
			oldHitbox.setLocation(bottomX, bottomY);
		}
		else if (laserframe > 0) {
			if (Math.round(laserframe) < laser.length) {
				g2.drawImage(laser[(int)laserframe], bottomX, bottomY, null);
				laserframe+=0.50d;
			}
			else laserframe = 0;

			hitbox.setLocation(bottomX, bottomY);
			oldHitbox.setLocation(bottomX, bottomY);
		}
		else {
			hitbox = null;
			oldHitbox.setLocation(bottomX, bottomY);
		}
		System.out.println(oldHitbox.getX() + ", " + oldHitbox.getY() + ", "+ oldHitbox.getWidth() + ", " + oldHitbox.getHeight());
		if (lasertimer > 0) lasertimer --;
	}
	public int getLasertimer() {
		return lasertimer;
	}
	public void setLasertimer(int time) {
		lasertimer = time;
	}
	public double getLaserframe() {
		return laserframe;
	}
	public void setLaserframe(double frame) {
		laserframe = frame;
	}
	public int getDamage() {
		return damage;
	}
	public Hitbox getHitbox() {
		Rectangle[] input = new Rectangle[1];
		if (hitbox != null) {
			input[0] = (Rectangle) hitbox.clone();
			return new Hitbox(input);
		} else return null;
	}
}
