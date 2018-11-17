package gamecore;

public class Bullet {

	private static final float SPEED_COEF =  1;

	private int cannonX;
	private int cannonY;
	private int targetX;
	private int targetY;
	private long creationTime;
	private float x;
	private float y;
	private float height;
	
	public void step() {
	}
	
	public boolean isDestroied() {
		return false;
	}

}
