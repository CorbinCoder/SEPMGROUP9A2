package CustomObjects;

import java.util.Random;

/**
 * The PasswordManager class provides functionality to manage Staff Member
 * Passwords
 */
public class PasswordManager {
	/**
	 * The Default Character Set from which a Password can be generated
	 */
	private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-=[]\\/~!@#$%^&*()+{}<>?";

	/**
	 * The Default Length of a generated Password
	 */
	private static final int DEFAULT_PW_LEN = 12;

	/**
	 * Resets the Password of a Staff Member
	 * 
	 * @param staff The Staff member to generate a new password for
	 * @return The Generated Password
	 */
	public static String resetPassword(StaffMember staff) {
		var rng = new Random();

		var pwBuilder = new StringBuilder(DEFAULT_PW_LEN);

		for (int i = 0; i < DEFAULT_PW_LEN; i++) {
			var index = rng.nextInt(CHAR_SET.length());
			pwBuilder.append(CHAR_SET.charAt(index));
		}

		var newPassword = pwBuilder.toString();

		staff.setPassword(newPassword);

		return newPassword;
	}
}
