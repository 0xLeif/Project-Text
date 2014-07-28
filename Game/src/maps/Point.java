package maps;

public class Point {
	private int x, y;

	/**
	 * Constructs a point object using the specified parameters
	 * 
	 * @param x
	 *            x-coord
	 * @param y
	 *            y-coord
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}