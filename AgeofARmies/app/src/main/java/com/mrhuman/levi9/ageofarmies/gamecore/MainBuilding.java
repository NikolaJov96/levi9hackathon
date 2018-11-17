package com.mrhuman.levi9.ageofarmies.gamecore;

public class MainBuilding extends Building {

	// amount of money to build this building
	static final int COST = 100;
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
		return COST;
	}


	@Override
	public void heal() {
		health += HEAL_STEP;
		if(health > INITIAL_HEALTH)
			health = INITIAL_HEALTH;
	}


	@Override
	public void step() {
		long currentTime = System.currentTimeMillis();
		if(currentTime - lastGainTime >= MILISECONDS) {
			gameModel.resources[player] += RESOURCE_GAIN;
			lastGainTime = currentTime;
			gameModel.parent.gotResources(x, y, RESOURCE_GAIN);
		}
	}


}
