package freedomFighter;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
public class Tutorial implements Level {
	
	// Player data
	private int score;
	private int pof = WorldEngine.pof;
	
	// Audio system
	private SoundDriver bgMusic;
	
	// Entities
	private ArrayList<Alien> aliens;
	private ArrayList<Alien> dyingAliens;
	private ArrayList<Projectile> alienFire;
	
	private Nyancat player;
	private ArrayList<Projectile> playerFire;
	
	// Completion
	private boolean completion;
	
	// Backgrounds
	private BufferedImage background;
	private double time_y0;
	
	// Timers
	private int timer1, timer2;
	private static final int minute = 10000;
	
	// FPS
	private int fps;
	long lastTimeChecked;
	int frames;
	
	private BufferedImage[] tutorialPages;
	private int tutorialPage = 0;
	private boolean enterKeyPressed, YKeyPressed;
	private boolean cupcakeSpawned;
	
	public Tutorial() {
		BufferedImage[] nyan = new BufferedImage[6];
		try {
			for (int i = 0; i < nyan.length; i++) {
				nyan[i] = GameDriver.addImage("nyancat\\"+ i +".png");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		player = new Nyancat(280, 480, nyan);
		score = WorldEngine.score;
		completion = false;
		
		String[] bgMusicSongs = new String[1];
		bgMusicSongs[0] = "Sound\\nyancat.wav";
		bgMusic = new SoundDriver(bgMusicSongs);
		
		aliens = new ArrayList<Alien>();
		dyingAliens = new ArrayList<Alien>();
		alienFire = new ArrayList<Projectile>();
		playerFire = new ArrayList<Projectile>();
		try {
			background = GameDriver.addImage("background\\space.png");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		tutorialPages = new BufferedImage[7];
		try {
			for (int i = 0; i < tutorialPages.length; i++) {
				tutorialPages[i] = GameDriver.addImage("nyancat\\tutorial" + i + ".png");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		lastTimeChecked = System.nanoTime();
		
	}
	public void draw(Graphics2D g2, boolean[] keys) {
		checkMusic();
		drawBackground(g2);
		spawnCupcake();
		clearFire();
		if (keys[11] && pof == 0) fireAutocannon();
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

		drawInfoPanel(g2, keys[10], keys[16]);
		if( completion == true ) {
			bgMusic.stop();
			bgMusic = null;
		}
	}
	public void drawBackground(Graphics2D g2) {
		g2.drawImage(background, 0, (int)time_y0, null);
		g2.drawImage(background, 0, (int)time_y0 - 1440, null);
		time_y0 += 1.0d;
		if (time_y0 >= 1440) time_y0 = 0;
	}
	public void drawInfoPanel(Graphics2D g2) {}
	public void drawInfoPanel(Graphics2D g2, boolean enterKey, boolean YKey) {
		if (enterKey && !enterKeyPressed) enterKeyPressed = true;
		if (enterKeyPressed && !enterKey) {
			if (tutorialPage < 6) tutorialPage++;
			else completion = true;
			enterKeyPressed = false;
		}
		
		if (YKey && !YKeyPressed) YKeyPressed = true;
		if (YKeyPressed && !YKey) {
			completion = true;
			YKeyPressed = false;
		}
		
		g2.drawImage(tutorialPages[tutorialPage], 640, 0, null);
		
		
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
		
		g2.setColor(Color.red);
		if (fps > 99) g2.drawString("FPS: " + fps, 1228, 715);
		else g2.drawString("FPS: " + fps, 1237, 715);
		if (score > 99999) g2.drawString("Score: " + score, 1204, 15);
		else if (score > 9999) g2.drawString("Score: " + score, 1209, 15);
		else if (score > 999) g2.drawString("Score: " + score, 1214, 15);
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
	public void spawnCupcake() {
		if (tutorialPage == 4 && !cupcakeSpawned) {
			aliens.add(new Cupcake(-300, 100));
			cupcakeSpawned = true;
		}
	}
	public void fireAutocannon() {
		Bullet firedL = new Bullet((int)player.getHitbox().getX(), (int)player.getHitbox().getY());
		Bullet firedR = new Bullet((int)player.getHitbox().getX() + (int)player.getHitbox().getWidth(), (int)player.getHitbox().getY());
		playerFire.add(firedL);
		playerFire.add(firedR);
	
		pof = WorldEngine.pof;
	}public void fireAliens() {
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
		for (int i = 0; i < alienFire.size(); i++) {
			if (player.getHitbox().intersects(alienFire.get(i).getHitbox())) {
				player.damage(alienFire.get(i).getDamage());
				if (!alienFire.get(i).isPenetrating()) {
					alienFire.remove(i);
					i--;
				}
			}
		}
	}
}