/*
 */
package vellum.hype.java;

import java.util.ArrayList;
import java.util.List;
import vellum.hype.Utils;

/**
 *
 * @author evan.summers
 */
public class JavaTokenizer {

    List<String> tokenList = new ArrayList();
    StringBuilder builder = new StringBuilder();
    char previous;
    boolean doubleQuoted;
    boolean singleQuoted;
    boolean word;
    boolean numeric;
    boolean comment;
    boolean escaped;
    boolean whitespace;
    boolean annotation;

    public List<String> tokenize(String line) {
        for (char ch : line.toCharArray()) {
            boolean appended = false;
            if (comment) {
            } else if (annotation) {
            } else if (escaped) {
                escaped = false;
            } else if (ch == '"') {
                if (doubleQuoted) {
                    appended = true;
                    builder.append(ch);
                    add();
                    doubleQuoted = false;
                } else {
                    add();
                    doubleQuoted = true;
                }
            } else if (doubleQuoted) {
                if (ch == '\\') {
                    escaped = true;
                }
            } else if (singleQuoted) {
                if (ch == '\\') {
                    escaped = true;
                }
            } else if (ch == '\'') {
                if (singleQuoted) {
                    appended = true;
                    builder.append(ch);
                    add();
                    singleQuoted = false;
                } else {
                    add();
                    singleQuoted = true;
                }
            } else if (ch == '@') {
                add();
                annotation = true;
            } else if (ch == '/') {
                if (previous == '/') {
                    comment = true;
                } else {
                    add();
                }
            } else if (!word) {
                add();
                if (Utils.isWord(ch)) {
                    word = true;
                }
            } else {
                if (!Utils.isWord(ch)) {
                    add();
                    word = false;
                }
            }
            if (!appended) {
                builder.append(ch);
            }
            previous = ch;
        }
        add();
        return tokenList;
    }

    void add() {
        if (builder.length() > 0) {
            //builder.append("~");
            tokenList.add(builder.toString());
            builder.setLength(0);
        }
    }
    
    
}
