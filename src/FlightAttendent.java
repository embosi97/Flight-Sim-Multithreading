import java.util.Random;
import java.util.concurrent.Semaphore;

public class FlightAttendent implements Runnable {
	private String name;
	private long time;
	private long initialWaitingTime;
	private Passengers temp;
	private Thread t;
	public static Semaphore flightIt = new Semaphore(0);

	public FlightAttendent(String name) {
		this.name = name;
		time = System.currentTimeMillis();
		this.t = new Thread(this);
	}

	
	public void run() {
		initialWaitingTime = new Random().nextInt(999);

		try {
			flightIt.acquire(); // queued here
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(initialWaitingTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		msg("All passengers! The gate doors will close soon. Walk up to the gate doors!");

		try {
			Thread.sleep(initialWaitingTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < Main.passengerNum; i++) {
			Passengers.passWait.release();
			Main.securityLine.incrementAndGet();
		}

		if (Main.securityLine.get() == Main.passengerNum) {
			// if there are no missing passengers
			msg("Okay, there are NO missing passengers! Good news! Time to board the plane!");
		} else {
			// if there are missing passengers at this point of the flight process
			msg("Some passengers are not at the gate doors, however, we cannot wait any longer. Time to board the plane!");
			msg("You must rebook your flight. Sorry for the inconvience!");
		}

		try {
			Thread.sleep(initialWaitingTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			flightIt.acquire(); // queued here
		} catch (Exception e) {
			e.printStackTrace();
		}

		msg("We're now boarding!");

		try {
			Thread.sleep(initialWaitingTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while ((Main.allZones.get(0).size() > 0) && (Main.allZones.get(1).size() > 0)
				&& (Main.allZones.get(2).size() > 0)) {
			enterPlaneZone1();

			try {
				Thread.sleep(initialWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			enterPlaneZone2();

			try {
				Thread.sleep(initialWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			enterPlaneZone3();
		}

		msg("We're all on board! Enjoy your flight! Flight should take around 2 hours or so.");

		try { // simulating a flight that will take 2 hours or so
			Thread.sleep(new Random().nextInt(4000));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// flight attendent will wake everyone up
		msg("Okay ladies and gents, we will land shortly. Get ready to listen to instructions!");
		// Thread.interrupt();

		try { // simulating a flight that will take 2 hours or so
			Thread.sleep(new Random().nextInt(4000)); // hence the long sleep time
		} catch (Exception e) {
			e.printStackTrace();
		}

		msg("Time to disembark the plane. You must leave in order!");

		// now they'll leave the plane
		// I tried using isAlive() and join() (in main) to order them
		while (Main.thePlane.size() > 0) { // if the plane isn't empty
			temp = Main.thePlane.remove();

			try { // taking their time getting off the plane
				Thread.sleep(initialWaitingTime);
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println(
					"[" + (System.currentTimeMillis() - time) + "] " + temp.getName() + " has gotten off the plane. " + "Current plane capacity is now: " + Main.thePlane.size() + ".");
		}
		
		try { // taking their time getting off the plane
			Thread.sleep(initialWaitingTime * 2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (Main.thePlane.size() == 0) {
			msg("I will clock out for today. Thank you and enjoy your time in Fantasy Land!");
		}

	}

	public void enterPlaneZone1() {
		if (Main.allZones.get(0).size() > 0) {
			Passengers temp = Main.allZones.get(0).remove();
			try {
				Thread.sleep(new Random().nextInt(1000));
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("[" + (System.currentTimeMillis() - time) + "] " + "All aboard! " + temp.getName()
					+ " will now sit at seat # " + temp.getSeatNum() + " in the plane.");
			Main.thePlane.add(temp);
			System.out.println("[" + (System.currentTimeMillis() - time) + "] "
					+ "The current capacity of the plane is " + Main.thePlane.size() + ".");
		}
	}

	public void enterPlaneZone2() {
		if (Main.allZones.get(1).size() > 0) {
			Passengers temp = Main.allZones.get(1).remove();
			try {
				Thread.sleep(new Random().nextInt(1000));
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("[" + (System.currentTimeMillis() - time) + "] " + temp.getName()
					+ " will now sit at seat # " + temp.getSeatNum() + " in the plane.");
			Main.thePlane.add(temp);
			System.out.println("[" + (System.currentTimeMillis() - time) + "] "
					+ "The current capacity of the plane is " + Main.thePlane.size() + ".");
		}
	}

	public void enterPlaneZone3() {
		if (Main.allZones.get(2).size() > 0) {
			Passengers temp = Main.allZones.get(2).remove();
			try {
				Thread.sleep(new Random().nextInt(1000));
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("[" + (System.currentTimeMillis() - time) + "] " + temp.getName()
					+ " will now sit at seat # " + temp.getSeatNum() + " in the plane.");
			Main.thePlane.add(temp);
			System.out.println("[" + (System.currentTimeMillis() - time) + "] "
					+ "The current capacity of the plane is " + Main.thePlane.size() + ".");
		}
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
