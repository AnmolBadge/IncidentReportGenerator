package com.incidentreport.gui;

import com.incidentreport.util.UIConstants;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * StatusBarPanel
 * --------------
 * Bottom status bar showing: application version, a short status message
 * ("Ready", "Report Saved", etc.) and the current user name. The status
 * message can be updated at runtime via setStatus().
 */
public class StatusBarPanel extends JPanel {

    private final JLabel statusLabel;

    public StatusBarPanel() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.LIGHT_GRAY);
        setPreferredSize(new Dimension(100, 28));
        setBorder(new javax.swing.border.CompoundBorder(
                new MatteBorder(1, 0, 0, 0, UIConstants.MID_GRAY),
                new EmptyBorder(4, UIConstants.PADDING_LARGE, 4, UIConstants.PADDING_LARGE)));

        JLabel versionLabel = new JLabel("Version " + UIConstants.APP_VERSION);
        versionLabel.setFont(UIConstants.FONT_SMALL);
        versionLabel.setForeground(UIConstants.SUBTLE_TEXT);
        add(versionLabel, BorderLayout.WEST);

        statusLabel = new JLabel("Ready", SwingConstants.CENTER);
        statusLabel.setFont(UIConstants.FONT_SMALL);
        statusLabel.setForeground(UIConstants.SUCCESS_GREEN);
        add(statusLabel, BorderLayout.CENTER);

        JLabel userLabel = new JLabel(System.getProperty("user.name", "User"), SwingConstants.RIGHT);
        userLabel.setFont(UIConstants.FONT_SMALL);
        userLabel.setForeground(UIConstants.SUBTLE_TEXT);
        add(userLabel, BorderLayout.EAST);
    }

    /** Updates the status message shown in the middle of the bar. */
    public void setStatus(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(UIConstants.SUCCESS_GREEN);
    }

    /** Updates the status message and shows it in red to indicate an error. */
    public void setError(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(UIConstants.ERROR_RED);
    }
}
