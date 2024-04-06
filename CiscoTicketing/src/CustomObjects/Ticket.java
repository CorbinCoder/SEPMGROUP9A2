package CustomObjects;

public class Ticket {
	
	public static enum Severity {
		LOW, MEDIUM, HIGH;
	}
	
	public static enum Level {
		ONE, TWO;
	}
	
	private String description;
	private int staffID;
	private int technicianID;
	
	private Severity severity;
	
	private Level level;
	
	public Ticket(String description, int staffID, int technicianID,
					Severity severity, Level level) {
			
			this.description = description;
			this.staffID = staffID;
			this.technicianID = technicianID;
			this.severity = severity;
			this.level = level;
	}
	
	public void increaseLevel() {
		if (this.level.equals(Level.ONE)) {
			this.level = Level.TWO;
		} else {
			System.out.println("Ticket is already at max level");
		}
	}
	
	public void decreaseLevel() {
		if (this.level.equals(Level.TWO)) {
			this.level = Level.ONE;
		} else {
			System.out.println("Ticket is already at min level");
		}
	}
	
	// Get & Set
	public  Level getLevel() {
		return this.level;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Severity getSeverity() {
		return this.severity;
	}
	
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
	
	public int getStaffID() {
		return this.staffID;
	}
	
	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}
	
	public int getTechnicianID() {
		return this.technicianID;
	}
	
	public void setTechnicianID(int technicianID) {
		this.technicianID = technicianID;
	}
	
}
