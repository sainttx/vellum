/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.util;

import java.util.Collection;

/**
 *
 * @author evan
 */
public class ListFormatter {

    public static final String SPACE_DELIMITER = " ";
    public static final String COMMA_DELIMITER = ", ";
    public static final String DASHED_DELIMITER = "-";
    public static final String BAR_DELIMITER = "|";
    
    public static final String SINGLE_QUOTE = "'";
    public static final String DOUBLE_QUOTE = "\"";

    public static final String DEFAULT_DELIMITER = COMMA_DELIMITER;
    
    public static ListFormatter formatter = new ListFormatter(false, COMMA_DELIMITER);
    public static ListFormatter displayFormatter = new ListFormatter(true, COMMA_DELIMITER);
    public static ListFormatter barExportFormatter = new ListFormatter(true, BAR_DELIMITER);
    public static ListFormatter spacedDisplayFormatter = new ListFormatter(true, SPACE_DELIMITER);
    public static ListFormatter spacedPrintFormatter = new ListFormatter(true, SPACE_DELIMITER);
    public static ListFormatter dashedFormatter = new ListFormatter(true, DASHED_DELIMITER);
    public static ListFormatter verboseFormatter = new ListFormatter(false, COMMA_DELIMITER);

    static {
        verboseFormatter.verbose = true;        
    }
    
    TypeFormatter typeFormatter;
    
    String delimiter = COMMA_DELIMITER;
    String quote = DOUBLE_QUOTE;
    boolean displayable = false;
    boolean verbose = false;    
    
    private ListFormatter(boolean displayable, String delimiter) {
        this.displayable = displayable;
        this.delimiter = delimiter;
        typeFormatter = new TypeFormatter(displayable);
    }
    
    
    public String formatArray(Collection collection) {
        return formatArray(collection.toArray());
    }

    public String formatArgs(Object ... args) {
        return formatArray(args);
    }
    
    public String formatArray(Object[] args) {
        if (args == null) {
            if (displayable) return "";
            return "{null}";
        }
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            if (builder.length() > 0) {
                builder.append(delimiter);
            }
            String string = typeFormatter.format(arg);
            if (string.contains(delimiter)) {
                builder.append("{");
                builder.append(string);
                builder.append("}");
            } else {
                builder.append(string);                
            }
        }
        return builder.toString();
    }

    public String formatQuote(Object[] args) {
        if (args == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            if (arg != null) {
                if (builder.length() > 0) {
                    builder.append(delimiter);
                }
                builder.append(quote);
                builder.append(Types.formatPrint(arg));
                builder.append(quote);
            }
        }
        return builder.toString();        
    }
    
    public static String formatVerbose(Object[] args) {
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            if (arg == null) {
                builder.append("null");
            } else {
                String string = Types.formatPrint(arg);
                if (Strings.isEmpty(string)) {
                    string = "empty";
                }
                if (arg.getClass() != String.class && !arg.getClass().isPrimitive()) {
                    builder.append("(");
                    builder.append(arg.getClass().getSimpleName());
                    builder.append(") ");
                }
                builder.append(string);
            }
        }
        return builder.toString();
    }

    
}

