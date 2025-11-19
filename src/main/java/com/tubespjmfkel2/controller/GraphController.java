package com.tubespjmfkel2.controller;

import java.util.HashMap;
import java.util.Map;

import com.mxgraph.view.mxGraph;
import com.tubespjmfkel2.model.Graph;
import com.tubespjmfkel2.model.Node;

/**
 * Controller yang bertanggung jawab mengelola struktur graph,
 * baik dalam bentuk data inti (backend) maupun visualisasi (frontend/UI)
 * menggunakan mxGraph.
 *
 * <p>
 * GraphController menyediakan operasi dasar seperti:
 * <ul>
 * <li>Menambah node</li>
 * <li>Menambah edge dengan bobot</li>
 * <li>Mengambil node berdasarkan nama</li>
 * <li>Membersihkan graph</li>
 * </ul>
 *
 * <p>
 * Class ini menghubungkan antara struktur graph untuk
 * perhitungan algoritma (kelas {@link Graph} dan {@link Node})
 * serta representasi grafis pada GUI untuk tampilan visual.
 * </p>
 */
public class GraphController {

    /** Struktur graph inti untuk perhitungan algoritmik. */
    private Graph coreGraph = new Graph();

    /** Object graph visual dari mxGraph untuk ditampilkan ke UI. */
    private mxGraph uiGraph = new mxGraph();

    /** Penyimpanan referensi edge UI berdasarkan pasangan node 'A->B'. */
    private Map<String, Object> edgeMap = new HashMap<>();

    /**
     * Mengambil objek graph inti yang digunakan algoritma.
     *
     * @return objek {@link Graph} yang menyimpan struktur node dan edge.
     */
    public Graph getCoreGraph() {
        return coreGraph;
    }

    /**
     * Mengambil objek visual mxGraph untuk ditampilkan dalam UI.
     *
     * @return instance {@link mxGraph}
     */
    public mxGraph getUiGraph() {
        return uiGraph;
    }

    /**
     * Mengambil map penyimpanan reference edge yang
     * digunakan untuk pewarnaan atau modifikasi tampilan edge.
     *
     * @return Map edge yang dipetakan berdasarkan format "From->To".
     */
    public Map<String, Object> getEdgeMap() {
        return edgeMap;
    }

    /**
     * Menambahkan sebuah node baru ke struktur graph dan tampilan UI.
     *
     * <p>
     * Langkah yang dilakukan:
     * <ol>
     * <li>Validasi nama node</li>
     * <li>Memastikan node belum ada</li>
     * <li>Membuat objek Node dan menambahkannya ke Graph</li>
     * <li>Menggambar node pada UI (mxGraph)</li>
     * </ol>
     * </p>
     *
     * @param name nama node baru yang ingin ditambahkan
     * @return pesan error jika gagal, atau {@code null} jika berhasil
     */
    public String addNode(String name) {
        if (name == null || name.trim().isEmpty())
            return "Nama node tidak boleh kosong!";

        if (findNode(name) != null)
            return "Node '" + name + "' sudah ada!";

        Node node = new Node(name);
        coreGraph.addNode(node);

        uiGraph.getModel().beginUpdate();
        try {
            uiGraph.insertVertex(
                    uiGraph.getDefaultParent(),
                    null,
                    name,
                    50, 50,
                    60, 60,
                    "shape=ellipse");
        } finally {
            uiGraph.getModel().endUpdate();
        }

        return null;
    }

    /**
     * Menambahkan edge berbobot (arah dari -> ke) ke graph.
     *
     * <p>
     * Langkah yang dilakukan:
     * <ol>
     * <li>Mengecek node asal dan tujuan</li>
     * <li>Mencegah self-loop</li>
     * <li>Mengecek bobot > 0</li>
     * <li>Menambah edge pada struktur algoritma (Graph)</li>
     * <li>Menggambar garis pada UI</li>
     * <li>Menyimpan referensi edge untuk pewarnaan/pengolahan berikutnya</li>
     * </ol>
     *
     * @param from   nama node asal
     * @param to     nama node tujuan
     * @param weight bobot edge (> 0)
     * @return pesan error jika gagal, atau {@code null} jika berhasil
     */
    public String addEdge(String from, String to, int weight) {

        Node nFrom = findNode(from);
        Node nTo = findNode(to);

        if (nFrom == null)
            return "Node asal tidak ditemukan!";
        if (nTo == null)
            return "Node tujuan tidak ditemukan!";
        if (from.equals(to))
            return "Tidak boleh ke dirinya sendiri!";
        if (weight <= 0)
            return "Bobot harus lebih besar dari 0!";
        if (edgeMap.containsKey(from + "->" + to))
            return "Edge ini sudah ada!";

        // Tambah ke struktur graph data
        nFrom.addDestination(nTo, weight);

        // Tambah ke tampilan UI
        uiGraph.getModel().beginUpdate();
        try {
            Object vFrom = findVertex(from);
            Object vTo = findVertex(to);

            Object edge = uiGraph.insertEdge(
                    uiGraph.getDefaultParent(),
                    null,
                    weight,
                    vFrom,
                    vTo);

            edgeMap.put(from + "->" + to, edge);

        } finally {
            uiGraph.getModel().endUpdate();
        }

        return null;
    }

    /**
     * Mencari objek node berdasarkan nama.
     *
     * @param name nama node
     * @return objek {@link Node} jika ditemukan, atau {@code null} jika tidak ada
     */
    public Node findNode(String name) {
        return coreGraph.getNodes()
                .stream()
                .filter(n -> n.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Mencari objek vertex mxGraph berdasarkan label (nama node).
     *
     * <p>
     * Digunakan saat menambahkan/memanipulasi edge di UI.
     * </p>
     *
     * @param name nama node yang dicari
     * @return objek vertex representasi di UI, atau {@code null} jika tidak ada
     */
    private Object findVertex(String name) {
        for (Object cell : uiGraph.getChildVertices(uiGraph.getDefaultParent())) {
            if (name.equals(uiGraph.getLabel(cell)))
                return cell;
        }
        return null;
    }

    /**
     * Mereset seluruh graph:
     * <ul>
     * <li>Menghapus seluruh node dan edge di struktur algoritmik</li>
     * <li>Menghapus seluruh tampilan vertex dan edge di UI</li>
     * <li>Membersihkan map penyimpanan edge</li>
     * </ul>
     *
     * <p>
     * Digunakan saat merestart visualisasi atau memulai graph baru.
     * </p>
     */
    public void resetGraph() {
        coreGraph = new Graph();
        edgeMap.clear();

        uiGraph.getModel().beginUpdate();
        try {
            Object[] cells = uiGraph.getChildCells(uiGraph.getDefaultParent(), true, true);
            if (cells != null && cells.length > 0)
                uiGraph.removeCells(cells);
        } finally {
            uiGraph.getModel().endUpdate();
        }
    }
}
