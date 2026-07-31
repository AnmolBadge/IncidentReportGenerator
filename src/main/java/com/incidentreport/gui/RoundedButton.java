package com.incidentreport.gui;

import com.incidentreport.util.UIConstants;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * RoundedButton
 * -------------
 * A JButton subclass painted with rounded corners so the application looks
 * like a modern dashboard instead of a default-Swing college project. Used
 * for every action button across the app.
 */
public class RoundedButton extends JButton {

    private final Color backgroundColor;
    private final Color hoverColor;
    private Color currentColor;
    private static final int ARC = 14;

    public RoundedButton(String text) {
        this(text, UIConstants.PRIMARY_BLUE, UIConstants.PRIMARY_BLUE_DARK);
    }

    public RoundedButton(String text, Color backgroundColor, Color hoverColor) {
        super(text);
        this.backgroundColor = backgroundColor;
        this.hoverColor = hoverColor;
        this.currentColor = backgroundColor;

        setFont(UIConstants.FONT_BUTTON);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                currentColor = RoundedButton.this.hoverColor;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                currentColor = RoundedButton.this.backgroundColor;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(isEnabled() ? currentColor : UIConstants.MID_GRAY);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC, ARC);
        g2.dispose();
        super.paintComponent(g);
    }
}
