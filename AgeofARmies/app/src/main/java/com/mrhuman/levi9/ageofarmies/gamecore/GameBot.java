package com.mrhuman.levi9.ageofarmies.gamecore;

import java.util.Random;

public class GameBot extends Thread {

	private static final int SLEEP_MILIS = 500;

	GameModel gameModel;
	
	public GameBot(GameModel gameModel) {
		this.gameModel = gameModel;
	}

	public void run() {
		while (gameModel.isRunning()) {
			gameModel.lock();

			// 0 build a factory, 1 build a cannon, 2 heal
            Random r = new Random();
			int decision = r.nextInt(2);

			if (decision == 0) {
			    for (int x = 0; x < gameModel.board.dimX; x++) {
                    for (int y = 0; y < gameModel.board.dimY; y++) {
                        if (gameModel.board.at(x, y) == null)
                            gameModel.build(1, 1, x, y);
                    }
                }
            }
            else if (decision == 1) {
                for (int x = 0; x < gameModel.board.dimX; x++) {
                    for (int y = 0; y < gameModel.board.dimY; y++) {
                        if (gameModel.board.at(x, y) == null)
                            gameModel.build(1, 2, x, y);
                    }
                }
            } else {
                for (int x = 0; x < gameModel.board.dimX; x++) {
                    for (int y = 0; y < gameModel.board.dimY; y++) {
                        Building b = gameModel.board.at(x, y);
                        if (b != null)
                            b.heal();

                    }
                }
            }

			gameModel.unlock();
			try {
				sleep(SLEEP_MILIS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

