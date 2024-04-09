package CustomObjects;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * The PasswordManager class provides functionality to manage Staff Member
 * Passwords
 */
public class PasswordManager {
	/**
	 * The Default Character Set from which a Password can be generated
	 */
	public static final String CHAR_SET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-=[]\\;',./~!@#$%^&*()_+{}|:\"<>?";

	/**
	 * The Regular Expression String that denotes the required password format
	 */
	public static final String REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[\\w\\p{Punct}]{20,}$";

	/**
	 * The Default Length of a generated Password
	 */
	private static final int DEFAULT_PW_LEN = 20;

	public static boolean isValidPassword(String password) {
		return Pattern.matches(REGEX, password);
	}

	/**
	 * Resets the Password of a Staff Member
	 * 
	 * @param staff The Staff member to generate a new password for
	 * @return The Generated Password
	 */
	public static String resetPassword(StaffMember staff) {
		var newPassword = genPassword();

		staff.setPassword(newPassword);

		return newPassword;
	}

	/**
	 * Generates a random password of DEFAULT_PW_LEN
	 * 
	 * @return A Pseudorandomly generated password
	 */
	public static String genPassword() {
		var rng = new Random();

		StringBuilder pwBuilder;
		var newPassword = "";

		do {
			pwBuilder = new StringBuilder(DEFAULT_PW_LEN);

			for (int i = 0; i < DEFAULT_PW_LEN; i++) {
				var index = rng.nextInt(CHAR_SET.length());
				pwBuilder.append(CHAR_SET.charAt(index));
			}

			newPassword = pwBuilder.toString();

		} while (!isValidPassword(newPassword));

		return newPassword;
	}
}
