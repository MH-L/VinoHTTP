import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class DBTransaction {

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
