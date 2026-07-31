package com.incidentreport.gui;

import com.incidentreport.util.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * NavigationPanel
 * ---------------
 * Left-hand navigation sidebar. Each entry is a flat, rounded-look button
 * that switches the center CardLayout to the matching screen. The currently
 * selected item is visually highlighted.
 */
public class NavigationPanel extends JPanel {

    public static final String DASHBOARD = "Dashboard";
    public static final String INCIDENT_DETAILS = "Incident Details";
    public static final String TIMELINE = "Timeline";
    public static final String IOC = "IOC";
    public static final String REMEDIATION = "Remediation";
    public static final String PREVIEW = "Preview";
    public static final String EXPORT = "Export";
    public static final String ABOUT = "About";

    private final Map<String, JButton> navButtons = new LinkedHashMap<>();
    private String activeCard = DASHBOARD;

    public NavigationPanel(NavigationListener listener) {
        setLayout(new BorderLayout());
        setBackground(UIConstants.WHITE);
        setPreferredSize(new Dimension(200, 100));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIConstants.MID_GRAY));

        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new javax.swing.BoxLayout(itemsPanel, javax.swing.BoxLayout.Y_AXIS));
        itemsPanel.setBackground(UIConstants.WHITE);
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(UIConstants.PADDING_MEDIUM, 0, 0, 0));

        JLabel brand = new JLabel("  MENU");
        brand.setFont(UIConstants.FONT_SMALL);
        brand.setForeground(UIConstants.SUBTLE_TEXT);
        brand.setBorder(BorderFactory.createEmptyBorder(4, UIConstants.PADDING_MEDIUM, 10, 0));
        itemsPanel.add(brand);

        String[] items = {DASHBOARD, INCIDENT_DETAILS, TIMELINE, IOC, REMEDIATION, PREVIEW, EXPORT, ABOUT};
        for (String item : items) {
            JButton button = createNavButton(item);
            ActionListener action = e -> {
                setActive(item);
                listener.onNavigate(item);
            };
            button.addActionListener(action);
            navButtons.put(item, button);
            itemsPanel.add(button);
        }

        add(itemsPanel, BorderLayout.NORTH);
        setActive(DASHBOARD);
    }

    private JButton createNavButton(String label) {
        JButton button = new JButton(label);
        button.setFont(UIConstants.FONT_NAV);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setBackground(UIConstants.WHITE);
        button.setForeground(UIConstants.DARK_GRAY_TEXT);
        button.setBorder(BorderFactory.createEmptyBorder(
                12, UIConstants.PADDING_LARGE, 12, UIConstants.PADDING_LARGE));
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        return button;
    }

    /** Highlights the given nav item as the active screen. */
    public void setActive(String card) {
        this.activeCard = card;
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            boolean isActive = entry.getKey().equals(card);
            JButton btn = entry.getValue();
            btn.setBackground(isActive ? UIConstants.LIGHT_BLUE : UIConstants.WHITE);
            btn.setForeground(isActive ? UIConstants.PRIMARY_BLUE : UIConstants.DARK_GRAY_TEXT);
            btn.setFont(isActive ? UIConstants.FONT_NAV.deriveFont(java.awt.Font.BOLD) : UIConstants.FONT_NAV);
        }
    }

    public String getActiveCard() {
        return activeCard;
    }

    /** Callback interface invoked when the user clicks a navigation item. */
    public interface NavigationListener {
        void onNavigate(String cardName);
    }
}
