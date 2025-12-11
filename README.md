# Tubes PJMF Kel 2

Proyek ini adalah aplikasi **Implementasi Algoritma Dijkstra pada Pencarian Rute Terpendek Menuju Bengkel Menggunakan
Spring Framework dan Swing**

Reference algorithm from https://www.baeldung.com/java-dijkstra

Framework: Spring

Library: Jgraphx, Swing, Common Csv, lombok

# 🧠 Anggota

- **1324001** Rizal Kusuma Awaludin
- **1324002** Saputra Bayu Wijaya
- **1324003** Muhammad Abyan Aufa
- **1324023** Muhammad Hilman Ansory

---

# 📦 Struktur Project

```
tubespjmfkel2
├── algorithm                         
│   └── Dijkstra.java                 # Implementasi murni algoritma Dijkstra
│
├── domain                            
│   ├── Edge.java                     # Kelas edge: menghubungkan dua vertex + bobot
│   ├── Graph.java                    # Struktur graf: menyimpan vertex & edge
│   └── Vertex.java                   # Representasi vertex/simpul dalam graf
│
├── dto                               
│   └── DijkstraResult.java           # DTO hasil perhitungan Dijkstra (jarak + path)
│
├── service                           
│   ├── DijkstraService.java          # Service untuk menjalankan algoritma Dijkstra
│   └── GraphService.java             # Service untuk manajemen graf (add vertex/edge)
│
├── TubesPJMFkel2Application.java     # Entry point aplikasi (main program)
│
└── view                              
    ├── asset                         # Folder aset gambar untuk GUI
    │   └── icon                      
    ├── MainFrame.java                # GUI utama: input, visualisasi, output Dijkstra
    └── WelcomeFrame.java             # GUI awal (welcome screen) sebelum masuk ke main

```

## 1. `Dijkstra.java`

Implementasi murni algoritma Dijkstra:

```java

public class Dijkstra {
    public static void calculateShortestPathFromSource(Vertex source) {
        source.setDistance(0);
        Set<Vertex> settledVertices = new HashSet<>();
        Set<Vertex> unsettledVertices = new HashSet<>();
        unsettledVertices.add(source);
        while (!unsettledVertices.isEmpty()) {
            Vertex currentVertex = getLowestDistanceVertex(unsettledVertices);
            unsettledVertices.remove(currentVertex);
            for (Edge edge : currentVertex.getNeighbors()) {
                Vertex neighbor = edge.getOpposite(currentVertex);
                int weight = edge.getWeight();
                if (!settledVertices.contains(neighbor)) {
                    calculateMinimumDistance(neighbor, weight, currentVertex);
                    unsettledVertices.add(neighbor);
                }
            }
            settledVertices.add(currentVertex);
        }
    }

    private static Vertex getLowestDistanceVertex(Set<Vertex> unsettledVertices) {
        Vertex lowestDistanceVertex = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Vertex vertex : unsettledVertices) {
            int currentDistance = vertex.getDistance();
            if (currentDistance < lowestDistance) {
                lowestDistance = currentDistance;
                lowestDistanceVertex = vertex;
            }
        }
        return lowestDistanceVertex;
    }

    private static void calculateMinimumDistance(
            Vertex evaluationVertex,
            Integer edgeWeight,
            Vertex currentVertex) {
        Integer sourceDistance = currentVertex.getDistance();
        if (sourceDistance + edgeWeight < evaluationVertex.getDistance()) {
            evaluationVertex.setDistance(sourceDistance + edgeWeight);
            List<Vertex> shortestPath = new ArrayList<>(currentVertex.getShortestPath());
            shortestPath.add(currentVertex);
            evaluationVertex.setShortestPath(shortestPath);
        }
    }
}
```

---

## 2. Package `controller`

Berisi kelas–kelas yang menjalankan logika penghubung antara **model** dan **view**.
Controller bertindak sebagai jembatan yang memproses perintah pengguna, memanggil model, dan mengirimkan hasil kembali
ke UI.

---

## 3. Package `dto`

Digunakan untuk menyimpan hasil eksekusi algoritma Dijkstra dan Bersifat **immutable**

---

## 4. Package `model`

Berisi seluruh **data dan logika murni aplikasi**, tanpa ketergantungan pada UI.

---

## 5. Package `view`

Berisi seluruh tampilan pengguna (UI), komponen–komponen grafis, dan utilitas tampilan.

---

