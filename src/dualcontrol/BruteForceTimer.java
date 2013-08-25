package dualcontrol;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import vellum.datatype.Nanos;

/**
 *
 * @author evans
 */
public class BruteForceTimer extends Thread implements Cloneable, Runnable {
    private final static Logger logger = Logger.getLogger(BruteForceTimer.class);
    Random random = new Random();
    Set<String> errorMessageSet = new TreeSet();
    int maximumCount;
    String keyStoreLocation;
    char[] keyStorePass;
    KeyStore keyStore;
    String alias;
    char[] keyPass;
    Exception exception;
    String result; 
    
    public BruteForceTimer(int count, String keyStoreLocation, char[] keyStorePass, 
            String alias, char[] keyPass) {
        this.maximumCount = count;
        this.keyStoreLocation = keyStoreLocation;
        this.keyStorePass = keyStorePass;
        this.alias = alias;
        this.keyPass = keyPass;
    }

    void start(int threadCount) throws Exception {
        List<BruteForceTimer> threadList = new ArrayList();
        for (int i = 0; i < threadCount; i++) {
            threadList.add((BruteForceTimer) this.clone());
        }
        for (int i = 0; i < threadCount; i++) {
            BruteForceTimer thread = threadList.get(i);
            thread.join();
            if (thread.exception != null) {
                logger.error(thread.exception);
            } else {
                logger.info(thread.result);
            }
        }
    }
    
    @Override
    public void run() {
        try {
            call();
        } catch (Exception e) {
            this.exception = e;
        }
    }
    
    void call() throws Exception {
        logger.info("generatePassword " + new String(generatePassword()));
        long correctTime = System.nanoTime();
        keyStore.getKey(alias, keyPass);
        correctTime = Nanos.elapsed(correctTime);
        long time = System.nanoTime();
        int exceptionCount = 0;
        for (int i = 0; i < maximumCount; i++) {
            try {
                char[] password = generatePassword();
                if (i%500 == 0) password = keyPass;                        
                keyStore.getKey(alias, password);
            } catch (Exception e) {
                errorMessageSet.add(e.getMessage());
                exceptionCount++;
            }
        }
        time = Nanos.elapsed(time);
        long avg = time/maximumCount/1000;
        result = String.format(
                "alias %s, count %d, exceptions %d (set %d), correct time %du, avg %du\n", 
                alias, maximumCount, exceptionCount, errorMessageSet.size(), 
                correctTime/1000, avg, 1000/avg);        
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
