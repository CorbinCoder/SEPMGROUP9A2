package Main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Collectors;

import CustomObjects.AccountValidator;
import CustomObjects.PasswordManager;
import CustomObjects.StaffMember;
import CustomObjects.Technician;
import CustomObjects.Technician.Level;
import CustomObjects.Ticket;
import CustomObjects.User;

public class TicketingSystem {
	private static final TicketingSystem INSTANCE = new TicketingSystem();
	private final ArrayList<User> users = new ArrayList<>();

	private ArrayList<Ticket> tickets = new ArrayList<>();
	private User currentUser;

	private Scanner scanner;

	private TicketingSystem() {
		// Seed Technicians into staffMembers
		this.scanner = new Scanner(System.in);
		currentUser = null;
	}

	public Scanner getScanner() {
		return this.scanner;
	}

	public static TicketingSystem getInstance() {
		return INSTANCE;
	}

	private void displayMainMenu() {
		System.out.println("Select an option:");
		System.out.println("1. Create new Staff Member");
		System.out.println("2. User login");
		System.out.println("3. Password reset");
		System.out.println("4. Exit");
		System.out.print("Enter your choice (1-4): ");
	}

	public void displayOptions() {

		int choice = 0;
		while (true) {
			if (currentUser == null) {
				displayMainMenu();
				try {
					choice = scanner.nextInt();
				} catch (InputMismatchException e) {
					System.out.println("Invalid input");
				}

				scanner.nextLine();

				switch (choice) {
				case 1:
					createNewStaffMember();
					break;
				case 2:
					login();
					break;
				case 3:
					resetPassword();
					break;
				case 4:
					exitProgram();
					return;
				case 5:
					for (User user : this.users) {
						System.out.println(user.getFullName());
					}
				default:
					System.out.println("Invalid choice. Please try again.");
				}
			} else {
				currentUser.options();
			}
			System.out.println();
		}
	}

	private void exitProgram() {
		System.out.println("Exiting the program...");
	}

	private void createNewStaffMember() {
		System.out.println("Creating a new staff member...");
		String email = promptUser("Enter a unique email address: ");
		String fullName = promptUser("Enter the full name: ");
		String phoneNumber = promptUser("Enter the phone number: ");
		String password = "";
		do {
			password = PasswordManager.readPassword();
			if (!PasswordManager.isValidPassword(password)) {
				System.out.println("Invalid password");
			}
		} while (!PasswordManager.isValidPassword(password));
		if (AccountValidator.validateUniqueDetails(email, fullName, phoneNumber)) {
			ArrayList<Ticket> allTickets = this.tickets;
			StaffMember newStaffMember = new StaffMember(email, fullName, phoneNumber, password);
			users.add(newStaffMember);
			System.out.println("New staff member created successfully!");
		} else {
			System.out.println("Failed to create new staff memeber, details not unique");
			return;
		}
	}

	private String promptUser(String prompt) {
		System.out.print(prompt);
		String input = scanner.nextLine();
		;
		return input;
	}

	private void login() {

		// Prompt for email address
		System.out.print("Enter your email address: ");
		String email = scanner.nextLine();

		// Prompt for password
		System.out.print("Enter your password: ");
		String password = new String(scanner.nextLine());

		if (AccountValidator.validateLoginDetails(email, password)) {
			System.out.println("Login successful!");
		} else {
			System.out.println("Invalid email or password. Please try again.");
		}

	}

	private void resetPassword() {
		System.out.println("Staff Member Password Reset");
		// Prompt for email address
		System.out.print("Enter your email address: ");
		String email = promptUser("Enter your email address: ");
		String fullName = promptUser("Enter your full name: ");
		String phoneNumber = promptUser("Enter your phone number: ");

		if (AccountValidator.validateResetPassword(email, fullName, phoneNumber)) {
			User usr = findUserByEmail(email);
			String newPassword = PasswordManager.resetPassword(usr);
			System.out.println("Password reset successfully!");
			System.out.println("Please use the following password to log in: " + newPassword);
		} else {
			System.out.println("Invalid user details");
		}
	}

	private User findUserByEmail(String email) {
		for (User usr : this.users) {
			if (usr.getEmail().equalsIgnoreCase(email)) {
				return usr;
			}
		}
		return null;
	}

	public void setCurrentUser(User user) {
		this.currentUser = user;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void addTicket(Ticket ticket) {
		assignTicket(ticket);
		this.tickets.add(ticket);
	}

	public void updateTicket(Ticket updatedTicket) {
		this.tickets.removeIf(ticket -> ticket.getID() == updatedTicket.getID());
		this.tickets.add(updatedTicket);
		if (updatedTicket.getTechnicianID() < 0) {
			assignTicket(updatedTicket);
		}
	}

	public void clearCurrentUser() {
		currentUser = null;
	}

	private void assignTicket(Ticket ticket) {
		Level level = mapTicketSeverityToLevel(ticket);

		ArrayList<Technician> technicians = this.users.stream().filter(user -> user instanceof Technician)
				.map(user -> (Technician) user).filter(technician -> technician.getLevel() == level)
				.collect(Collectors.toCollection(ArrayList::new));

		if (!technicians.isEmpty()) {
			Technician assignedTechnician = technicians.stream()
					.min(Comparator.comparingInt(Technician::getAssignedTicketCount)).orElse(null);

			if (assignedTechnician != null) {
				ticket.setTechnicianID(assignedTechnician.getID());
				assignedTechnician.addTicket(ticket);
				System.out.println("Ticket assigned to technician: " + assignedTechnician.getFullName());
			} else {
				System.out.println("No available technician found for the ticket level.");
			}
		} else {
			System.out.println("No technician found for the ticket level.");
		}
	}

	private Level mapTicketSeverityToLevel(Ticket ticket) {
		switch (ticket.getSeverity()) {
		case LOW:
		case MEDIUM:
			return Level.ONE;
		case HIGH:
			return Level.TWO;
		}
		return null;
	}

	/**
	 * Adds the known technicians to the object staffMembers
	 */
	void seedTechnicians() {
		var technicians = new ArrayList<Technician>() {
			{
				add(new Technician("harry.styles@team.cisco.com", "Harry Styles", "0425363455",
						"P:\"/|\"\\)1pgi*{tCe#0.", Level.ONE));
				add(new Technician("niall.horan@team.cisco.com", "Niall Horan", "0425411004", "7O7|dYH4PbogT?_P&<Hp",
						Level.ONE));
				add(new Technician("liam.payne@team.cisco.com", "Liam Payne", "0425002266", ",dP\"_sUQ\\EklO(0s+37c",
						Level.ONE));
				add(new Technician("louis.tomlinson@team.cisco.com", "Louis Tomlinson", "0425789123",
						"QyDgn2a{1r&5\"F}LIXX&", Level.TWO));
				add(new Technician("zayn.malik@team.cisco.com", "Zayn Malik", "0425121333", "W'oA%m%QV#TtYlc*Q_9_",
						Level.TWO));
			}
		};

		this.users.addAll(technicians);
	}
}
