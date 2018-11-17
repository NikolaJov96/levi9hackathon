package com.mrhuman.levi9.ageofarmies.gamecore;

import java.io.Serializable;
import java.util.ArrayList;

public class GameModel extends Thread implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_DIMENSION_X = 30;
	private static final int DEFAULT_DIMENSION_Y = 30;
	
	GameModelParent parent;
	private GameBot gameBot;
	
	Board board;
	ArrayList<Bullet> bullets;
	
	// 0 is a player, 1 is a bot
	int resources[];
	private MainBuilding mainBuildings[];
	
	private boolean running;
	private volatile boolean locked = false;

	public GameModel(GameModelParent parent, GameBot gameBot) {
		this.parent = parent;
		this.gameBot = gameBot;
		board = new Board(DEFAULT_DIMENSION_X, DEFAULT_DIMENSION_Y);
		initModel();
	}

	public GameModel(GameModelParent parent, GameBot gameBot, int dimX, int dimY) {
		this.parent = parent;
		this.gameBot = gameBot;
		board = new Board(dimX, dimY);
		initModel();
	}

	private void initModel() {
		resources = new int[2];
		mainBuildings = new MainBuilding[2];
		build(0, 0, 1,1);
		build(1, 0, board.dimX-2, board.dimY-2);
		bullets = new ArrayList<Bullet>();
		running = true;
		start();
		gameBot = new GameBot(this);
		gameBot.start();
	}
	
	public synchronized Building build(int player, int buildingType, int x, int y) {
	    int cost;
	    Building b;
	    if (buildingType == 0) {
            cost = MainBuilding.COST;
            b = new MainBuilding(this, x, y, player);
        }
	    else if (buildingType == 1) {
            cost = Factory.COST;
            b = new Factory(this, x, y, player);
        }
	    else {
            cost = Cannon.COST;
            b = new Cannon(this, x, y, player);
        }

        if (resources[player] >= cost) {
            if (board.add(b, x, y) == true)
                return b;
            else
                return  null;
        }

        return  null;
	} 
	
	public synchronized boolean haveEnoughCostToBuild(int player, int buildingType) {
        int cost;

        if (buildingType == 0) {
            cost = MainBuilding.COST;
        }
        else if (buildingType == 1) {
            cost = Factory.COST;
        }
        else {
            cost = Cannon.COST;
        }

        return resources[player] >= cost;
	}

	public synchronized void heal(int player, int x, int y) {
	    Building b = board.at(x, y);
	    if (b.player != player) return;

	    if (resources[player] >= Building.HEAL_COST) {
	        b.heal();
	        resources[player] -= Building.HEAL_COST;
        }
	}
	public synchronized void upgrade(int player, int x, int y) {
	}

	public synchronized boolean isRunning() {
		return running;
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

                for (int i = bullets.size()-1; i >= 0; i--) {
                    Bullet b = bullets.get(i);
                    b.step();
                    if (b.isDestroyed()) {
                        Building target = board.at(b.targetX, b.targetY);
                        if (target != null) {
                            target.hit(Bullet.demage);
                        }
                        bullets.remove(b);
                    }
                }

                for (int i = board.buildings.size()-1; i >= 0; i--) {
                    Building b = board.buildings.get(i);
                    b.step();
                    if (b.isDestroyed()) {
                        if (b == mainBuildings[0])
                            parent.gameOver(1);
                        else if (b == mainBuildings[1])
                            parent.gameOver(0);

                        board.buildings.remove(b);

                    }
                }


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

