/*
 * (c) Copyright 2010, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 */
package bizserver.html;

import bizmon.printer.Printer;
import bizmon.util.Strings;
import bizmon.util.Types;

/**
 *
 * @author evans
 */
public class TablePrinter {

    Printer out;
    int index = 0;
    int columnCount = 0;
    boolean highlightAlternate = true;

    public TablePrinter(Printer out) {
        this.out = out;
    }

    public void setHighlightAlternate(boolean highlightAlternate) {
        this.highlightAlternate = highlightAlternate;
    }

    public void table() {
        out.printf("<table class='resultSet'>\n");
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

    public void theadEnd() {
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
    }

    public void tbodyEnd() {
        out.printf("</tbody>\n");
    }

    public void tableEnd() {
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
