package com.incidentreport.util;

import java.awt.Color;
import java.awt.Font;

/**
 * UIConstants
 * -----------
 * Central place for every color, font and spacing value used across the
 * application. Keeping them here means the whole GUI can be re-themed by
 * editing a single file, and every panel looks consistent.
 */
public final class UIConstants {

    // Prevent instantiation - this class only holds constants.
    private UIConstants() {
    }

    // ---------------------------------------------------------------
    // Color Palette: Blue + White + Light Gray professional dashboard
    // ---------------------------------------------------------------
    public static final Color PRIMARY_BLUE      = new Color(25, 85, 160);
    public static final Color PRIMARY_BLUE_DARK = new Color(15, 60, 120);
    public static final Color ACCENT_BLUE       = new Color(66, 133, 244);
    public static final Color LIGHT_BLUE        = new Color(232, 240, 254);
    public static final Color WHITE             = Color.WHITE;
    public static final Color LIGHT_GRAY        = new Color(245, 246, 248);
    public static final Color MID_GRAY          = new Color(220, 223, 228);
    public static final Color DARK_GRAY_TEXT    = new Color(45, 52, 64);
    public static final Color SUBTLE_TEXT       = new Color(110, 118, 129);
    public static final Color SUCCESS_GREEN     = new Color(46, 160, 90);
    public static final Color ERROR_RED         = new Color(200, 55, 55);
    public static final Color WARNING_ORANGE    = new Color(220, 140, 30);

    // ---------------------------------------------------------------
    // Fonts
    // ---------------------------------------------------------------
    public static final Font FONT_TITLE      = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER     = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_SUBHEADER  = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_LABEL      = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_FIELD      = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON     = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_NAV        = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL      = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO       = new Font("Consolas", Font.PLAIN, 12);

    // ---------------------------------------------------------------
    // Spacing
    // ---------------------------------------------------------------
    public static final int PADDING_SMALL  = 6;
    public static final int PADDING_MEDIUM = 12;
    public static final int PADDING_LARGE  = 20;

    // ---------------------------------------------------------------
    // Application Info
    // ---------------------------------------------------------------
    public static final String APP_NAME    = "Incident Report Generator";
    public static final String APP_VERSION = "1.0.0";
    public static final String APP_AUTHOR  = "Security Operations Team";
}
