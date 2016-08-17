public class SanityChecker {
	/**
	 * Username regex checker. Username can only contain numbers, alphabet letters,
	 * and underscores. The length must be between 3 and 20.
	 * @param username
	 * @return
	 */
	public static boolean isUsernameOK(String username) {
		return username.matches("^[a-zA-Z0-9._-]{3,20}$");
	}

	/**
	 * Password regex checker. Password must contain at least 8 chars with
	 * at least a number, a letter and a special character. The length must
	 * be between 8 and 18.
	 * @param password
	 * @return
	 */
	public static boolean isPasswordOK(String password) {
		return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,18}$");
	}
}
