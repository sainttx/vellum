/*
 * Copyright Evan Summers
 * 
 */
package bizstat.filewatcher;

import java.io.File;
import java.util.Collection;

/**
 *
 * @author evan
 */
public interface DirWatcherListener {
    public void dirChanged(DirWatcherTask watcher, Collection<File> files);
    
}
