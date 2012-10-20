/*
 * Copyright Evan Summers
 * 
 */
package vellum.html;

import vellum.printer.Printer;

/**
 *
 * @author evan
 */
public class HtmlPrinter {
    Printer out;

    public HtmlPrinter(Printer out) {
        this.out = out;
    }

    public void h(int i, String text) {
        String element = "h" + i;
        out.printf("<%s>%s</%s>", element, text, element);
    }

    public void div(String style) {
        out.printf("<div class='%s'>\n", style);
    }    

    public void divClose() {
        out.printf("</div>\n");
    }    
    
}
