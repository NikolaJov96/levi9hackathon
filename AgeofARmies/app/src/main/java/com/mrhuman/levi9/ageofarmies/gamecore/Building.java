package gamecore;

public abstract class Building {
	// number of buildings used for id
	private static int counter = 0;

	private GameModel gameModel;
	// position
	private int x;
	private int y;
	private float angle;
	private int player;
	private int health;
	private int level;
	
	private static final int HEAL_STEP = 10;
	private static final int HEAL_COST = 10;

	private int id; 
	
	public Building(GameModel gameModel, int x, int y, int player, int health, int level) {
		this.gameModel = gameModel;
		this.x = x;
		this.y = y;
		this.player = player;
		this.health = health;
		this.level = level;
		id = ++counter;
	}
	
	public static int healCost() {
		return HEAL_COST;
	}
	
	// cost required to build a building 
	public abstract int cost();
	
	public void hit(int damage) {
		health -= damage;
		if (health < 0) health = 0;
	}
	
	public abstract void heal();
	
	public boolean isDestroyed() {
		return health <= 0;
	}
	
	public int upgradeCost() {
		// FIX
		return 0;
	}
	
	public abstract void step();
	
}
