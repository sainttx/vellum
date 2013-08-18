
package dualcontrol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import sun.security.tools.KeyTool;

/**
 *
 * @author evans
 */
public class DualControlKeyTool { 
    final static Logger logger = Logger.getLogger(DualControlKeyTool.class);
    String aliasPrefix = System.getProperty("dualcontrol.alias");
    int submissionCount = Integer.getInteger("dualcontrol.submissions", 3);
    String[] args; 
    
    public static void main(String[] args) throws Exception {        
        new DualControlKeyTool().start(args);
    }
    
    void start(String[] args) throws Exception {
        this.args = args;
        Map<String, char[]> dualMap = new DualControlReader().readDualMap(submissionCount);
        for (String alias : dualMap.keySet()) {
            keyTool(String.format("%s-%s", aliasPrefix, alias), dualMap.get(alias));
        }
    }

    public void keyTool(String alias, char[] keypass) throws Exception {
        logger.debug("keyTool alias " + alias);
        List<String> argList = new ArrayList(Arrays.asList(args));
        argList.add("-alias");
        argList.add(alias);
        argList.add("-keypass");
        argList.add(new String(keypass));
        KeyTool.main(argList.toArray(new String[argList.size()]));
    }            
}