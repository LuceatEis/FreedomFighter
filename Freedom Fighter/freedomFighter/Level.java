package freedomFighter;
import java.awt.Graphics2D;
// The parent class of all levels within the game itself
public interface Level {
	void draw(Graphics2D g2, boolean[] keys);
	void drawBackground(Graphics2D g2);
	void drawInfoPanel(Graphics2D g2);
	boolean isDone(); // Level must be able to report if the player has completed the level
	boolean isDefeated(); // Level must specify if it is over because the player was destroyed
	int getScore();
}
