package org.openjfx;

public class MeasurementThread extends Thread {

	private App app;
	private boolean running = true;

	public MeasurementThread(App app) {
		this.app = app;
	}
	
	@Override
	public void run() {
		Tracking tracking = app.getTracking();
		while(running) {
			tracking.takeMeasurements2();
	        try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stopMeasuring() {
		this.running = false;
	}

}
