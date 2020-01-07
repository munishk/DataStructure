package graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

public class WeightedGraphAdjacencyList {

	private Map<Vertex, Set<VertexAndDistance>> adjacencyList = new HashMap<>();

	public void addUnDirectedEdge(Vertex source, Vertex destination, int distance) {
		addDirectedEdge(source, destination, distance);
		addDirectedEdge(destination, source, distance);
	}

	public void addDirectedEdge(Vertex source, Vertex destination, int distance) {
		Set<VertexAndDistance> vertexList = adjacencyList.get(source);
		if (vertexList == null) {
			if (destination == null) {
				adjacencyList.put(source, null);
			} else {
				vertexList = new HashSet<>();
				vertexList.add(new VertexAndDistance(destination, distance));
				adjacencyList.put(source, vertexList);
			}
		} else {
			vertexList.add(new VertexAndDistance(destination, distance));
		}
	}

	public void display() {
		StringBuilder strBuilder = new StringBuilder();
		boolean removeParen = false;
		for (Vertex source : adjacencyList.keySet()) {
			strBuilder.append(source + " -> ");
			if (adjacencyList.get(source) == null) {
				continue;
			}
			for (VertexAndDistance dest : adjacencyList.get(source)) {
				strBuilder.append(dest.vertex + "-" + dest.distance + ", ");
				removeParen = true;
			}
			if (removeParen) {
				strBuilder.delete(strBuilder.length() - 2, strBuilder.length() - 1);
				strBuilder.append("\n");
				removeParen = false;
			}
		}
		System.out.println(strBuilder);
	}

	public void mst() {
		PriorityQueue<Path> pq = new PriorityQueue<>(new Comparator<Path>() {
			@Override
			public int compare(Path o1, Path o2) {
				return o1.distance - o2.distance;
			}
		});
		List<Path> mstPaths = new ArrayList<>();
		Set<Vertex> visited = new HashSet<>();
		Vertex source = adjacencyList.keySet().iterator().next();
		while (true) {
			scanUnvisitedNeighbours(source, visited, pq);
			Path minPath = pq.poll();
			if (minPath == null) {
				break;
			}

			mstPaths.add(minPath);
			cleanUpPQ(pq, minPath.dest);
			source = minPath.dest;
		}

		// finally, display mst path
		StringBuilder strbuilder = new StringBuilder();
		if (mstPaths.size() > 1) {
			strbuilder.append(mstPaths.get(0));
		}
		for (int i = 1; i < mstPaths.size(); i++) {
			strbuilder.append("->" + mstPaths.get(i));
		}
		System.out.println(strbuilder);
	}

	private void cleanUpPQ(PriorityQueue<Path> pq, Vertex dest) {
		pq.removeIf((path) -> {
			return path.dest.equals(dest);
		});
	}

	private void scanUnvisitedNeighbours(Vertex source, Set<Vertex> visited, PriorityQueue<Path> pq) {
		visited.add(source);
		Set<VertexAndDistance> neighbours = adjacencyList.get(source);
		if (neighbours == null) {
			return;
		}

		for (VertexAndDistance neighbour : neighbours) {
			if (!visited.contains(neighbour.vertex)) {
				pq.add(new Path(source, neighbour.vertex, neighbour.distance));
			}
		}
	}

	public void shortestPath(Vertex source, Vertex dest) {
		Set<Vertex> visited = new HashSet<>();

		Map<Vertex, ShortestDistance> shortestPathMap = new HashMap<>();
		Vertex current = source;
		while (true) {
           computePathFrom(current, shortestPathMap, visited);
           
           Vertex vertexWithMinDist = getUnvisitedShortestPath(shortestPathMap, visited);
           
           if(vertexWithMinDist == null || vertexWithMinDist.equals(dest)) {
        	   break;
           }
           
           current = vertexWithMinDist;
		}
		
		Stack<Path> shortestPath = new Stack<>();
		int totalDistance = -1;
		current = dest;
		while(!source.equals(current)) {
			ShortestDistance sd = shortestPathMap.get(current);
			if(sd == null) {
				System.out.println("Destination is not reachable from source");
				break;
			}
			if(totalDistance < 0) {
				totalDistance = sd.totalDistance;
			}
			shortestPath.push(new Path(sd.prevVertex, current, getDistance(sd.prevVertex, current)));
			current = sd.prevVertex;
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		if(shortestPath.size() > 1) {
			stringBuilder.append(shortestPath.pop());
		}
		
		while(!shortestPath.isEmpty()) {
			stringBuilder.append("->" + shortestPath.pop());
		}
		
		System.out.println("Shortest path: " + stringBuilder);
		System.out.println("Total distance: " + totalDistance);
		
	}
	
	private int getDistance(Vertex source, Vertex dest) {
		int distance = -1;
		Set<VertexAndDistance> neghbours = adjacencyList.get(source);
		if(neghbours == null) {
			return distance;
		}
		
		for (VertexAndDistance vd: neghbours) {
			if(vd.vertex.equals(dest)) {
				return vd.distance;
			}
		}
		return distance;
	}
	
	private Vertex getUnvisitedShortestPath(Map<Vertex, ShortestDistance> shortestPathMap, Set<Vertex> visited) {
		Vertex minDistVertex = null;
		for(Vertex vertex: shortestPathMap.keySet()) {
			if(!visited.contains(vertex)) {
				if(minDistVertex == null || shortestPathMap.get(minDistVertex).totalDistance > shortestPathMap.get(vertex).totalDistance) {
					minDistVertex = vertex;
				}
			}
		}
		return minDistVertex;
	}

	private void computePathFrom(Vertex source, Map<Vertex, ShortestDistance> shortestPathMap, Set<Vertex> visited) {
		 visited.add(source);
		 Set<VertexAndDistance> neighbours = adjacencyList.get(source);
		 if(neighbours == null) {
			 return;
		 }
		 
		 ShortestDistance sourceDistance = shortestPathMap.get(source);
		 
		 for(VertexAndDistance neighbour: neighbours) {
			 if(!visited.contains(neighbour)) {
			 ShortestDistance existingShortestDistance = shortestPathMap.get(neighbour.vertex);
			 if(existingShortestDistance == null) {
				 if(sourceDistance == null) {
				 shortestPathMap.put(neighbour.vertex, new ShortestDistance(neighbour.distance, source));
				 }else {
					 shortestPathMap.put(neighbour.vertex, new ShortestDistance(neighbour.distance + sourceDistance.totalDistance, source));
				 }
			 }else {
				 if(sourceDistance == null || existingShortestDistance.totalDistance > sourceDistance.totalDistance + neighbour.distance) {
					 existingShortestDistance.prevVertex = source;
					 existingShortestDistance.totalDistance = sourceDistance.totalDistance + neighbour.distance;
				 }
			 }
			 }
		 }
	 }

	public static void main(String[] args) {
		WeightedGraphAdjacencyList graph = new WeightedGraphAdjacencyList();

		Vertex A = new Vertex("A");
		Vertex B = new Vertex("B");
		Vertex C = new Vertex("C");
		Vertex D = new Vertex("D");
		Vertex E = new Vertex("E");
		Vertex F = new Vertex("F");
		Vertex G = new Vertex("G");
		Vertex H = new Vertex("H");

		/*
		 * Minimum spanning tree for weighted graph example
		 * graph.addUnDirectedEdge(A, B, 6);
		graph.addUnDirectedEdge(A, D, 4);
		graph.addUnDirectedEdge(B, D, 7);
		graph.addUnDirectedEdge(B, C, 10);
		graph.addUnDirectedEdge(B, E, 7);

		graph.addUnDirectedEdge(D, E, 12);
		graph.addUnDirectedEdge(D, C, 8);
		graph.addUnDirectedEdge(C, E, 6);

		graph.addUnDirectedEdge(C, F, 6);
		graph.addUnDirectedEdge(E, F, 7);

		graph.mst();*/
		
		
		/** Dijkastra shortest distance example */
		graph.addDirectedEdge(A, B, 50);
		graph.addDirectedEdge(A, D, 80);
		graph.addDirectedEdge(B, C, 60);
		graph.addDirectedEdge(B, D, 90);
		graph.addDirectedEdge(D, E, 70);
		graph.addDirectedEdge(D, C, 20);
		
		graph.addDirectedEdge(C, E, 40);
		
		graph.addDirectedEdge(E, B, 40);
		
		graph.display();
		
		graph.shortestPath(A, E);

	}

	private static class Vertex {
		private String name;

		public Vertex(final String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private static class ShortestDistance {
		int totalDistance;
		Vertex prevVertex;

		public ShortestDistance(int totalDistance, Vertex prevVertex) {
			super();
			this.totalDistance = totalDistance;
			this.prevVertex = prevVertex;
		}

		@Override
		public String toString() {
			return "[" + prevVertex + totalDistance + "]";
		}
	}

	private static class VertexAndDistance {
		Vertex vertex;
		int distance;

		public VertexAndDistance(Vertex vertex, int distance) {
			super();
			this.vertex = vertex;
			this.distance = distance;
		}

		@Override
		public String toString() {
			return "[" + vertex + distance + "]";
		}
	}

	private static class Path {
		private Vertex source;
		private Vertex dest;
		private int distance;

		public Path(Vertex source, Vertex dest, int distance) {
			super();
			this.source = source;
			this.dest = dest;
			this.distance = distance;
		}

		@Override
		public String toString() {
			return "[" + source + dest + distance + "]";
		}

	}
}
