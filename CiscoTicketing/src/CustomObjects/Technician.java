package CustomObjects;

import java.util.InputMismatchException;

import CustomObjects.Ticket.Severity;
import CustomObjects.Ticket.Status;
import Main.TicketingSystem;

// A custom class that allows a staff member to be declared as a technician,
// allowing them to perform service tasks, and have tickets assigned to them.
public class Technician extends User {

	// Indicates the service desk level that the technician sits on.
	private Level level;

	private int technicianID;

	private static int technicianIDGenerator = 0;

	public static enum Level {
		ONE, TWO;
	}

	public Technician(String email, String fullName, String phoneNumber, String password, Level level) {
		super(email, fullName, phoneNumber, password);
		this.technicianID = ++technicianIDGenerator;
		this.level = level;
	}

	public void options() {
		int choice = 0;
		boolean exit = false;

		while (!exit) {
			try {

				System.out.println("Select an option:");
				System.out.println("1. View tickets assigned to you");
				System.out.println("2. Increase ticket severity");
				System.out.println("3. Decrease ticket severity");
				System.out.println("4. Change ticket status");
				System.out.println("5. Exit");
				System.out.print("Enter your choice (1-4): ");

				try {
					choice = scanner.nextInt();
				} catch (InputMismatchException e) {
					System.out.println("Invalid input. Please enter a number.");
					scanner.nextLine();
					continue;
				}
				scanner.nextLine();

				switch (choice) {
				case 1:
					super.displayUserTickets();
					break;
				case 2:
					int ticketId = promptUserInt("Enter ticket ID: ");
					escalateSeverity(ticketId);
					break;
				case 3:

					ticketId = promptUserInt("Enter ticket ID: ");
					deEscalateSeverity(ticketId);
					break;
				case 4:
					ticketId = promptUserInt("Enter ticket ID: ");
					setTicketStatus(ticketId);
					break;

				case 5:
					System.out.println("Logging out...");
					TicketingSystem.getInstance().clearCurrentUser();
					exit = true;
					break; // Exit the switch statement
				default:
					System.out.println("Invalid choice. Please try again.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid choice. Please try again.");
			}

			System.out.println(); // Print a blank line for readability
		}
	}

	public void displayTickets() {
		for (Ticket tckt : this.tickets) {
			tckt.display();
		}
	}

	private int promptUserInt(String prompt) {
		int input = 0;
		boolean validInput = false;

		while (!validInput) {
			System.out.print(prompt);
			try {
				input = scanner.nextInt();
				validInput = true;
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a valid integer value.");
				scanner.nextLine(); // Clear the invalid input from the scanner
			}
		}
		return input;
	}

	private void escalateSeverity(int ticketId) {
		Ticket ticket = tickets.stream().filter(t -> t.getID() == ticketId).findFirst().orElse(null);

		if (ticket != null) {
			if (ticket.getSeverity() != Severity.HIGH) {
				ticket.escalateSeverity();
				reassignTicket(ticket);
			} else {
				System.out.println("Ticket already set to High");
			}

		} else {
			System.out.println("Ticket not found with ID: " + ticketId);
		}
	}

	public void deEscalateSeverity(int ticketId) {
		Ticket ticket = tickets.stream().filter(t -> t.getID() == ticketId).findFirst().orElse(null);

		if (ticket != null) {
			if (ticket.getSeverity() != Severity.LOW) {
				ticket.deEscalateSeverity();
				reassignTicket(ticket);
			} else {
				System.out.println("Ticket already set to LOW");
			}

		} else {
			System.out.println("Ticket not found with ID: " + ticketId);
		}
	}

	private void setTicketStatus(int ticketId) {
		Ticket ticket = tickets.stream().filter(t -> t.getID() == ticketId).findFirst().orElse(null);
		Status status = null;
		if (ticket != null) {
			ticket.display();
			int intStatus = this
					.promptUserInt("Enter new status 1 = Open, 2 = Closed and Resolved, 3 = Closed and Unresloved: ");
			switch (intStatus) {
			case 1:
				status = Status.OPEN;
				break;
			case 2:
				status = Status.CLOSE_AND_RESOLVED;
				break;
			case 3:
				status = Status.CLOSED_AND_UNRESOLVED;
				break;
			default:
				System.out.println("invalid value");
			}
			ticket.setStatus(status);
			TicketingSystem.getInstance().updateTicket(ticket);
		} else {
			System.out.println("Ticket not found");
		}
	}

	private void reassignTicket(Ticket ticket) {
		Level level = null;
		switch (ticket.getSeverity()) {
		case LOW:
		case MEDIUM:
			level = Level.ONE;
			break;
		case HIGH:
			level = Level.TWO;
			break;
		}
		if (level != this.getLevel()) {
			ticket.setTechnicianID(-1);
			tickets.remove(ticket);
		}
		TicketingSystem.getInstance().updateTicket(ticket);
	}

	public void addTicket(Ticket ticket) {
		this.tickets.add(ticket);
	}

	public int getAssignedTicketCount() {
		return this.tickets.size();
	}

	public int getID() {
		return this.technicianID;
	}

	// Get & Set
	public Level getLevel() {
		return this.level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}
}
