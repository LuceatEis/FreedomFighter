package freedomFighter;
import java.awt.Graphics2D;
public interface Projectile {
	Hitbox getHitbox();
	void moveAndDraw(Graphics2D g2);
	int getDamage();
	boolean isPenetrating(); /* i.e. that the projectile is currently capable of penetrating ships. irrelevant for alien projectiles. may only have limited # of penetrations
	 						  * since this method is only called upon impact with an alien ship, a projectile may modify itself when this method is called */
}
