package gamecore;


public class Cannon extends Building {

	private static final int COST = 40;
	private static final int INITIAL_HEALTH = 100;
	private static final int MILISECONDS = 3000;
	
	private long lastShotTime;
	

	public Cannon(GameModel gameModel, int x, int y, int player, int health, int level) {
		super(gameModel, x, y, player, INITIAL_HEALTH, 0);
		lastShotTime = System.currentTimeMillis();
	}


	@Override
	public int cost() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void heal() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void step() {
		// TODO Auto-generated method stub
		
	}

}
