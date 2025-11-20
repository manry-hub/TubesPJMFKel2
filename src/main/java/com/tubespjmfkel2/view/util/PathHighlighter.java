package com.tubespjmfkel2.view.util;

import com.mxgraph.view.mxGraph;
import com.tubespjmfkel2.controller.GraphController;

import java.util.List;

/**
 * PathHighlighter adalah utilitas untuk menyorot jalur tertentu pada graf
 * yang ditampilkan menggunakan JGraphX ({@link mxGraph}).
 * 
 * <p>
 * Class ini digunakan untuk menandai jalur (path) pada graf dengan mengubah
 * warna dan ketebalan edge. Semua edge default akan dikembalikan ke style hitam
 * sebelum edge pada jalur diberi style khusus.
 * </p>
 */
public class PathHighlighter {

    /**
     * Menyorot jalur tertentu pada graph.
     *
     * <p>
     * Semua edge akan dikembalikan ke style default (hitam) terlebih dahulu,
     * kemudian edge pada jalur yang diberikan akan diwarnai hijau dan ditebalkan.
     * </p>
     *
     * @param graph           Graph yang akan dimodifikasi ({@link mxGraph}).
     * @param graphController Controller yang menyediakan mapping edge dan kontrol
     *                        graph.
     * @param path            Daftar nama vertex yang membentuk jalur yang akan
     *                        disorot.
     */
    public static void highlight(mxGraph graph, GraphController graphController, List<String> path) {
        graph.getModel().beginUpdate();
        try {
            // Reset semua edge ke style default
            graphController.getEdgeMap().forEach((k, e) -> {
                graph.setCellStyle("strokeColor=black;strokeWidth=1", new Object[] { e });
            });

            // Sorot edge pada jalur
            for (int i = 0; i < path.size() - 1; i++) {
                colorEdge(graph, graphController, path.get(i), path.get(i + 1));
                colorEdge(graph, graphController, path.get(i + 1), path.get(i)); // jika graph bidirectional
            }

        } finally {
            graph.getModel().endUpdate();
        }
    }

    /**
     * Memberi warna hijau dan menebalkan edge tertentu.
     *
     * @param graph           Graph yang dimodifikasi.
     * @param graphController Controller yang menyediakan mapping edge.
     * @param from            Nama vertex awal edge.
     * @param to              Nama vertex akhir edge.
     */
    private static void colorEdge(mxGraph graph, GraphController graphController, String from, String to) {
        Object e = graphController.getEdgeMap().get(from + "->" + to);
        if (e != null) {
            graph.setCellStyle("strokeColor=green;strokeWidth=3", new Object[] { e });
        }
    }
}
