import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Minghao on 3/27/2016.
 */
public final class SecureRandomGenerator {
    private SecureRandom random = new SecureRandom();
    private int length = 10;

    public SecureRandomGenerator(int length) {
        this.length = length;
    }

    public String nextSessionId() {
        return new BigInteger(length, random).toString(32);
    }
}
