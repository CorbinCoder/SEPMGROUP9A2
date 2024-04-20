package CustomObjects;

import Main.TicketingSystem; 
import java.util.Date;
import java.util.List;

public class TicketArchiver implements Runnable {
    private int checkInterval = 600000; // 10 minutes in milliseconds
    private TicketingSystem ticketingSystem;

    public TicketArchiver(TicketingSystem system) {
        this.ticketingSystem = system;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println("Checking and archiving tickets...");
                archiveClosedTickets();
                Thread.sleep(checkInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted, failed to complete operation");
            }
        }
    }

    private void archiveClosedTickets() {
        Date now = new Date();
        long twentyFourHours = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

        List<Ticket> tickets = ticketingSystem.getAllTickets(); 
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == Ticket.Status.CLOSE_AND_RESOLVED || ticket.getStatus() == Ticket.Status.CLOSED_AND_UNRESOLVED) {
                long timeSinceClosed = now.getTime() - ticket.getClosureTime().getTime(); 
                if (!ticket.isArchived() && timeSinceClosed > twentyFourHours) { 
                    ticketingSystem.archiveTicket(ticket); 
                }
            }
        }
    }

    public static void main(String[] args) {
        TicketingSystem system = TicketingSystem.getInstance(); 
        Thread archiverThread = new Thread(new TicketArchiver(system));
        archiverThread.start();
        System.out.println("Ticket Archiver is running...");
    }
}
