/*
 Source https://code.google.com/p/vellum by @evanxsummers
 * 
 */
package vellum.cryptostore;

import static junit.framework.Assert.*;
import dualcontrol.AesPbeStore;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import org.junit.Test;
import vellum.datatype.Millis;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class PbeStoreTest {

    static Logr logger = LogrFactory.getLogger(PbeStoreTest.class);

    char[] password = "test1234".toCharArray();
    String alias = "dek2013";
    String type = "JCEKS";
    String text = "all your base all belong to us";
    
    @Test
    public void testGenerate() throws Exception {
        testGenerate(1000);
        testGenerate(10000);
        testGenerate(100000);
        testGenerate(500000);
        testGenerate(1000000);
    }
    
    public void testGenerate(int iterationCount) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        long millis = System.currentTimeMillis();
        new AesPbeStore(iterationCount).store(baos, type, alias, text.getBytes(), password);
        millis = Millis.elapsed(millis);
        System.out.printf("store %s %d %dms: %s\n", alias, iterationCount, millis, text);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        millis = System.currentTimeMillis();
        byte[] loadBytes = new AesPbeStore().load(bais, type, alias, password);
        millis = Millis.elapsed(millis);
        System.out.printf("load %s %d %dms: %s\n", alias, iterationCount, millis, 
                new String(loadBytes));
        assertTrue(Arrays.equals(loadBytes, text.getBytes()));
    }
}
