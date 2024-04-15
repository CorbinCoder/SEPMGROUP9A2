package Main;

import CustomObjects.*;
import CustomObjects.Ticket.Level;
import CustomObjects.Ticket.Severity;
import CustomObjects.Ticket.Status;

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
			} else if (currentUser != null) {

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

			String password = "";
			if (System.console() == null) {
				// If System.console() is null (Running from IDE - Eclipse/IntelliJ)
				password = scanner.nextLine();
			} else {
				password = new String(System.console().readPassword());
			}

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
		String password = "";
		if (System.console() == null) {
			// If System.console() is null (Running from IDE - Eclipse/IntelliJ)
			password = scanner.nextLine();
		} else {
			password = new String(System.console().readPassword());
		}

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

	    // Input for ticket description.
	    while (true) {
	        System.out.print("> Please enter a description of the issue: ");
	        description = scanner.next();

	        if (description.equals("")) {
	            System.out.println("Error. Description cannot be empty.");
	        } else if (description.length() > 250) {
	            System.out.println("Error. Description cannot be greater than 250 characters.");
	        } else {
	            break;
	        }
	    }

	    // Input for ticket severity.
	    while (true) {
	        System.out.print("> Please enter the severity of the issue (1=LOW, 2=MEDIUM, 3=HIGH): ");
	        String severityEntry = scanner.next();

	        if (severityEntry.equals("1")) {
	            severity = Severity.LOW;
	            level = Level.ONE;  // Assign to Level ONE technician
	            break;
	        } else if (severityEntry.equals("2")) {
	            severity = Severity.MEDIUM;
	            level = Level.ONE;  // Assign to Level ONE technician
	            break;
	        } else if (severityEntry.equals("3")) {
	            severity = Severity.HIGH;
	            level = Level.TWO;  // Assign to Level TWO technician
	            break;
	        } else if (severityEntry.equals("")) {
	            System.out.println("Error. Severity entry cannot be empty.");
	        } else {
	            System.out.println("Error. Severity entry is invalid.");
	        }
	    }

	  // Assigning a technician based on the determined level and creating a ticket.
    Technician assignedTechnician = availableTechnician(level);
    if (assignedTechnician != null) {
        tickets.add(new Ticket(description, currentUser.getID(), assignedTechnician.getID(), severity, level));
        System.out.println("\n> Ticket successfully created and assigned to " + assignedTechnician.getFullName()
                           + "\nPlease await a response from our service team.\n");
	    } else {
	        System.out.println("No available technicians to handle this ticket.");
	    }
	}

	// Display a list of tickets assigned to the current user, if they are a
	// Technician
	private void displayAssignedTickets() {
	        
		Ticket selectedTicket;
		
	        while (true) {

	    	    if (currentUser instanceof Technician) {
	    	        ArrayList<Ticket> tempTickets = new ArrayList<>();
	    	        for (Ticket ticket : tickets) {
	    	            if (ticket.getTechnicianID() == currentUser.getID()) {
	    	                tempTickets.add(ticket);
	    	            }
	    	        }

	    	        System.out.println("\nASSIGNED TICKETS");
	    	        for (int i = 0; i < tempTickets.size(); i++) {
	    	            System.out.println("\n> TICKET #" + (i + 1));
	    	            tempTickets.get(i).display();
	    	        }
	        	
	        	var input = 0;
	        	
	        	System.out.println("> What would you like to do?\n"
	        						+ "1. Edit ticket severity"
	        						+ "2. Update ticket status"
	        						+ "3. Leave comment");
	        	System.out.print("Please select: ");
	        	
	        	try {
	        		input = Integer.parseInt(scanner.next());	
	        	} catch (NumberFormatException e1) {
	        		System.out.println("Error. Input is invalid");
	        	}
	        	
	        	switch(input) {
	        	case 1:
	        		selectedTicket = chooseTicketFromList(tempTickets);
	        		changeTicketSeverity(selectedTicket);
	        		break;
	        	case 2:
	        		selectedTicket = chooseTicketFromList(tempTickets);
	        		changeTicketStatus(selectedTicket);
	        		break;
	        	case 3:
//	        		leaveComment(tempTicket);
	        		break;
        		default:
        			System.out.println("Error. Input is invalid");
	        	}
	        }
	    }
	}
	
	private void changeTicketSeverity(Ticket selectedTicket) {
		
		while (true) {
			try {

	             System.out.println("What would you like to do:"
	                                + "\n1. Increase severity"
	                                + "\n2. Decrease severity"
	                                + "\n3. EXIT");
	             System.out.print("Please select: ");
	             int choice = Integer.parseInt(scanner.next());

	             if (choice == 1) {
	                 if (selectedTicket.increaseSeverityLevel()) {
	                     updateTicketSeverity(selectedTicket.getID(), selectedTicket.getSeverity());
	                     System.out.println("Ticket severity increased to " + selectedTicket.getSeverity() + " and reassigned if necessary.");
	                 }
	             } else if (choice == 2) {
	                 if (selectedTicket.decreaseSeverityLevel()) {
	                     updateTicketSeverity(selectedTicket.getID(), selectedTicket.getSeverity());
	                     System.out.println("Ticket severity decreased to " + selectedTicket.getSeverity());
	                 }
	             } else if (choice == 3) {
	                 System.out.println("Exiting...");
	                 break;
	             }

	         } catch (NumberFormatException | IndexOutOfBoundsException e) {
	             System.out.println("Error. Invalid input. Please enter a valid ticket number.");
	         }
		}
	}
	
	private void changeTicketStatus(Ticket ticket) {
		
		int input = 0;
		
		System.out.println("Current status of ticket is: " + ticket.getStatus().toString());
		
		System.out.println("> Select new status for ticket"
				+ "1. New"
				+ "2. In progress"
				+ "3. Resolved"
				+ "4. Exit");
		System.out.print("Please select: ");

		try {
			input = Integer.parseInt(scanner.next());
		} catch(NumberFormatException e1) {
			System.out.println("Error. Input is invalid");
		}
		
		switch (input) {
		case 1:
			ticket.setStatus(Status.NEW);
			break;
		case 2:
			ticket.setStatus(Status.IN_PROGRESS);
			break;
		case 3:
			ticket.setStatus(Status.RESOLVED);
			break;
		default:
			System.out.println("Error. Input is invalid");
			return;
		}
		
		System.out.println("Ticket status updated to " + ticket.getStatus().toString());
		
	}
	
	private Ticket chooseTicketFromList(ArrayList<Ticket> assignedTickets) {
		
		Ticket choice = null;
		int input = 0;
		
		System.out.println("Please choose a ticket (1 - " + assignedTickets.size());
		
		try {
			
			input = (Integer.parseInt(scanner.next())-1);
				
		} catch (NumberFormatException e1) {
			System.out.println("Error. Input is invalid");
		}
		
		if (input >= 0 && input <= assignedTickets.size()) {
			choice = assignedTickets.get(input);
		}

		return choice;	
	}

	private Technician availableTechnician(Level level) {
	    Technician leastBusyTech = null;
	    int minTicketCount = Integer.MAX_VALUE;

	    for (StaffMember member : staffMembers) {
	        if (member instanceof Technician) {
	            Technician tech = (Technician) member;
	            if (tech.getLevel() == level) {
	                int count = 0;
	                for (Ticket ticket : tickets) {
	                    if (ticket.getTechnicianID() == tech.getID()) {
	                        count++;
	                    }
	                }
	                if (count < minTicketCount) {
	                    minTicketCount = count;
	                    leastBusyTech = tech;
	                }
	            }
	        }
	    }
	    return leastBusyTech; // Return the Technician with the least tickets, or null if none found
	}

	// Helper method to find a ticket by ID (assuming such a method does not exist yet)
	private Ticket findTicketById(int ticketId) {
	    for (Ticket ticket : tickets) {
	        if (ticket.getID() == ticketId) {
	            return ticket;
	        }
	    }
	    return null; // Ticket not found
	}	
	
	public void updateTicketSeverity(int ticketId, Severity newSeverity) {
	    Ticket ticketToUpdate = findTicketById(ticketId);

	    if (ticketToUpdate != null) {
	        Severity oldSeverity = ticketToUpdate.getSeverity();
	        Level oldLevel = ticketToUpdate.getLevel();

	        ticketToUpdate.setSeverity(newSeverity);

	        if ((newSeverity == Severity.HIGH && oldLevel != Level.TWO) ||
	            (newSeverity != Severity.HIGH && oldLevel != Level.ONE)) {

	            Level newLevel = (newSeverity == Severity.HIGH) ? Level.TWO : Level.ONE;

	            if (newLevel != oldLevel) {
	                Technician newTechnician = availableTechnician(newLevel);

	                if (newTechnician != null) {
	                    ticketToUpdate.setTechnicianID(newTechnician.getID());
	                    ticketToUpdate.setLevel(newLevel);
	                    System.out.println("Ticket severity updated and reassigned to " + newTechnician.getFullName());
	                } else {
	                    System.out.println("No available technicians to handle the updated ticket.");
	                }
	            } else {
	                System.out.println("Ticket severity updated but no reassignment needed.");
	            }
	        } else {
	            System.out.println("Ticket severity updated without changing level.");
	        }
	    } else {
	        System.out.println("Ticket not found.");
	    }
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
