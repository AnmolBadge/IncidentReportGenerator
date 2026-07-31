package com.incidentreport.gui;

import com.incidentreport.model.IOCEntry;
import com.incidentreport.model.IncidentReport;
import com.incidentreport.model.SystemAffected;
import com.incidentreport.model.TimelineEvent;
import com.incidentreport.service.ReportManager;
import com.incidentreport.util.DateTimeUtils;
import com.incidentreport.util.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;

/**
 * PreviewPanel
 * ------------
 * Renders a clean, formatted, read-only preview of the entire incident
 * report exactly as it will appear when exported. Has a "Refresh Preview"
 * button so the user can pull in the latest edits from every other screen.
 */
public class PreviewPanel extends JPanel {

    private final ReportManager reportManager;
    private final JTextArea previewArea;

    public PreviewPanel(ReportManager reportManager) {
        this.reportManager = reportManager;
        setLayout(new BorderLayout(0, UIConstants.PADDING_MEDIUM));
        setBackground(UIConstants.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        JLabel header = new JLabel("Report Preview");
        header.setFont(UIConstants.FONT_HEADER);
        header.setForeground(UIConstants.DARK_GRAY_TEXT);
        topBar.add(header, BorderLayout.WEST);

        RoundedButton refreshButton = new RoundedButton("Refresh Preview");
        refreshButton.setPreferredSize(new java.awt.Dimension(160, 34));
        topBar.add(refreshButton, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        previewArea = new JTextArea();
        previewArea.setEditable(false);
        previewArea.setFont(UIConstants.FONT_MONO);
        previewArea.setBackground(UIConstants.WHITE);
        previewArea.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM,
                UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM));

        JScrollPane scroll = new JScrollPane(previewArea);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.MID_GRAY));
        add(scroll, BorderLayout.CENTER);

        refreshButton.addActionListener(e -> refreshFromModel());
    }

    /** Rebuilds the preview text from the current state of the shared IncidentReport model. */
    public void refreshFromModel() {
        IncidentReport report = reportManager.getCurrentReport();
        StringBuilder sb = new StringBuilder();

        sb.append("=========================================================\n");
        sb.append("                    INCIDENT REPORT\n");
        sb.append("=========================================================\n");
        sb.append("Generated on: ").append(DateTimeUtils.currentDisplayDate())
          .append(" at ").append(DateTimeUtils.currentDisplayTime()).append("\n\n");

        sb.append("--- 1. INCIDENT DETAILS ---\n");
        sb.append("Title:          ").append(safe(report.getTitle())).append("\n");
        sb.append("Type:           ").append(safe(report.getIncidentType())).append("\n");
        sb.append("Reported By:    ").append(safe(report.getReportedBy())).append("\n");
        sb.append("Department:     ").append(safe(report.getDepartment())).append("\n");
        sb.append("Date / Time:    ").append(safe(report.getIncidentDate()))
          .append(" ").append(safe(report.getIncidentTime())).append("\n");
        sb.append("Priority:       ").append(safe(report.getPriority())).append("\n");
        sb.append("Description:\n  ").append(safe(report.getDescription())).append("\n\n");

        sb.append("--- 2. SYSTEMS AFFECTED (").append(report.getSystemsAffected().size()).append(") ---\n");
        for (SystemAffected s : report.getSystemsAffected()) {
            sb.append("  * ").append(safe(s.getComputerName()))
              .append(" | IP: ").append(safe(s.getIpAddress()))
              .append(" | OS: ").append(safe(s.getOperatingSystem()))
              .append(" | Dept: ").append(safe(s.getDepartment())).append("\n");
        }
        if (report.getSystemsAffected().isEmpty()) sb.append("  (none recorded)\n");
        sb.append("\n");

        sb.append("--- 3. TIMELINE (").append(report.getTimeline().size()).append(" events) ---\n");
        for (TimelineEvent e : report.getTimeline()) {
            sb.append("  * [").append(e.getDate()).append(" ").append(e.getTime()).append("] ")
              .append(safe(e.getDescription())).append("\n");
        }
        if (report.getTimeline().isEmpty()) sb.append("  (none recorded)\n");
        sb.append("\n");

        sb.append("--- 4. INDICATORS OF COMPROMISE (").append(report.getIocs().size()).append(") ---\n");
        for (IOCEntry ioc : report.getIocs()) {
            sb.append("  * IP: ").append(safe(ioc.getSuspiciousIp()))
              .append(" | Domain: ").append(safe(ioc.getDomain()))
              .append(" | URL: ").append(safe(ioc.getUrl()))
              .append(" | Hash: ").append(safe(ioc.getFileHash()))
              .append(" | File: ").append(safe(ioc.getFileName())).append("\n");
        }
        if (report.getIocs().isEmpty()) sb.append("  (none recorded)\n");
        sb.append("\n");

        sb.append("--- 5. REMEDIATION ---\n");
        sb.append("Immediate Actions:\n  ").append(safe(report.getImmediateActions())).append("\n\n");
        sb.append("Investigation Summary:\n  ").append(safe(report.getInvestigationSummary())).append("\n\n");
        sb.append("Root Cause:\n  ").append(safe(report.getRootCause())).append("\n\n");
        sb.append("Remediation Steps:\n  ").append(safe(report.getRemediationSteps())).append("\n\n");
        sb.append("Future Recommendations:\n  ").append(safe(report.getFutureRecommendations())).append("\n");

        sb.append("=========================================================\n");

        previewArea.setText(sb.toString());
        previewArea.setCaretPosition(0);
    }

    private String safe(String value) {
        return (value == null || value.trim().isEmpty()) ? "N/A" : value;
    }
}
