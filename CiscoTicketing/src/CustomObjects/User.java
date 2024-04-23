package CustomObjects;

import java.util.ArrayList;
import java.util.Scanner;

import Main.TicketingSystem;

public abstract class User {
	private String email;
	private String fullName;
	private String phoneNumber;
	private String password;
	protected ArrayList<Ticket> tickets;
	protected Scanner scanner;

	public User(String email, String fullName, String phoneNumber, String password) {
		this.email = email;
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.tickets = new ArrayList<>();
		this.scanner = TicketingSystem.getInstance().getScanner();
	}

	public void displayUserTickets() {
		System.out.println("\n> USER TICKETS");
		for (Ticket ticket : this.tickets) {
			ticket.display();
		}
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

	public ArrayList<Ticket> getTickets() {
		return tickets;
	}

	// teting method remove from production
	public void addTicket(Ticket ticket) {
		this.tickets.add(ticket);
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

	public void removeArchivedTickets() {
		this.tickets.removeIf(ticket -> ticket.isArchived());
	}

	public abstract void options();
}