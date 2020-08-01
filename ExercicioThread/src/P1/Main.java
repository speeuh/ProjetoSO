package P1;

import java.util.concurrent.Semaphore;

public class Main extends Thread {
	public static void main(String[] args) {
		Semaphore semaforo = new Semaphore(5);
		for(int id = 0; id < 25; id++) {
			Thread t = new ThreadTriatlo(id, semaforo);
			t.start();
		}
	}
}
