package com.example;

import com.example.gui.AppWindow;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppWindow::new);
    }
}
