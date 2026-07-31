package com.incidentreport.gui;

import com.incidentreport.model.IncidentReport;
import com.incidentreport.service.ReportManager;
import com.incidentreport.util.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridLayout;

/**
 * RemediationPanel
 * ----------------
 * Screen for documenting the response to the incident: immediate actions
 * taken, a summary of the investigation, the identified root cause, the
 * remediation steps applied, and recommendations to prevent recurrence.
 */
public class RemediationPanel extends JPanel {

    private final ReportManager reportManager;

    private JTextArea immediateActionsArea;
    private JTextArea investigationSummaryArea;
    private JTextArea rootCauseArea;
    private JTextArea remediationStepsArea;
    private JTextArea futureRecommendationsArea;

    public RemediationPanel(ReportManager reportManager) {
        this.reportManager = reportManager;
        setLayout(new BorderLayout(0, UIConstants.PADDING_MEDIUM));
        setBackground(UIConstants.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        JLabel header = new JLabel("Remediation");
        header.setFont(UIConstants.FONT_HEADER);
        header.setForeground(UIConstants.DARK_GRAY_TEXT);
        add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 1, 0, UIConstants.PADDING_MEDIUM));
        grid.setOpaque(false);

        JPanel topRow = new JPanel(new GridLayout(1, 2, UIConstants.PADDING_MEDIUM, 0));
        topRow.setOpaque(false);
        immediateActionsArea = new JTextArea();
        investigationSummaryArea = new JTextArea();
        topRow.add(createTextBlock("Immediate Actions", immediateActionsArea));
        topRow.add(createTextBlock("Investigation Summary", investigationSummaryArea));

        JPanel bottomRow = new JPanel(new GridLayout(1, 3, UIConstants.PADDING_MEDIUM, 0));
        bottomRow.setOpaque(false);
        rootCauseArea = new JTextArea();
        remediationStepsArea = new JTextArea();
        futureRecommendationsArea = new JTextArea();
        bottomRow.add(createTextBlock("Root Cause", rootCauseArea));
        bottomRow.add(createTextBlock("Remediation Steps", remediationStepsArea));
        bottomRow.add(createTextBlock("Future Recommendations", futureRecommendationsArea));

        grid.add(topRow);
        grid.add(bottomRow);
        add(grid, BorderLayout.CENTER);

        RoundedButton saveButton = new RoundedButton("Save Remediation to Report");
        saveButton.setPreferredSize(new java.awt.Dimension(240, 38));
        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        southPanel.add(saveButton);
        add(southPanel, BorderLayout.SOUTH);
        saveButton.addActionListener(e -> saveToModel(true));
    }

    private JPanel createTextBlock(String title, JTextArea area) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(UIConstants.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.MID_GRAY),
                BorderFactory.createEmptyBorder(
                        UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM,
                        UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)));

        JLabel label = new JLabel(title);
        label.setFont(UIConstants.FONT_SUBHEADER);
        label.setForeground(UIConstants.PRIMARY_BLUE);

        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(UIConstants.FONT_FIELD);
        JScrollPane scroll = new JScrollPane(area);

        panel.add(label, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    /** Copies the text areas into the shared IncidentReport model. */
    public void saveToModel(boolean showConfirmation) {
        IncidentReport report = reportManager.getCurrentReport();
        report.setImmediateActions(immediateActionsArea.getText().trim());
        report.setInvestigationSummary(investigationSummaryArea.getText().trim());
        report.setRootCause(rootCauseArea.getText().trim());
        report.setRemediationSteps(remediationStepsArea.getText().trim());
        report.setFutureRecommendations(futureRecommendationsArea.getText().trim());

        if (showConfirmation) {
            JOptionPane.showMessageDialog(this, "Remediation details saved to the report.",
                    "Saved", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /** Refreshes the text areas from the shared IncidentReport model. */
    public void refreshFromModel() {
        IncidentReport report = reportManager.getCurrentReport();
        immediateActionsArea.setText(report.getImmediateActions());
        investigationSummaryArea.setText(report.getInvestigationSummary());
        rootCauseArea.setText(report.getRootCause());
        remediationStepsArea.setText(report.getRemediationSteps());
        futureRecommendationsArea.setText(report.getFutureRecommendations());
    }

    /** Clears every remediation field (used by the global "Clear Form" action). */
    public void clearAllFields() {
        immediateActionsArea.setText("");
        investigationSummaryArea.setText("");
        rootCauseArea.setText("");
        remediationStepsArea.setText("");
        futureRecommendationsArea.setText("");
    }
}
