package freedomFighter;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
public class LevelSeven implements Level {
	
	// Player data
	private int score;
	private int pof = WorldEngine.pof;
	
	// Audio system
	private SoundDriver bgMusic;
	
	// Entities
	private ArrayList<Alien> aliens;
	private ArrayList<Alien> dyingAliens;
	private ArrayList<Projectile> alienFire;
	
	private Player player;
	private ArrayList<Projectile> playerFire;
	
	// Completion
	private boolean completion;
	
	// Backgrounds
	private BufferedImage background;
	private BufferedImage cloud_layer;
	private double time_y0, time_y3;
	
	// Timers
	private int timer1, timer2, timer3;
	private static final int minute = 10000;
	
	// FPS
	private int fps;
	long lastTimeChecked;
	int frames;
	
	public LevelSeven() {
		player = new Player(280, 480, GameDriver.addImage("Eurofighter Typhoon.png"));
		score = WorldEngine.score;
		completion = false;
		
		String[] bgMusicSongs = new String[1];
		bgMusicSongs[0] = "Sound\\asia.wav";
		bgMusic = new SoundDriver(bgMusicSongs);
		
		aliens = new ArrayList<Alien>();
		dyingAliens = new ArrayList<Alien>();
		alienFire = new ArrayList<Projectile>();
		playerFire = new ArrayList<Projectile>();
		try {
			cloud_layer = GameDriver.addImage("background\\Cloud Layer.png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			background = GameDriver.addImage("background\\Kashgar.jpg");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		lastTimeChecked = System.nanoTime();
	}
	public void draw(Graphics2D g2, boolean[] keys) {
		checkMusic();
		drawBackground(g2);
		spawnStage1();
		spawnStage2();
		spawnStage3();
		clearFire();
		if (keys[11] && pof == 0) fireAutocannon();
		// missile fire goes here
		fireAliens();
		damageAliens();
		damagePlayer();
		// Draw Alien projectiles
		for (Projectile x : alienFire) {
			x.moveAndDraw(g2);
		}
		// Draw player projectiles
		for (Projectile x : playerFire) {
			x.moveAndDraw(g2);
		}
		// Movement of aliens and player (player on top of aliens if they happen to intersect)
		for (int i = 0; i < aliens.size(); i++) {
			aliens.get(i).moveAndDraw(g2); // each alien object is responsible for its own movement patterns!
		}
		// Dying aliens dealt with here
		for (int i = 0; i < dyingAliens.size(); i++) {
			dyingAliens.get(i).drawDeath(g2);
			if (dyingAliens.get(i).isDoneDying()) {
				dyingAliens.remove(i);
				i--;
			}
		}
		if (player.isDead()) {
			player.drawDeath(g2);
		}
		else {
			player.moveAndDraw(g2, keys[12], keys[13], keys[14], keys[15]);
		}
		
		// Weapons fire decrementers 
		if (pof > 0) pof--;
		
		// FPS check
		frames++;
		if (System.nanoTime() - lastTimeChecked >= 1000000000L) {
			fps = frames;
			frames = 0;
			lastTimeChecked = System.nanoTime();
		}
		
		drawInfoPanel(g2);
		if( (isDefeated() && player.isDoneDying()) || ( (timer3 >= minute) && aliens.size() == 0 )  ) {
			completion = true;
			bgMusic.stop();
			bgMusic = null;
		}
	}
	public void drawBackground(Graphics2D g2) {
		g2.drawImage(background, 0, (int)time_y0, null);
		g2.drawImage(background, 0, (int)time_y0 - 1440, null);
		time_y0+=0.5d;
		g2.drawImage(cloud_layer, 0, (int)time_y3, null); 
		g2.drawImage(cloud_layer, 0, (int)time_y3 - 720, null); 
		time_y3+=2.0;
		if (time_y3 >= 720) time_y3 = 0;
		if (time_y0 >= 1440) time_y0 = 0;
	}
	public void drawInfoPanel(Graphics2D g2) {
		Rectangle panel = new Rectangle(640, 0, 640, 720);
		g2.setColor(Color.lightGray);
		g2.fill(panel);
		
		g2.setColor(Color.red);
		g2.drawString("Health", 675, 65);
		g2.setColor(Color.white);
		Rectangle healthBar = new Rectangle(750, 35, 400, 50);
		g2.fill(healthBar);
		g2.setColor(Color.red);
		Rectangle currentHealth = new Rectangle(750, 35, (int)(400 * (double)(player.getHealth()*1.0d/WorldEngine.health)), 50);
		g2.fill(currentHealth);
		g2.setColor(Color.black);
		g2.drawString("" + player.getHealth(), 940, 65);
		
		if (WorldEngine.armor > 0) {
			g2.setColor(Color.darkGray);
			g2.drawString("Armor: " + (int)Math.round(WorldEngine.armor * 100) + "% of incoming damage absorbed", 675, 115);
		}
		
		if (WorldEngine.maxShield != 0) {
			g2.setColor(Color.blue);
			g2.drawString("Shields", 675, 165);
			g2.setColor(Color.white);
			Rectangle shieldBar = new Rectangle(750, 135, 400, 50);
			g2.fill(shieldBar);
			g2.setColor(Color.blue);
			Rectangle currentShield = new Rectangle(750, 135, (int)(400 * (player.getShield()/WorldEngine.maxShield)), 50);
			g2.fill(currentShield);
			g2.setColor(Color.black);
			g2.drawString("" + (int)player.getShield(), 940, 165);
			g2.setColor(Color.blue);
			g2.drawString("Shield recharge in: " + player.getShieldRechargeTimer(), 675, 215);
		}
		
		g2.setColor(Color.red);
		if (fps > 99) g2.drawString("FPS: " + fps, 1228, 715);
		else g2.drawString("FPS: " + fps, 1237, 715);
		if (score > 999) g2.drawString("Score: " + score, 1212, 15);
		else if (score > 99) g2.drawString("Score: " + score, 1220, 15);
		else if (score > 9) g2.drawString("Score: " + score, 1225, 15);
		else g2.drawString("Score: " + score, 1230, 15);
	}
	public boolean isDone() {
		return completion;
	}
	public boolean isDefeated() {
		if (player.getHealth() <= 0) return true;
		else return false;
	}
	public int getScore() {
		return score;
	}
	public void clearFire() {
		for (int j = 0; j < playerFire.size(); j++) {
			if (playerFire.get(j).getHitbox().getY() + playerFire.get(j).getHitbox().getHeight()<0) {
				playerFire.remove(j);
				j--;
			}
		}
		
		for (int j = 0; j < alienFire.size(); j++) {
			if (alienFire.get(j).getHitbox().getY() + alienFire.get(j).getHitbox().getHeight()<0) {
				alienFire.remove(j);
				j--;
			}
		}
	}
	public void checkMusic() {
		if (!bgMusic.isPlaying()) {
			Random rnd = new Random();
			bgMusic.play(rnd.nextInt(bgMusic.getNumOfClips()));
		}
	}
	public void spawnStage1() {
		if (timer1 < minute / 2) {
			timer1++;
			if (aliens.size() == 0) {
				aliens.add(new DestroyerTwo(20, -200, 2));
				aliens.add(new DestroyerTwo(20, -400, 2));
				aliens.add(new DestroyerTwo(20, -600, 2));
				aliens.add(new DestroyerTwo(20, -800, 2));
				aliens.add(new DestroyerTwo(20, -1000, 2));
				aliens.add(new DestroyerTwo(20, -1200, 2));
				aliens.add(new DestroyerTwo(20, -1400, 2));
				aliens.add(new DestroyerTwo(20, -1600, 2));
				aliens.add(new DestroyerTwo(20, -1800, 2));
				aliens.add(new DestroyerTwo(20, -2000, 2));
			}
		}
	}
	public void spawnStage2() {
		if (timer1 >= minute / 2 && timer2 < minute / 2) {
			timer2++;
			if (aliens.size() == 0) {
				aliens.add(new RayDestroyer(28, -500, 2));
				aliens.add(new RayDestroyer(181, -500, 2));
				aliens.add(new RayDestroyer(334, -500, 2));
				aliens.add(new RayDestroyer(28, -300, 2));
				aliens.add(new RayDestroyer(181, -300, 2));
				aliens.add(new RayDestroyer(334, -300, 2));
			}
		}
	}
	public void spawnStage3() {
		if (timer2 >= minute / 2 && timer3 < minute) {
			timer3++;
			if (aliens.size() == 0) {
				aliens.add(new DestroyerTwo(20, -200, 3));
				aliens.add(new DestroyerTwo(20, -400, 3));
				aliens.add(new DestroyerTwo(20, -600, 3));
				aliens.add(new DestroyerTwo(20, -800, 3));
				aliens.add(new DestroyerTwo(20, -1000, 3));
				aliens.add(new DestroyerTwo(20, -1200, 3));
				aliens.add(new DestroyerTwo(20, -1400, 3));
				aliens.add(new DestroyerTwo(20, -1600, 3));
				aliens.add(new DestroyerTwo(20, -1800, 3));
				aliens.add(new DestroyerTwo(20, -2000, 3));
				
				aliens.add(new RayDestroyer(28, -400, 1));
				aliens.add(new RayDestroyer(156, -200, 1));
				aliens.add(new RayDestroyer(284, -400, 1));
				aliens.add(new RayDestroyer(412, -200, 1));
			}
		}
	}
	public void fireAutocannon() {
		Bullet firedL = new Bullet((int)player.getHitbox().getX(), (int)player.getHitbox().getY());
		Bullet firedR = new Bullet((int)player.getHitbox().getX() + (int)player.getHitbox().getWidth(), (int)player.getHitbox().getY());
		playerFire.add(firedL);
		playerFire.add(firedR);
	
		pof = WorldEngine.pof;
	}
	public void fireAliens() {
		for (Alien x: aliens) {
			if (x.isTimeToFire()) {
				for (Projectile y : (x.fireProjectiles()) ) {
					alienFire.add(y);
				}
			}
		}
	}
	public void damageAliens() {
		for (int i = 0; i < aliens.size(); i++) {
			for (int j = 0; j < playerFire.size(); j++) {
				if (aliens.get(i).getHitbox().intersects(playerFire.get(j).getHitbox())) {
					aliens.get(i).damage(playerFire.get(j).getDamage());
					if(!playerFire.get(j).isPenetrating()) {
						playerFire.remove(j);
						j--;
					}
				}
			}
			if(aliens.get(i).isDead()) {
				dyingAliens.add(aliens.get(i));
				score += aliens.get(i).getScore();
				aliens.remove(i);
				i--;
			}
		}
	}
	public void damagePlayer() {
		try {
			for (int i = 0; i < alienFire.size(); i++) {
				if (player.getHitbox().intersects(alienFire.get(i).getHitbox())) {
					player.damage(alienFire.get(i).getDamage());
					if (!alienFire.get(i).isPenetrating()) {
						alienFire.remove(i);
						i--;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}