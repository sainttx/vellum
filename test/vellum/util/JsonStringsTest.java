/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 * 
 */
package vellum.util;

import crocserver.app.JsonStrings;
import org.junit.Test;

/**
 *
 * @author evan
 */
public class JsonStringsTest {

    String response = "{\"token_type\" : \"Bearer\";\n\"type\" : \"any\";}";
    @Test
    public void test() {
        System.out.println(JsonStrings.get(response, "token_type"));
    }
}
