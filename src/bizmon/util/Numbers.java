/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 */
package bizmon.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author evans
 */
public class Numbers {

    public static final NumberFormat currencyNumberFormat = NumberFormat.getCurrencyInstance(Locales.enZA);

    public static String formatMoney(String string) {
        if (string == null) {
            return null;
        }
        int index = string.length() - 2;
        return "R" + string.substring(0, index) + "." + string.substring(index);
    }

    public static String formatMoney(BigDecimal value) {
        return formatMoney(value.doubleValue());
    }

    public static String formatMoney(double value) {
        NumberFormat format = DecimalFormat.getInstance(Locales.enZA);
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setGroupingUsed(true);
        return format.format(value);
    }

    public static boolean equals(BigDecimal amount, BigDecimal other) {
        return amount.intValue() == other.intValue();
    }

    public static boolean equalsAbs(BigDecimal amount, BigDecimal other) {
        return Math.abs(amount.intValue()) == Math.abs(other.intValue());
    }

    public static boolean equalsNegative(BigDecimal amount, BigDecimal other) {
        return amount.intValue() == -other.intValue();
    }

    public static BigDecimal newBigDecimal(Double value, int scale) {
        BigDecimal d = new BigDecimal(value);
        d.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return d;
    }

    public static boolean isDecimal(Object value) {
        if (value == null) return false;
        return Types.equalsAny(value.getClass(), Double.class, double.class,
                Float.class, float.class, BigDecimal.class);
    }

    public static String formatMoneyAlt(double value) {
        return String.format("%.2f", value);
    }

    public static long divide(long operand, long divisor, long nan) {
        if (divisor == 0) return nan;
        return operand/divisor;
    }

    public static void main(String[] args) {
        System.out.println(formatMoneyAlt((double) 514329.455000001));
        System.out.println(formatMoneyAlt((double) 514329.455));
        System.out.println(formatMoney(514329.455000001));
        System.out.println(formatMoney(514329.455));
        System.out.println(newBigDecimal(514329.455000001, 2).toString());
        System.out.println(newBigDecimal(514329.455, 2).toString());
    }

    public static int getInt(long value) {
        return (int) value;
    }
}
