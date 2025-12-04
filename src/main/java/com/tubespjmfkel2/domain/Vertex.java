package com.tubespjmfkel2.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Vertex {
    private String name;
    private List<Edge> neighbors = new ArrayList<>();
    private List<Vertex> shortestPath = new ArrayList<>();
    private Integer distance = Integer.MAX_VALUE;

    public void addNeighbor(Edge edge) {
        neighbors.add(edge);
    }
}
