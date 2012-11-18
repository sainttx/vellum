/*
 * Copyright Evan Summers
 * 
 */
package crocserver.app;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map.Entry;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.parameter.StringMap;

/**
 *
 * @author evan
 */
public class JsonStrings {
    static Logr logger = LogrFactory.getLogger(JsonStrings.class);

    public static String get(String json, String key) {
        JsonElement element = new JsonParser().parse(json);
        JsonObject object = element.getAsJsonObject();
        object.entrySet();
        return element.getAsJsonObject().get("key").getAsString();
    }

    public static StringMap getStringMap(String json) {
        StringMap map = new StringMap();
        for (Entry<String, JsonElement> entry : getJsonObject(json).entrySet()) {
            map.put(entry.getKey(), entry.getValue().getAsString());
        }
        return map;
    }

    public static JsonObject getJsonObject(String json) {
        return new JsonParser().parse(json).getAsJsonObject();
    }
    
    public static String buildJson(StringMap map) {
        return new Gson().toJson(map);
    }
}
