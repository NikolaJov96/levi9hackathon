package gamecore;

public class GameBot extends Thread {

	private static final int SLEEP_MILIS = 500;

	GameModel gameModel;
	
	public GameBot(GameModel gameModel) {
		this.gameModel = gameModel;
	}

	public void run() {
		while (gameModel.isRunning()) {
			gameModel.lock();
			// TODO
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

