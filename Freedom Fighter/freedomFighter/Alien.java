package freedomFighter;

import java.awt.Graphics2D;

interface Alien {
	Hitbox getHitbox();
	void moveAndDraw(Graphics2D g2);
	void damage(int damageDealt);
	boolean isDead();
	boolean isDoneDying();
	void drawDeath(Graphics2D g2);
	int getHealth();
	String toString();
	int getScore();
	boolean isTimeToFire();
	Projectile[] fireProjectiles();
	int getMaxHealth();
	int getDX();
	int getDY();
}
