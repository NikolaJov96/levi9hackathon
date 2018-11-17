package gamecore;

import java.io.Serializable;
import java.util.ArrayList;

public class GameModel extends Thread implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_DIMENSION_X = 30;
	private static final int DEFAULT_DIMENSION_Y = 30;
	
	private GameModelParent parent;
	private GameBot gameBot;
	
	private Board board;
	private ArrayList<Bullet> bullets;
	
	// 0 is a player, 1 is a bot
	private int resources[];
	private MainBuilding mainBuildings[];
	
	private boolean running;
	private volatile boolean locked;

	public GameModel() {
		// TODO
		initModel();
	}

	private void initModel() {
		running = true;
		start();
		gameBot = new GameBot(this);
		gameBot.start();
	}
	
	public synchronized Building build(int player, int buildingType, int x, int y) {
		return null;
	} 
	
	public synchronized boolean haveEnoughCostToBuild(int player, int buildingType) {
		return false;
	}

	public synchronized void heal(int player, int x, int y) {
	}
	public synchronized void upgrade(int player, int x, int y) {
	}

	public synchronized boolean isRunning() {
		return false;
	}
	
	public synchronized void lock() {
		locked = true;
	}
	
	public synchronized void unlock() {
		locked = false;
		notifyAll();
	}
	
	public void run() {
		while (running) {
			long startTime = System.currentTimeMillis();
			synchronized(this) {
			while (locked)
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// TODO
			}
			long endTime = System.currentTimeMillis();
			try {
				if (1000 / 60 - (endTime - startTime) > 0)
					sleep(1000 / 60 - (endTime - startTime));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}

