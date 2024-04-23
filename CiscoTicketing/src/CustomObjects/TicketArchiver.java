package CustomObjects;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import CustomObjects.Ticket.Status;
import Main.TicketingSystem;

public class TicketArchiver implements Runnable {
//	private int checkInterval = 600000; // 10 minutes in milliseconds
	private int checkInterval = 6000; // 10 minutes in milliseconds
	private TicketingSystem ticketingSystem;

	public TicketArchiver(TicketingSystem system) {
		this.ticketingSystem = system;
	}

	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
//				System.out.println("Checking and archiving tickets...");
				if (archiveClosedTickets()) {
					removeArchivedTicketsFromUserLists();
				}
				;
				Thread.sleep(checkInterval);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println("Thread was interrupted, failed to complete operation");
			}
		}
	}

	private boolean archiveClosedTickets() {
		boolean foundTicketToArchive = false;
		LocalDateTime now = LocalDateTime.now();
		List<Ticket> tickets = TicketingSystem.getInstance().getAllTickets(); // Use the instance variable
		for (Ticket ticket : tickets) {
			if ((ticket.getStatus() == Status.CLOSE_AND_RESOLVED || ticket.getStatus() == Status.CLOSED_AND_UNRESOLVED)
					&& !ticket.isArchived()) {
				Duration duration = Duration.between(ticket.getClosureTime(), now);
				if (duration.toHours() >= 24) {
					ticket.setArchived(true);
					foundTicketToArchive = true;
				}
			}
		}
		return foundTicketToArchive;
	}

	private void removeArchivedTicketsFromUserLists() {
		TicketingSystem.getInstance().getUsers().forEach(user -> user.removeArchivedTickets());
	}

	public static void main(String[] args) {
		TicketingSystem system = TicketingSystem.getInstance();
		Thread archiverThread = new Thread(new TicketArchiver(system));
		archiverThread.start();
//		System.out.println("Ticket Archiver is running...");
	}
}
