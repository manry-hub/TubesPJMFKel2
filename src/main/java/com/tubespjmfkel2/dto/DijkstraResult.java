package com.tubespjmfkel2.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) yang digunakan untuk menyimpan hasil
 * perhitungan algoritma Dijkstra.
 *
 * <p>
 * Class ini merepresentasikan hasil berupa:
 * <ul>
 * <li>List urutan node yang membentuk rute terpendek</li>
 * <li>Total jarak (distance) yang ditempuh dari node awal ke akhir</li>
 * </ul>
 *
 * <p>
 * Objek ini merupakan immutable sehingga nilai hanya dapat diisi
 * melalui constructor dan tidak dapat dimodifikasi kembali.
 * </p>
 */
public class DijkstraResult {

    /** List nama node dalam urutan rute terpendek. */
    private final List<String> path;

    /** Total jarak (bobot) dari rute yang ditemukan. */
    private final int distance;

    /**
     * Membuat instance baru hasil perhitungan Dijkstra.
     *
     * @param path     daftar nama node yang membentuk rute terpendek
     * @param distance total bobot jarak dari rute tersebut
     */
    public DijkstraResult(List<String> path, int distance) {
        this.path = path;
        this.distance = distance;
    }

    /**
     * Mengambil urutan node yang membentuk rute terpendek.
     *
     * @return daftar nama node sesuai urutan perjalanan
     */
    public List<String> getPath() {
        return path;
    }

    /**
     * Mengambil nilai total jarak dari rute terpendek.
     *
     * @return total bobot jarak rute
     */
    public int getDistance() {
        return distance;
    }
}
