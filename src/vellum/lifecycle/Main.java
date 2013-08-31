/*
 */
package vellum.lifecycle;

import vellum.config.ConfigMap;
import vellum.config.ConfigParser;

/**
 *
 * @author evan.summers
 */
public class Main {
    
    public static void main(String[] args, Initialisable instance) {
        try {
            instance.init();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }    

    public static void main(String[] args, ConfigMapInitialisable instance, ConfigMap configMap) {
        try {
            instance.init(configMap);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }    
    
}
