/*
 * Copyright Evan Summers
 * 
 */
package json;

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
