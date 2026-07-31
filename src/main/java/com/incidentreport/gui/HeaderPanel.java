package com.incidentreport.gui;

import com.incidentreport.util.DateTimeUtils;
import com.incidentreport.util.UIConstants;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * HeaderPanel
 * -----------
 * Top header bar showing the application title on the left and a live
 * date/time clock on the right. The clock updates every second using a
 * Swing Timer (never blocks the Event Dispatch Thread).
 */
public class HeaderPanel extends JPanel {

    private final JLabel dateLabel;
    private final JLabel timeLabel;

    public HeaderPanel() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.PRIMARY_BLUE);
        setPreferredSize(new Dimension(100, 64));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(
                0, UIConstants.PADDING_LARGE, 0, UIConstants.PADDING_LARGE));

        JLabel titleLabel = new JLabel(UIConstants.APP_NAME);
        titleLabel.setFont(UIConstants.FONT_TITLE);
        titleLabel.setForeground(UIConstants.WHITE);
        add(titleLabel, BorderLayout.WEST);

        JPanel clockPanel = new JPanel();
        clockPanel.setLayout(new java.awt.GridLayout(2, 1));
        clockPanel.setOpaque(false);

        dateLabel = new JLabel(DateTimeUtils.currentDisplayDate(), SwingConstants.RIGHT);
        dateLabel.setFont(UIConstants.FONT_LABEL);
        dateLabel.setForeground(UIConstants.WHITE);

        timeLabel = new JLabel(DateTimeUtils.currentDisplayTime(), SwingConstants.RIGHT);
        timeLabel.setFont(UIConstants.FONT_SUBHEADER);
        timeLabel.setForeground(UIConstants.WHITE);

        clockPanel.add(dateLabel);
        clockPanel.add(timeLabel);
        add(clockPanel, BorderLayout.EAST);

        // Update the clock every second.
        Timer clockTimer = new Timer(1000, e -> {
            dateLabel.setText(DateTimeUtils.currentDisplayDate());
            timeLabel.setText(DateTimeUtils.currentDisplayTime());
        });
        clockTimer.start();
    }
}
