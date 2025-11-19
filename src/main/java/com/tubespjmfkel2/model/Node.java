package com.tubespjmfkel2.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Representasi dari sebuah simpul (node) dalam graph.
 *
 * Setiap node memiliki:
 * <ul>
 * <li>Nama unik</li>
 * <li>Jarak saat ini dari node sumber (distance)</li>
 * <li>Daftar node tetangga beserta bobot edge menuju tetangga tersebut</li>
 * <li>Daftar jalur terpendek yang mengarah ke node ini</li>
 * </ul>
 *
 * Kelas ini digunakan sebagai struktur data utama untuk menjalankan
 * algoritma Dijkstra.
 */
public class Node {

    /** Nama atau label unik dari node */
    private final String name;

    /** Daftar node yang menyusun jalur terpendek menuju node ini */
    private List<Node> shortestPath = new LinkedList<>();

    /** Jarak dari node sumber. Default = tak hingga */
    private Integer distance = Integer.MAX_VALUE;

    /** Daftar node tetangga beserta bobot edge menuju masing-masing tetangga */
    private Map<Node, Integer> adjacentNodes = new HashMap<>();

    /**
     * Membuat instance node baru dengan nama tertentu.
     *
     * @param name nama unik node
     */
    public Node(String name) {
        this.name = name;
    }

    /**
     * Menambahkan edge dari node ini menuju node lain dengan bobot tertentu.
     *
     * @param destination node tujuan
     * @param distance    bobot edge
     */
    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public String getName() {
        return name;
    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    /**
     * Mengembalikan status node ke kondisi awal sebelum perhitungan,
     * yaitu jarak tak hingga dan path kosong.
     */
    public void reset() {
        this.distance = Integer.MAX_VALUE;
        this.shortestPath.clear();
    }

}
