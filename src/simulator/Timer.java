package simulator;

import simulator.utils.Astrophysics;


public class Timer extends Thread{
	private Thread thread;
	private double elapsedTime;
	private double lastTime;
	private double time = 0;
	private int samples = 0;
	private double average = 0;
	private double deltaTime;
	private Window window;
	
	public Timer(Window window) {
		this.elapsedTime = 0;
		this.window = window;
	}
	
	public void run() {
		while (true){
			lastTime = time;
			time = elapsedTime * Math.sqrt(Astrophysics.G);
			deltaTime = time-lastTime;
			double sum = average*samples;
			sum += deltaTime;
			samples += 1;
			average = sum/samples;
			window.setSimSpeedLabel((int) deltaTime);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addTime(double t) {
		elapsedTime += t;
	}
	
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.setName("Timer");
			thread.start();
		}
	}
}
