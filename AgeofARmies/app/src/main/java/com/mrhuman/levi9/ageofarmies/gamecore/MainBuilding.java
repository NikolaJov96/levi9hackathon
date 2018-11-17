package gamecore;

public class MainBuilding extends Building {

	// amount of money to build this building
	private static final int COST = 100;
	private static final int INITIAL_HEALTH = 100;
	// amount of money this building produces
	private static final int RESOURCE_GAIN = 10;
	// period of producing money
	private static final int MILISECONDS = 5000;
	// last time the building produced money
	private long lastGainTime;
	

	public MainBuilding(GameModel gameModel, int x, int y, int player) {
		super(gameModel, x, y, player, INITIAL_HEALTH, 0);
		lastGainTime = System.currentTimeMillis();
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
