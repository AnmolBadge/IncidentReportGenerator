package com.incidentreport.gui;

import com.incidentreport.service.ReportManager;
import com.incidentreport.util.DateTimeUtils;
import com.incidentreport.util.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * DashboardPanel
 * --------------
 * Professional welcome screen shown when the application starts. Displays
 * small summary cards (total reports generated this session, today's date,
 * application status) and a short quick-start guide.
 */
public class DashboardPanel extends JPanel {

    private final ReportManager reportManager;
    private JLabel totalReportsValue;

    public DashboardPanel(ReportManager reportManager) {
        this.reportManager = reportManager;
        setLayout(new BorderLayout(0, UIConstants.PADDING_LARGE));
        setBackground(UIConstants.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        JLabel welcome = new JLabel("Welcome back");
        welcome.setFont(UIConstants.FONT_HEADER);
        welcome.setForeground(UIConstants.DARK_GRAY_TEXT);

        JLabel subtitle = new JLabel(
                "Use the menu on the left to build a complete incident response report.");
        subtitle.setFont(UIConstants.FONT_LABEL);
        subtitle.setForeground(UIConstants.SUBTLE_TEXT);

        JPanel titleBlock = new JPanel(new GridLayout(2, 1, 0, 4));
        titleBlock.setOpaque(false);
        titleBlock.add(welcome);
        titleBlock.add(subtitle);
        add(titleBlock, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, UIConstants.PADDING_LARGE, 0));
        cardsPanel.setOpaque(false);

        totalReportsValue = new JLabel("0");
        cardsPanel.add(createStatCard("Total Reports", totalReportsValue));
        cardsPanel.add(createStatCard("Today's Date", new JLabel(DateTimeUtils.currentDisplayDate())));
        JLabel statusValue = new JLabel("Operational");
        statusValue.setForeground(UIConstants.SUCCESS_GREEN);
        cardsPanel.add(createStatCard("Application Status", statusValue));

        add(cardsPanel, BorderLayout.CENTER);

        add(createQuickGuide(), BorderLayout.SOUTH);
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(UIConstants.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.MID_GRAY),
                BorderFactory.createEmptyBorder(
                        UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                        UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.FONT_SUBHEADER);
        titleLabel.setForeground(UIConstants.SUBTLE_TEXT);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(UIConstants.PRIMARY_BLUE);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createQuickGuide() {
        JPanel guidePanel = new JPanel(new BorderLayout());
        guidePanel.setBackground(UIConstants.WHITE);
        guidePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.MID_GRAY),
                BorderFactory.createEmptyBorder(
                        UIConstants.PADDING_MEDIUM, UIConstants.PADDING_LARGE,
                        UIConstants.PADDING_MEDIUM, UIConstants.PADDING_LARGE)));

        JLabel header = new JLabel("Quick Guide");
        header.setFont(UIConstants.FONT_SUBHEADER);
        header.setForeground(UIConstants.PRIMARY_BLUE);

        JTextArea steps = new JTextArea(
                "1. Fill in Incident Details (and any Systems Affected on the second tab).\n"
              + "2. Add Timeline events describing what happened and when.\n"
              + "3. Record any Indicators of Compromise (IOC) discovered.\n"
              + "4. Document Remediation actions and future recommendations.\n"
              + "5. Open Preview to review the full report, then use Export to save it as a PDF.");
        steps.setEditable(false);
        steps.setOpaque(false);
        steps.setFont(UIConstants.FONT_LABEL);
        steps.setForeground(UIConstants.DARK_GRAY_TEXT);
        steps.setLineWrap(true);
        steps.setWrapStyleWord(true);
        steps.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        guidePanel.add(header, BorderLayout.NORTH);
        guidePanel.add(steps, BorderLayout.CENTER);
        return guidePanel;
    }

    /** Refreshes the "Total Reports" stat card. Call this whenever a save/export happens. */
    public void refreshStats() {
        totalReportsValue.setText(String.valueOf(reportManager.getTotalReportsThisSession()));
    }
}
