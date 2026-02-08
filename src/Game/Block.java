package Game;

import java.awt.Rectangle;

public class Block {
	private int x, y, size;
	
	public Block(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, size, size);
	}
	
	public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }
}
