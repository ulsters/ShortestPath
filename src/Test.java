import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import algorithm.ShortestPath;
import algorithm.FloydWarshall;
import file.readAllFiles;
import graph.Edge;
import graph.GenerateVertex;
import graph.Vertex;

public class Test {

    public static void main(String[] args) throws IOException {

        List<String> substrateFile = readAllFiles.substrateFileName();
        Scanner input = new Scanner(System.in);

        for (int i = 0; i < substrateFile.size(); i++) { // run substrate
            // get all substrate vertices
            List<Vertex> substrate = new ArrayList<Vertex>();
            GenerateVertex gVertex = new GenerateVertex(substrateFile.get(i));
            substrate = gVertex.getVertices();

            showVertices(substrate); // show all vertices

            // show unsorted substrate vertices
            System.out.println(Arrays.toString(substrate.toArray()));

            // Get source node id from the user
            System.out.println("Enter the source id:");
            int sourceNode = input.nextInt(); // hold the id value as integer

            // Get destination node id from the user
            System.out.println("Enter the destination id:");
            int destinationNode = input.nextInt(); // hold the id value as integer

            // Create ShortestPath() object with the substrate network
            ShortestPath sObj = new ShortestPath(substrateFile.get(i));

            // Define the list of vertices in the path with specific bandwidth
            List<Vertex> path = sObj.defineShortestPath(sourceNode, destinationNode, 5);

            int totalDistance = sObj.getTotalDistance(); // distance from source to destination

            // Print out the results to show the user
            System.out.printf("Dijkstra's Algorithm - Path %d to %d:\t%s\n", sourceNode, destinationNode,
                    Arrays.toString(path.toArray()));
            System.out.printf("Total distance:\t%d\n", totalDistance);

            System.out.println("=============================================");

            // Run Floyd-Warshall algorithm and show its output
            runFloydWarshall(substrate);
            System.out.println("=============================================");
        } // end-for substrate files

        input.close(); // close Scanner object
    }

    // show all vertices in a topology with their connections
    public static void showVertices(List<Vertex> vertexList) {
        for (Vertex currentVertex : vertexList) {
            System.out.println("Node[" + currentVertex + "]: Cpu[" + currentVertex.getCPU() + "]");
            for (Edge connectedEdge : currentVertex.adjacencies) {
                System.out.println("Edge:[" + connectedEdge.getStartVertex() + "]--[" + connectedEdge.getTargetVertex()
                        + "]: BW[" + connectedEdge.getBandwidth() + "], Cost[" + connectedEdge.getEdgeWeight() + "]");
            } // end-for connectedEdge
            System.out.println("---------------------------------------------");
        } // end-for vertexList
        System.out.println("");
    }

    public static void runFloydWarshall(List<Vertex> substrate) {
        FloydWarshall floydWarshall = new FloydWarshall();
        // Assuming you want to find the shortest path for all pairs of nodes
        int numNodes = substrate.size();
        for (int sourceNode = 0; sourceNode < numNodes; sourceNode++) {
            for (int destinationNode = 0; destinationNode < numNodes; destinationNode++) {
                List<Vertex> path = floydWarshall.findShortestPath(substrate, sourceNode, destinationNode, 5);
                System.out.println("Floyd-Warshall Algorithm - Shortest Path from Node " + sourceNode + " to Node " + destinationNode + ":");
                System.out.println(Arrays.toString(path.toArray()));
                System.out.println("=============================================");
            }
        }
    }
}