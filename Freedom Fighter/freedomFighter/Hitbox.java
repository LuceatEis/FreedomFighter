package freedomFighter;
import java.awt.Rectangle;
public class Hitbox {
	private Rectangle[] parts;
	public Hitbox(Rectangle[] input) {
		parts = new Rectangle[input.length];
		for (int i = 0; i < parts.length; i++) {
			parts[i] = input[i];
		}
	}
	public Hitbox(Hitbox cloningSubject) {
		Rectangle[] cloningParts = cloningSubject.getParts();
		parts = new Rectangle[cloningParts.length];
		for (int i = 0; i < parts.length; i++) {
			parts[i] = (Rectangle) cloningParts[i].clone();
		}
	}
	public boolean intersects(Hitbox otherHitbox) {
		Rectangle[] otherParts = otherHitbox.getParts();
		for (int i = 0; i < parts.length; i++) {
			for (int j = 0; j < otherParts.length; j++) {
				if (parts[i].intersects(otherParts[j])) {
					return true;
				}
			}
		}
		return false;
	}
	public Rectangle[] getParts() {
		return parts;
	}
	public double getY() {
		double out = parts[0].getY();
		for (int i = 1; i < parts.length; i++) {
			if (parts[i].getY() < out) {
				out = parts[i].getY();
			}
		}
		return out;
	}
	public double getX() {
		double out = parts[0].getX();
		for (int i = 1; i < parts.length; i++) {
			if (parts[i].getX() < out) out = parts[i].getX();
		}
		return out;
	}
	public void translate(int dx, int dy) {
		for (Rectangle x : parts) {
			x.translate(dx, dy);
		}
	}
	public int getWidth() {
		double minX = getX();
		double maxX = parts[0].getX() + parts[0].getWidth();
		for (int i = 1; i < parts.length; i++) {
			if (parts[i].getX() + parts[0].getWidth() > maxX) maxX = parts[i].getX() + parts[0].getWidth();
		}
		return (int)(maxX - minX);
	}
	public int getHeight() {
		double minY = getY();
		double maxY = parts[0].getY() + parts[0].getHeight();
		for (int i = 1; i < parts.length; i++) {
			if (parts[i].getY() + parts[0].getHeight() > maxY) maxY = parts[i].getY() + parts[0].getHeight();
		}
		return (int)(maxY - minY);
	}
	public String toString() {
		String output = "";
		for (Rectangle x : parts) {
			output += x.toString();
		}
		return output;
	}
}
