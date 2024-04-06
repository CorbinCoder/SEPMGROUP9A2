package CustomObjects;

public class AccountValidator {

    public static boolean validateLoginDetails(String email, String password) {
        for (StaffMember sm : TicketingSystem.getInstance().getStaffMembers()) {
            if (sm.getEmail().equals(email) && sm.getPassword().equals(password)) {
                TicketingSystem.getInstance().setCurrentStaffMember(sm);
                return true;
            }
        }
        return false;
    }
}
