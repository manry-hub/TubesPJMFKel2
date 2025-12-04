package com.tubespjmfkel2.service;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tubespjmfkel2.dto.DijkstraResult;
import com.tubespjmfkel2.algorithm.Dijkstra;
import com.tubespjmfkel2.domain.Vertex;

@Service
public class DijkstraService {

    private GraphService graphService;

    @Autowired
    public DijkstraService(GraphService graphService) {
        this.graphService = graphService;
    }

    public DijkstraResult findShortestPath(String vertexStartInput, String vertexEndInput) {

        Vertex vertexStart = graphService.findVertex(vertexStartInput);
        Vertex vertexEnd = graphService.findVertex(vertexEndInput);

        if (vertexStart == null || vertexEnd == null)
            return null;

        // Kasus start == end
        if (vertexStartInput.equals(vertexEndInput))
            return new DijkstraResult(List.of(vertexStartInput), 0);

        // Reset dahulu distance dan shortestpathnya
        for (Vertex vertex : graphService.getGraph().getVertices()) {
            vertex.setDistance(Integer.MAX_VALUE);
            vertex.getShortestPath().clear();
        }
        // Jalankan Dijkstra
        Dijkstra.calculateShortestPathFromSource(vertexStart);

        // Jika tidak ada jalur (distance tetap MAX_VALUE)
        if (vertexEnd.getDistance() == Integer.MAX_VALUE) {
            return null;
        }

        // Bentuk path
        List<String> path = new ArrayList<>();

        // Masukkan node hasil shortestPath
        for (Vertex vertex : vertexEnd.getShortestPath()) {
            path.add(vertex.getName());
        }
        // Mengembalikan hasil dalam bentuk DTO
        path.add(vertexEnd.getName());
        return new DijkstraResult(path, vertexEnd.getDistance());
    }
}
