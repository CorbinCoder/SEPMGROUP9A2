package CustomObjects;

import java.util.function.Predicate;

import Main.TicketingSystem;

public class AccountValidator {

	public static boolean validateLoginDetails(String email, String password) {
		for (User usr : TicketingSystem.getInstance().getUsers()) {
			if (usr.getEmail().equals(email) && PasswordManager.verifyStaffPassword(usr, password)) {
				TicketingSystem.getInstance().setCurrentUser(usr);
				return true;
			}
		}
		return false;
	}

	public static boolean validateResetPassword(String email, String fullName, String phoneNumber) {
		for (User usr : TicketingSystem.getInstance().getUsers()) {
			if (usr.getEmail().equals(email) && usr.getFullName().equals(fullName)
					&& usr.getPhoneNumber().equals(phoneNumber)) {
				return true;
			}
		}
		return false;

	}

	public static boolean validateUniqueDetails(String email, String fullName, String phoneNumber) {
		Predicate<User> emailPredicate = sm -> sm.getEmail().equalsIgnoreCase(email);
		Predicate<User> fullNamePredicate = sm -> sm.getFullName().equalsIgnoreCase(fullName);
		Predicate<User> phoneNumberPredicate = sm -> sm.getPhoneNumber().equalsIgnoreCase(phoneNumber);

		return isUnique(emailPredicate) && isUnique(fullNamePredicate) && isUnique(phoneNumberPredicate);
	}

	private static boolean isUnique(Predicate<User> predicate) {
		return TicketingSystem.getInstance().getUsers().stream().noneMatch(predicate);
	}
}
