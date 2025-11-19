package com.tubespjmfkel2.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Kelas Graph merepresentasikan struktur data graf sederhana
 * yang terdiri dari sekumpulan Node.
 *
 * <p>
 * Graph ini tidak menyimpan struktur edge secara langsung,
 * karena setiap Node bertanggung jawab untuk menyimpan daftar
 * ketetanggaannya sendiri. Dengan demikian, Graph hanya berfungsi
 * sebagai kontainer node.
 * </p>
 *
 * <h2>Fungsi Utama:</h2>
 * <ul>
 * <li><b>addNode(Node node)</b> – menambahkan node baru ke dalam graf.</li>
 * <li><b>getNodes()</b> – mengambil seluruh Node yang ada dalam graf.</li>
 * <li><b>resetAllNodes()</b> – mereset seluruh node ke nilai awal
 * (misalnya distance, visited flag, dll). Ini berguna setelah proses
 * algoritma seperti Dijkstra agar graf siap digunakan kembali.</li>
 * </ul>
 */
public class Graph {

    private Set<Node> nodes = new HashSet<>();

    /**
     * Menambahkan sebuah node ke dalam graf.
     *
     * @param node Node yang akan ditambahkan.
     */
    public void addNode(Node node) {
        nodes.add(node);
    }

    /**
     * Mengambil seluruh node yang tersimpan dalam graf.
     *
     * @return Set berisi objek Node.
     */
    public Set<Node> getNodes() {
        return nodes;
    }

    /**
     * Mereset seluruh node pada graf agar dapat digunakan ulang
     * dalam perhitungan algoritma. Metode ini akan memanggil
     * method reset() pada setiap Node.
     */
    public void resetAllNodes() {
        for (Node n : nodes)
            n.reset();
    }

}
