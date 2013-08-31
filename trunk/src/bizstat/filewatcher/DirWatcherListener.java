/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package bizstat.filewatcher;

import java.io.File;
import java.util.Collection;

/**
 *
 * @author evan.summers
 */
public interface DirWatcherListener {
    public void dirChanged(DirWatcherTask watcher, Collection<File> files);
    
}
