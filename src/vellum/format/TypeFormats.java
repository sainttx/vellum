/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.format;

import java.util.Collection;
import java.util.Date;
import vellum.util.DateFormats;
import vellum.util.Lists;
import vellum.util.Strings;
/**
 *
 * @author evan
 */
public class TypeFormats {

    public static TypeFormats formatter = new TypeFormats(false);
    public static TypeFormats verboseFormatter = new TypeFormats(true);
    public static TypeFormats displayFormatter = new TypeFormats(true);
    
    static {
        verboseFormatter.verbose = true;
        displayFormatter.displayable = true;
    }
    
    boolean displayable = false;
    boolean verbose = false;
    
    TypeFormats(boolean displayable) {
        this.displayable = displayable;
    }
    
    public String format(Object arg) {
        if (arg == null) {
            if (displayable) return "";
            return "null";
        } else if (arg instanceof Class) {
            return ((Class) arg).getSimpleName();
        } else if (arg instanceof Date) {
            return DateFormats.timestampFormat.format((Date) arg);
        } else if (Strings.isEmpty(arg.toString())) {
            return "empty";
        } else if (arg instanceof byte[]) {
            return String.format("[%s]", Lists.format(Lists.toList((byte[]) arg)));
        } else if (arg instanceof Object[]) {
            return String.format("[%s]", Lists.format((Object[]) arg));
        } else if (arg instanceof String[]) {
            return String.format("[%s]", Lists.format((String[]) arg));
        } else if (arg instanceof Collection) {
            return String.format("[%s]", Lists.format((Collection) arg));
        } else {
            return arg.toString();
        }
    }    

}
