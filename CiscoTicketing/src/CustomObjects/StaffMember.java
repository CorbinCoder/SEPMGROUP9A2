package CustomObjects;

import CustomObjects.Ticket.Level;
import CustomObjects.Ticket.Severity;

public class StaffMember {
	
	private static int staffIDGenerator = 0;
	private int staffID;
	private String email;
	private int phone;
	private String fName;
	private String lName;
	private String password;
	
	public StaffMember(String email, int phone,
						String fName, String lName, String password) {
		
		this.staffID = ++staffIDGenerator;
		this.email = email;
		this.phone = phone;
		this.fName = fName;
		this.lName = lName;
		this.password = password;
		
	}
	
	public Ticket generateTicket(String description, Severity severity, Level level) {
		
		Ticket temp;
		
		temp = new Ticket(description, this.staffID, 0, severity, level);
		
		return temp;
	}
	
	// Get & Set
	public int getStaffID() {
		return this.staffID;
	}
	
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getPhone() {
		return this.phone;
	}
	
	public void setPhone(int phone) {
		this.phone = phone;
	}
	
	public String getFName() {
		return this.fName;
	}
	
	public void setFName(String fName) {
		this.fName = fName;
	}
	
	public String getLName() {
		return this.lName;
	}
	
	public void setLName(String lName) {
		this.lName = lName;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
