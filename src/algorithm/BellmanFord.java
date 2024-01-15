package algorithm;

import graph.Edge;
import graph.Vertex;

import java.util.ArrayList;
import java.util.List;

public class BellmanFord extends ShortestPath {

    private List<Vertex> vertices;

    public BellmanFord(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public List<Vertex> findShortestPath(int sourceID, int destinationID, int bandwidth) {
        int numVertices = vertices.size();
        int[] distance = new int[numVertices];
        int[] predecessor = new int[numVertices];

        initialize(sourceID, distance, predecessor);

        // Relax edges repeatedly
        for (int i = 1; i < numVertices; i++) {
            for (Vertex vertex : vertices) {
                if (vertex.adjacencies != null) {
                    for (Edge edge : vertex.adjacencies) {
                        if (edge.getBandwidth() >= bandwidth) {
                            relax(edge, distance, predecessor);
                        }
                    }
                }
            }
        }

        // Check for negative cycles
        for (Vertex vertex : vertices) {
            if (vertex.adjacencies != null) {
                for (Edge edge : vertex.adjacencies) {
                    if (edge.getBandwidth() >= bandwidth && relax(edge, distance, predecessor)) {
                        // If relaxation occurs in the last iteration, there's a negative cycle
                        System.out.println("Graph contains a negative weight cycle.");
                        return new ArrayList<>();
                    }
                }
            }
        }

        // Reconstruct the path
        List<Vertex> path = new ArrayList<>();
        reconstructPath(sourceID, destinationID, predecessor, path);

        return path;
    }

    private int getIndex(int vertexID) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).name == vertexID) {
                return i;
            }
        }
        return -1;
    }

    private void initialize(int sourceID, int[] distance, int[] predecessor) {
        int numVertices = vertices.size();
        for (int i = 0; i < numVertices; i++) {
            distance[i] = Integer.MAX_VALUE;
            predecessor[i] = -1;
        }
        distance[getIndex(sourceID)] = 0;
    }

    private boolean relax(Edge edge, int[] distance, int[] predecessor) {
        int sourceIndex = getIndex(edge.getStartVertex().name);
        int targetIndex = getIndex(edge.getTargetVertex().name);
        int weight = edge.getEdgeWeight();

        if (distance[sourceIndex] != Integer.MAX_VALUE && distance[sourceIndex] + weight < distance[targetIndex]) {
            distance[targetIndex] = distance[sourceIndex] + weight;
            predecessor[targetIndex] = sourceIndex;
            return true;
        }
        return false;
    }

    private void reconstructPath(int sourceID, int destinationID, int[] predecessor, List<Vertex> path) {
        int sourceIndex = getIndex(sourceID);
        int destinationIndex = getIndex(destinationID);

        if (destinationIndex == -1 || sourceIndex == -1) {
            System.out.println("Invalid source or destination node.");
            return;
        }

        int current = destinationIndex;
        while (current != sourceIndex) {
            path.add(vertices.get(current));
            current = predecessor[current];
            if (current == -1) {
                System.out.println("No path exists from Node " + sourceID + " to Node " + destinationID);
                return;
            }
        }
        path.add(vertices.get(sourceIndex));
        // Reverse the path to have it in the correct order
        java.util.Collections.reverse(path);
    }
}