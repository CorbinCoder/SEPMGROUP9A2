package Main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import CustomObjects.AccountValidator;
import CustomObjects.PasswordManager;
import CustomObjects.StaffMember;
import CustomObjects.Technician;
import CustomObjects.Technician.Level;
import CustomObjects.Ticket;
import CustomObjects.Ticket.Severity;
import CustomObjects.Ticket.Status;
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
						tickets.stream().forEach(ticket -> ticket.display());
						break;

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
		String email = promptUser("Enter a unique email address: ",
				"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
		String fullName = promptUser("Enter the full name: ", "^[a-zA-Z]+(?:\\s[a-zA-Z]+)*$");
		String phoneNumber = promptUser("Enter the phone number: ", "[0-9]+");

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

	private String promptUser(String prompt, String regex) {
		System.out.print(prompt);
		String input = scanner.nextLine();

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);

		while (!matcher.matches()) {
			System.out.println("Invalid input. Please try again.");
			System.out.print(prompt);
			input = scanner.nextLine();
			matcher = pattern.matcher(input); // Reinitialize the matcher with the new input
		}

		return input;
	}

	private void login() {

		// Prompt for email address
		System.out.print("Enter your email address: ");
		String email = scanner.nextLine();

		// Prompt for password
		String password = PasswordManager.readPassword("Enter your password: ");

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
		String email = promptUser("Enter a unique email address: ",
				"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
		String fullName = promptUser("Enter the full name: ", "^[a-zA-Z]+(?:\\s[a-zA-Z]+)*$");
		String phoneNumber = promptUser("Enter the phone number: ", "[0-9]+");

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

	/**
	 * Retrieves a list of all tickets that are closed and archived.
	 * 
	 * @return a list of tickets that are both closed and archived.
	 */
	public ArrayList<Ticket> getClosedAndArchivedTickets() {
		return tickets.stream()
				.filter(ticket -> (ticket.getStatus() == Ticket.Status.CLOSE_AND_RESOLVED
						|| ticket.getStatus() == Ticket.Status.CLOSED_AND_UNRESOLVED) && ticket.isArchived())
				.collect(Collectors.toCollection(ArrayList::new));
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

	public ArrayList<Ticket> getAllTickets() {
		return this.tickets;
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
				ticket.setTechnicianName(assignedTechnician.getFullName());
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
	 * Maps each Ticket created within a specific date range by the Ticket Status
	 * 
	 * @param startTime Specifies the start of the time range
	 * @param endTime   Specifies the end of the time range
	 * @return A Map of tickets, where the key corresponds to the Ticket Status
	 */
	public Map<Ticket.Status, List<Ticket>> getTicketStatusReport(LocalDateTime startTime, LocalDateTime endTime) {
		List<Ticket> createdTickets = this.tickets.stream()
				.filter(t -> t.getCreationTime().isAfter(startTime)
						&& t.getCreationTime().isBefore(endTime))
				.collect(Collectors.toList());

		var openedTickets = new ArrayList<Ticket>();
		var closedResolvedTickets = new ArrayList<Ticket>();
		var closedUnresolvedTickets = new ArrayList<Ticket>();

		for (Ticket ticket : createdTickets) {
			var ticketStatus = ticket.getStatus();

			switch (ticketStatus) {
				case CLOSED_AND_UNRESOLVED:
					closedUnresolvedTickets.add(ticket);
					break;
				case CLOSE_AND_RESOLVED:
					closedResolvedTickets.add(ticket);
					break;
				default:
					openedTickets.add(ticket);
					break;
			}
		}

		var statusReport = new HashMap<Ticket.Status, List<Ticket>>();
		statusReport.put(Ticket.Status.CLOSED_AND_UNRESOLVED, closedUnresolvedTickets);
		statusReport.put(Ticket.Status.CLOSE_AND_RESOLVED, closedResolvedTickets);
		statusReport.put(Ticket.Status.OPEN, openedTickets);

		return statusReport;
	}

	/**
	 * Adds the known technicians to the object staffMembers
	 */
	void seedTechnicians() {
		var technicians = new ArrayList<Technician>() {
			{
				add(new Technician("harry.styles@team.cisco.com", "Harry Styles", "0425363455",
						"P:\"/%\"\\)1pgi*{tCe#0.", Level.ONE));
				add(new Technician("niall.horan@team.cisco.com", "Niall Horan", "0425411004", "7O7&2dYH4PbogT?_P&<Hp",
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

	void testData(int amountTickets) {

		PrintStream originalOut = System.out;
		try {
			System.setOut(new PrintStream(
					new FileOutputStream(System.getProperty("os.name").startsWith("Windows") ? "NUL" : "/dev/null")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Random random = new Random();
		var staffMembers = new ArrayList<StaffMember>() {
			{
				add(new StaffMember("patricia.smith@demo.com", "Patricia Smith", "0776172603", "oyoxpWn=]x?}ImNqMpsH"));
				add(new StaffMember("robert.miller@sample.com", "Robert Miller", "0185074039", "`ndHx~'s>]0tiTrp5c$y"));
				add(new StaffMember("james.miller@example.com", "James Miller", "0276776872", "&Fkq.;/}tw<Iqu@jd+@I"));
				add(new StaffMember("michael.johnson@example.com", "Michael Johnson", "0102241558",
						"ia\"\"q(]O&v[rly/O0mhh"));
				add(new StaffMember("robert.johnson@test.com", "Robert Johnson", "0508417220",
						"$\\?SH8/T;[Q_al3Wyb1L"));
				add(new StaffMember("patricia.brown@example.com", "Patricia Brown", "0191952420",
						"nl<M]n]+v:9LC+E&(~~t"));
				add(new StaffMember("robert.garcia@sample.com", "Robert Garcia", "0720165987", "@J)[.4?vy5W}hE{J~K!r"));
				add(new StaffMember("michael.williams@myapp.com", "Michael Williams", "0676046904",
						"#=]f[v{FYxkh-pcrFzl4"));
				add(new StaffMember("jennifer.davis@example.com", "Jennifer Davis", "0621067644",
						"jM<6sO]0q&W6dd$k^4^A"));
			}
		};
		String[] staffNames = { "Patricia Smith", "Robert Miller", "James Miller", "Michael Johnson", "Robert Garcia",
				"Robert Johnson", "Patricia Brown", "Michael Williams", "Jennifer Davis" };
		this.users.addAll(staffMembers);

		for (int i = 1; i <= amountTickets; i++) {

			int pick = random.nextInt(3);
			Severity serverity = null;
			switch (pick) {
				case 0:
					serverity = Severity.LOW;
					break;
				case 1:
					serverity = Severity.MEDIUM;
					break;
				case 2:
					serverity = Severity.HIGH;
					break;
			}
			String desc = "Service Issue " + i;

			addTicket(new Ticket(desc, staffNames[random.nextInt(9)], serverity,
					LocalDateTime.now().minusMinutes((int) (Math.random() * 1440))));

		}

		tickets.forEach(ticket -> {
			int pick = random.nextInt(3);
			switch (pick) {
				case 0:
					ticket.setStatus(Ticket.Status.OPEN);
					break;
				case 1:
					ticket.setStatus(Ticket.Status.CLOSE_AND_RESOLVED);
					break;
				case 2:
					ticket.setStatus(Ticket.Status.CLOSED_AND_UNRESOLVED);
					break;
			}
		});
		tickets.forEach(ticket -> {
			users.stream()
					.filter(user -> user instanceof StaffMember
							&& ((StaffMember) user).getFullName().equals(ticket.getStaffName()))
					.forEach(user -> ((StaffMember) user).addTicket(ticket));
		});

		// add ticket to test archive
		LocalDateTime twentyFiveHoursPast = LocalDateTime.now().minusHours(25);
		Ticket archiveTicket = new Ticket(
				"old ticket",
				"Patricia Brown",
				Severity.MEDIUM,
				twentyFiveHoursPast.minusHours(random.nextInt(24)));
		addTicket(archiveTicket);
		archiveTicket.setStatus(Status.CLOSE_AND_RESOLVED);
		archiveTicket.setClosureTime(twentyFiveHoursPast);
		System.setOut(originalOut);
	};
}
