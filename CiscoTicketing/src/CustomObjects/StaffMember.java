package CustomObjects;

import CustomObjects.Ticket.Level;
import CustomObjects.Ticket.Severity;

// A custom class used to define a staff member object, that may create service
// tickets, and add them to the system.
public class StaffMember {

	// Declare static variable to generate each staff member ID.
  	private static int staffIDGenerator = 0;
  	
  	// Declare variables used to store staff member information.
  	private int staffID;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String password;

    // Constructor for staff member object.
    public StaffMember(String email, String fullName, String phoneNumber, String password) {
    	
    	// Increment staff ID generator then assign to new staff member.
        this.staffID = ++staffIDGenerator;
        
        // Staff member email, full name, phone number, and password. Variables are verified
        // by the controller class.
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
  
    // So a staff member may generate a ticket. Performed in staff member, not in controlling
    // class so that the staff ID may be directly added to the ticket without need for another
    // method to determine the staff ID.
  	public Ticket generateTicket(String description, int assignedTo, Severity severity, Level level) {
		
  		// Declare and initialize a new ticket object, and return it to the calling class.
		Ticket temp = new Ticket(description, this.staffID, assignedTo, severity, level);
		
		return temp;
	  }

    // Getters
  	public int getID() {
  		return this.staffID;
  	}
  	
    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "StaffMember{" +
                "email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}