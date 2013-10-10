package freedomFighter;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public class WorldEngine extends GameDriver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 548067431680847912L;
	// Gamestates
	private int gamestate;
	public static final int MAIN_MENU = 0;
	public static final int INGAME = 1;
	public static final int MISSION_BRIEFING = 2;
	public static final int STORE = 3;
	public static final int GAME_OVER = 4;
	public static final int MISSION_COMPLETE = 5;
	public static final int TRUE_END = 6;
	
	// Substates of primary gamestates
	private int ingameLevel;
	private Level currentLevel;
	
	// Images
	private BufferedImage briefing_screen, mainmenu_screen;
	private BufferedImage victory_screen = GameDriver.addImage("briefings\\victory.png");
	private int countUp, countUp2;
	private BufferedImage defeat_screen = GameDriver.addImage("briefings\\defeat.png");
	private BufferedImage endgame_screen = GameDriver.addImage("briefings\\true end.png");
	// Audio system
	private SoundDriver menuMusic, gameOver, victory, trueEnd; 
	
	// Indicates which fighter the player has chosen and other information about the player
	//private int playerChoice;  awaiting implementation
	static int dy, dx;
	static int health;
	static double armor;
	static double maxShield, shieldRechargeRate;
	static int shieldRechargeTime;
	static int damage, pof;
	static int total_score, score;
	
	private boolean keyPressed[];
	
	private Font futuraLight;
	
	
	public WorldEngine() {
		gamestate = MAIN_MENU;
		// temporarily defaulting players to the f22
		
		ingameLevel = 0; // -2 is testing level
		
		dy = 7;
		dx = 10;
		health = 200;
		damage = 2;
		pof = 100;
		
		keyPressed = new boolean[keys.length];
		resetKeyPressed();
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			futuraLight = Font.createFont(Font.TRUETYPE_FONT, new File("FuturaLight.ttf"));
			ge.registerFont(futuraLight);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	public void draw(Graphics2D g2) {
		// g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); 
		// g2.setFont(new Font("FuturaLight", Font.PLAIN, 18));
		// Main menu - players initialize game here by either starting a new game or loading an old one
		if (gamestate == MAIN_MENU) {
			if (mainmenu_screen == null) mainmenu_screen = addImage("splash.png");
			g2.drawImage(mainmenu_screen, 0, 0, null);
			// randomly loops the background menu music
			if (menuMusic == null) {
				String[] menuMusicSongs = new String[1];
				menuMusicSongs[0] = "Sound\\Defcon Theme 15.wav";
				menuMusic = new SoundDriver(menuMusicSongs);

				menuMusic.loop(0);
			}
			// enter key
			if (keys[10] && !keyPressed[10]) keyPressed[10] = true;
			if (!keys[10] && keyPressed[10]) { 
				gamestate = MISSION_BRIEFING;
				mainmenu_screen = null;
				System.out.println("Entering game.");
			}
		}
		else if (gamestate == INGAME) {
			// Level structure
			// Initializes level
			if (currentLevel == null) {
				switch (ingameLevel) { 
				case 0: currentLevel = new Tutorial();
						 System.out.println("Tutorial level loaded.");
						 break; 
				case 1: currentLevel = new LevelOne();
						System.out.println("Level 1 loaded.");
						break;
				case 2: currentLevel = new LevelTwo();
						System.out.println("Level 2 loaded.");
						break;
				case 3: currentLevel = new LevelThree();
						System.out.println("Level 3 loaded.");
						break;
				case 4: currentLevel = new LevelFour();
						System.out.println("Level 4 loaded.");
						break;
				case 5: currentLevel = new LevelFive();
						System.out.println("Level 5 loaded.");
						break; 
				case 6: currentLevel = new LevelSix();
						System.out.println("Level 6 loaded.");
						break;		
				case 7: currentLevel = new LevelSeven();
						System.out.println("Level 7 loaded.");
						break; 
				case 8: currentLevel = new LevelEight();
						System.out.println("Level 8 loaded.");
						break; 
				case 9: currentLevel = new LevelNine();
						System.out.println("Level 9 loaded.");
						break;
				case 10: currentLevel = new LevelTen();
						 System.out.println("Level 10 loaded.");
						 break;
				default: gamestate = Integer.MAX_VALUE;
						 break; // fatal error
				}
			}
			else {
				currentLevel.draw(g2, keys);
				if (currentLevel.isDone()) {
					if (!currentLevel.isDefeated()) {
						if (ingameLevel != 0) System.out.println("Level " + ingameLevel + " completed.");
						else System.out.println("Tutorial completed.");
						if (ingameLevel == 0) {
							ingameLevel++;
							currentLevel = null;
							gamestate = MISSION_BRIEFING;
						}
						else if (ingameLevel != 10) {
							ingameLevel++;
							gamestate = MISSION_COMPLETE;
						} else {
							ingameLevel = 1;
							gamestate = TRUE_END;
						}
					}
					else {
						System.out.println("Level " + ingameLevel + " failed.");
						currentLevel = null;
						gamestate = GAME_OVER;
					}
					System.gc();
				}
			}
			
		}
		else if (gamestate == MISSION_BRIEFING) {
			// Briefing screens are directly drawn within the world engine due to their simplicity
			/* switch (ingameLevel)
			 * case 1: g2.drawImage etc.
			 */
			if (briefing_screen == null) briefing_screen = addImage("briefings\\" +(ingameLevel - 1) +".png");
			g2.drawImage(briefing_screen, 0, 0, null);
			
			if (menuMusic == null) {
				String[] menuMusicSongs = new String[1];
				menuMusicSongs[0] = "Sound\\Defcon Theme 15.wav";
				menuMusic = new SoundDriver(menuMusicSongs);

				menuMusic.loop(0);
			}
			
			if (keys[0] && !keyPressed[0]) keyPressed[0] = true; // w key
			else if (keys[11] && !keyPressed[11]) keyPressed[11] = true; // space bar
			else if (keys[23] && !keyPressed[23]) keyPressed[23] = true; // [ to save
			else if (keys[24] && !keyPressed[24]) keyPressed[24] = true; // ] to load
			if (!keys[0] && keyPressed[0]) { // w key starts game
				resetKeyPressed();
				menuMusic.stop();
				menuMusic = null;
				gamestate = INGAME;
				briefing_screen = null;
				System.out.println("Entering cockpit.");
			}
			else if (!keys[11] && keyPressed[11]) { // space bar goes to store
				resetKeyPressed();
				gamestate = STORE;
				System.out.println("Entering hangar.");
			}
			else if (!keys[23] && keyPressed[23]) { // [ to save
				resetKeyPressed();
				save();
			}
			else if (!keys[24] && keyPressed[24]) { // ] to load
				resetKeyPressed();
				load();
			}
		}
		else if (gamestate == STORE) {
			// Store screens are directly drawn within the world engine due to their simplicity
			g2.setColor(Color.white);
			g2.fillRect(0, 0, 1280, 720);
			g2.drawImage(addImage("briefings\\hangar.png"), 0, 0, null);
			
			Font defaultFont = g2.getFont();
			
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setFont(new Font("FuturaLight", Font.PLAIN, 36));
			g2.setColor(Color.red);
			g2.drawString("Score: " + score, 600, 115);
			
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
			g2.setFont(defaultFont);
			g2.setColor(Color.black);
			// Damage
			g2.drawString("Damage [ y ]", 222, 250);
			g2.drawString("Reinforce the tips of autocannon", 170, 270);
			g2.drawString("rounds for increased damage.", 175, 282);
			
			g2.drawString("Current damage: " + damage, 210, 322);
			g2.drawString("After upgrade: " + (damage+2), 210, 342);
			g2.drawString("Cost: 100 score", 210, 362);
			// Period of Fire
			g2.drawString("Period of Fire [ u ]", 463, 250);
			g2.drawString("Reduce the time it takes for the", 427, 270);
			g2.drawString("twin autocannons to fire.", 445, 282);
			
			if (pof > 10) {
				g2.drawString("Current period of fire: " + pof, 445, 322);
				g2.drawString("After upgrade: " + (pof - 10), 445, 342);
				g2.drawString("Cost: " + 100 * (int)Math.pow(2, (100 - pof) / 10)+ " score", 445, 362);
			}
			else g2.drawString("Upgraded to minimum period of fire.", 415, 322);
			// Health
			g2.drawString("Health [ i ]", 740, 250);
			g2.drawString("Improve the hull integrity", 703, 270);
			g2.drawString("of the jet fighter.", 723, 282);
			
			g2.drawString("Current health: " + health, 710, 322);
			g2.drawString("After upgrade: " + (health + 10), 710, 342);
			g2.drawString("Cost: 10 score", 710, 362);
			// Armor
			g2.drawString("Armor [ o ]", 998, 250);
			g2.drawString("Reinforce armor to reduce", 955, 270);
			g2.drawString("incoming damage.", 975, 282);
			
			if (armor < .70d) {
				g2.drawString("Current armor: " + (int)Math.round(armor * 100) + "%", 970, 322);
				g2.drawString("After upgrade: " + (int)Math.round( (armor + 0.01d) * 100 )+ "%", 970, 342);
				g2.drawString("Cost: 100 score", 970, 362);
			}
			else g2.drawString("Upgraded to maximum armor.", 945, 322);
			// Shields
			g2.drawString("Shield [ h ]", 287, 560);
			g2.drawString("Strengthen a regenerating", 246, 580);
			g2.drawString("layer of protection.", 270, 592);
			
			g2.drawString("Current shield: " + maxShield, 265, 632);
			g2.drawString("After upgrade: " + (maxShield + 10), 265, 652);
			g2.drawString("Cost: 50 score", 265, 672);
			// Shield Recharge Rate
			g2.drawString("Shield Recharge Rate [ j ]", 570, 560);
			g2.drawString("Improve the rate at which", 571, 580);
			g2.drawString("the shield regenerates.", 577, 592);
			
			if (maxShield == 0) g2.drawString("Shield not yet purchased.", 570, 632);
			else {
				g2.drawString("Current rate: " +  Math.round(100*shieldRechargeRate + 0.05d)/100.0d, 580, 632);
				g2.drawString("After upgrade: " + Math.round( 100*(shieldRechargeRate + 0.05d) )/100.0d, 580, 652);
				g2.drawString("Cost: 500 score", 580, 672);
			}
			// Time to Shield Recharge
			g2.drawString("Shield Recharge Timer [ k ]", 888, 560);
			g2.drawString("Reduce the time it takes for the shield", 860, 580);
			g2.drawString("to begin recovering after being hit.", 870, 592);
			
			if (maxShield == 0 || shieldRechargeTime == 0) g2.drawString("Shield not yet purchased.", 895, 632);
			else if (shieldRechargeTime > 100) {
				g2.drawString("Current time: " + shieldRechargeTime, 900, 632);
				g2.drawString("After upgrade: " + (shieldRechargeTime - 100), 900, 652);
				g2.drawString("Cost: 1000 score", 900, 672);
			}
			else g2.drawString("Upgraded to minimum time.", 890, 632);
			
			
			if (keys[11] && !keyPressed[11]) keyPressed[11] = true; // space key
			else if (keys[16] && !keyPressed[16]) keyPressed[16] = true; // y for bullet damage
			else if (keys[17] && !keyPressed[17]) keyPressed[17] = true; // u for bullet rate of fire
			else if (keys[18] && !keyPressed[18]) keyPressed[18] = true; // i for health
			else if (keys[19] && !keyPressed[19]) keyPressed[19] = true; // o for armor
			else if (keys[20] && !keyPressed[20]) keyPressed[20] = true; // h for shields
			else if (keys[21] && !keyPressed[21]) keyPressed[21] = true; // j for rate that shields recharge
			else if (keys[22] && !keyPressed[22]) keyPressed[22] = true; // k for time after hit that shields begin recharging
			if (!keys[11] && keyPressed[11]) { // space goes back to briefing
				resetKeyPressed();
				gamestate = MISSION_BRIEFING;
				System.out.println("Returning to briefing.");
			}
			else if (!keys[16] && keyPressed[16]) { // y for bullet damage
				resetKeyPressed();
				if (score - 100 >= 0) {
					score -= 100;
					damage += 2;
				}
			}
			else if (!keys[17] && keyPressed[17]) { // u for bullet rate of fire
				resetKeyPressed();
				int pof_level = (100 - pof) / 10;
				if (score - 100 * (int)Math.pow(2, pof_level) >= 0 && pof > 10) {
					score -= 100 * (int)Math.pow(2, pof_level);
					pof -= 10;
				}
			}
			else if (!keys[18] && keyPressed[18]) { // i for health
				resetKeyPressed();
				if (score - 10 >= 0) {
					score -= 10;
					health += 10;
				}
			}
			else if (!keys[19] && keyPressed[19]) { // o for armor
				resetKeyPressed();
				if (score - 100 >= 0 && armor < .70d) {
					score -= 100;
					armor += 0.01d;
				}
			}
			else if (!keys[20] && keyPressed[20]) { // h for shields
				resetKeyPressed();
				if (score - 50 >= 0) {
					score -= 50;
					if (maxShield == 0) {
						shieldRechargeRate = 0.05d;
						shieldRechargeTime = 1000;
					}
					maxShield += 10;
				}
			}
			else if (!keys[21] && keyPressed[21]) { // j for rate that shields recharge
				resetKeyPressed();
				if (score - 500 >= 0 && maxShield != 0) {
					score -= 500;
					shieldRechargeRate += 0.05d;
				}
			}
			else if (!keys[22] && keyPressed[22]) { // k for time after hit that shields begin recharging
				resetKeyPressed();
				if (score - 1000 >= 0 && maxShield != 0 && shieldRechargeTime > 100) {
					score -= 1000;
					shieldRechargeTime -= 100;
				}	
			}
			
		}
		else if (gamestate == GAME_OVER) {
			g2.drawImage(defeat_screen, 0, 0, null);
			if (gameOver == null) {
				String[] gameOverSongs = new String[1];
				gameOverSongs[0] = "Sound\\KommSusserTod.wav"; // you done goof'd
				gameOver = new SoundDriver(gameOverSongs);
				gameOver.loop(0);
			}
			if (keys[10] && !keyPressed[10]) keyPressed[10] = true;
			if (!keys[10] && keyPressed[10]) { 
				keyPressed[10] = false;
				System.out.println("Returning to briefing.");
				gameOver.stop();
				gameOver = null;
				gamestate = MISSION_BRIEFING;
			}
		}
		else if (gamestate == MISSION_COMPLETE) {
			g2.drawImage(victory_screen, 0, 0, null);
			
			if (victory == null) {
				String[] victorySongs = new String[1];
				victorySongs[0] = "Sound\\rex.wav"; 
				victory = new SoundDriver(victorySongs);
				victory.loop(0);
			}
			
			g2.setColor(Color.white);
			if (countUp < currentLevel.getScore() - score) {
				g2.drawString("Score earned: " + countUp, 575, 320);
				if (currentLevel.getScore() - score > 1000) countUp += 10;
				else countUp++;
			}
			else if (countUp2 < currentLevel.getScore() ) {
				g2.drawString("Score earned: " + (countUp), 575, 320);
				g2.drawString("Total score: " + (countUp2), 575, 350);
				if ( currentLevel.getScore() > 1000) countUp2 += 10;
				else countUp2++;
			}
			else {
				g2.drawString("Score earned: " + (countUp), 575, 320);
				g2.drawString("Total score: " + (countUp2), 575, 350);
			}
			
			if (keys[10] && !keyPressed[10]) keyPressed[10] = true;
			if (!keys[10] && keyPressed[10]) { 
				keyPressed[10] = false;
				countUp = 0;
				countUp2 = 0;
				total_score += currentLevel.getScore() - score;
				score = currentLevel.getScore();
				currentLevel = null;
				victory.stop();
				victory = null;
				System.out.println("Returning to briefing.");
				gamestate = MISSION_BRIEFING;
			}
		}
		else if (gamestate == TRUE_END) {
			g2.drawImage(endgame_screen, 0, 0, null);
			// Congratulates players on victory and allows players to play again, wiping their data unless they saved
			if (trueEnd == null) {
				String[] trueEndSongs = new String[1];
				trueEndSongs[0] = "Sound\\dies irae.wav"; 
				trueEnd = new SoundDriver(trueEndSongs);
				trueEnd.loop(0);
			}
			g2.setColor(Color.white);
			if (countUp < currentLevel.getScore() - score) {
				g2.drawString("Score earned: " + countUp, 575, 320);
				if (currentLevel.getScore() - score > 1000) countUp += 10;
				else countUp++;
			}
			else if (countUp2 < currentLevel.getScore() ) {
				g2.drawString("Score earned: " + (countUp), 575, 320);
				g2.drawString("Total score: " + (countUp2), 575, 350);
				if ( currentLevel.getScore() > 1000) countUp2 += 10;
				else countUp2++;
			}
			else {
				g2.drawString("Score earned: " + (countUp), 575, 320);
				g2.drawString("Total score: " + (countUp2), 575, 350);
				g2.drawString("Total score earned this playthrough: " + total_score, 575, 380);
			}
			if (keys[10] && !keyPressed[10]) keyPressed[10] = true;
			if (!keys[10] && keyPressed[10]) { 
				keyPressed[10] = false;
				countUp = 0;
				countUp2 = 0;
				currentLevel = null;

				ingameLevel = 1;
				health = 200;
				armor = 0.0d;
				maxShield = 0;
				shieldRechargeRate = 0;
				shieldRechargeTime = 0;
				damage = 2;
				pof = 100;
				total_score = 0;
				score = 0;
				
				trueEnd.stop();
				trueEnd = null;
				System.out.println("Returning to briefing.");
				gamestate = MISSION_BRIEFING;
			}
		}
		else {
			// Indicates severe error
			g2.setColor(Color.white);
			Rectangle dude = new Rectangle(0, 0, 1280, 720);
			g2.fill(dude);
			g2.setColor(Color.RED);
			g2.drawString("DUDE YOU SCREWED UP THE GAME AND FOUND A BUG PLEASE CONTACT US ASAP", 500, 500);
		}
	}
	public void resetKeyPressed() {
		for (int i = 0; i < keyPressed.length; i++) {
			keyPressed[i] = false;
		}
	}
	public void save() {
		try {
			FileOutputStream saveFile = new FileOutputStream("freedomFigher.sav");
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			save.writeObject((Integer)health);
			save.writeObject((Double)armor);
			save.writeObject((Double)maxShield);
			save.writeObject((Double)shieldRechargeRate);
			save.writeObject((Integer)shieldRechargeTime);
			save.writeObject((Integer)damage);
			save.writeObject((Integer)pof);
			save.writeObject((Integer)total_score);
			save.writeObject((Integer)score);
			save.writeObject((Integer)ingameLevel);
			saveFile.close();
			System.out.println("Game successfully saved.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void load() {
		try {
			FileInputStream saveFile = new FileInputStream("freedomFigher.sav");
			ObjectInputStream save = new ObjectInputStream(saveFile);
			health = (int) save.readObject();
			armor = (double) save.readObject();
			maxShield = (double) save.readObject();
			shieldRechargeRate = (double) save.readObject();
			shieldRechargeTime = (int) save.readObject();
			damage = (int) save.readObject();
			pof = (int) save.readObject();
			total_score = (int) save.readObject();
			score = (int) save.readObject();
			ingameLevel = (int) save.readObject();
			saveFile.close();
			System.out.println("Game successfully loaded.");
		} catch (Exception e) {
			System.out.println("No save file found.");
		}
	}
}
