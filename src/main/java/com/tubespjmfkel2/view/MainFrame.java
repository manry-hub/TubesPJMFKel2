package com.tubespjmfkel2.view;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;

import com.tubespjmfkel2.domain.Vertex;
import com.tubespjmfkel2.service.GraphService;
import com.tubespjmfkel2.service.DijkstraService;
import com.tubespjmfkel2.dto.DijkstraResult;

import org.apache.commons.csv.CSVFormat;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.io.File;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainFrame extends JFrame {

    private final GraphService graphService;
    private final DijkstraService dijkstraService;

    private mxGraph uiGraph = new mxGraph();
    private Map<String, Object> uiVertexMap = new HashMap<>();
    private Map<String, Object> uiEdgeMap = new HashMap<>();
    private mxGraphComponent graphComponent = new mxGraphComponent(uiGraph);

    @Autowired
    public MainFrame(GraphService graphService, DijkstraService dijkstraService) {
        this.graphService = graphService;
        this.dijkstraService = dijkstraService;
        initUI();
    }


    private void initUI() {

        setTitle("Pencarian Rute Terpendek Menuju Bengkel");

        JButton btnAddVertex = new JButton("➕ Tambah Titik Tempat");
        JButton btnAddEdge = new JButton("➕ Tambah Jarak");
        JButton btnFindPath = new JButton("🔎 Cari Rute Terpendek");
        JButton btnFindNearest = new JButton("🛠 Cari Bengkel Terdekat");
        JButton btnResetGraph = new JButton("➖ Reset Semua");

        btnAddVertex.addActionListener(e -> addVertex());
        btnAddEdge.addActionListener(e -> addEdge());
        btnFindPath.addActionListener(e -> findPath());
        btnResetGraph.addActionListener(e -> resetGraph());
        btnFindNearest.addActionListener(e -> findNearestWorkshop());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.DARK_GRAY);

        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.add(btnAddVertex);
        leftPanel.add(btnAddEdge);
        leftPanel.add(btnResetGraph);

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.add(btnFindPath);
        rightPanel.add(btnFindNearest);


        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        Image bgImage = loadImage();
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        backgroundPanel.add(graphComponent, BorderLayout.CENTER);

        graphComponent.setOpaque(false);
        graphComponent.getViewport().setOpaque(false);
        graphComponent.getGraphControl().setOpaque(false);
        graphComponent.setConnectable(false);

        setContentPane(backgroundPanel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private Image loadImage() {
        try {
            return ImageIO.read(new File(
                    "/home/ughway/Debiancode/Myprojects/TubesPJMFKel2/src/main/java/com/tubespjmfkel2/view/asset/BackgroundWhite.png"));
        } catch (Exception e) {
            System.out.println("Gagal load image: " + e.getMessage());
            return null;
        }
    }

    public boolean importCSV(String path) {
        try (var reader = new java.io.FileReader(path)) {

            var csv = CSVFormat.DEFAULT.builder()
                    .setHeader("type", "source", "destination", "weight")
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(reader);

            int v = 0, e = 0;

            for (var row : csv) {
                String type = row.get("type");
                String source = row.get("source");
                String destination = row.get("destination");
                String weight = row.get("weight");

                if (type.equalsIgnoreCase("V")) {
                    String error = graphService.addVertex(source);
                    if (error == null) {
                        addVertexUI(source);
                        v++;
                    }
                }

                if (type.equalsIgnoreCase("E")) {
                    String error = graphService.addEdge(source, destination, Integer.parseInt(weight));
                    if (error == null) {
                        addEdgeUI(source, destination, Integer.parseInt(weight));
                        e++;
                    }
                }
            }

            JOptionPane.showMessageDialog(null, "✔ Jumlah Vertex: " + v + "\n✔ Jumlah Edge: " + e);
            refreshGraph();
            return true;

        } catch (Exception error) {
            return false;
        }
    }

    private void addVertex() {
        String vertexName = JOptionPane.showInputDialog("Nama Titik Tempat:");
        String error = graphService.addVertex(vertexName);

        if (error != null) {
            JOptionPane.showMessageDialog(null, error);
            return;
        }

        addVertexUI(vertexName);
        refreshGraph();
    }

    private static final Map<String, String> ICON_MAP = Map.of(
            "rumah", "rumah.png",
            "bengkel", "bengkel.png",
            "taman", "taman.png",
            "stasiun", "stasiun.png",
            "masjid", "masjid.png",
            "gereja", "gereja.png",
            "toko", "toko.png",
            "kafe", "kafe.png",
            "kampus", "kampus.png"

    );

    private void addVertexUI(String vertexName) {

        uiGraph.getModel().beginUpdate();
        try {

            String lower = vertexName.toLowerCase();
            String iconFile = null;
            for (var entry : ICON_MAP.entrySet()) {
                if (lower.contains(entry.getKey())) {
                    iconFile = entry.getValue();
                    break;
                }
            }

            String imagePath = null;
            if (iconFile != null) {
                imagePath = "/home/ughway/Debiancode/Myprojects/TubesPJMFKel2/src/main/java/com/tubespjmfkel2/view/asset/icon/"
                        + iconFile;
            }

            File imgFile = (imagePath != null) ? new File(imagePath) : null;

            String style;
            if (imgFile != null && imgFile.exists()) {
                style =
                        "shape=image;" +
                                "image=" + imgFile.toURI() + ";" +
                                "imageWidth=80;" +
                                "imageHeight=80;" +
                                "fontColor=black;" +
                                "fontSize=20;" +
                                "align=center;" +
                                "verticalAlign=top;" +
                                "labelPosition=bottom;" +
                                "verticalLabelPosition=bottom;";
            } else {
                style =
                        "shape=ellipse;" +
                                "strokeWidth=3;" +
                                "fontSize=20;" +
                                "strokeColor=white;" +
                                "fontColor=black;";
            }

            Object uiVertex = uiGraph.insertVertex(
                    uiGraph.getDefaultParent(),
                    null,
                    vertexName,
                    100, 100,
                    100, 110,
                    style
            );

            uiVertexMap.put(vertexName, uiVertex);

        } finally {
            uiGraph.getModel().endUpdate();
        }
    }


    private void addEdge() {
        String source = JOptionPane.showInputDialog("Dari Titik Tempat:");
        String destination = JOptionPane.showInputDialog("Menuju Titik Tempat:");
        String weight = JOptionPane.showInputDialog("Jarak (km):");

        String error = graphService.addEdge(source, destination, Integer.parseInt(weight));

        if (error != null) {
            JOptionPane.showMessageDialog(null, error);
            return;
        }

        addEdgeUI(source, destination, Integer.parseInt(weight));
        refreshGraph();
    }

    private void addEdgeUI(String source, String destination, int weight) {

        Object vertexSource = uiVertexMap.get(source);
        Object vertexDestination = uiVertexMap.get(destination);

        uiGraph.getModel().beginUpdate();
        try {
            Object edge = uiGraph.insertEdge(
                    uiGraph.getDefaultParent(),
                    null,
                    weight,
                    vertexSource,
                    vertexDestination,
                    "endArrow=none;strokeColor=black;rounded=true;strokeWidth=3;fontColor=black;fontSize=20;");

            uiEdgeMap.put(source + "->" + destination, edge);
            uiEdgeMap.put(destination + "->" + source, edge);

        } finally {
            uiGraph.getModel().endUpdate();
        }
    }

    private void findNearestWorkshop() {

        String start = JOptionPane.showInputDialog("Lokasi Anda Sekarang:");

        if (start == null || start.isBlank()) {
            JOptionPane.showMessageDialog(null, "Input tidak boleh kosong");
            return;
        }

        // Ambil semua vertex yang namanya mengandung kata "Bengkel"
        List<String> workshopList = graphService.getGraph().getVertices().stream()
                .map(Vertex::getName)
                .filter(n -> n.toLowerCase().contains("bengkel"))
                .toList();

        if (workshopList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tidak ada vertex yang terdeteksi sebagai bengkel!");
            return;
        }

        // Panggil fitur baru dari service
        DijkstraResult result = dijkstraService.findNearestDestination(start, workshopList);

        if (result == null) {
            JOptionPane.showMessageDialog(null, "Tidak ditemukan bengkel yang dapat dicapai!");
            return;
        }

        // Highlight path seperti fitur existing
        highlightPath(result.getPath());

        // Tampilkan hasil ke dialog
        StringBuilder sb = new StringBuilder();
        sb.append("Bengkel Terdekat dari ").append(start).append("\n\n");
        sb.append("Total jarak: ").append(result.getDistance()).append(" km\n\n");
        sb.append("Rute:\n");

        List<String> p = result.getPath();
        for (int i = 0; i < p.size(); i++) {
            sb.append(p.get(i));
            if (i < p.size() - 1)
                sb.append(" → ");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void findPath() {
        String start = JOptionPane.showInputDialog("Dari Titik:");
        String end = JOptionPane.showInputDialog("Menuju Titik:");

        DijkstraResult result = dijkstraService.findShortestPath(start, end);

        if (result == null) {
            JOptionPane.showMessageDialog(null, "Rute tidak ditemukan!");
            return;
        }

        highlightPath(result.getPath());

        StringBuilder sb = new StringBuilder();
        sb.append("Rute Terpendek\n")
                .append("Dari: ").append(start).append("\n")
                .append("Menuju: ").append(end).append("\n")
                .append("Total Jarak: ").append(result.getDistance()).append(" km\n\n")
                .append("Urutan Titik:\n");

        List<String> path = result.getPath();
        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i));
            if (i < path.size() - 1)
                sb.append(" → ");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void highlightPath(List<String> path) {

        uiGraph.getModel().beginUpdate();
        try {
            uiEdgeMap.forEach((k, e) -> uiGraph.setCellStyle(
                    "strokeColor=red;strokeWidth=3;endArrow=none;rounded=true;fontColor=black;fontSize=20",
                    new Object[]{e}));

            for (int i = 0; i < path.size() - 1; i++) {
                colorEdge(path.get(i), path.get(i + 1));
            }

        } finally {
            uiGraph.getModel().endUpdate();
        }
    }

    private void colorEdge(String source, String destination) {
        Object edge = uiEdgeMap.get(source + "->" + destination);
        if (edge != null) {
            uiGraph.setCellStyle(
                    "strokeColor=green;strokeWidth=5;endArrow=none;rounded=true;fontColor=black;fontSize=20",
                    new Object[]{edge});
        }
    }

    public void resetGraph() {
        graphService.reset();
        uiVertexMap.clear();
        uiEdgeMap.clear();

        uiGraph.getModel().beginUpdate();
        try {
            uiGraph.removeCells(uiGraph.getChildCells(uiGraph.getDefaultParent(), true, true), true);
        } finally {
            uiGraph.getModel().endUpdate();
        }

        refreshGraph();
    }

    private void refreshGraph() {
        SwingUtilities.invokeLater(() -> {
            mxHierarchicalLayout layout = new mxHierarchicalLayout(uiGraph);
            layout.setOrientation(SwingConstants.WEST);
            layout.setIntraCellSpacing(70);

            uiGraph.getModel().beginUpdate();
            try {
                layout.execute(uiGraph.getDefaultParent());
            } finally {
                uiGraph.getModel().endUpdate();
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            autoCenterGraph(uiGraph, graphComponent);
            graphComponent.refresh();
            repaint();
            revalidate();
        });
    }

    public void autoCenterGraph(mxGraph uiGraph, mxGraphComponent graphComponent) {
        SwingUtilities.invokeLater(() -> {
            Dimension size = graphComponent.getSize();
            if (size.width == 0 || size.height == 0)
                return;

            mxRectangle bounds = uiGraph.getGraphBounds();
            if (bounds == null)
                return;

            double dx = (size.getWidth() - bounds.getWidth()) / 2 - bounds.getX();
            double dy = (size.getHeight() - bounds.getHeight()) / 2 - bounds.getY();

            Object parent = uiGraph.getDefaultParent();
            Object[] cells = uiGraph.getChildCells(parent, true, true);

            uiGraph.getModel().beginUpdate();
            try {
                uiGraph.moveCells(cells, dx, dy);
            } finally {
                uiGraph.getModel().endUpdate();
            }

            uiGraph.refresh();
        });
    }

}
