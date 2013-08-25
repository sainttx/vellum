package dualcontrol;

import java.security.KeyStore;
import java.util.Random;
import org.apache.log4j.Logger;
import vellum.datatype.Millis;
import vellum.datatype.Nanos;

/**
 *
 * @author evans
 */
public class BruteForceTimer {
    private final static Logger logger = Logger.getLogger(BruteForceTimer.class);
    Random random = new Random();
    
    void test(int count, KeyStore keyStore, String alias, char[] correctPassword) throws Exception {
        logger.info("generatePassword " + new String(generatePassword()));
        long correctTime = System.nanoTime();
        keyStore.getKey(alias, correctPassword);
        correctTime = Nanos.elapsed(correctTime);
        long time = System.nanoTime();
        int exceptionCount = 0;
        for (int i = 0; i < count; i++) {
            try {
                char[] password = generatePassword();
                if (i%200 == 0) password = correctPassword;                        
                keyStore.getKey(alias, password);
            } catch (Exception e) {
                exceptionCount++;
            }
        }
        logger.info(String.format(
                "alias %s, count %d, exceptions %d, correct time %d us, average time %d us\n", 
                alias, count, exceptionCount, correctTime/1000, Nanos.elapsed(time)/count/1000));
    }

    char[] generatePassword() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            builder.append(randomChar());
        }
        return builder.toString().toCharArray();
    }
    
    char randomChar() {
        char first = 'a';
        char last = 'z';
        return (char) (first + (random.nextInt(last - first)));
    }
}
