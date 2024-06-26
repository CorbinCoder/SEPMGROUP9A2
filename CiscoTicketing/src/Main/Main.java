package Main;

import CustomObjects.TicketArchiver;

// A system that allows a staff member to log in using a distinct user name and
// password, then create service tickets that are added to a list of tickets
// currently held by the system, assign those tickets to a service technician,
// and observe the progress of the ticket. Some staff members may also be service
// technicians that have tickets assigned to them to be resolved. If the user does
// not currently have a profile in the system, they are prompted to create a new
// profile by entering their information into the system.

public class Main {

	public static void main(String[] args) {

		// Create a new instance of the ticketing system controlling class.
		TicketingSystem ticketingSystem = TicketingSystem.getInstance();
		ticketingSystem.seedTechnicians();

		// Add test data to the system. Comment out to add no test data
		ticketingSystem.testData(20);

		// Start the Ticket Archiver thread
		TicketArchiver archiver = new TicketArchiver(ticketingSystem);
		Thread archiverThread = new Thread(archiver);
		archiverThread.start();

		// Display the menu of the ticketing system.
		ticketingSystem.displayOptions();

		System.exit(0);

	}
}
