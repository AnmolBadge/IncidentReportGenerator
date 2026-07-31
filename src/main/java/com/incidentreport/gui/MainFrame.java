package com.incidentreport.gui;

import com.incidentreport.service.ReportManager;
import com.incidentreport.util.UIConstants;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

/**
 * MainFrame
 * ---------
 * The application's main window. Lays out:
 *   - HeaderPanel on the north
 *   - NavigationPanel on the west
 *   - a CardLayout-driven center area with every screen
 *   - StatusBarPanel on the south
 * It also owns the shared ReportManager instance and wires every panel to
 * the same instance so all screens operate on one consistent report.
 */
public class MainFrame extends JFrame implements ExportPanel.AppActions {

    private final ReportManager reportManager;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private DashboardPanel dashboardPanel;
    private IncidentDetailsPanel incidentDetailsPanel;
    private TimelinePanel timelinePanel;
    private IOCPanel iocPanel;
    private RemediationPanel remediationPanel;
    private PreviewPanel previewPanel;
    private ExportPanel exportPanel;
    private AboutPanel aboutPanel;

    private StatusBarPanel statusBarPanel;
    private NavigationPanel navigationPanel;

    public MainFrame() {
        super(UIConstants.APP_NAME);
        this.reportManager = new ReportManager();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(1150, 720);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);

        buildMenuBar();
        buildLayout();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                confirmExit();
            }
        });
    }

    private void buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New Report");
        JMenuItem saveItem = new JMenuItem("Save Report");
        JMenuItem loadItem = new JMenuItem("Load Report");
        JMenuItem exportItem = new JMenuItem("Export PDF");
        JMenuItem exitItem = new JMenuItem("Exit");

        newItem.addActionListener(e -> showCard(NavigationPanel.EXPORT));
        saveItem.addActionListener(e -> showCard(NavigationPanel.EXPORT));
        loadItem.addActionListener(e -> showCard(NavigationPanel.EXPORT));
        exportItem.addActionListener(e -> showCard(NavigationPanel.EXPORT));
        exitItem.addActionListener(e -> confirmExit());

        fileMenu.add(newItem);
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpItem = new JMenuItem("Help Topics");
        JMenuItem aboutItem = new JMenuItem("About");
        helpItem.addActionListener(e -> showCard(NavigationPanel.ABOUT));
        aboutItem.addActionListener(e -> showCard(NavigationPanel.ABOUT));
        helpMenu.add(helpItem);
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void buildLayout() {
        setLayout(new BorderLayout());

        add(new HeaderPanel(), BorderLayout.NORTH);

        navigationPanel = new NavigationPanel(this::showCard);
        add(navigationPanel, BorderLayout.WEST);

        dashboardPanel = new DashboardPanel(reportManager);
        incidentDetailsPanel = new IncidentDetailsPanel(reportManager);
        timelinePanel = new TimelinePanel(reportManager);
        iocPanel = new IOCPanel(reportManager);
        remediationPanel = new RemediationPanel(reportManager);
        previewPanel = new PreviewPanel(reportManager);
        exportPanel = new ExportPanel(reportManager, this);
        aboutPanel = new AboutPanel();

        cardPanel.add(dashboardPanel, NavigationPanel.DASHBOARD);
        cardPanel.add(incidentDetailsPanel, NavigationPanel.INCIDENT_DETAILS);
        cardPanel.add(timelinePanel, NavigationPanel.TIMELINE);
        cardPanel.add(iocPanel, NavigationPanel.IOC);
        cardPanel.add(remediationPanel, NavigationPanel.REMEDIATION);
        cardPanel.add(previewPanel, NavigationPanel.PREVIEW);
        cardPanel.add(exportPanel, NavigationPanel.EXPORT);
        cardPanel.add(aboutPanel, NavigationPanel.ABOUT);

        add(cardPanel, BorderLayout.CENTER);

        statusBarPanel = new StatusBarPanel();
        add(statusBarPanel, BorderLayout.SOUTH);
    }

    /** Switches the CardLayout to the given card and refreshes its data from the model. */
    private void showCard(String cardName) {
        cardLayout.show(cardPanel, cardName);
        navigationPanel.setActive(cardName);

        // Refresh whichever screen is being shown so it reflects the latest model data
        // (important after Save/Load/Clear/New actions triggered from the Export screen).
        if (cardName.equals(NavigationPanel.INCIDENT_DETAILS)) {
            incidentDetailsPanel.refreshFromModel();
        } else if (cardName.equals(NavigationPanel.TIMELINE)) {
            timelinePanel.refreshFromModel();
        } else if (cardName.equals(NavigationPanel.IOC)) {
            iocPanel.refreshFromModel();
        } else if (cardName.equals(NavigationPanel.REMEDIATION)) {
            remediationPanel.refreshFromModel();
        } else if (cardName.equals(NavigationPanel.PREVIEW)) {
            previewPanel.refreshFromModel();
        } else if (cardName.equals(NavigationPanel.DASHBOARD)) {
            dashboardPanel.refreshStats();
        }
    }

    private void confirmExit() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit Incident Report Generator?",
                "Confirm Exit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // ------------------------------------------------------------------
    // ExportPanel.AppActions implementation
    // ------------------------------------------------------------------

    @Override
    public void refreshAllPanels() {
        dashboardPanel.refreshStats();
        incidentDetailsPanel.refreshFromModel();
        timelinePanel.refreshFromModel();
        iocPanel.refreshFromModel();
        remediationPanel.refreshFromModel();
        previewPanel.refreshFromModel();
    }

    @Override
    public void clearAllForms() {
        reportManager.newReport();
        incidentDetailsPanel.clearAllFields();
        remediationPanel.clearAllFields();
        timelinePanel.refreshFromModel();
        iocPanel.refreshFromModel();
        previewPanel.refreshFromModel();
        dashboardPanel.refreshStats();
    }

    @Override
    public void setStatus(String message) {
        statusBarPanel.setStatus(message);
    }

    @Override
    public void setStatusError(String message) {
        statusBarPanel.setError(message);
    }
}
