package com.incidentreport.gui;

import com.incidentreport.model.IOCEntry;
import com.incidentreport.service.ReportManager;
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
 * IOCPanel
 * --------
 * Screen for recording Indicators of Compromise discovered during the
 * investigation: suspicious IPs, domains, URLs, file hashes and file names.
 * Entries are displayed in a JTable and can be added, deleted or cleared.
 */
public class IOCPanel extends JPanel {

    private final ReportManager reportManager;

    private JTextField ipField;
    private JTextField domainField;
    private JTextField urlField;
    private JTextField hashField;
    private JTextField fileNameField;
    private DefaultTableModel tableModel;
    private JTable table;

    public IOCPanel(ReportManager reportManager) {
        this.reportManager = reportManager;
        setLayout(new BorderLayout(0, UIConstants.PADDING_MEDIUM));
        setBackground(UIConstants.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        JLabel header = new JLabel("Indicators of Compromise (IOC)");
        header.setFont(UIConstants.FONT_HEADER);
        header.setForeground(UIConstants.DARK_GRAY_TEXT);
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, UIConstants.PADDING_MEDIUM));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(UIConstants.PADDING_MEDIUM, 0, 0, 0));
        content.add(buildFormPanel(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"Suspicious IP", "Domain", "URL", "File Hash", "File Name"}, 0) {
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

        ipField = new JTextField();
        domainField = new JTextField();
        urlField = new JTextField();
        hashField = new JTextField();
        fileNameField = new JTextField();

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; fields.add(new JLabel("Suspicious IP:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; fields.add(ipField, gbc);
        gbc.gridx = 2; gbc.weightx = 0; fields.add(new JLabel("Domain:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; fields.add(domainField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; fields.add(new JLabel("URL:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; fields.add(urlField, gbc);
        gbc.gridx = 2; gbc.weightx = 0; fields.add(new JLabel("File Hash:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; fields.add(hashField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; fields.add(new JLabel("File Name:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1; fields.add(fileNameField, gbc);
        gbc.gridwidth = 1;

        JPanel buttonRow = new JPanel();
        buttonRow.setOpaque(false);
        RoundedButton addBtn = new RoundedButton("Add IOC");
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

        addBtn.addActionListener(e -> addIOC());
        deleteBtn.addActionListener(e -> deleteSelected());
        clearBtn.addActionListener(e -> clearAll());

        return card;
    }

    private void addIOC() {
        boolean hasAnyValue = !ValidationUtils.isEmpty(ipField.getText())
                || !ValidationUtils.isEmpty(domainField.getText())
                || !ValidationUtils.isEmpty(urlField.getText())
                || !ValidationUtils.isEmpty(hashField.getText())
                || !ValidationUtils.isEmpty(fileNameField.getText());

        if (!hasAnyValue) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in at least one IOC field before adding.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!ValidationUtils.isValidIP(ipField.getText())) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid IP Address (e.g. 203.0.113.25) or leave it blank.",
                    "Invalid IP Address", JOptionPane.ERROR_MESSAGE);
            return;
        }

        IOCEntry entry = new IOCEntry(
                ipField.getText().trim(), domainField.getText().trim(), urlField.getText().trim(),
                hashField.getText().trim(), fileNameField.getText().trim());
        reportManager.getCurrentReport().getIocs().add(entry);
        refreshTable();

        ipField.setText("");
        domainField.setText("");
        urlField.setText("");
        hashField.setText("");
        fileNameField.setText("");
    }

    private void deleteSelected() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to delete first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        reportManager.getCurrentReport().getIocs().remove(selected);
        refreshTable();
    }

    private void clearAll() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove all IOC entries from this report?", "Confirm Clear",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            reportManager.getCurrentReport().getIocs().clear();
            refreshTable();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (IOCEntry entry : reportManager.getCurrentReport().getIocs()) {
            tableModel.addRow(new Object[]{
                    entry.getSuspiciousIp(), entry.getDomain(), entry.getUrl(),
                    entry.getFileHash(), entry.getFileName()});
        }
    }

    /** Called whenever this panel becomes visible so the table reflects the current model. */
    public void refreshFromModel() {
        refreshTable();
    }
}
