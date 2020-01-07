package graph;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class GraphAdjacencyList {

	private Map<Vertex, Set<Vertex>> adjacencyList = new HashMap<>();

	public void addEdge(Vertex source, Vertex destination) {
		Set<Vertex> vertexList = adjacencyList.get(source);
		if (vertexList == null) {
			if (destination == null) {
				adjacencyList.put(source, null);
			} else {
				vertexList = new HashSet<>();
				vertexList.add(destination);
				adjacencyList.put(source, vertexList);
			}
		} else {
			vertexList.add(destination);
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
			for (Vertex dest : adjacencyList.get(source)) {
				strBuilder.append(dest + ", ");
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

	public Vertex getUnvisitedAdjacentVertex(final Vertex source) {
		if (adjacencyList.get(source) == null) {
			return null;
		}
		for (Vertex dest : adjacencyList.get(source)) {
			if (!dest.visited) {
				return dest;
			}
		}
		return null;
	}

	public void dfs() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("--------- DFS -----------\n");
		Stack<Vertex> stack = new Stack<>();
		Vertex startVertex = adjacencyList.keySet().iterator().next();
		stack.push(startVertex);
		strBuilder.append(startVertex + " -> ");
		startVertex.markVisited();

		while (!stack.isEmpty()) {
			Vertex adjacentVertex = getUnvisitedAdjacentVertex(stack.peek());
			if (adjacentVertex == null) {
				stack.pop();
			} else {
				stack.push(adjacentVertex);
				strBuilder.append(adjacentVertex + " -> ");
				adjacentVertex.markVisited();
			}
		}

		for (Vertex source : adjacencyList.keySet()) {
			source.markUnvisited();
		}
		System.out.println(strBuilder.delete(strBuilder.length() - 4, strBuilder.length() - 1));
	}

	public void bfs() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("--------- BFS -----------\n");
		Queue<Vertex> queue = new ArrayDeque<>();
		Vertex startVertex = adjacencyList.keySet().iterator().next();
		queue.add(startVertex);
		strBuilder.append(startVertex + " -> ");
		startVertex.markVisited();

		while (!queue.isEmpty()) {
			Vertex adjacentVertex = getUnvisitedAdjacentVertex(queue.peek());
			if (adjacentVertex == null) {
				queue.remove();
			} else {
				queue.add(adjacentVertex);
				strBuilder.append(adjacentVertex + " -> ");
				adjacentVertex.markVisited();
			}
		}

		for (Vertex source : adjacencyList.keySet()) {
			source.markUnvisited();
		}
		System.out.println(strBuilder.delete(strBuilder.length() - 4, strBuilder.length() - 1));
	}

	public boolean isLoopExists(Vertex source) {
		Map<Vertex, VisitStatus> visitedStatusMap = new HashMap<>();
		Stack<Vertex> stack = new Stack<>();
		Vertex current = source;
		visitedStatusMap.put(current, VisitStatus.IN_PROGRESS);
		stack.push(current);

		while (!stack.isEmpty()) {
			try {
				current = getUnvisitedAdjacentVertex(stack.peek(), visitedStatusMap);
				if (current == null) {
					visitedStatusMap.put(stack.pop(), VisitStatus.COMPLETED);
				} else {
					visitedStatusMap.put(current, VisitStatus.IN_PROGRESS);
					stack.push(current);
				}
			} catch (CyclicGraphException e) {
				return true;
			}
		}
		return false;
	}

	public Vertex getUnvisitedAdjacentVertex(final Vertex source, Map<Vertex, VisitStatus> visitedStatusMap)
			throws CyclicGraphException {
		if (adjacencyList.get(source) == null) {
			return null;
		}
		for (Vertex dest : adjacencyList.get(source)) {
			VisitStatus visitStatus = visitedStatusMap.get(dest);
			if (visitStatus == null) { // indicates not yet visited.
				return dest;
			} else if (VisitStatus.IN_PROGRESS.equals(visitStatus)) {
				throw new CyclicGraphException();
			}
		}
		return null;
	}

	public void topologicalSort() {
		Set<Vertex> visited = new HashSet<>();
		Stack<Vertex> stack = new Stack<>();

		for (Vertex vertex : adjacencyList.keySet()) {
			if(!visited.contains(vertex)) {
			topologicalSortRecur(vertex, stack, visited);
			}
		}

		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(stack.pop());
		while (!stack.isEmpty()) {
			strBuilder.append("->" + stack.pop());
		}
		System.out.println("Topological order: " + strBuilder.toString());
	}

	private void topologicalSortRecur(Vertex source, Stack<Vertex> stack, Set<Vertex> visited) {
		visited.add(source);
		Set<Vertex> adjacentVertices = adjacencyList.get(source);
		if (adjacentVertices != null) {
			for (Vertex vertex : adjacentVertices) {
				if (!visited.contains(vertex)) {
					topologicalSortRecur(vertex, stack, visited);
				}
			}
		}
		stack.push(source);
	}

	public static void main(String[] args) {
		GraphAdjacencyList graph = new GraphAdjacencyList();

		Vertex A = new Vertex("A");
		Vertex B = new Vertex("B");
		Vertex C = new Vertex("C");
		Vertex D = new Vertex("D");
		Vertex E = new Vertex("E");
		Vertex F = new Vertex("F");
		Vertex G = new Vertex("G");
		Vertex H = new Vertex("H");

		graph.addEdge(A, D);
		graph.addEdge(A, E);

		graph.addEdge(B, E);

		graph.addEdge(E, G);

		graph.addEdge(D, G);

		graph.addEdge(G, H);

		graph.addEdge(C, F);

		graph.addEdge(F, H);

		graph.display();

		graph.topologicalSort();

		/*
		 * //graph.dfs();
		 * 
		 * //graph.bfs();
		 * 
		 * boolean isCycle = graph.isLoopExists(A); System.out.println(isCycle ?
		 * "Cycle exists" : "Cycle does not exists");
		 */

	}

	private static class Vertex {
		private String name;

		private boolean visited;

		public Vertex(final String name) {
			this.name = name;
		}

		public void markVisited() {
			visited = true;
		}

		public void markUnvisited() {
			visited = false;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private static enum VisitStatus {
		IN_PROGRESS, COMPLETED;
	}

}
