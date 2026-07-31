package com.incidentreport.gui;

import com.incidentreport.service.ReportManager;
import com.incidentreport.util.DateTimeUtils;
import com.incidentreport.util.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

/**
 * ExportPanel
 * -----------
 * Screen 7 of the application: houses the main report-level actions
 * (New Report, Save Report, Load Report, Clear Form, Export PDF, Exit).
 * File locations are always chosen through JFileChooser - there is no
 * command-line interaction anywhere in this application.
 */
public class ExportPanel extends JPanel {

    private final ReportManager reportManager;
    private final AppActions actions;

    public ExportPanel(ReportManager reportManager, AppActions actions) {
        this.reportManager = reportManager;
        this.actions = actions;

        setLayout(new BorderLayout(0, UIConstants.PADDING_LARGE));
        setBackground(UIConstants.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        JLabel header = new JLabel("Export & Report Actions");
        header.setFont(UIConstants.FONT_HEADER);
        header.setForeground(UIConstants.DARK_GRAY_TEXT);
        add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));
        grid.setBorder(BorderFactory.createEmptyBorder(UIConstants.PADDING_LARGE, 0, 0, 0));
        grid.setOpaque(false);

        grid.add(actionCard("New Report", "Start a fresh, empty incident report.",
                this::handleNewReport, UIConstants.PRIMARY_BLUE));
        grid.add(actionCard("Save Report", "Save the current report to a JSON file.",
                this::handleSaveReport, UIConstants.PRIMARY_BLUE));
        grid.add(actionCard("Load Report", "Load a previously saved JSON report.",
                this::handleLoadReport, UIConstants.PRIMARY_BLUE));
        grid.add(actionCard("Clear Form", "Clear all fields on every screen.",
                this::handleClearForm, UIConstants.WARNING_ORANGE));
        grid.add(actionCard("Export PDF", "Export the current report as a PDF document.",
                this::handleExportPdf, UIConstants.SUCCESS_GREEN));
        grid.add(actionCard("Exit", "Close the application.",
                this::handleExit, UIConstants.ERROR_RED));

        add(grid, BorderLayout.CENTER);
    }

    private JPanel actionCard(String title, String description, Runnable action, java.awt.Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(UIConstants.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.MID_GRAY),
                BorderFactory.createEmptyBorder(
                        UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                        UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.FONT_SUBHEADER);
        titleLabel.setForeground(UIConstants.DARK_GRAY_TEXT);

        JLabel descLabel = new JLabel("<html><body style='width:180px'>" + description + "</body></html>");
        descLabel.setFont(UIConstants.FONT_SMALL);
        descLabel.setForeground(UIConstants.SUBTLE_TEXT);

        JPanel textPanel = new JPanel(new BorderLayout(0, 6));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(descLabel, BorderLayout.CENTER);

        RoundedButton button = new RoundedButton(title, color, color.darker());
        button.setPreferredSize(new Dimension(140, 36));
        button.addActionListener(e -> action.run());

        JPanel buttonWrap = new JPanel(new BorderLayout());
        buttonWrap.setOpaque(false);
        buttonWrap.add(button, BorderLayout.WEST);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(buttonWrap, BorderLayout.SOUTH);
        return card;
    }

    // ------------------------------------------------------------------
    // Action handlers
    // ------------------------------------------------------------------

    private void handleNewReport() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Start a new report? Any unsaved changes will be lost.",
                "New Report", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            reportManager.newReport();
            actions.refreshAllPanels();
            actions.setStatus("New report started");
        }
    }

    private void handleSaveReport() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Incident Report as JSON");
        chooser.setFileFilter(new FileNameExtensionFilter("JSON Report (*.json)", "json"));
        chooser.setSelectedFile(new File("incident_report_" + DateTimeUtils.fileTimestamp() + ".json"));

        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = ensureExtension(chooser.getSelectedFile(), ".json");
        try {
            reportManager.saveToFile(file);
            actions.refreshAllPanels();
            actions.setStatus("Report saved");
            JOptionPane.showMessageDialog(this,
                    "Report saved successfully to:\n" + file.getAbsolutePath(),
                    "Save Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            actions.setStatusError("Save failed");
            JOptionPane.showMessageDialog(this,
                    "Could not save the report:\n" + ex.getMessage(),
                    "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLoadReport() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load Incident Report from JSON");
        chooser.setFileFilter(new FileNameExtensionFilter("JSON Report (*.json)", "json"));

        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = chooser.getSelectedFile();
        try {
            reportManager.loadFromFile(file);
            actions.refreshAllPanels();
            actions.setStatus("Report loaded");
            JOptionPane.showMessageDialog(this,
                    "Report loaded successfully from:\n" + file.getAbsolutePath(),
                    "Load Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            actions.setStatusError("Load failed");
            JOptionPane.showMessageDialog(this,
                    "Could not load the report:\n" + ex.getMessage(),
                    "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleClearForm() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Clear every field on every screen? This cannot be undone.",
                "Confirm Clear Form", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            actions.clearAllForms();
            actions.setStatus("Form cleared");
        }
    }

    private void handleExportPdf() {
        // Validate the essential fields exist before exporting.
        if (com.incidentreport.util.ValidationUtils.isEmpty(reportManager.getCurrentReport().getTitle())) {
            JOptionPane.showMessageDialog(this,
                    "Please enter at least an Incident Title (in Incident Details) before exporting.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export Incident Report as PDF");
        chooser.setFileFilter(new FileNameExtensionFilter("PDF Document (*.pdf)", "pdf"));
        chooser.setSelectedFile(new File("incident_report_" + DateTimeUtils.fileTimestamp() + ".pdf"));

        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = ensureExtension(chooser.getSelectedFile(), ".pdf");
        try {
            reportManager.exportToPdf(file);
            actions.refreshAllPanels();
            actions.setStatus("PDF exported");
            JOptionPane.showMessageDialog(this,
                    "PDF exported successfully to:\n" + file.getAbsolutePath(),
                    "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            actions.setStatusError("Export failed");
            JOptionPane.showMessageDialog(this,
                    "Could not export the PDF:\n" + ex.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleExit() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit Incident Report Generator?",
                "Confirm Exit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private File ensureExtension(File file, String extension) {
        if (!file.getName().toLowerCase().endsWith(extension)) {
            return new File(file.getParentFile(), file.getName() + extension);
        }
        return file;
    }

    /**
     * Callback interface implemented by MainFrame so this panel can trigger
     * cross-cutting actions (refreshing every screen, clearing every form,
     * updating the status bar) without depending directly on MainFrame.
     */
    public interface AppActions {
        void refreshAllPanels();
        void clearAllForms();
        void setStatus(String message);
        void setStatusError(String message);
    }
}
