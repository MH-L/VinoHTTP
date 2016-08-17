import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;

/**
 * Created by Kelvin Liu on Jul. 27th, 2016
 */
public class RedisKeeper {
    /**
     * Get the identity(i.e. username) of the user with token or session ID.
     * @param tokenOrSessionID
     * @return
     */
    public static String getUserIdentity(String tokenOrSessionID) {
        if (tokenOrSessionID == null || tokenOrSessionID.equals(""))
            return null;

        RedisClient client = new RedisClient("localhost");
        RedisConnection<String, String> conn = (RedisConnection<String, String>) client.connect();

        return conn.get(tokenOrSessionID);
    }

    /**
     * Expires the original token and issue a new one.
     * @param originalToken
     * @return
     */
    public static String refreshToken(String originalToken, String username) {
        RedisClient client = new RedisClient("localhost");
        RedisConnection<String, String> conn = (RedisConnection<String, String>) client.connect();
        conn.del(originalToken);

        String loginToken = new SecureRandomGenerator(256).nextSessionId();
        while (conn.exists(loginToken)) {
            loginToken = new SecureRandomGenerator(256).nextSessionId();
        }
        conn.set(loginToken, username);
        return loginToken;
    }
}
