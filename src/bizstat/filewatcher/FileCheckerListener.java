/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
