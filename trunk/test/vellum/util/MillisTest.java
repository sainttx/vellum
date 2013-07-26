/*
 * Copyright Evan Summers
 * 
 */
package vellum.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import vellum.datatype.Milli;

/**
 *
 * @author evan
 */
public class MillisTest {

    @Test
    public void breakingBad() {
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date(0)));
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis())));
        System.out.println(Milli.formatAsSeconds(System.currentTimeMillis() % Milli.fromDays(1)));
    }
    
    @Test
    public void testIntervalSeconds() {
        Assert.assertEquals(Milli.formatAsSeconds(1000), "00:00:01");
        Assert.assertEquals(Milli.formatAsSeconds(60000), "00:01:00");
        Assert.assertEquals(Milli.formatAsSeconds(3600000), "01:00:00");
    }

    @Test
    public void testIntervalMillis() {
        Assert.assertEquals(Milli.format(1001), "00:00:01,001");
        Assert.assertEquals(Milli.format(60888), "00:01:00,888");
        Assert.assertEquals(Milli.format(3600999), "01:00:00,999");
    }    
    
    @Test
    public void testParse() {
        Assert.assertEquals(Milli.parse("1 SECONDS"), 1000);
        Assert.assertEquals(Milli.parse("1m"), 60000);
        Assert.assertEquals(Milli.parse("60m"), 3600000);
        Assert.assertEquals(Milli.parse("60m"), Milli.parse("1h"));
        Assert.assertEquals(Milli.parse("24h"), Milli.parse("1d"));
    }
    
}
