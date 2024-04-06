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

    private TicketingSystem() {}

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
                System.out.println("3. Exit");

                System.out.print("Enter your choice (1-3): ");
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
            				// To be implemented.
//            				displayUserTickets();
            				break;
            			case 3:
            				// To be implemented.
//            				displayAssignedTickets();
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
        	} else {
        		
        		System.out.println("Select an option:");
            	System.out.println("1. Log support ticket");
            	System.out.println("2. View your tickets");
            	System.out.println("3. Exit");
            	try {
            		choice = scanner.nextInt();
            		scanner.nextLine();
            		
            		switch (choice) {
            			case 1:
            				createTicket();
            				break;
            			case 2:
            				// To be implemented.
//            				displayUserTickets();
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
            String password = scanner.nextLine();

            if (isValidPassword(password)) {
                return password;
            } else {
                System.out.println("Invalid password. Please try again.");
            }
        }
    }
    
    // So that a user may generate a service ticket, and add it to the list of current tickets.
    private void createTicket() {
    	
    	// Declare variables used to create ticket.
    	String description;
    	String severityEntry;
    	Severity severity;
    	String levelEntry;
    	Level level;
    	
    	// While input is invalid, continue to request input.
    	while (true) {
    		
    		// Request and receive input for ticket description.
    		System.out.print("Please enter a decription of the issue: ");
        	description = scanner.next();
        	
        	// If the input is empty, display an error.
        	if (description.equals("")) {

        		System.out.println("Error. Description cannot be empty.");
        		
        	// If the description is greater than 250 characters, do not accept and inform user.
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
    		System.out.print("Please enter the severity of the issue (1=LOW, 2=MEDIUM, 3=HIGH): ");
        	severityEntry = scanner.next();
        	
        	// If the severity entry is valid and matches a severity level stored by the system,
        	// initialize the severity variable with the corresponding value entered by the user.
        	// Then exit selection process.
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
        		System.out.println("Error. Severity entry cannot be invalid.");
        	// If the entry does not match a corresponding severity level, inform user.
        	} else {
        		System.out.println("Error. Severity entry is invalid.");
        	}
    	}
    	
    	// While input is invalid, continue to request input.
    	while (true) {
    		
    		// Request and receive input for ticket service desk level.
    		System.out.print("Please enter which level of service desk you would like to send the ticket to (1, 2); ");
        	levelEntry = scanner.next();
        	
        	// If the level entry is valid and matches the service desk levels stored by the system,
        	// initialize the level variable with the corresponding value and exit the selection process.
        	if (levelEntry.equals("1")) {
        		level = Level.ONE;
        		break;
        	} else if (levelEntry.equals("2")) {
        		level = Level.TWO;
        		break;
        	// If the entry is empty, print an error.
        	} else if (levelEntry.equals("")) {
        	System.out.println("Error. Service desk level entry cannot be blank.");
        	// If the entry does not match a corresponding service desk level, print an error.
        	} else {
        		System.out.println("Error. Level entry is invalid.");
        	}
    	}
    		
    	// When all of the required details have been entered, and confirmed as valid, initialize a
    	// new ticket object and add it to the list of tickets stored by the system, then inform user.
		tickets.add(new Ticket(description, currentUser.getID(), staffMembers.get(availableTechnician(level)).getID(), severity, level));
		System.out.println("Ticket succesfully created and added to ticket list."
							+ "Please await a response from our service team.");
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
   
    	// Return the index of the technician with least number of tickets assigned to them that
    	// works on the requested service desk level.
    	return techIndex;
    	
    }

    private void staffMemberLogin() {

    }
    
    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{20,}$";
        return password.matches(passwordRegex);
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
}