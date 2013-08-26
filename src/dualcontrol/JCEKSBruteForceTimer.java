/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import vellum.datatype.Millis;
import vellum.datatype.Nanos;

/**
 *
 * @author evans
 */
public class JCEKSBruteForceTimer extends Thread implements Cloneable, Runnable {
    private final static Logger logger = Logger.getLogger(JCEKSBruteForceTimer.class);
    Random random;
    Set<String> errorMessageSet;
    int maximumCount;
    String keyStoreLocation;
    char[] keyStorePass;
    String alias;
    char[] keyPass;
    Exception exception;
    String result; 
    
    public JCEKSBruteForceTimer(int maximumCount, String keyStoreLocation, char[] keyStorePass, 
            String alias, char[] keyPass) {
        this.random = new Random();
        this.errorMessageSet = new TreeSet();
        this.maximumCount = maximumCount;
        this.keyStoreLocation = keyStoreLocation;
        this.keyStorePass = keyStorePass;
        this.alias = alias;
        this.keyPass = keyPass;
    }

    void start(int threadCount) throws Exception {
        logger.info("keyStoreLocation " + keyStoreLocation);
        logger.info("alias " + alias);
        logger.info("keyPass " + new String(keyPass));
        logger.info("generatePassword " + new String(generatePassword()));
        List<JCEKSBruteForceTimer> threadList = new ArrayList();
        long nanos = System.nanoTime();
        for (int i = 0; i < threadCount; i++) {
            JCEKSBruteForceTimer thread = new JCEKSBruteForceTimer(maximumCount, 
                    keyStoreLocation, keyStorePass, alias, keyPass);
            thread.start();
            threadList.add(thread);
        }
        for (JCEKSBruteForceTimer thread : threadList) {
            thread.join();
            if (thread.exception != null) {
                logger.error(thread.exception);
            } else {
                logger.info(thread.result);
            }
        }
        nanos = Nanos.elapsed(nanos);
        long average = nanos/maximumCount/threadCount;
        System.out.printf("threads %d, count %d, time %s, avg %s\n",
                threadCount, maximumCount, Millis.format(Nanos.toMillis(nanos)), 
                Nanos.formatMillis(average));
        if (average > 0) {
            System.out.printf("%d guesses per millisecond\n", 1000*1000/average);
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
        KeyStore keyStore = DualControlKeyStores.loadLocalKeyStore(keyStoreLocation, keyStorePass);
        long correctNanos = System.nanoTime();
        keyStore.getKey(alias, keyPass);
        correctNanos = Nanos.elapsed(correctNanos);
        long nanos = System.nanoTime();
        int exceptionCount = 0;
        for (int i = 0; i < maximumCount; i++) {
            try {
                char[] password = generatePassword();
                if (i%500 == 0) password = keyPass;                        
                logger.trace("key " + keyStore.getKey(alias, password).getAlgorithm());
            } catch (Exception e) {
                errorMessageSet.add(e.getMessage());
                exceptionCount++;
            }
        }
        nanos = Nanos.elapsed(nanos);
        long average = nanos/maximumCount;
        result = String.format(
                "%s %d, exceptions %d (%d), correct %s, time %s, avg %s\n", 
                alias, maximumCount, exceptionCount, errorMessageSet.size(), 
                Nanos.formatMillis(correctNanos), Nanos.formatMillis(nanos), 
                Nanos.formatMillis(average));
        
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
    
    public static void main(String[] args) throws Exception {
        if (args.length != 6) {
            System.err.println("usage: threads count keystore storepass alias keypass"); 
        } else {
            new JCEKSBruteForceTimer(Integer.parseInt(args[1]), args[2], args[3].toCharArray(),
                    args[4], args[5].toCharArray()).start(Integer.parseInt(args[0]));
        }
    }
    
}
