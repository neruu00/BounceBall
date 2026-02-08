package Game;

import java.awt.Rectangle;

public class Block {
	static private final int SIZE = 40;
	private int x, y;
	
	public Block(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, SIZE, SIZE);
	}
	
	public int getX() { return x; }
    public int getY() { return y; }
    public static int getSIZE() { return SIZE; }
}
