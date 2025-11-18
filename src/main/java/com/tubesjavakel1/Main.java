package com.tubesjavakel1;

import javax.swing.SwingUtilities;

import com.tubesjavakel1.gui.AppWindow;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppWindow::new);
    }
}
