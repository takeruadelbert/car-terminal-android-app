package com.stn.carterminal.helper;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class TakeruHelper {
    private static final String INPUT_DATE_PATTERN = "yyyy-MM-dd";
    private static final String OUTPUT_DATE_PATTERN = "dd MMMM yyyy";
    private static final Locale locale = new Locale("id", "ID");

    public static boolean validateHost(String inputHost) {
        Pattern pattern = Pattern.compile("^"
                + "(((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}" // Domain name
                + "|"
                + "localhost" // localhost
                + "|"
                + "(([0-9]{1,3}\\.){3})[0-9]{1,3})" // Ip
                + ":"
                + "[0-9]{1,5}$"); // Port
        return pattern.matcher(inputHost).matches();
    }

    public static String convertStringDateToPlainDate(String stringDate) {
        try {
            SimpleDateFormat input = new SimpleDateFormat(INPUT_DATE_PATTERN, locale);
            SimpleDateFormat output = new SimpleDateFormat(OUTPUT_DATE_PATTERN, locale);
            Date date = new Date(input.parse(stringDate).getTime());
            return output.format(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
