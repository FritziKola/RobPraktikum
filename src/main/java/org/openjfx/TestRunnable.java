package org.openjfx;

public class TestRunnable implements Runnable {

	@Override
	public void run() {
		int x = 0;
		while(x < 20) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Test");
			x++;
		}
	}

}
