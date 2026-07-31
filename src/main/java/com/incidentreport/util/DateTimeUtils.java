package com.incidentreport.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * DateTimeUtils
 * -------------
 * Helper methods for getting and formatting the current date/time, used by
 * the header clock, default form values and the PDF export footer.
 */
public final class DateTimeUtils {

    private DateTimeUtils() {
    }

    private static final DateTimeFormatter DISPLAY_DATE =
            DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
    private static final DateTimeFormatter DISPLAY_TIME =
            DateTimeFormatter.ofPattern("hh:mm:ss a");
    private static final DateTimeFormatter FORM_DATE =
            DateTimeFormatter.ofPattern(ValidationUtils.DATE_PATTERN);
    private static final DateTimeFormatter FORM_TIME =
            DateTimeFormatter.ofPattern(ValidationUtils.TIME_PATTERN);

    /** Returns today's date formatted for on-screen display, e.g. "Thursday, 30 July 2026". */
    public static String currentDisplayDate() {
        return LocalDate.now().format(DISPLAY_DATE);
    }

    /** Returns the current time formatted for on-screen display, e.g. "02:35:10 PM". */
    public static String currentDisplayTime() {
        return LocalTime.now().format(DISPLAY_TIME);
    }

    /** Returns today's date formatted for form fields, e.g. "2026-07-30". */
    public static String currentFormDate() {
        return LocalDate.now().format(FORM_DATE);
    }

    /** Returns the current time formatted for form fields, e.g. "14:35". */
    public static String currentFormTime() {
        return LocalTime.now().format(FORM_TIME);
    }

    /** Returns a timestamp suitable for default file names, e.g. "20260730_143510". */
    public static String fileTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }
}
