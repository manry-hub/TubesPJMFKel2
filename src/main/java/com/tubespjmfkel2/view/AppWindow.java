package com.tubespjmfkel2.view;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.tubespjmfkel2.controller.DijkstraController;
import com.tubespjmfkel2.controller.GraphController;
import com.tubespjmfkel2.dto.DijkstraResult;

/**
 * Kelas utama antarmuka grafis (GUI) aplikasi pencarian rute terpendek
 * menggunakan algoritma Dijkstra.
 *
 * <p>
 * Kelas ini berfungsi sebagai:
 * <ul>
 * <li>Tempat interaksi pengguna seperti menambah node, menambah edge,
 * menjalankan perhitungan rute terpendek, dan mereset grafik</li>
 * <li>Menghubungkan layer UI dengan controller logika aplikasi</li>
 * <li>Menampilkan visualisasi graph melalui {@link GraphPanel}</li>
 * </ul>
 *
 * <p>
 * Menggunakan Swing sebagai framework UI dan JGraphX sebagai library
 * visualisasi graf.
 * </p>
 */
public class AppWindow extends JFrame {

    /** Controller yang menangani operasi graph model dan UI graph. */
    private final GraphController graphController = new GraphController();

    /** Controller yang menangani proses perhitungan algoritma Dijkstra. */
    private final DijkstraController dijkstraController = new DijkstraController(graphController);

    /** Panel utama untuk menampilkan graf dalam bentuk visual. */
    private final GraphPanel graphPanel = new GraphPanel(graphController);

    /**
     * Konstruktor utama yang menginisialisasi jendela, tombol menu, dan panel graf.
     */
    public AppWindow() {
        super("Pencarian Rute Terpendek Menuju Bengkel");

        // Tombol - tombol aksi
        JButton btnAddNode = new JButton("Tambah Titik");
        JButton btnAddEdge = new JButton("Tambah Jarak");
        JButton btnFindPath = new JButton("Cari Rute Terpendek");
        JButton btnReset = new JButton("Reset Semua");

        // Listener masing-masing tombol
        btnAddNode.addActionListener(e -> addNode());
        btnAddEdge.addActionListener(e -> addEdge());
        btnFindPath.addActionListener(e -> findPath());
        btnReset.addActionListener(e -> resetAll());

        // Panel yang menampung tombol
        JPanel top = new JPanel();
        top.add(btnAddNode);
        top.add(btnAddEdge);
        top.add(btnFindPath);
        top.add(btnReset);

        // Layout utama jendela
        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(graphPanel, BorderLayout.CENTER);

        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ========================================================================
    // Menambah Node
    // ========================================================================

    /**
     * Menampilkan dialog input untuk menambah node baru pada graf.
     * Jika input valid, node ditambahkan ke model dan tampilan diperbarui.
     */
    private void addNode() {
        String name = JOptionPane.showInputDialog("Nama Tempat:");

        if (name == null || name.trim().isEmpty())
            return;

        String result = graphController.addNode(name);

        if (result != null)
            JOptionPane.showMessageDialog(this, result);

        graphPanel.refresh();
    }

    // ========================================================================
    // Menambah Edge
    // ========================================================================

    /**
     * Menambah hubungan (edge) antar dua node dengan bobot jarak.
     * Menggunakan dialog untuk menerima input asal, tujuan, dan bobot.
     */
    private void addEdge() {
        String from = JOptionPane.showInputDialog("Dari Node:");
        String to = JOptionPane.showInputDialog("Menuju Node:");

        if (from == null || to == null)
            return;

        String weightStr = JOptionPane.showInputDialog("Jarak (km):");
        if (weightStr == null)
            return;

        int weight;
        try {
            weight = Integer.parseInt(weightStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Bobot harus angka!");
            return;
        }

        String result = graphController.addEdge(from, to, weight);

        if (result != null)
            JOptionPane.showMessageDialog(this, result);

        graphPanel.refresh();
    }

    // ========================================================================
    // Menjalankan Dijkstra
    // ========================================================================

    /**
     * Menjalankan perhitungan rute terpendek menggunakan algoritma Dijkstra.
     *
     * <p>
     * Jika rute ditemukan:
     * <ul>
     * <li>Graf ditandai (highlighting) pada jalur yang dilalui</li>
     * <li>Informasi rute dan total jarak ditampilkan dalam dialog</li>
     * </ul>
     *
     * <p>
     * Jika gagal, pengguna diberi pesan bahwa rute tidak tersedia.
     * </p>
     */
    private void findPath() {

        String start = JOptionPane.showInputDialog("Dari:");
        String end = JOptionPane.showInputDialog("Menuju:");

        if (start == null || end == null)
            return;

        DijkstraResult result = dijkstraController.runDijkstra(start, end);

        if (result == null) {
            JOptionPane.showMessageDialog(this, "Rute tidak ditemukan!");
            return;
        }

        List<String> path = result.getPath();
        int distance = result.getDistance();

        PathHighlighter.highlight(
                graphController.getUiGraph(),
                graphController,
                path);

        StringBuilder sb = new StringBuilder();
        sb.append("Rute Terpendek\n")
                .append("Dari: ").append(start).append("\n")
                .append("Menuju: ").append(end).append("\n")
                .append("Total Jarak: ").append(distance).append(" km\n\n")
                .append("Urutan Titik:\n");

        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i));
            if (i < path.size() - 1)
                sb.append(" → ");
        }

        JOptionPane.showMessageDialog(this, sb.toString());
    }

    // ========================================================================
    // Reset Seluruh Data
    // ========================================================================

    /**
     * Mereset seluruh graf dan tampilan visual menjadi kondisi awal.
     * Akan menampilkan dialog konfirmasi sebelum memproses reset.
     */
    private void resetAll() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin mereset semua?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            graphController.resetGraph();
            graphPanel.refresh();
        }
    }
}
