package CustomObjects;

import CustomObjects.Ticket.Level;
import CustomObjects.Ticket.Severity;

public class StaffMember {

  	private static int staffIDGenerator = 0;
  	private int staffID;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String password;

    public StaffMember(String email, String fullName, String phoneNumber, String password) {
        this.staffID = ++staffIDGenerator;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
  
  	public Ticket generateTicket(String description, Severity severity, Level level) {
		
		Ticket temp = new Ticket(description, this.staffID, 0, severity, level);
		
		return temp;
	  }

    // Getters
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