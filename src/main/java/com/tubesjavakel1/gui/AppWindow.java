package com.tubesjavakel1.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.tubesjavakel1.algorithm.Dijkstra;
import com.tubesjavakel1.gui.graph.GraphManager;
import com.tubesjavakel1.gui.graph.GraphPanel;
import com.tubesjavakel1.gui.graph.PathHighlighter;

import java.util.List;

public class AppWindow extends JFrame {

    private GraphManager gm = new GraphManager();

    public AppWindow() {
        super("Graph + Dijkstra + JGraphX");

        JButton btnAddNode = new JButton("Add Node");
        JButton btnAddEdge = new JButton("Add Edge");
        JButton btnFindPath = new JButton("Find Shortest Path");

        btnAddNode.addActionListener(e -> addNode());
        btnAddEdge.addActionListener(e -> addEdge());
        btnFindPath.addActionListener(e -> findPath());

        JPanel top = new JPanel();
        top.add(btnAddNode);
        top.add(btnAddEdge);
        top.add(btnFindPath);

        add(top, BorderLayout.NORTH);
        add(new GraphPanel(gm), BorderLayout.CENTER);

        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addNode() {
        String name = JOptionPane.showInputDialog("Node:");
        if (name != null && !name.isEmpty()) {
            gm.addNode(name);
        }
    }

    private void addEdge() {
        String from = JOptionPane.showInputDialog("From:");
        String to = JOptionPane.showInputDialog("To:");
        int w = Integer.parseInt(JOptionPane.showInputDialog("Weight:"));
        gm.addEdge(from, to, w);
    }

    private void findPath() {
        String start = JOptionPane.showInputDialog("Start:");
        String end = JOptionPane.showInputDialog("End:");

        List<String> path = Dijkstra.run(gm.adj, start, end);
        if (path == null) {
            JOptionPane.showMessageDialog(this, "Path tidak ditemukan!");
            return;
        }

        PathHighlighter.highlight(gm.graph, gm.parent, gm, path);
    }
}
