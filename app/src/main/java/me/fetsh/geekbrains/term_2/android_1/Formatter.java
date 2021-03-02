package me.fetsh.geekbrains.term_2.android_1;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Formatter {

    public static DecimalFormat defaultFormatter;
    static {
        defaultFormatter = new DecimalFormat("###,###.#", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        defaultFormatter.setMaximumFractionDigits(340);
    }

    public static String format(String str) {
        return new StringFormatter(str).format();
    }

    public static String format(Double result) {
        return new DoubleFormatter(result).format();
    }

    private static class StringFormatter {

        private static final int chunkSize = 3;
        private static final String delimiter = ",";

        private final String value;

        public StringFormatter(String str) {
            this.value = str;
        }

        public String format() {
            String[] parts = value.split("\\.");
            String whole = partition(parts[0]);
            if (parts.length == 2) {
                whole += ("." + parts[1]);
            } else if (value.endsWith(".")) {
                whole += ".";
            }
            return whole;
        }

        private String partition(final String str) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(str.length() - i - 1);
                if (i != 0 && i % chunkSize == 0 && c != '-') {
                    result.append(delimiter);
                }
                result.append(c);
            }
            return result.reverse().toString();
        }
    }

    private static class DoubleFormatter {
        private final Double value;
        public DoubleFormatter(Double value) {
            this.value = value;
        }

        public String format() {
            if (value > 999999999999d || value < -999999999999d) return Double.toString(value);
            return defaultFormatter.format(value);
        }
    }
}
