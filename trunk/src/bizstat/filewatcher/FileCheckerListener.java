/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package bizstat.filewatcher;

/**
 *
 * @author evan.summers
 */
public interface FileCheckerListener {
    public void fileChanged(FileChecker checker);
    
}
