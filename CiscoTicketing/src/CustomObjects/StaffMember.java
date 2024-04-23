package CustomObjects;

import java.util.InputMismatchException;
import java.util.function.Predicate;

import CustomObjects.Ticket.Severity;
import Main.TicketingSystem;

// A custom class used to define a staff member object, that may create service
// tickets, and add them to the system.
public class StaffMember extends User {

	// Declare static variable to generate each staff member ID.
	private static int staffIDGenerator = 0;

	// Declare variables used to store staff member information.
	private int staffID;

	// Constructor for staff member object.
	public StaffMember(String email, String fullName, String phoneNumber, String password) {
		super(email, fullName, phoneNumber, password);
		// Increment staff ID generator then assign to new staff member.
		this.staffID = ++staffIDGenerator;
	}

	public void options() {
		int choice = 0;
		boolean exit = false;

		while (!exit) {
			System.out.println("Select an option:");
			System.out.println("1. Log support ticket");
			System.out.println("2. View your tickets");
			System.out.println("3. Exit");
			System.out.print("Enter your choice (1 - 3): ");

			try {
				choice = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a number.");
				scanner.nextLine();
				continue;
			}

			scanner.nextLine(); // Consume the newline character

			switch (choice) {
			case 1:
				createTicket();
				break;
			case 2:
				super.displayUserTickets();
				break;
			case 3:
				System.out.println("Logging out...");
				TicketingSystem.getInstance().clearCurrentUser();
				exit = true;
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}

			System.out.println(); // Print a blank line for readability
		}
	}

	protected void createTicket() {
		String description = getValidInput("> Please enter a description of the issue: ",
				"Error. Description cannot be empty.", "Error. Description cannot be greater than 250 characters.",
				input -> !input.isEmpty() && input.length() <= 250);

		Severity severity = getValidSeverity("> Please enter the severity of the issue (1=LOW, 2=MEDIUM, 3=HIGH): ",
				"Error. Severity entry cannot be empty.", "Error. Severity entry is invalid.");

		Ticket newTicket = new Ticket(description, this.getFullName(), severity);
		TicketingSystem.getInstance().addTicket(newTicket);
		this.tickets.add(newTicket);

		System.out.println("\n> Ticket successfully created and added to the ticket list.");
		System.out.println("Please await a response from our service team.\n");
	}

	private String getValidInput(String prompt, String emptyMessage, String invalidMessage,
			Predicate<String> validator) {
		while (true) {
			System.out.print(prompt);
			String input = scanner.nextLine().trim();
			if (input.isEmpty()) {
				System.out.println(emptyMessage);
			} else if (!validator.test(input)) {
				System.out.println(invalidMessage);
			} else {
				return input;
			}
		}
	}

	private Severity getValidSeverity(String prompt, String emptyMessage, String invalidMessage) {
		while (true) {
			String input = getValidInput(prompt, emptyMessage, invalidMessage, entry -> entry.matches("[1-3]"));
			switch (input) {
			case "1":
				return Severity.LOW;
			case "2":
				return Severity.MEDIUM;
			case "3":
				return Severity.HIGH;
			}
		}
	}

	// Getters
	public int getID() {
		return this.staffID;
	}

	@Override
	public String toString() {
		return "StaffMember{" + "email='" + this.getEmail() + '\'' + ", fullName='" + this.getFullName() + '\''
				+ ", phoneNumber='" + this.getPhoneNumber() + '\'' + '}';
	}
}