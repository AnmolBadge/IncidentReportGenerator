package com.incidentreport.gui;

import com.incidentreport.util.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridLayout;

/**
 * AboutPanel
 * ----------
 * Screen 8 of the application. Uses a JTabbedPane with two tabs:
 *   - "About": application name, version, developer, description, tech stack.
 *   - "Help": simple step-by-step usage instructions for the user.
 */
public class AboutPanel extends JPanel {

    public AboutPanel() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIConstants.FONT_LABEL);
        tabbedPane.addTab("About", buildAboutTab());
        tabbedPane.addTab("Help", buildHelpTab());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel buildAboutTab() {
        JPanel panel = new JPanel(new BorderLayout(0, UIConstants.PADDING_LARGE));
        panel.setBackground(UIConstants.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        JLabel title = new JLabel(UIConstants.APP_NAME);
        title.setFont(UIConstants.FONT_TITLE);
        title.setForeground(UIConstants.PRIMARY_BLUE);
        panel.add(title, BorderLayout.NORTH);

        JPanel infoGrid = new JPanel(new GridLayout(0, 1, 0, 10));
        infoGrid.setOpaque(false);
        infoGrid.add(infoRow("Version:", UIConstants.APP_VERSION));
        infoGrid.add(infoRow("Developer:", UIConstants.APP_AUTHOR));
        infoGrid.add(infoRow("Description:",
                "A desktop application for building, saving and exporting professional "
              + "security incident response reports - from initial details through to "
              + "timeline, indicators of compromise, remediation and a print-ready PDF."));
        infoGrid.add(infoRow("Technologies Used:",
                "Java 17, Java Swing, Apache PDFBox (PDF export), Gson (JSON save/load)"));

        panel.add(infoGrid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel infoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(UIConstants.FONT_SUBHEADER);
        labelComponent.setForeground(UIConstants.DARK_GRAY_TEXT);
        labelComponent.setPreferredSize(new java.awt.Dimension(150, 20));

        JLabel valueComponent = new JLabel("<html><body style='width:500px'>" + value + "</body></html>");
        valueComponent.setFont(UIConstants.FONT_LABEL);
        valueComponent.setForeground(UIConstants.SUBTLE_TEXT);

        row.add(labelComponent, BorderLayout.WEST);
        row.add(valueComponent, BorderLayout.CENTER);
        return row;
    }

    private JPanel buildHelpTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        JTextArea helpText = new JTextArea(
                "HOW TO USE INCIDENT REPORT GENERATOR\n\n"
              + "1. Dashboard\n"
              + "   See a quick summary of your session and a short guide.\n\n"
              + "2. Incident Details\n"
              + "   Fill in the core details of the incident on the first tab, and list any\n"
              + "   affected computers/systems on the second tab. Click 'Save Details to Report'.\n\n"
              + "3. Timeline\n"
              + "   Add each notable event with its date, time and a short description.\n\n"
              + "4. IOC\n"
              + "   Record suspicious IPs, domains, URLs, file names and file hashes found\n"
              + "   during the investigation.\n\n"
              + "5. Remediation\n"
              + "   Document the immediate actions, investigation summary, root cause,\n"
              + "   remediation steps and future recommendations, then click 'Save Remediation to Report'.\n\n"
              + "6. Preview\n"
              + "   Click 'Refresh Preview' any time to see the complete, formatted report.\n\n"
              + "7. Export\n"
              + "   Use 'Save Report' to store your work as a JSON file you can reopen later\n"
              + "   with 'Load Report', or 'Export PDF' to generate the final PDF document.\n"
              + "   'Clear Form' wipes every field, and 'New Report' starts over completely.\n\n"
              + "All actions happen through this window - there is no command line involved.");
        helpText.setEditable(false);
        helpText.setOpaque(false);
        helpText.setFont(UIConstants.FONT_LABEL);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);

        panel.add(new JScrollPane(helpText), BorderLayout.CENTER);
        return panel;
    }
}
