
package dualcontrol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import sun.security.tools.KeyTool;

/**
 *
 * @author evans
 */
public class DualControlKeyTool extends DualControl {    
    static String aliasPrefix = System.getProperty("dualcontrol.alias");
    String[] args; 
    
    public static void main(String[] args) throws Exception {        
        new DualControlKeyTool().start(args);
    }
    
    void start(String[] args) throws Exception {
        this.args = args;
        Map<String, String> dualMap = DualControl.mapCombinations(Integer.getInteger("dualcontrol.inputs", 3));
        for (String alias : dualMap.keySet()) {
            keyTool(String.format("%s-%s", aliasPrefix, alias), dualMap.get(alias));
        }
    }

    public void keyTool(String alias, String keypass) throws Exception {
        System.err.printf("DualControlKeyTool %s %s\n", alias, keypass);
        List<String> argList = new ArrayList(Arrays.asList(args));
        argList.add("-alias");
        argList.add(alias);
        argList.add("-keypass");
        argList.add(keypass);
        KeyTool.main(argList.toArray(new String[argList.size()]));
    }            
}
