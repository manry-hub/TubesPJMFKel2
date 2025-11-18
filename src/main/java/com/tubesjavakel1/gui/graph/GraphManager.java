package com.tubesjavakel1.gui.graph;

import com.mxgraph.view.mxGraph;
import java.util.HashMap;
import java.util.Map;

public class GraphManager {

    public mxGraph graph = new mxGraph();
    public Object parent = graph.getDefaultParent();

    public Map<String, Object> nodeMap = new HashMap<>();
    public Map<String, Map<String, Integer>> adj = new HashMap<>();

    public GraphManager() {
        graph.getModel().beginUpdate();
        graph.getModel().endUpdate();
    }

    public void addNode(String name) {
        graph.getModel().beginUpdate();
        try {
            String style = "shape=ellipse;perimeter=ellipsePerimeter";
            Object v = graph.insertVertex(parent, null, name, 50, 50, 60, 60, style);

            nodeMap.put(name, v);
            adj.put(name, new HashMap<>());
        } finally {
            graph.getModel().endUpdate();
        }
    }

    public void addEdge(String from, String to, int weight) {
        graph.getModel().beginUpdate();
        try {
            graph.insertEdge(parent, null, weight, nodeMap.get(from), nodeMap.get(to));
            adj.get(from).put(to, weight);
            adj.get(to).put(from, weight); // undirected
        } finally {
            graph.getModel().endUpdate();
        }
    }
}
