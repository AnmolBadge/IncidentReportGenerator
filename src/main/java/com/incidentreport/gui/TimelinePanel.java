package com.incidentreport.gui;

import com.incidentreport.model.TimelineEvent;
import com.incidentreport.service.ReportManager;
import com.incidentreport.util.DateTimeUtils;
import com.incidentreport.util.UIConstants;
import com.incidentreport.util.ValidationUtils;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * TimelinePanel
 * -------------
 * Screen for building the incident timeline: add dated/timed events which
 * are displayed in a JTable, with the ability to delete a selected event
 * or clear the whole timeline.
 */
public class TimelinePanel extends JPanel {

    private final ReportManager reportManager;

    private JTextField dateField;
    private JTextField timeField;
    private JTextField descriptionField;
    private DefaultTableModel tableModel;
    private JTable table;

    public TimelinePanel(ReportManager reportManager) {
        this.reportManager = reportManager;
        setLayout(new BorderLayout(0, UIConstants.PADDING_MEDIUM));
        setBackground(UIConstants.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        JLabel header = new JLabel("Timeline Builder");
        header.setFont(UIConstants.FONT_HEADER);
        header.setForeground(UIConstants.DARK_GRAY_TEXT);
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, UIConstants.PADDING_MEDIUM));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(UIConstants.PADDING_MEDIUM, 0, 0, 0));

        content.add(buildFormPanel(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Date", "Time", "Description"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setFont(UIConstants.FONT_FIELD);
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
    }

    private JPanel buildFormPanel() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UIConstants.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.MID_GRAY),
                BorderFactory.createEmptyBorder(
                        UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM,
                        UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)));

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        dateField = new JTextField(DateTimeUtils.currentFormDate(), 10);
        timeField = new JTextField(DateTimeUtils.currentFormTime(), 6);
        descriptionField = new JTextField();

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; fields.add(new JLabel("Date (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0; fields.add(dateField, gbc);
        gbc.gridx = 2; gbc.weightx = 0; fields.add(new JLabel("Time (HH:mm):"), gbc);
        gbc.gridx = 3; gbc.weightx = 0; fields.add(timeField, gbc);
        gbc.gridx = 4; gbc.weightx = 0; fields.add(new JLabel("Description:"), gbc);
        gbc.gridx = 5; gbc.weightx = 1; fields.add(descriptionField, gbc);

        JPanel buttonRow = new JPanel();
        buttonRow.setOpaque(false);
        RoundedButton addBtn = new RoundedButton("Add Event");
        RoundedButton deleteBtn = new RoundedButton("Delete Selected", UIConstants.ERROR_RED, UIConstants.ERROR_RED.darker());
        RoundedButton clearBtn = new RoundedButton("Clear All", UIConstants.WARNING_ORANGE, UIConstants.WARNING_ORANGE.darker());
        for (RoundedButton b : new RoundedButton[]{addBtn, deleteBtn, clearBtn}) {
            b.setPreferredSize(new Dimension(140, 34));
        }
        buttonRow.add(addBtn);
        buttonRow.add(deleteBtn);
        buttonRow.add(clearBtn);

        card.add(fields, BorderLayout.CENTER);
        card.add(buttonRow, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addEvent());
        deleteBtn.addActionListener(e -> deleteSelected());
        clearBtn.addActionListener(e -> clearAll());

        return card;
    }

    private void addEvent() {
        if (!ValidationUtils.isValidDate(dateField.getText())) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid date in yyyy-MM-dd format.",
                    "Invalid Date", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!ValidationUtils.isValidTime(timeField.getText())) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid time in HH:mm 24-hour format.",
                    "Invalid Time", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (ValidationUtils.isEmpty(descriptionField.getText())) {
            JOptionPane.showMessageDialog(this,
                    "Please describe what happened for this event.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TimelineEvent event = new TimelineEvent(
                dateField.getText().trim(), timeField.getText().trim(), descriptionField.getText().trim());
        reportManager.getCurrentReport().getTimeline().add(event);
        refreshTable();
        descriptionField.setText("");
    }

    private void deleteSelected() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to delete first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        reportManager.getCurrentReport().getTimeline().remove(selected);
        refreshTable();
    }

    private void clearAll() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove all timeline events from this report?", "Confirm Clear",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            reportManager.getCurrentReport().getTimeline().clear();
            refreshTable();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (TimelineEvent event : reportManager.getCurrentReport().getTimeline()) {
            tableModel.addRow(new Object[]{event.getDate(), event.getTime(), event.getDescription()});
        }
    }

    /** Called whenever this panel becomes visible so the table reflects the current model. */
    public void refreshFromModel() {
        refreshTable();
    }
}
