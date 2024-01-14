// FloydWarshallAlgorithm.java
package algorithm;

import graph.Edge;
import graph.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FloydWarshall extends ShortestPath {


    private int[][] distanceMatrix;
    private int[][] intermediateVertices;
    
    public FloydWarshall() {
        this.distanceMatrix = null; // Initialize distanceMatrix as needed
        this.intermediateVertices = null; // Initialize intermediateVertices as needed
    }

    public int[][] getIntermediateVertices() {
        return intermediateVertices;
    }

    public List<Vertex> findShortestPath(List<Vertex> vertices, int sourceID, int destinationID, int bandwidth) {
        initializeDistanceMatrix(vertices, bandwidth);
        performFloydWarshall(vertices.size());

        List<Vertex> path = new ArrayList<>();
        int sourceIndex = getIndex(vertices, sourceID);
        int destinationIndex = getIndex(vertices, destinationID);

        if (distanceMatrix[sourceIndex][destinationIndex] != Integer.MAX_VALUE) {
            reconstructPath(sourceIndex, destinationIndex, path, vertices);
        }

        return path;
    }

    private void initializeDistanceMatrix(List<Vertex> vertices, int bandwidth) {
        int numVertices = vertices.size();
        distanceMatrix = new int[numVertices][numVertices];
        intermediateVertices = new int[numVertices][numVertices];

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                distanceMatrix[i][j] = Integer.MAX_VALUE;
                intermediateVertices[i][j] = -1;
            }
        }
        for (Vertex vertex : vertices) {
            int sourceIndex = getIndex(vertices, vertex.name); // Assuming name is the ID of the vertex
            for (Edge edge : vertex.adjacencies) {
                if (edge.getBandwidth() >= bandwidth) {
                    Vertex targetNode = edge.getTargetVertex();
                    int edgeWeight = edge.getEdgeWeight();
                    int targetIndex = getIndex(vertices, targetNode.name); // Assuming name is the ID of the vertex
                    distanceMatrix[sourceIndex][targetIndex] = edgeWeight;
                    intermediateVertices[sourceIndex][targetIndex] = sourceIndex;
                }
            }
        }
    }

    private void performFloydWarshall(int numVertices) {
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (distanceMatrix[i][k] != Integer.MAX_VALUE &&
                            distanceMatrix[k][j] != Integer.MAX_VALUE &&
                            distanceMatrix[i][k] + distanceMatrix[k][j] < distanceMatrix[i][j]) {
                        distanceMatrix[i][j] = distanceMatrix[i][k] + distanceMatrix[k][j];
                        intermediateVertices[i][j] = intermediateVertices[k][j];
                    }
                }
            }
        }
    }

    private void reconstructPath(int source, int destination, List<Vertex> path, List<Vertex> vertices) {
        if (source == destination) {
            path.add(vertices.get(source));
        } else if (intermediateVertices[source][destination] == -1) {
            System.out.println("No path exists from Node " + source + " to Node " + destination);
        } else {
            reconstructPath(source, intermediateVertices[source][destination], path, vertices);
            path.add(vertices.get(destination));
        }
    }

    private int getIndex(List<Vertex> vertices, int vertexID) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).name == vertexID) {
                return i;
            }
        }
        return -1;
    }
}

