package com.tubespjmfkel2.algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tubespjmfkel2.domain.Edge;
import com.tubespjmfkel2.domain.Vertex;

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