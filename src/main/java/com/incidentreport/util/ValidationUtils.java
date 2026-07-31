package com.incidentreport.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * ValidationUtils
 * ---------------
 * Small, self-contained validation helpers used throughout the GUI to check
 * user input before it is accepted (empty fields, IP addresses, dates and
 * times). Every method returns a boolean so callers can decide how to react
 * (usually by popping up a JOptionPane).
 */
public final class ValidationUtils {

    private ValidationUtils() {
    }

    // Expected date format: yyyy-MM-dd (e.g. 2026-07-30)
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    // Expected time format: HH:mm (24-hour clock, e.g. 14:35)
    public static final String TIME_PATTERN = "HH:mm";

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern(TIME_PATTERN);

    // Simple IPv4 validation pattern: four groups of 0-255 separated by dots.
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    /**
     * Returns true if the given text is null, empty, or only whitespace.
     */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * Validates a string as a proper IPv4 address, e.g. "192.168.1.10".
     * An empty string is treated as valid here since IP fields are
     * sometimes optional - callers should call isEmpty() separately
     * for required-field checks.
     */
    public static boolean isValidIP(String ip) {
        if (isEmpty(ip)) {
            return true;
        }
        return IPV4_PATTERN.matcher(ip.trim()).matches();
    }

    /**
     * Validates a date string against the expected yyyy-MM-dd format.
     */
    public static boolean isValidDate(String date) {
        if (isEmpty(date)) {
            return false;
        }
        try {
            LocalDate.parse(date.trim(), DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates a time string against the expected HH:mm 24-hour format.
     */
    public static boolean isValidTime(String time) {
        if (isEmpty(time)) {
            return false;
        }
        try {
            LocalTime.parse(time.trim(), TIME_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
