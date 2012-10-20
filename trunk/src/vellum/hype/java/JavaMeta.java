/*
 */
package vellum.hype.java;

import vellum.util.Lists;

/**
 *
 * @author evans
 */
public class JavaMeta {

    public static final String beginPattern = "<pre style=\"java\">";
    public static final String endPattern = "</pre>";

    public static final String[] accessKeywords = {
        "public", "protected", "private"
    };

    public static final String[] classKeywords = {
        "class", "interface", "enum"
    };
    
    public static final String[] keywords = {
        "public", "protected", "private", 
        "abstract", "class", "interface", "extends", "implements",         
        "void", "int", "short", "long", "boolean", 
        "synchronized", "throws",
        "null", "true", "false", 
        "new", "this", "super",
        "return", "throw",         
        "if", "while", "for",
        "try", "catch", "finally"                       
    };

}
