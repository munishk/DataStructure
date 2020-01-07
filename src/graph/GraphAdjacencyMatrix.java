package graph;

import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;

import stack.MyStack;

public class GraphAdjacencyMatrix {

	private int[][] adjacencyMatrix;

	private Vertex[] vertices;

	private int currentVertexPos = -1;

	public GraphAdjacencyMatrix(int vertextCount) {
		vertices = new Vertex[vertextCount];
		adjacencyMatrix = new int[vertextCount][vertextCount];

	}

	public void addVertex(Vertex v) {
		vertices[++currentVertexPos] = v;
	}

	public void addEdge(int source, int dest, int distance) {
		adjacencyMatrix[source][dest] = distance;
	}

	public int getUnvisitedAdjacentVertex(int index) {
		for (int j = 0; j < vertices.length; j++) {
			if (adjacencyMatrix[index][j] > 0 && !vertices[j].visited) {
				return j;
			}
		}
		return -1;
	}
	
	
	
	public void bfs() {
		Queue<Integer> queue = new ArrayDeque<>();
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("---------- BFS -----------\n");
		queue.add(0);
		vertices[0].markVisited();
		strBuilder.append(vertices[0] + " -> ");
		while(!queue.isEmpty()) {
			int index = getUnvisitedAdjacentVertex(queue.peek());
			if(index == -1) {
				queue.remove();
			}else {
			strBuilder.append(vertices[index] + " -> ");
			vertices[index].markVisited();
			queue.add(index);
			}
		}
		
		for (int i = 0; i < vertices.length; i++) {
			vertices[i].markUnvisited();
		}
		System.out.println(strBuilder.delete(strBuilder.length()-4, strBuilder.length() -1));
	}

	public void dfs() {
		MyStack<Integer> stack = new MyStack<>(vertices.length);
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("---------- DFS -----------\n");

			stack.push(0);
			vertices[0].markVisited();
			strBuilder.append(vertices[0] + " -> ");
			
			while (!stack.isEmpty()) {
				int adjacentVertex = getUnvisitedAdjacentVertex(stack.peek());
				if (adjacentVertex == -1) {
					stack.pop();
				} else {
					vertices[adjacentVertex].markVisited();
					strBuilder.append(vertices[adjacentVertex] + " -> ");
					stack.push(adjacentVertex);
				}

			}


		for (int i = 0; i < vertices.length; i++) {
			vertices[i].markUnvisited();
		}
		System.out.println(strBuilder.delete(strBuilder.length()-4, strBuilder.length() -1));
	}
	
	public void mstw() {
		
		PriorityQueue<Vertex> pQueue = new PriorityQueue<>();
		pQueue.add(vertices[0]);
		while(true) {
			
		}
		
	}

	public void display() {
		final StringBuilder strBuilder = new StringBuilder();
		boolean removePrevParen = false;
		for (int i = 0; i < vertices.length; i++) {
			strBuilder.append(vertices[i] + " -> ");
			for (int j = 0; j < vertices.length; j++) {
				if (adjacencyMatrix[i][j] > 0) {
					strBuilder.append(vertices[j] + ", ");
					removePrevParen = true;
				}
			}
			if (removePrevParen) {
				strBuilder.delete(strBuilder.length() - 2, strBuilder.length() - 1);
				removePrevParen = false;
			}
			strBuilder.append("\n");
		}
		System.out.println(strBuilder);
	}

	public static void main(String[] args) {
		GraphAdjacencyMatrix graph = new GraphAdjacencyMatrix(4);

		graph.addVertex(new Vertex("A"));
		graph.addVertex(new Vertex("B"));
		graph.addVertex(new Vertex("C"));
		graph.addVertex(new Vertex("D"));

		graph.addEdge(0, 1, 1); // A -> B
		graph.addEdge(0, 2, 1); // A -> C

		graph.addEdge(1, 0, 1); // B -> A
		//graph.addEdge(1, 2, 1); // B -> C
		graph.addEdge(1, 3, 1); // B -> D

		graph.addEdge(2, 0, 1); // C -> A

		graph.display();

		graph.dfs();
		
		graph.bfs();

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

		public Vertex getSource() {
			return source;
		}

		public Vertex getDest() {
			return dest;
		}

		public int getDistance() {
			return distance;
		}
		
		
		
	}

}
