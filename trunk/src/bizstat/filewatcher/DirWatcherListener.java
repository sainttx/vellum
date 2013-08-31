/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
