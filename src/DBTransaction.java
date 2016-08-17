public class DBTransaction {
	public static boolean storeWine(int wineID, String wineName) {
		return false;
	}

	public static boolean deleteWine(int wineID) {
		return false;
	}

	public static int createUser(String username, String password, String email,
			String city, boolean isAdmin) {
		if (!(SanityChecker.isPasswordOK(password)))
			return RegisterUserHandler.PASSWORD_INVALID;
        if (!(SanityChecker.isUsernameOK(username)))
            return RegisterUserHandler.INVALID_USERNAME;
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session ss = sf.openSession();
        Transaction tx = ss.beginTransaction();
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(password);
        String sqlQueryStr = "INSERT INTO User(name, administrator, email, password, city) " +
                "VALUES (\'%s\', false, \'%s\', \'%s\', %s)";
        username = Utility.escapeSQL(username);
        encryptedPassword = Utility.escapeSQL(encryptedPassword);
        System.out.println(encryptedPassword);
        email = Utility.escapeSQL(email);
        if (city != null) {
            city = Utility.escapeSQL(city);
            city = "\'" + city + "\'";
        }
        sqlQueryStr = String.format(sqlQueryStr, username, email, encryptedPassword, city);

        Query sqlQuery = ss.createSQLQuery(sqlQueryStr);
        try {
            sqlQuery.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getClass());
            return RegisterUserHandler.USERNAME_EXISTS;
        }

        tx.commit();
        ss.close();
		return RegisterUserHandler.ACCOUNT_CREATED_SUCCESSFULLY;
	}

    public static boolean checkIsAdmin(String username) {
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session ss = sf.openSession();
        ss.beginTransaction();
        String hqlStr = "From User WHERE username = :uname";
        Query hqlQuery = ss.createQuery(hqlStr);
        hqlQuery.setParameter("uname", username);
        try {
            User correspondingUser = (User) hqlQuery.uniqueResult();
            ss.close();
            return correspondingUser.isAdministrator();
        } catch (Exception e) {
            return false;
        }
    }

}
