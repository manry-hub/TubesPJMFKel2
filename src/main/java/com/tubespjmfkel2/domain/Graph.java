package com.tubespjmfkel2.domain;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph {

    private Set<Vertex> vertices = new HashSet<>();
    private List<Edge> edges = new ArrayList<>();

    public Set<Vertex> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addVertex(Vertex vertexName) {
        vertices.add(vertexName);
    }

    public void addEdge(Vertex source, Vertex destination, int weight) {
        Edge edge = new Edge(source, destination, weight);
        source.addNeighbor(edge);
        destination.addNeighbor(edge);
    }

    public void clear() {
        vertices.clear();
        edges.clear();
    }

}
