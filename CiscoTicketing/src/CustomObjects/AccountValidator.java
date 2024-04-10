package CustomObjects;

import Main.TicketingSystem;

public class AccountValidator {

	public static boolean validateLoginDetails(String email, String password) {
		for (StaffMember sm : TicketingSystem.getInstance().getStaffMembers()) {
			if (sm.getEmail().equals(email) && PasswordManager.verifyStaffPassword(sm, password)) {
				TicketingSystem.getInstance().setCurrentStaffMember(sm);
				return true;
			}
		}
		return false;
	}
}
