package com.tubespjmfkel2.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WelcomeFrame extends JFrame {
    private final MainFrame mainFrame;

    @Autowired
    public WelcomeFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();

    }

    private void initUI() {
        setTitle("Kelompok 2");
        setSize(900, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        add(mainPanel);

        JLabel title = new JLabel(
                "<html><center>Aplikasi Pencarian Rute Terpendek Menuju Bengkel<br>"
                        + "Menggunakan Algoritma Dijkstra</center></html>",
                SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        mainPanel.add(title, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        btnPanel.setOpaque(false);

        JButton btnImport = new JButton("Import CSV");
        JButton btnMasuk = new JButton("Masuk ke Aplikasi");

        btnImport.setPreferredSize(new Dimension(180, 40));
        btnMasuk.setPreferredSize(new Dimension(180, 40));

        btnPanel.add(btnImport);
        btnPanel.add(btnMasuk);
        mainPanel.add(btnPanel, BorderLayout.CENTER);

        // event import CSV
        btnImport.addActionListener(e -> importCsv());

        // masuk ke mainframe
        btnMasuk.addActionListener(e -> {
            mainFrame.setVisible(true);

            dispose();
        });

    }

    private void importCsv() {
        String pathCsv = JOptionPane.showInputDialog("Masukkan path CSV:");

        if (pathCsv == null || pathCsv.isBlank())
            return;

        try {

            boolean ok = mainFrame.importCSV(pathCsv);
            mainFrame.setVisible(true);
            if (!ok) {
                mainFrame.dispose();
                JOptionPane.showMessageDialog(
                        this,
                        "Import CSV gagal. Periksa file!",
                        "Gagal",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            dispose();


        } catch (Exception error) {
            JOptionPane.showMessageDialog(
                    this,
                    "Terjadi kesalahan: " + error.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            int w = getWidth();
            int h = getHeight();

            GradientPaint paint = new GradientPaint(
                    0, 0, new Color(17, 49, 71),
                    0, h, new Color(13, 80, 125));

            g2.setPaint(paint);
            g2.fillRect(0, 0, w, h);
        }
    }
}
