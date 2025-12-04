package com.tubespjmfkel2.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Edge {
    private Vertex source;

    private Vertex destination;

    private int weight;

    public Vertex getOpposite(Vertex currentVertex) {
        return currentVertex.equals(source) ? destination : source;
    }
}

