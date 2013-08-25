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
    int count = 0;
    int maximumCount;
    KeyStore keyStore;
    String alias;
    char[] correctPassword;
    Exception exception;
    String result; 
    
    public BruteForceTimer(int count, KeyStore keyStore, String alias, char[] correctPassword) {
        this.maximumCount = count;
        this.keyStore = keyStore;
        this.alias = alias;
        this.correctPassword = correctPassword;
    }

    void start(int threadCount) throws Exception {
        List<BruteForceTimer> threadList = new ArrayList();
        long nanos = System.nanoTime();
        for (int i = 0; i < threadCount; i++) {
            BruteForceTimer instance = (BruteForceTimer) this.clone();
            instance.start();
            threadList.add(instance);
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
        logger.info(String.format("threads %d, average %dus", threadCount, 
                nanos/threadCount/maximumCount/1000));
    }
    
    public void run() {
        try {
            call();
        } catch (Exception e) {
            this.exception = e;
        }
    }
    
    void call() throws Exception {
        logger.info("generatePassword " + new String(generatePassword()));
        long correctNanos = System.nanoTime();
        keyStore.getKey(alias, correctPassword);
        correctNanos = Nanos.elapsed(correctNanos);
        long nanos = System.nanoTime();
        int exceptionCount = 0;
        while (count < maximumCount) {
            try {
                char[] password = generatePassword();
                if (count%500 == 0) password = correctPassword;                        
                keyStore.getKey(alias, password);
            } catch (Exception e) {
                errorMessageSet.add(e.getMessage());
                exceptionCount++;
            }
            count++;
        }
        nanos = Nanos.elapsed(nanos);
        long avg = nanos/maximumCount;
        result = String.format(
                "alias %s, count %d, exceptions %d (set %d), correct time %du, avg %du\n", 
                alias, maximumCount, exceptionCount, errorMessageSet.size(), 
                correctNanos/1000, avg/1000, 1000000/avg);
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
