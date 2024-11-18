package com.example.gestionabscenceenseignants.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    // Conversion de Date en String
    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
        return formatter.format(date);
    }

    // Conversion de String en Date
    public static Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
        return formatter.parse(dateString);
    }

    // Obtenir la date actuelle
    public static String getCurrentDate() {
        Date currentDate = new Date();
        return formatDate(currentDate);
    }

    // Autres méthodes utiles : ajouter des jours, comparer des dates, etc.
}