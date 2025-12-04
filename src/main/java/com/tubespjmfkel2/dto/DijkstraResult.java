package com.tubespjmfkel2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DijkstraResult {

    private List<String> path;

    private int distance;
}