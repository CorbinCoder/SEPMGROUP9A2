package CustomObjects;
import CustomObjects.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.InputMismatchException;

public class TicketingSystem {
    private static final TicketingSystem INSTANCE = new TicketingSystem();
    private StaffMember currentStaffMember; 
    private final ArrayList<StaffMember> staffMembers = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    private TicketingSystem() {}

    public static TicketingSystem getInstance() {
        return INSTANCE;
    }

    public ArrayList<StaffMember> getStaffMembers() {
        return staffMembers;
    }
    public void setCurrentStaffMember(StaffMember staffMember) {
        this.currentStaffMember = staffMember;
    }
    
    public void displayOptions() {
        int choice;

        while (true) {
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

    public void staffMemberLogin() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (StaffMember sm : staffMembers) {
            if (sm.getEmail().equals(email) && sm.getPassword().equals(password)) {
                this.setCurrentStaffMember(sm);
                System.out.println("Login successful. Welcome, " + sm.getFullName() + "!");
                return;
            }
        }
        System.out.println("Login failed. Please check your credentials and try again.");
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{20,}$";
        return password.matches(passwordRegex);
    }

    private boolean isUnique(Predicate<StaffMember> predicate) {
        return staffMembers.stream().noneMatch(predicate);
    }
}
