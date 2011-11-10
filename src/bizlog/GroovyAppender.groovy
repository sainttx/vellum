/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */

package bizmonger
import org.apache.log4j.AppenderSkeleton
import org.apache.log4j.Level
import org.apache.log4j.spi.LoggingEvent

/**
 *
 * @author evans
 */
class GroovyMonAppender extends AppenderSkeleton {
	
    protected void append(LoggingEvent le) {
        println "Groovy ${le.getMessage()}"
    }

    public boolean requiresLayout() {
        return true;
    }

    @Override
    public void close() {
    }

    public String toString() {
        return "1.0"
    }
}

