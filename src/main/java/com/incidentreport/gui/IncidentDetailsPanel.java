package com.incidentreport.gui;

import com.incidentreport.model.IncidentReport;
import com.incidentreport.model.SystemAffected;
import com.incidentreport.service.ReportManager;
import com.incidentreport.util.DateTimeUtils;
import com.incidentreport.util.UIConstants;
import com.incidentreport.util.ValidationUtils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * IncidentDetailsPanel
 * --------------------
 * Screen 2 of the application. Uses a JTabbedPane with two tabs:
 *   - "Incident Details": the core fields describing the incident.
 *   - "Systems Affected": a table of computers/systems impacted.
 * All fields are backed directly by the shared IncidentReport model object
 * so data is never lost when switching screens.
 */
public class IncidentDetailsPanel extends JPanel {

    private final ReportManager reportManager;

    // Incident Details fields
    private JTextField titleField;
    private JComboBox<String> typeCombo;
    private JTextField reportedByField;
    private JTextField departmentField;
    private JTextField dateField;
    private JTextField timeField;
    private JComboBox<String> priorityCombo;
    private JTextArea descriptionArea;

    // Systems Affected fields
    private JTextField sysComputerField;
    private JTextField sysIpField;
    private JTextField sysOsField;
    private JTextField sysDeptField;
    private JTextField sysDescField;
    private DefaultTableModel systemsTableModel;
    private JTable systemsTable;

    public IncidentDetailsPanel(ReportManager reportManager) {
        this.reportManager = reportManager;
        setLayout(new BorderLayout());
        setBackground(UIConstants.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIConstants.FONT_LABEL);
        tabbedPane.addTab("Incident Details", buildDetailsTab());
        tabbedPane.addTab("Systems Affected", buildSystemsTab());
        add(tabbedPane, BorderLayout.CENTER);
    }

    // ------------------------------------------------------------------
    // Tab 1: Incident Details
    // ------------------------------------------------------------------

    private JPanel buildDetailsTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIConstants.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        titleField = new JTextField();
        addFormRow(panel, gbc, row++, "Incident Title:", titleField);

        typeCombo = new JComboBox<>(new String[]{
                "Malware", "Phishing", "Data Breach", "Unauthorized Access",
                "Denial of Service", "Insider Threat", "Other"});
        addFormRow(panel, gbc, row++, "Incident Type:", typeCombo);

        reportedByField = new JTextField();
        addFormRow(panel, gbc, row++, "Reported By:", reportedByField);

        departmentField = new JTextField();
        addFormRow(panel, gbc, row++, "Department:", departmentField);

        dateField = new JTextField(DateTimeUtils.currentFormDate());
        addFormRow(panel, gbc, row++, "Incident Date (yyyy-MM-dd):", dateField);

        timeField = new JTextField(DateTimeUtils.currentFormTime());
        addFormRow(panel, gbc, row++, "Incident Time (HH:mm):", timeField);

        priorityCombo = new JComboBox<>(new String[]{"Low", "Medium", "High", "Critical"});
        addFormRow(panel, gbc, row++, "Priority:", priorityCombo);

        descriptionArea = new JTextArea(6, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(UIConstants.FONT_FIELD);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(UIConstants.FONT_LABEL);
        panel.add(descLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        panel.add(descScroll, gbc);
        row++;

        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        RoundedButton saveButton = new RoundedButton("Save Details to Report");
        saveButton.setPreferredSize(new java.awt.Dimension(220, 38));
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(saveButton, gbc);
        saveButton.addActionListener(e -> saveDetailsToModel(true));

        return panel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel label = new JLabel(labelText);
        label.setFont(UIConstants.FONT_LABEL);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        field.setFont(UIConstants.FONT_FIELD);
        panel.add(field, gbc);
    }

    /**
     * Validates and copies the Incident Details tab's fields into the shared
     * IncidentReport model. Returns true if validation passed.
     */
    public boolean saveDetailsToModel(boolean showConfirmation) {
        if (ValidationUtils.isEmpty(titleField.getText())) {
            JOptionPane.showMessageDialog(this, "Incident Title is required.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (ValidationUtils.isEmpty(reportedByField.getText())) {
            JOptionPane.showMessageDialog(this, "Reported By is required.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!ValidationUtils.isValidDate(dateField.getText())) {
            JOptionPane.showMessageDialog(this,
                    "Incident Date must be in yyyy-MM-dd format, e.g. 2026-07-30.",
                    "Invalid Date", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!ValidationUtils.isValidTime(timeField.getText())) {
            JOptionPane.showMessageDialog(this,
                    "Incident Time must be in HH:mm 24-hour format, e.g. 14:35.",
                    "Invalid Time", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        IncidentReport report = reportManager.getCurrentReport();
        report.setTitle(titleField.getText().trim());
        report.setIncidentType((String) typeCombo.getSelectedItem());
        report.setReportedBy(reportedByField.getText().trim());
        report.setDepartment(departmentField.getText().trim());
        report.setIncidentDate(dateField.getText().trim());
        report.setIncidentTime(timeField.getText().trim());
        report.setPriority((String) priorityCombo.getSelectedItem());
        report.setDescription(descriptionArea.getText().trim());

        if (showConfirmation) {
            JOptionPane.showMessageDialog(this, "Incident details saved to the report.",
                    "Saved", JOptionPane.INFORMATION_MESSAGE);
        }
        return true;
    }

    // ------------------------------------------------------------------
    // Tab 2: Systems Affected
    // ------------------------------------------------------------------

    private JPanel buildSystemsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, UIConstants.PADDING_MEDIUM));
        panel.setBackground(UIConstants.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        sysComputerField = new JTextField();
        sysIpField = new JTextField();
        sysOsField = new JTextField();
        sysDeptField = new JTextField();
        sysDescField = new JTextField();

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Computer Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; formPanel.add(sysComputerField, gbc);
        gbc.gridx = 2; gbc.weightx = 0; formPanel.add(new JLabel("IP Address:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; formPanel.add(sysIpField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; formPanel.add(new JLabel("Operating System:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; formPanel.add(sysOsField, gbc);
        gbc.gridx = 2; gbc.weightx = 0; formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; formPanel.add(sysDeptField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1; formPanel.add(sysDescField, gbc);
        gbc.gridwidth = 1;

        JPanel buttonRow = new JPanel();
        buttonRow.setOpaque(false);
        RoundedButton addBtn = new RoundedButton("Add System");
        RoundedButton deleteBtn = new RoundedButton("Delete Selected", UIConstants.ERROR_RED, UIConstants.ERROR_RED.darker());
        RoundedButton clearBtn = new RoundedButton("Clear All", UIConstants.WARNING_ORANGE, UIConstants.WARNING_ORANGE.darker());
        addBtn.setPreferredSize(new java.awt.Dimension(130, 34));
        deleteBtn.setPreferredSize(new java.awt.Dimension(150, 34));
        clearBtn.setPreferredSize(new java.awt.Dimension(100, 34));
        buttonRow.add(addBtn);
        buttonRow.add(deleteBtn);
        buttonRow.add(clearBtn);

        systemsTableModel = new DefaultTableModel(
                new Object[]{"Computer Name", "IP Address", "OS", "Department", "Description"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        systemsTable = new JTable(systemsTableModel);
        systemsTable.setRowHeight(24);
        systemsTable.setFont(UIConstants.FONT_FIELD);
        JScrollPane tableScroll = new JScrollPane(systemsTable);

        JPanel topArea = new JPanel(new BorderLayout());
        topArea.setOpaque(false);
        topArea.add(formPanel, BorderLayout.CENTER);
        topArea.add(buttonRow, BorderLayout.SOUTH);

        panel.add(topArea, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);

        addBtn.addActionListener(e -> addSystem());
        deleteBtn.addActionListener(e -> deleteSelectedSystem());
        clearBtn.addActionListener(e -> clearSystems());

        return panel;
    }

    private void addSystem() {
        if (ValidationUtils.isEmpty(sysComputerField.getText())) {
            JOptionPane.showMessageDialog(this, "Computer Name is required.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!ValidationUtils.isValidIP(sysIpField.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid IP Address (e.g. 192.168.1.10).",
                    "Invalid IP Address", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SystemAffected system = new SystemAffected(
                sysComputerField.getText().trim(),
                sysIpField.getText().trim(),
                sysOsField.getText().trim(),
                sysDeptField.getText().trim(),
                sysDescField.getText().trim());
        reportManager.getCurrentReport().getSystemsAffected().add(system);
        refreshSystemsTable();

        sysComputerField.setText("");
        sysIpField.setText("");
        sysOsField.setText("");
        sysDeptField.setText("");
        sysDescField.setText("");
    }

    private void deleteSelectedSystem() {
        int selected = systemsTable.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to delete first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        reportManager.getCurrentReport().getSystemsAffected().remove(selected);
        refreshSystemsTable();
    }

    private void clearSystems() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove all affected systems from this report?", "Confirm Clear",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            reportManager.getCurrentReport().getSystemsAffected().clear();
            refreshSystemsTable();
        }
    }

    private void refreshSystemsTable() {
        systemsTableModel.setRowCount(0);
        for (SystemAffected s : reportManager.getCurrentReport().getSystemsAffected()) {
            systemsTableModel.addRow(new Object[]{
                    s.getComputerName(), s.getIpAddress(), s.getOperatingSystem(),
                    s.getDepartment(), s.getDescription()});
        }
    }

    /** Called whenever this panel becomes visible so the table reflects the current model. */
    public void refreshFromModel() {
        refreshSystemsTable();
        IncidentReport report = reportManager.getCurrentReport();
        titleField.setText(report.getTitle());
        reportedByField.setText(report.getReportedBy());
        departmentField.setText(report.getDepartment());
        if (!ValidationUtils.isEmpty(report.getIncidentDate())) {
            dateField.setText(report.getIncidentDate());
        }
        if (!ValidationUtils.isEmpty(report.getIncidentTime())) {
            timeField.setText(report.getIncidentTime());
        }
        if (!ValidationUtils.isEmpty(report.getIncidentType())) {
            typeCombo.setSelectedItem(report.getIncidentType());
        }
        if (!ValidationUtils.isEmpty(report.getPriority())) {
            priorityCombo.setSelectedItem(report.getPriority());
        }
        descriptionArea.setText(report.getDescription());
    }

    /** Clears every field on both tabs (used by the global "Clear Form" action). */
    public void clearAllFields() {
        titleField.setText("");
        typeCombo.setSelectedIndex(0);
        reportedByField.setText("");
        departmentField.setText("");
        dateField.setText(DateTimeUtils.currentFormDate());
        timeField.setText(DateTimeUtils.currentFormTime());
        priorityCombo.setSelectedItem("Medium");
        descriptionArea.setText("");
        refreshSystemsTable();
    }
}
