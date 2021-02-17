import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

class Main {
	//***********************************************************************************************************************************************************************
	//CS340 Project 2 (Semaphores) by Emiljano.
	//I put a lot of sleeps so the process would be a little longer (60 seconds at least).
	//Unlike the first project, this one reliably executes without any problems.
	//With semaphores, it seems the process is a lot cleaner (much fewer data structures as well).
	
    //given variables
	public static int passengerNum = 30;
	public static int counterNum = 3;
	public static int groupNum = 4;
	
	
	private static String passengerName;
    
	//atomic integers
	public static AtomicInteger peopleServed = new AtomicInteger(0);
	public static AtomicInteger securityLine = new AtomicInteger(0);
	public static AtomicInteger boarded = new AtomicInteger(0);

	//zone data structures
	public static Queue<Passengers> zone1 = new LinkedList<>();
	public static Queue<Passengers> zone2 = new LinkedList<>();
	public static Queue<Passengers> zone3 = new LinkedList<>();
	public static ArrayList<Queue<Passengers>> allZones = new ArrayList<>();

	//passenger arrays
	public static Thread[] passengersT = new Thread[passengerNum];
	public static Passengers[] passengers = new Passengers[passengerNum];

	public static ArrayList<Integer> ss = new ArrayList<Integer>();// for boarding passes

	public static Queue<Passengers> wait = new LinkedList<>(); // for the KioskC lines
	public static Queue<Passengers> thePlane = new LinkedList<>(); // where the passengers will board

	public static void main(String[] args) {

		//3 zones of 10 people each for the boarding of the plane
		allZones.add(zone1);
		allZones.add(zone2);
		allZones.add(zone3);

		for (int i = 1; i <= 30; i++) {
			ss.add(i);
		}
		
        //creating the KioskC and FlightAttendent threads
		KioskC kioskCT = new KioskC("Kiosks: ");
		FlightAttendent flightAttendentT = new FlightAttendent("Flight Attendent: ");

		// I will NOT use Collections for Sorting purposes, just for shuffling
		Collections.shuffle(ss); // for random numbers (boarding pass)
		System.out.println("Boarding tickets: " + ss.toString());

//		System.out.println("The passengers are: ");

		// passenger threads must start first
		for (int i = 0; i < passengerNum; i++) {
			if ((i % 2) == 0) {
				// to simulate real life (both males and females)
				passengerName = "John Doe";
			} else {
				passengerName = "Jane Doe";
			}

			passengers[i] = new Passengers(String.format(passengerName + " #%d", (i + 1)));

			passengersT[i] = new Thread(passengers[i]);
		}

		for (int i = 0; i < passengerNum; i++) {
			passengersT[i].start(); // start() calls run each time
		}
		
		//KioskC and FlightAttendent threads will work in tandem with the passenger threads
		//Their actions and ability to proceed is contingent on the passengers (and vice versa)

		kioskCT.start();

		flightAttendentT.start();

	}

}