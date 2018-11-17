package com.mrhuman.levi9.ageofarmies.gamecore;

public class Factory extends Building {

	static final int COST = 20;
	private static final int INITIAL_HEALTH = 100;
	private static final int RESOURCE_GAIN = 60;
	private static final int MILISECONDS = 3000;
	
	private long lastGainTime;
	

	public Factory(GameModel gameModel, int x, int y, int player) {
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
