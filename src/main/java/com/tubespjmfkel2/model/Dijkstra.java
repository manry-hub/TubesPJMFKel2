package com.tubespjmfkel2.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Kelas ini mengimplementasikan algoritma Dijkstra untuk mencari
 * rute terpendek dari satu titik (source node) menuju seluruh node lain
 * di dalam sebuah graph berarah atau tak berarah dengan edge berbobot positif.
 *
 * Algoritma ini bekerja dengan cara:
 * <ul>
 * <li>Menetapkan node sumber (source) sebagai jarak 0</li>
 * <li>Memilih node dengan jarak sementara (distance) terendah</li>
 * <li>Melakukan relaksasi jarak terhadap node tetangga</li>
 * <li>Memindahkan node tersebut ke dalam himpunan node yang telah diproses</li>
 * <li>Mengulangi hingga semua node telah diproses</li>
 * </ul>
 */
public class Dijkstra {

    /**
     * Menjalankan algoritma Dijkstra untuk menghitung jarak terpendek
     * dari node sumber (source) menuju seluruh node dalam graph.
     *
     * @param graph  graph yang berisi node serta edge dan bobotnya
     * @param source node awal tempat proses pencarian rute dimulai
     * @return graph yang sudah terisi informasi jarak terpendek pada setiap node
     */
    public static Graph calculateShortestPathFromSource(Graph graph, Node source) {

        // Node sumber memiliki jarak 0 sebagai titik awal
        source.setDistance(0);

        // Himpunan node yang sudah dipastikan jaraknya final
        Set<Node> settledNodes = new HashSet<>();

        // Himpunan node yang masih harus diperiksa
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (!unsettledNodes.isEmpty()) {

            // Ambil node dengan jarak (distance) paling kecil
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);

            // Periksa semua node tetangga dari node saat ini
            for (Map.Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {

                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();

                // Jika tetangga belum dipastikan jaraknya, lakukan relaksasi
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }

            // Node sudah selesai diproses
            settledNodes.add(currentNode);
        }

        return graph;
    }

    /**
     * Mengambil node dengan jarak (distance) terkecil dari himpunan node
     * yang belum dipastikan jaraknya.
     *
     * @param unsettledNodes daftar node yang masih perlu dihitung
     * @return node dengan jarak terpendek
     */
    private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {

        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;

        // Telusuri semua node untuk menentukan jarak paling kecil
        for (Node node : unsettledNodes) {
            int nodeDistance = node.getDistance();

            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }

        return lowestDistanceNode;
    }

    /**
     * Melakukan proses relaksasi jarak terhadap node tetangga.
     * Jika jarak (distance) baru lebih kecil dari jarak sebelumnya,
     * maka jarak node di-update serta jalur terpendek diperbarui.
     *
     * @param evaluationNode node tetangga yang sedang diperiksa
     * @param edgeWeight     bobot edge dari sumber menuju node tetangga
     * @param sourceNode     node sumber saat ini
     */
    private static void calculateMinimumDistance(
            Node evaluationNode,
            Integer edgeWeight,
            Node sourceNode) {

        Integer sourceDistance = sourceNode.getDistance();

        // Jika jarak baru lebih kecil, lakukan update
        if (sourceDistance + edgeWeight < evaluationNode.getDistance()) {

            evaluationNode.setDistance(sourceDistance + edgeWeight);

            // Salin path dari sumber dan tambahkan node ini
            List<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);

            evaluationNode.setShortestPath(shortestPath);
        }
    }

}
