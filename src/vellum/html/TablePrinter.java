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
public class TablePrinter extends HtmlPrinter {

    int index = 0;
    int columnCount = 0;
    boolean highlightAlternate = true;
    String style = "resultSet";
    
    public TablePrinter(Printer out) {
        super(out);
    }

    public void setHighlightAlternate(boolean highlightAlternate) {
        this.highlightAlternate = highlightAlternate;
    }

    public void tableDiv(String style) {
        out.printf("<div class='%s'>\n", style);
        out.printf("<table class='%s'>\n", style);
    }
    
    public void table(String style) {
        out.printf("<table class='%s'>\n", style);
    }
    
    public void table() {
        table(style);
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

    public void theadClose() {
        out.printf("</thead>\n");
        out.flush();
    }

    public void tr() {
        out.printf("<tr class='row%d'>\n", index % 2);
        if (highlightAlternate) index++;
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
        trClose();
    }

    public void trhd(String label, Object value) {
        tr();
        out.printf("<td class='rowLabel'>%s</td>\n", label);
        out.printf("<td>%s</td>\n", value);
        trClose();
    }
    
    public void tdClose() {
        out.printf("</td>\n");
    }
    
    public void trClose() {
        out.printf("</tr>\n");
    }
    
    public void tbodyClose() {
        out.printf("</tbody>\n");
    }

    public void tableClose() {
        out.printf("</table>\n");
    }

    public void tableDivClose() {
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
