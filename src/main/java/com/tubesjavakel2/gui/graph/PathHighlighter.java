package com.tubesjavakel2.gui.graph;

import com.mxgraph.view.mxGraph;

public class PathHighlighter {

    public static void highlight(mxGraph graph, Object parent, GraphManager gm,
            java.util.List<String> path) {

        graph.getModel().beginUpdate();
        try {
            Object[] edges = graph.getChildEdges(parent);
            for (Object e : edges) {
                graph.setCellStyle("strokeColor=black", new Object[] { e });
            }

            for (int i = 0; i < path.size() - 1; i++) {
                colorEdge(graph, gm, path.get(i), path.get(i + 1));
                colorEdge(graph, gm, path.get(i + 1), path.get(i));
            }

        } finally {
            graph.getModel().endUpdate();
        }
    }

    private static void colorEdge(mxGraph graph, GraphManager gm, String from, String to) {
        Object[] edges = graph.getChildEdges(gm.parent);

        for (Object e : edges) {
            Object src = graph.getModel().getTerminal(e, true);
            Object dst = graph.getModel().getTerminal(e, false);

            String s = (String) graph.getModel().getValue(src);
            String d = (String) graph.getModel().getValue(dst);

            if (s.equals(from) && d.equals(to)) {
                graph.setCellStyle("strokeColor=red;strokeWidth=3", new Object[] { e });
            }
        }
    }
}
