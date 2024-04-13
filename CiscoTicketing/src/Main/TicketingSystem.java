package Main;

import CustomObjects.*;
import CustomObjects.Ticket.Level;
import CustomObjects.Ticket.Severity;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.InputMismatchException;

public class TicketingSystem {
	private static final TicketingSystem INSTANCE = new TicketingSystem();
	private final ArrayList<StaffMember> staffMembers = new ArrayList<>();
	private ArrayList<Ticket> tickets = new ArrayList<>();
	private final Scanner scanner = new Scanner(System.in);
	private StaffMember currentUser;

	private TicketingSystem() {
		// Seed Technicians into staffMembers
		seedTechnicians();
	}

	public static TicketingSystem getInstance() {
		return INSTANCE;
	}

	public void displayOptions() {
		int choice;

		while (true) {

			if (currentUser == null) {

				System.out.println("Select an option:");
				System.out.println("1. Create new Staff Member");
				System.out.println("2. Staff Member login");
				System.out.println("3. Staff Member reset password");
				System.out.println("4. Exit");

				System.out.print("Enter your choice (1-4): ");
				try {
					choice = scanner.nextInt();
					scanner.nextLine(); // Consume the newline character

					switch (choice) {
						case 1:
							createNewStaffMember();
							break;
						case 2:
							staffMemberLogin();
							break;
						case 3:
							staffMemberResetPassword();
							break;
						case 4:
							System.out.println("Exiting the program...");
							return; // Exit the method and terminate the loop
						default:
							System.out.println("Invalid choice. Please try again.");
					}
				} catch (InputMismatchException e) {
					System.out.println("Invalid input. Please try again.");
					scanner.nextLine(); // Clear the invalid input
				}

				System.out.println(); // Print a blank line for readability

				// If the user is a technician.
			} else if (currentUser instanceof Technician) {

				System.out.println("Select an option:");
				System.out.println("1. Log support ticket");
				System.out.println("2. View tickets you have made");
				System.out.println("3. View tickets assigned to you");
				System.out.println("4. Exit");

				System.out.println("Enter your choice (1-4): ");
				try {
					choice = scanner.nextInt();
					scanner.nextLine();

					switch (choice) {
						case 1:
							createTicket();
							break;
						case 2:
							 displayUserTickets();
							break;
						case 3:
							 displayAssignedTickets();
							break;
						case 4:
							System.out.println("Exiting ticket viewer...");
							return; // Exits the current menu and returns to main.
					}
				} catch (InputMismatchException e1) {
					System.out.println("Inavlid input. Please try again.");
					scanner.nextLine();
				} catch (NumberFormatException e2) {
					System.out.println("Inavlid input. Please try again.");
					scanner.nextLine();
				}
				// If current user is a staff member.
			} else  if (currentUser != null) {

				System.out.println("Select an option:");
				System.out.println("1. Log support ticket");
				System.out.println("2. View your tickets");
				System.out.println("3. Exit");
				System.out.print("Enter your choice (1 - 3): ");
				try {
					choice = scanner.nextInt();
					scanner.nextLine();

					switch (choice) {
						case 1:
							createTicket();
							break;
						case 2:
							 displayUserTickets();
							break;
						case 3:
							System.out.println("Exiting ticket viewer...");
							return; // Exits the current menu and returns to main.
					}
				} catch (InputMismatchException e1) {
					System.out.println("Inavlid input. Please try again.");
					scanner.nextLine();
				} catch (NumberFormatException e2) {
					System.out.println("Inavlid input. Please try again.");
					scanner.nextLine();
				}
			}
		}
	}

	private void createNewStaffMember() {
		System.out.println("Creating a new staff member...");

		String email = promptUniqueEmail();
		String fullName = promptUniqueFullName();
		String phoneNumber = promptUniquePhoneNumber();
		String password = promptValidPassword();

		StaffMember newStaffMember = new StaffMember(email, fullName, phoneNumber, password);
		staffMembers.add(newStaffMember);
		System.out.println("New staff member created successfully!");
	}

	private String promptUniqueEmail() {
		while (true) {
			System.out.print("Enter a unique email address: ");
			String email = scanner.nextLine();

			if (isUnique(sm -> sm.getEmail().equalsIgnoreCase(email))) {
				return email;
			} else {
				System.out.println("Email address already exists. Please try again.");
			}
		}
	}

	private String promptUniqueFullName() {
		while (true) {
			System.out.print("Enter the full name: ");
			String fullName = scanner.nextLine();

			if (isUnique(sm -> sm.getFullName().equalsIgnoreCase(fullName))) {
				return fullName;
			} else {
				System.out.println("Full name already exists. Please try again.");
			}
		}
	}

	private String promptUniquePhoneNumber() {
		while (true) {
			System.out.print("Enter the phone number: ");
			String phoneNumber = scanner.nextLine();

			if (isUnique(sm -> sm.getPhoneNumber().equals(phoneNumber))) {
				return phoneNumber;
			} else {
				System.out.println("Phone number already exists. Please try again.");
			}
		}
	}

	private String promptValidPassword() {
		while (true) {
			System.out.print("Choose a password (min 20 characters, mix of uppercase, lowercase, and alphanumeric): ");
//			String password = new String(System.console().readPassword());
			
			String password = new String(scanner.nextLine());

			if (PasswordManager.isValidPassword(password)) {
				return password;
			} else {
				System.out.println("Invalid password. Please try again.");
			}
		}
	}
	
	private void staffMemberLogin() {
		System.out.println("Staff Member Login");

		// Prompt for email address
		System.out.print("Enter your email address: ");
		String email = scanner.nextLine();

		// Prompt for password
		System.out.print("Enter your password: ");
//		String password = new String(System.console().readPassword());
		String password = new String(scanner.nextLine());

		if (AccountValidator.validateLoginDetails(email, password)) {
			System.out.println("Login successful!");
			// TODO: Proceed with staff member actions or display staff member menu
		} else {
			System.out.println("Invalid email or password. Please try again.");
		}
	}

	private void staffMemberResetPassword() {
		System.out.println("Staff Member Password Reset");

		// Prompt for email address
		System.out.print("Enter your email address: ");
		String email = scanner.nextLine();

		StaffMember staffMember = findStaffMemberByEmail(email);

		if (staffMember != null) {
			System.out.print("Enter your full name: ");
			String fullName = scanner.nextLine();

			System.out.print("Enter your phone number: ");
			String phoneNumber = scanner.nextLine();
			if (staffMember.getFullName().equalsIgnoreCase(fullName) &&
					staffMember.getPhoneNumber().equals(phoneNumber)) {
				String newPassword = PasswordManager.resetPassword(staffMember);
				System.out.println("Password reset successfully!");
				System.out.println("Please use the following password to log in: " + newPassword);
			} else {
				System.out.println("Invalid name or phone number");
			}
		} else {
			System.out.println("Invalid email. Please try again.");
		}
	}

	private StaffMember findStaffMemberByEmail(String email) {
		for (StaffMember staffMember : staffMembers) {
			if (staffMember.getEmail().equalsIgnoreCase(email)) {
				return staffMember;
			}
		}
		return null;
	}

	private boolean isUnique(Predicate<StaffMember> predicate) {
		return staffMembers.stream().noneMatch(predicate);
	}

	public ArrayList<StaffMember> getStaffMembers() {
		return this.staffMembers;
	}

	public void setCurrentStaffMember(StaffMember staffMember) {
		this.currentUser = staffMember;
	}

	// So a user may generate a service ticket, and add it to the list of
	// current tickets
	private void createTicket() {

		// Declare variables used to create ticket.
		String description;
		Severity severity;
		Level level;

		// While input is invalid, continue to request input.
		while (true) {

			// Request and receive input for ticket description.
			System.out.print("> Please enter a decription of the issue: ");
			description = scanner.next();

			// If the input is empty, display an error.
			if (description.equals("")) {

				System.out.println("Error. Description cannot be empty.");

			// If the description is greater than 250 characters, do not accept and 
			// inform user.
			} else if (description.length() > 250) {

				System.out.println("Error. Description cannot be greater than 250 characters.");

			// Else, exit the selection process.
			} else {
				break;
			}
		}

		// While input is invalid, continue to request input.
		while (true) {

			// Request and receive input for ticket severity.
			System.out.print("> Please enter the severity of the issue (1=LOW, 2=MEDIUM, 3=HIGH): ");
			String severityEntry = scanner.next();

			// If the severity entry is valid and matches a severity level stored by the
			// system, initialize the severity variable with the corresponding value entered 
			// by the user, then exit selection process.
			if (severityEntry.equals("1")) {
				severity = Severity.LOW;
				break;
			} else if (severityEntry.equals("2")) {
				severity = Severity.MEDIUM;
				break;
			} else if (severityEntry.equals("3")) {
				severity = Severity.HIGH;
				break;
			// If the entry is empty, display an error.
			} else if (severityEntry.equals("")) {
				System.out.println("Error. Severity entry cannot be empty.");
			// If the entry does not match a corresponding severity level, inform user.
			} else {
				System.out.println("Error. Severity entry is invalid.");
			}
		}

		// While input is invalid, continue to request input.
		while (true) {

			// Request and receive input for ticket service desk level.
			System.out.print("> Please enter which level of service desk you would like to send the ticket to (1, 2); ");
			String levelEntry = scanner.next();

			// If the level entry is valid and matches the service desk levels stored by the
			// system, initialize the level variable with the corresponding value and exit 
			// the selection process
			if (levelEntry.equals("1")) {
				level = Level.ONE;
				break;
			} else if (levelEntry.equals("2")) {
				level = Level.TWO;
				break;
			// If the entry is empty, display an error.
			} else if (levelEntry.equals("")) {
				System.out.println("Error. Service desk level entry cannot be blank.");
			// If the entry does not match a corresponding service desk level, display an
			// error.
			} else {
				System.out.println("Error. Level entry is invalid.");
			}
		}

		// When all of the required details have been entered, and confirmed as valid,
		// initialize a new ticket object and add it to the list of tickets stored by 
		// the system, then inform user.
		tickets.add(new Ticket(description, currentUser.getID(), staffMembers.get(availableTechnician(level)).getID(),
				severity, level));
		System.out.println("\n> Ticket succesfully created and added to ticket list."
							+ "\nPlease await a response from our service team.\n");
	}
	
	// Display a list of tickets assigned to the current user, if they are a Technician
	private void displayAssignedTickets() {
		
		// Check if the current user is a Technician
		if (currentUser instanceof Technician) {
			
			// Initialize a new list to store tickets assigned to the Technician
			ArrayList<Ticket> tempTickets = new ArrayList<>();
			
			// Iterate through the list of tickets
			for (Ticket ticket : tickets) {
				
				// If the current ticket is assigned to the current Technician
				if (ticket.getTechnicianID() == currentUser.getID()) {
					
					// Add the current ticket to the temporary list
					tempTickets.add(ticket);
					
				}
			}
			
			System.out.println("\nASSIGNED TICKETS");
			
			// Iterate through temporary ticket list, and increment an index
			for (int i = 0; i < tempTickets.size(); i++) {
				
				// Print out the current ticket number for selection
				System.out.println("\n> TICKET #" + (i+1));
				// Display the details of the current ticket
				tempTickets.get(i).display();
			}
			
			// Ask user if they would like to edit the severity of a ticket assigned to them
			System.out.print("Would you like to edit the severity of a ticket (Y/N): ");
				
			// While still selecting
			while (true) {
				
				// Receive answer from user
				String input = scanner.next();
				
				// If yes
				if (input.toLowerCase().equals("y")) {
					
					// For user input
					int selection;
					// To store selected tickets
					Ticket tempTicket;
					
					// While input is invalid
					while (true) {
						
						// Request selection input from user
						System.out.print("\nEnter ticket number you would like to edit (1 - " + (tempTickets.size()+1));
					
						try {
							
							// Receive selection from user
							selection = (Integer.parseInt(scanner.next())-1);
							// Assign the selected ticket to the temporary ticket
							tempTicket = tempTickets.get(selection);
							//Exit the while loop
							break;
							
						} catch (NumberFormatException e1) {
							System.out.println("Error. Entry is invalid.");
						} catch (NullPointerException e2) {
							System.out.println("Error. Enter a value in the correct range.");
						}
					}
						
					// While still selecting
					while (true) {
						
						// Ask if user would like to increase or decrease severity of a ticket
						// or exit the selection.
						System.out.println("> What would you like to do:"
											+ "1. Increase severity"
											+ "2. Decrease severity"
											+ "3. EXIT");
						System.out.print("Please select: ");
						
						try {
							
							// Receive user selection input
							selection = Integer.parseInt(scanner.next());
							
						} catch (NumberFormatException e1) {
							System.out.println("Error. Input is invalid.");	
						}
						
						// If the user wishes to decrease ticket severity
						if (selection == 1) {
							// Increase severity of current ticket. If successful, inform user
							// and exit selection process.
							if (tempTicket.increaseSeverityLevel()) {
								System.out.println("Ticket level increased to " 
										+ tempTicket.getSeverity());
								break;
							}
						// If the user wishes to increase ticket severity
						} else if (selection == 2) {
							// Decrease severity of a ticket. If successful, inform user
							// and exit selection process
							if (tempTicket.decreaseSeverityLevel()) {
								System.out.println("Ticket level decreased to " 
										+ tempTicket.getSeverity());
								break;
							}
						// If the user wishes to exit selection process
						} else if (selection == 3) {
							System.out.println("Exiting...");
							break;
						}
					}
					break;
				// If no, exit selection process
				} else if (input.toLowerCase().equals("n")) {
					System.out.println("Exiting...");
					break;
				} else {
					System.out.println("Error. Input is invalid.");
				}	
			}
		}
	}

	private int availableTechnician(Level level) {

		int techIndex = 0;
		int count = 0;
		int techTotal = 0;

		// Iterate through staff member list.
		for (StaffMember technician : staffMembers) {

			// If the current staff member is a technician.
			if (technician instanceof Technician) {

				// If the technician is on the corresponding service desk level.
				if (((Technician) technician).getLevel().equals(level)) {

					// Reset the count.
					count = 0;

					// Iterate through the tickets in the ticket list.
					for (Ticket ticket : tickets) {

						// If the current ticket technician ID is a match for the current
						// technician of the for loop.
						if (ticket.getTechnicianID() == technician.getID()) {

							// Iterate the count.
							count++;
						}
					}
					// If the count is less than the technician with the least number
					// of tickets assigned to them so far.
					if (count < techTotal) {

						// Set the new total;
						techTotal = count;
						// Get the index of the current technician in the staff member list.
						techIndex = staffMembers.indexOf(technician);
					}
				}
			}
		}

		// Return the index of the technician with least number of tickets assigned to
		// them that works on the requested service desk level
		return techIndex;
	}
	
	// To display a list of service tickets created by the user.
	private void displayUserTickets() {
		
		// Temp list of tickets assigned to user.
		ArrayList<Ticket> tempTickets = new ArrayList<>();
		
		// Iterate through all tickets
		for (Ticket ticket : tickets) {
			
			// If the staff ID on the ticket matches the current user
			if (ticket.getStaffID() == currentUser.getID()) {
				
				// Add current ticket to temp tickets
				tempTickets.add(ticket);
				
			}
			
		}
	
		System.out.println("\n> TICKETS CREATED BY USER");
		
		// Iterate through temp tickets
		for (Ticket ticket : tempTickets) {
			
			// Display the ticket information to the console
			ticket.display();
			
		}
		
	}

	/**
	 * Adds the known technicians to the object staffMembers
	 */
	private void seedTechnicians() {
		var technicians = new ArrayList<Technician>() {
			{
				add(new Technician(
						"harry.styles@team.cisco.com",
						"Harry Styles",
						"0425363455",
						"P:\"/|\"\\)1pgi*{tCe#0.",
						Level.ONE));
				add(new Technician(
						"niall.horan@team.cisco.com",
						"Niall Horan",
						"0425411004",
						"7O7|dYH4PbogT?_P&<Hp",
						Level.ONE));
				add(new Technician(
						"liam.payne@team.cisco.com",
						"Liam Payne",
						"0425002266",
						",dP\"_sUQ\\EklO(0s+37c",
						Level.ONE));
				add(new Technician(
						"louis.tomlinson@team.cisco.com",
						"Louis Tomlinson",
						"0425789123",
						"QyDgn2a{1r&5\"F}LIXX&",
						Level.TWO));
				add(new Technician(
						"zayn.malik@team.cisco.com",
						"Zayn Malik",
						"0425121333",
						"W'oA%m%QV#TtYlc*Q_9_",
						Level.TWO));
			}
		};

		this.staffMembers.addAll(technicians);
	}
}
