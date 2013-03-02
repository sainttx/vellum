/*
 */
package vellum.crypto;

import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evans
 */
public class IterationCountManager {
    private static Logr logger = LogrFactory.getLogger(IterationCountManager.class);
    private long hashCount = 0;
    private long totalMillis = 0;
    private int initialIterationCount;
    private int iterationCount;
    private final long thresholdMillis; 

    public IterationCountManager(int iterationCount, long thresholdMillis) {
        this.initialIterationCount = iterationCount;
        this.thresholdMillis = thresholdMillis;
        this.iterationCount = iterationCount;
    }
    
    public void handleMillis(long millis) {
        hashCount++;
        totalMillis += millis;
        long averageMillis = totalMillis / hashCount;
        if (averageMillis < thresholdMillis/2) {
            iterationCount += 1000;
            logger.warn("iterationCount increased", iterationCount);
        } else if (averageMillis > thresholdMillis) {
            iterationCount -= 10;
            logger.warn("millis above threshold", millis);
        }
    }

    public int getIterationCount() {
        if (hashCount > 1000 && iterationCount > initialIterationCount*5/4) {
            return iterationCount % 1000;  
        } else {
            return initialIterationCount;
        }
    }        
}
