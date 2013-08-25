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
    String alias;
    char[] keyPass;
    Exception exception;
    String result; 
    
    public BruteForceTimer(int maximumCount, String keyStoreLocation, char[] keyStorePass, 
            String alias, char[] keyPass) {
        this.maximumCount = maximumCount;
        this.keyStoreLocation = keyStoreLocation;
        this.keyStorePass = keyStorePass;
        this.alias = alias;
        this.keyPass = keyPass;
    }

    void start(int threadCount) throws Exception {
        List<BruteForceTimer> threadList = new ArrayList();
        long nanos = System.nanoTime();
        for (int i = 0; i < threadCount; i++) {
            BruteForceTimer thread = (BruteForceTimer) this.clone();
            thread.start();
            threadList.add(thread);
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
        nanos = Nanos.elapsed(nanos);
        long averagePerSecond = nanos/maximumCount/threadCount/1000;
        logger.info(String.format("threads %d, count %d, time %dms, avg %d/s\n",
                threadCount, maximumCount, nanos/1000/1000, averagePerSecond));
        if (averagePerSecond > 0) {
            logger.info("guesses per second " + 1000/averagePerSecond);
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
        KeyStore keyStore = DualControlKeyStores.loadKeyStore(keyStoreLocation, keyStorePass);
        logger.info("generatePassword " + new String(generatePassword()));
        long correctTime = System.nanoTime();
        keyStore.getKey(alias, keyPass);
        correctTime = Nanos.elapsed(correctTime);
        long nanos = System.nanoTime();
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
        nanos = Nanos.elapsed(nanos);
        long averagerPerSecond = nanos/maximumCount/1000;
        result = String.format(
                "alias %s, count %d, exceptions %d (set %d), correct time %dus, avg %d/s\n", 
                alias, maximumCount, exceptionCount, errorMessageSet.size(), 
                correctTime/1000, averagerPerSecond);       
        
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
