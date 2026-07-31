package com.incidentreport;

import com.incidentreport.gui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main
 * ----
 * Application entry point. Sets a clean look-and-feel and launches the
 * MainFrame on the Swing Event Dispatch Thread, as required for all Swing
 * applications. There is no command-line interaction in this application -
 * everything happens inside the GUI window that this class opens.
 */
public class Main {

    public static void main(String[] args) {
        // Use the system look-and-feel as a base so native widgets (like the
        // JFileChooser dialog) feel at home on the user's operating system.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // If the system look-and-feel can't be loaded, Swing's default is used instead.
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
