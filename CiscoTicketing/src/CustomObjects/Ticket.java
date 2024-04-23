package CustomObjects;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// A custom class that allows a service ticket object to be generated by a user,
// added to a list of current tickets, and assigned to a service technician.
public class Ticket {

	public static enum Severity {
		LOW, MEDIUM, HIGH;
	}

	public static enum Status {
		OPEN, CLOSE_AND_RESOLVED, CLOSED_AND_UNRESOLVED;
	}

	// Declare variables used to store ticket information.
	private static int ticketIDGenerator;
	private String description;
	private int ticketID;
	private int staffID;
	private int technicianID;
	private String staffName;
	private String technicianName;
	private Status status;
	private Severity severity;
	private LocalDateTime creationTime;
	private LocalDateTime closureTime;
	private boolean archived;
	private Duration openDuration;

	// Constructor for ticket object.
	public Ticket(String description, String staffName, Severity severity) {

		// Contains a description of the issue, the ID of the staff member that
		// created the ticket, the ID of the technician it has been issued to, the
		// severity of the issue, and the service desk level for it to be issued to.
		this.ticketID = ++ticketIDGenerator;
		this.description = description;
		this.staffName = staffName;
		this.severity = severity;
		this.status = Status.OPEN;
		this.creationTime = LocalDateTime.now();
		this.archived = false;
	}

	// test data constructor
	public Ticket(String description, String staffName, Severity severity, LocalDateTime creationTime) {

		// Contains a description of the issue, the ID of the staff member that
		// created the ticket, the ID of the technician it has been issued to, the
		// severity of the issue, and the service desk level for it to be issued to.
		this.ticketID = ++ticketIDGenerator;
		this.description = description;
		this.staffName = staffName;
		this.severity = severity;
		this.status = Status.OPEN;
		this.creationTime = creationTime;
		this.archived = false;
		this.openDuration = null;
	}

	public void escalateSeverity() {
		if (this.severity != Severity.HIGH) {
			switch (this.severity) {
			case LOW:
				this.severity = Severity.MEDIUM;
				System.out.println("Ticket severity escalated from LOW to MEDIUM.");
				break;
			case MEDIUM:
				this.severity = Severity.HIGH;
				System.out.println("Ticket severity escalated from MEDIUM to HIGH.");
				break;
			default:
				break;
			}
		} else {
			System.out.println("Ticket severity is already at the highest level (HIGH).");
		}
	}

	public void deEscalateSeverity() {
		if (this.severity != Severity.LOW) {
			switch (this.severity) {
			case MEDIUM:
				this.severity = Severity.LOW;
				System.out.println("Ticket severity de-escalated from MEDIUM to LOW.");
				break;
			case HIGH:
				this.severity = Severity.MEDIUM;
				System.out.println("Ticket severity de-escalated from HIGH to MEDIUM.");
				break;
			default:
				break;
			}
		} else {
			System.out.println("Ticket severity is already at the lowest level (LOW).");
		}
	}

	// Display ticket information over several lines.
	public void display() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String closureTime = (this.closureTime != null) ? this.closureTime.format(formatter) : "N/A";
		String openDurtaionString = "N/A";
		if (this.openDuration != null) {
			int openDurationMinutes = (int) this.openDuration.toMinutes();
			openDurtaionString = openDurationMinutes < 60 ? String.valueOf(openDurationMinutes) + " minutes"
					: String.valueOf(openDurationMinutes / 60) + " hours";
		}
		System.out.println("\nTicket ID: " + this.ticketID + "\nTicket Creator: " + this.staffName + "\nAssigned Tech: "
				+ this.technicianName + "\nDescription: " + this.description + "\nTicket Severity: "
				+ this.severity.toString() + "\nTicket Status: " + this.status.toString() + "\n" + "Opened at: "
				+ this.creationTime.format(formatter) + "\n" + "Closed at: " + closureTime + "\n"
				+ "Time ticket open (minutes): " + openDurtaionString + "\n" + "Archived: " + this.archived + "\n");
	}

	// Get & Set
	public int getID() {
		return this.ticketID;
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
		if (severity != null) {
			this.severity = severity;
		} else {
			System.out.println("Invalid severity value. Severity not updated.");
		}
	}

	public String getStaffName() {
		return this.staffName;
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

	public void setTechnicianName(String technicianName) {
		this.technicianName = technicianName;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setArchived(boolean status) {
		this.archived = status;
	}

	public void setClosureTime(LocalDateTime time) {
		this.closureTime = time;
	}

	public void setStatus(Status status) {
		Status prevStatus = this.status;
		if (status == Status.CLOSE_AND_RESOLVED || status == Status.CLOSED_AND_UNRESOLVED) {
			this.closureTime = LocalDateTime.now();
		}
		if (prevStatus == Status.OPEN
				&& (status == Status.CLOSE_AND_RESOLVED || status == Status.CLOSED_AND_UNRESOLVED)) {
			this.openDuration = Duration.between(this.creationTime, LocalDateTime.now());
		}
		this.status = status;
	}

	public LocalDateTime getClosureTime() {
		return this.closureTime;
	}

	public boolean isArchived() {
		return this.archived;
	}
}
