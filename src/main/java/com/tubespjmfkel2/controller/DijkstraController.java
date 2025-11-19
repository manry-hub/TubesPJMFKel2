package com.tubespjmfkel2.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tubespjmfkel2.dto.DijkstraResult;
import com.tubespjmfkel2.model.Dijkstra;
import com.tubespjmfkel2.model.Node;

/**
 * Controller yang bertanggung jawab menjalankan proses perhitungan
 * shortest path menggunakan algoritma Dijkstra.
 *
 * <p>
 * DijkstraController tidak menyimpan struktur graph sendiri,
 * melainkan memanfaatkan GraphController sebagai sumber data graf
 * dan utilitas pengelolaannya. Class ini hanya fokus pada logika
 * menjalankan algoritma dan memproses hasilnya menjadi DTO yang
 * siap dipakai oleh GUI atau lapisan presentasi lainnya.
 * </p>
 */
public class DijkstraController {

    private final GraphController graphController;

    /**
     * Konstruktor DijkstraController.
     *
     * @param graphController controller yang menyediakan graph dan operasi
     *                        pendukungnya.
     */
    public DijkstraController(GraphController graphController) {
        this.graphController = graphController;
    }

    /**
     * Menjalankan algoritma Dijkstra dari simpul awal hingga simpul tujuan.
     *
     * <p>
     * Metode ini akan:
     * <ol>
     * <li>Memvalidasi input nama node start dan end.</li>
     * <li>Mengambil Node sebenarnnya dari Graph melalui GraphController.</li>
     * <li>Melakukan reset seluruh node pada graph agar siap dihitung.</li>
     * <li>Menjalankan algoritma Dijkstra.</li>
     * <li>Jika ada jalur, mengumpulkan path menjadi daftar nama node.</li>
     * <li>Mengembalikan hasil dalam bentuk DTO {@link DijkstraResult}.</li>
     * </ol>
     * </p>
     *
     * @param start nama node awal
     * @param end   nama node tujuan
     * @return objek {@link DijkstraResult} berisi path dan total jarak,
     *         atau <code>null</code> jika node tidak valid atau tidak ada jalur.
     */
    public DijkstraResult runDijkstra(String start, String end) {

        if (start == null || end == null)
            return null;

        Node startNode = graphController.findNode(start);
        Node endNode = graphController.findNode(end);

        if (startNode == null || endNode == null)
            return null;

        // Kasus start == end
        if (start.equals(end))
            return new DijkstraResult(Arrays.asList(start), 0);

        // Reset dahulu semua node
        graphController.getCoreGraph().resetAllNodes();

        // Jalankan Dijkstra
        Dijkstra.calculateShortestPathFromSource(
                graphController.getCoreGraph(),
                startNode);

        // Jika unreachable
        if (endNode.getDistance() == Integer.MAX_VALUE)
            return null;

        // Bentuk path
        List<String> path = new ArrayList<>();
        for (Node n : endNode.getShortestPath())
            path.add(n.getName());

        if (!path.contains(end))
            path.add(end);

        return new DijkstraResult(path, endNode.getDistance());
    }
}
