package com.tubespjmfkel2.model.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Kelas Graph merepresentasikan struktur data graf sederhana
 * yang terdiri dari sekumpulan Vertex.
 *
 * <p>
 * Graph ini tidak menyimpan struktur edge secara langsung,
 * karena setiap Vertex bertanggung jawab menyimpan daftar
 * ketetanggaannya sendiri. Dengan demikian, Graph hanya berfungsi
 * sebagai kontainer vertex.
 * </p>
 *
 * <h2>Fungsi Utama:</h2>
 * <ul>
 * <li><b>addVertex(Vertex vertex)</b> – menambahkan vertex baru ke dalam
 * graf.</li>
 * <li><b>getVertices()</b> – mengambil seluruh vertex yang ada dalam graf.</li>
 * <li><b>resetAllVertices()</b> – mereset seluruh vertex ke nilai awal
 * (misalnya distance, path, visited flag, dll). Ini berguna agar graf siap
 * digunakan kembali setelah algoritma seperti Dijkstra dijalankan.</li>
 * </ul>
 */
public class Graph {

    private Set<Vertex> vertices = new HashSet<>();

    /**
     * Menambahkan sebuah vertex ke dalam graf.
     *
     * @param vertex vertex yang akan ditambahkan.
     */
    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    /**
     * Mengambil seluruh vertex yang tersimpan dalam graf.
     *
     * @return Set berisi objek Vertex.
     */
    public Set<Vertex> getVertices() {
        return vertices;
    }

    /**
     * Mereset seluruh vertex pada graf agar dapat digunakan ulang
     * dalam perhitungan algoritma. Metode ini akan memanggil
     * method reset() pada setiap Vertex.
     */
    public void resetAllVertices() {
        for (Vertex v : vertices)
            v.reset();
    }

}
