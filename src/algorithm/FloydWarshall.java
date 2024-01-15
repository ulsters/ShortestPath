// FloydWarshallAlgorithm
package algorithm;

import graph.Edge;
import graph.Vertex;

import java.util.ArrayList;
import java.util.List;

public class FloydWarshall extends ShortestPath {

    // Distances between vertices
    private int[][] distanceMatrix;

    // Intermediate vertices in the shortest path
    private int[][] intermediateVertices;

    public FloydWarshall() {
        this.distanceMatrix = null;
        this.intermediateVertices = null;
    }

    public int[][] getIntermediateVertices() {
        return intermediateVertices;
    }

    // Find the shortest path between sourceID and destinationID for edges with bandwidth
    public List<Vertex> findShortestPath(List<Vertex> vertices, int sourceID, int destinationID, int bandwidth) {
        initializeDistanceMatrix(vertices, bandwidth);
        performFloydWarshall(vertices.size());

        List<Vertex> path = new ArrayList<>();
        int sourceIndex = getIndex(vertices, sourceID);
        int destinationIndex = getIndex(vertices, destinationID);

        // If there exists a path from source to destination
        if (distanceMatrix[sourceIndex][destinationIndex] != Integer.MAX_VALUE) {
            reconstructPath(sourceIndex, destinationIndex, path, vertices);
        }

        return path; // Return the shortest path
    }

    // Initialize distanceMatrix and intermediateVertices based on the edges with sufficient bandwidth
    private void initializeDistanceMatrix(List<Vertex> vertices, int bandwidth) {
        int numVertices = vertices.size();
        distanceMatrix = new int[numVertices][numVertices];
        intermediateVertices = new int[numVertices][numVertices];

        // Initialize matrices with initial values
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                distanceMatrix[i][j] = Integer.MAX_VALUE;
                intermediateVertices[i][j] = -1;
            }
        }

        // Populate matrices with actual edge weights
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

    // Floyd-Warshall algorithm to find all-pairs shortest paths
    private void performFloydWarshall(int numVertices) {
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (distanceMatrix[i][k] != Integer.MAX_VALUE &&
                            distanceMatrix[k][j] != Integer.MAX_VALUE &&
                            distanceMatrix[i][k] + distanceMatrix[k][j] < distanceMatrix[i][j]) {
                        // Update the distance and intermediate vertices
                        distanceMatrix[i][j] = distanceMatrix[i][k] + distanceMatrix[k][j];
                        intermediateVertices[i][j] = intermediateVertices[k][j];
                    }
                }
            }
        }
    }

    // Reconstruct the shortest path from source to destination using intermediate vertices
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

    // get the index of a vertex in the list based on its ID
    private int getIndex(List<Vertex> vertices, int vertexID) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).name == vertexID) {
                return i;
            }
        }
        return -1;
    }
}