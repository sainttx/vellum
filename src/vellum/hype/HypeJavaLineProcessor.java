/*
 * 
 */
package vellum.hype;

import vellum.util.Lists;
import vellum.hype.java.JavaMeta;
import java.util.List;
import vellum.hype.java.JavaTokenizer;

/**
 *
 * @author evans
 */
public class HypeJavaLineProcessor {

    HypeContext context = new HypeContext();
    String line;
    StringBuilder builder = new StringBuilder();
    String previousToken;
    String previousWord;
    String previousKeyword;
    boolean publicLine = false; 
    int index = 0;
    
    public HypeJavaLineProcessor(String line) {
        this.line = line;
    }
    
    public String process() throws Exception {
        builder.setLength(0);
        List<String> tokenList = new JavaTokenizer().tokenize(line);
        if (tokenList.size() > 0) {
            for (; index < tokenList.size() - 1; index++) {
                processJava(tokenList.get(index), tokenList.get(index + 1));
            }
            processJava(tokenList.get(index), null);
        }
        return builder.toString();
    }

    private void processJava(String token, String nextToken) throws Exception {
        int length = builder.length();
        if (Utils.isWhitespace(token)) {
        } else if (token.startsWith("\"")) {
            builder.append("<span class=\"character\">");
            builder.append(token);
            builder.append("</span>");
        } else if (token.startsWith("//")) {
            builder.append("<span class=\"comment\">");
            builder.append(token);
            builder.append("</span>");
        } else if (token.startsWith("@")) {
            builder.append("<span class=\"comment\">");
            builder.append(token);
            builder.append("</span>");
        } else if (Lists.contains(JavaMeta.keywords, token)) {
            builder.append("<span class=\"keyword-directive\">");
            builder.append(token);
            builder.append("</span>");
            if (token.equals("public")) {
                publicLine = true;
            }
            previousKeyword = token;
        } else {
            if (Utils.isWord(token)) {
                if (publicLine) {
                    if (nextToken != null && nextToken.equals("(")) {
                        builder.append("<b>");
                        builder.append(token);
                        builder.append("</b>");
                    } else if (Lists.contains(JavaMeta.classKeywords, previousKeyword)) {
                        builder.append("<b>");
                        builder.append(token);
                        builder.append("</b>");
                    }
                    publicLine = false;
                } else {
                    builder.append(token);
                }
                previousWord = token;
            }
        }
        if (builder.length() == length) {
            builder.append(token);
        }
        previousToken = token;
    }
}
