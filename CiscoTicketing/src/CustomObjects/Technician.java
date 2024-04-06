package CustomObjects;

import CustomObjects.Ticket.Level;

// A custom class that allows a staff member to be declared as a technician,
// allowing them to perform service tasks, and have tickets assigned to them.
public class Technician extends StaffMember{
	
	// Indicates the service desk level that the technician sits on.
	private Level level;

	// Constructor for technician class. Uses super to initialize variables from
	// parent class, and takes level as an additional variable.
	public Technician(String email, String fullName, String phoneNumber, String password, Level level) {
		super(email, fullName, phoneNumber, password);
		this.level = level;
	}
	
	// Get & Set
	public Level getLevel() {
		return this.level;
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}
}
