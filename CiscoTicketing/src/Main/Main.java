package Main;
import CustomObjects.TicketingSystem;

public class Main {
	public static void main(String[] args) {
		TicketingSystem ticketingSystem = TicketingSystem.getInstance();
		ticketingSystem.displayOptions();
	}
}