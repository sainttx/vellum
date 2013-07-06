/*
 * Copyright Evan Summers
 * 
 */
package vellum.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import vellum.datatype.Millis;

/**
 *
 * @author evan
 */
public class MillisTest {

    @Test
    public void breakingBad() {
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date(0)));
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis())));
        System.out.println(Millis.formatAsSeconds(System.currentTimeMillis() % Millis.fromDays(1)));
    }
    
    @Test
    public void testIntervalSeconds() {
        Assert.assertEquals(Millis.formatAsSeconds(1000), "00:00:01");
        Assert.assertEquals(Millis.formatAsSeconds(60000), "00:01:00");
        Assert.assertEquals(Millis.formatAsSeconds(3600000), "01:00:00");
    }

    @Test
    public void testIntervalMillis() {
        Assert.assertEquals(Millis.format(1001), "00:00:01,001");
        Assert.assertEquals(Millis.format(60888), "00:01:00,888");
        Assert.assertEquals(Millis.format(3600999), "01:00:00,999");
    }    
    
    @Test
    public void testParse() {
        Assert.assertEquals(Millis.parse("1 SECONDS"), 1000);
        Assert.assertEquals(Millis.parse("1m"), 60000);
        Assert.assertEquals(Millis.parse("60m"), 3600000);
        Assert.assertEquals(Millis.parse("60m"), Millis.parse("1h"));
        Assert.assertEquals(Millis.parse("24h"), Millis.parse("1d"));
    }
    
}
