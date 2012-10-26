/*
 * (c) Copyright 2011, iPay (Pty) Ltd
 */
package vellum.html;

import vellum.printer.Printer;
import vellum.util.Strings;
import vellum.util.Types;

/**
 *
 * @author evans
 */
public class HtmlPrinter {

    int index = 0;
    int columnCount = 0;
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

    public void _div() {
        out.printf("</div>\n");
    }    

    public void spanf(String style, String format, Object ... args) {
        out.printf("<span class='%s'>%s</span>\n", style, String.format(format, args));
    }    
    
    public void a_(String href, String text) {
        out.printf("<a href='%s'>%s</a>\n", href, text);
    }    
    
    public void a(String href) {
        out.printf("<a href='%s'>\n", href);
    }    

    public void a(String style, String href) {
        out.printf("<a class='%s' href='%s'>\n", style, href);
    }    
    
    public void _a() {
        out.printf("</a>\n");
    }    
    
    public void tableDiv(String style) {
        out.printf("<div class='%s'>\n", style);
        out.printf("<table class='%s'>\n", style);
    }
    
    public void table(String style) {
        out.printf("<table class='%s'>\n", style);
    }
    
    public void thead() {
        out.printf("<thead>\n");
    }

    public void tbody() {
        out.printf("<tbody>\n");
    }

    public void trh(String... names) {
        trh();
        columnCount = names.length;
        for (String name : names) {
            th(name);
        }
    }

    public void trh() {
        out.printf("<tr>\n");
    }

    public void th(String string) {
        out.printf("<th>%s</th>\n", string);
    }

    public void thh(String string) {
        out.printf("<th class='sub'>%s</th>\n", string);
    }

    public void trhh() {
        out.printf("<tr>\n");
    }

    public void _thead() {
        out.printf("</thead>\n");
        out.flush();
    }

    public void tr0() {
        out.printf("<tr class='row%d'>\n", 0);
    }
    
    public void tr() {
        out.printf("<tr class='row%d'>\n", index++ % 2);
    }

    public void td(String type, Object value) {
        out.printf("<td class='%sCell'>%s</td>\n", type, Types.formatDisplay(value));
    }

    public void trd(Object... values) {
        tr();
        for (Object value : values) {
            if (value == null) {
                td("null", "");
            } else {
                td(Types.getStyleClass(value.getClass()), value);
            }
        }
        _tr();
    }

    public void trhd(String label, Object value) {
        tr();
        out.printf("<td class='rowLabel'>%s</td>\n", label);
        out.printf("<td>%s</td>\n", value);
        _tr();
    }
    
    public void tdClose() {
        out.printf("</td>\n");
    }
    
    public void _tr() {
        out.printf("</tr>\n");
    }
    
    public void _tbody() {
        out.printf("</tbody>\n");
    }

    public void _table() {
        out.printf("</table>\n");
    }

    public void _tableDiv() {
        out.printf("</div>\n");
        out.printf("</table>\n");
    }
    
    public int getIndex() {
        return index;
    }

    public void pre(String string) {
        out.printf("<pre>");
        out.printf(Strings.escapeHtml(string));
        out.printf("</pre>\n");
    }
    

}
