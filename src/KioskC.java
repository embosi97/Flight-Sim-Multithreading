import java.util.Random;
import java.util.concurrent.Semaphore;

public class KioskC implements Runnable {
	private String name;
	private long time;
	private long initialWaitingTime;

	private int clerkCheck;
	private Thread t;
	public static Semaphore startUp = new Semaphore(0);

	// Kiosk has a name and a number that distinguishes it from the other respective
	// kiosk
	public KioskC(String name) {
		this.name = name;
		time = System.currentTimeMillis();
		this.t = new Thread(this);
	}


	public void run() {
		initialWaitingTime = new Random().nextInt(999);

		try {
			startUp.acquire(); // queued here
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (Main.peopleServed.get() < Main.counterNum * 10) {

			// remove one passenger
			Passengers temp = Main.wait.remove();
			int boardingPass = Main.ss.get(0);
			temp.setSeatNum(boardingPass);
			Main.peopleServed.incrementAndGet();
			Main.ss.remove(0);

			try {
				Thread.sleep(initialWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// deciding which KioskC randomly
			if ((new Random().nextInt(100) + 1) % 2 == 0) {
				clerkCheck = 1;
			} else {
				clerkCheck = 2;
			}

			System.out.println("[" + (System.currentTimeMillis() - time) + "]" + "Kiosk #" + clerkCheck + " is serving "
					+ temp.getName());
		}

		if (Main.peopleServed.get() == Main.passengerNum) {
			for (int i = 0; i < Main.passengerNum; i++) {
				Passengers.passWait.release(); // release all 30 passengers from the passWait queue
			}
		}

		msg("All Passengers are good to go!");

	}

	public void msg(String m) {
		System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + " " + m);
	}

	public String getName() {
		return name;
	}
	
	public void start() {
		t.run();
	}
}