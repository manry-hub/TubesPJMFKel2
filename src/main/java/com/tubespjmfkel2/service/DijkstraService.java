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

        if (vertexStartInput.equals(vertexEndInput))
            return new DijkstraResult(List.of(vertexStartInput), 0);

        for (Vertex vertex : graphService.getGraph().getVertices()) {
            vertex.setDistance(Integer.MAX_VALUE);
            vertex.getShortestPath().clear();
        }
        Dijkstra.calculateShortestPathFromSource(vertexStart);

        if (vertexEnd.getDistance() == Integer.MAX_VALUE) {
            return null;
        }

        List<String> path = new ArrayList<>();

        for (Vertex vertex : vertexEnd.getShortestPath()) {
            path.add(vertex.getName());
        }
        path.add(vertexEnd.getName());
        return new DijkstraResult(path, vertexEnd.getDistance());
    }

    public DijkstraResult findNearestDestination(String vertexStartInput, List<String> destinations) {

        Vertex start = graphService.findVertex(vertexStartInput);
        if (start == null)
            return null;

        for (Vertex v : graphService.getGraph().getVertices()) {
            v.setDistance(Integer.MAX_VALUE);
            v.getShortestPath().clear();
        }

        Dijkstra.calculateShortestPathFromSource(start);

        Vertex nearest = null;
        int minDist = Integer.MAX_VALUE;

        for (String destName : destinations) {
            Vertex dest = graphService.findVertex(destName);
            if (dest == null) continue;

            if (dest.getDistance() < minDist) {
                nearest = dest;
                minDist = dest.getDistance();
            }
        }

        if (nearest == null)
            return null;

        List<String> path = new ArrayList<>();
        for (Vertex v : nearest.getShortestPath()) {
            path.add(v.getName());
        }
        path.add(nearest.getName());

        return new DijkstraResult(path, minDist);
    }

}
