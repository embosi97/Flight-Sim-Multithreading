import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

class Passengers implements Runnable {
	// Semaphores:
	public static Semaphore passWait = new Semaphore(0);
	public static Semaphore mutexA = new Semaphore(1);

	private AtomicInteger seatNumber = new AtomicInteger(-999);
	// Statics:
	private String name;
	private long time;
	private long initialWaitingTime;

	public Passengers(String name) {
		this.name = name;
	    time = System.currentTimeMillis();
	}

	public void run() {
		initialWaitingTime = new Random().nextInt(999);

		try {
			Thread.sleep(initialWaitingTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		msg("has arrived to the airport!");

		try {
			Thread.sleep(initialWaitingTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		try {

			mutexA.acquire();

			Main.wait.add(this); // adds passenger to a thread

			if (Main.wait.size() == Main.passengerNum) {
				KioskC.startUp.release();
			}
			mutexA.release();

			try {
				Thread.sleep(initialWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			passWait.acquire(); // wait until KiosKC releases:::

			msg("is getting on line to the kiosk with boarding pass #" + this.getSeatNum());
			// System.out.println("we out here: " + this.name);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		msg("is going to the security line");

		try {
			Thread.sleep(initialWaitingTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (this.getSeatNum() > 0) {

//			Thread.currentThread().setPriority(10); // to simulate rushing to the security line

			try {
				Thread.sleep(initialWaitingTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			msg("has passed through the security line.");

//			Thread.currentThread().setPriority(Thread.NORM_PRIORITY); // set it to default

			try {
				Thread.sleep(initialWaitingTime);
			} catch (Exception e) {
				e.printStackTrace();
			}

			msg("has taken a seat at the gate.");

			// Now the flight attendent joins in
			try {

				mutexA.acquire();

				Main.wait.add(this); // adds passenger

				try {
					Thread.sleep(initialWaitingTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				msg("is leaving the seating area and getting ready to board. ");

				if (Main.wait.size() == Main.passengerNum) {
					FlightAttendent.flightIt.release();
				}
				mutexA.release();

				try {
					Thread.sleep(initialWaitingTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				passWait.acquire(); // wait until FlightAttendent releases:::

				// System.out.println("we out here: " + this.name);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(initialWaitingTime);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				mutexA.acquire();
				if (Main.wait.size() > 0) {
					if (Main.zone1.size() <= (((Main.groupNum * 2) + 2))
							&& (this.getSeatNum() <= 10 && this.getSeatNum() > 0)) {
						Main.zone1.add(this);
						Main.boarded.incrementAndGet();
						try {
							Thread.sleep(initialWaitingTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("[" + (System.currentTimeMillis() - time) + "] " + this.getName()
								+ " is getting into Zone 1 with pass # " + this.getSeatNum() + ".");
					} else if ((Main.zone2.size() <= ((Main.groupNum * 2) + 2))
							&& (this.getSeatNum() <= 20 && this.getSeatNum() > 10)) {

						Main.zone2.add(this);
						Main.boarded.incrementAndGet();
						try {
							Thread.sleep(initialWaitingTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("[" + (System.currentTimeMillis() - time) + "] " + this.getName()
								+ " is getting into Zone 2 with pass # " + this.getSeatNum() + ".");
					} else if ((Main.zone3.size() <= ((Main.groupNum * 2) + 2))
							&& (this.getSeatNum() <= 30 && this.getSeatNum() > 20)) {

						Main.zone3.add(this);
						Main.boarded.incrementAndGet();
						try {
							Thread.sleep(initialWaitingTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("[" + (System.currentTimeMillis() - time) + "] " + this.getName()
								+ " is getting into Zone 3 with a boarding pass # of " + this.getSeatNum() + ".");
					}
				}

				System.out.println("[" + (System.currentTimeMillis() - time) + "] " + "The total zones number is " + Main.boarded.get()); // for tracking purposes
				mutexA.release();

			} catch (Exception e) {
				e.printStackTrace();
			}

			// ***********************************************************************************************

			if (Main.boarded.get() == 30) {
				FlightAttendent.flightIt.release();
			}

			try {
				Thread.sleep(initialWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

//	    
//	     try { 
//	      .acquire(); // wait until flight releases:::
//
//	      // System.out.println("we out here: " + this.name);
//	    } catch (InterruptedException e) {
//	      e.printStackTrace();
//	    }

		}
	}

	public void setSeatNum(int num) {
		seatNumber.set(num);
		// msg("got num " + Integer.toString(this.seatNumber));
	}

	public int getSeatNum() {
		return seatNumber.get();
	}

	public void msg(String m) {
		System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + " " + m);
	}

	public String getName() {
		return name;
	}
	

}


  
