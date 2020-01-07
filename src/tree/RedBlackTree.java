package tree;

public class RedBlackTree<T extends Comparable<T>> {

	Node<T> ROOT;

	public void insert(T data) {
		if (ROOT == null) {
			ROOT = new Node<>(data, null, null, null, Color.BLACK);
			return;
		}

		Node<T> current = ROOT;
		Node<T> parent = ROOT;
		boolean isLeft = true;
		while (current != null) {

			if (requiresColorFlip(current)) {
				flipColor(current);
			}

			// After flip, check if two right nodes are consecutive, needs
			// rotation.
			if (requiresRotation(current.parent)) {
				handleRotation(current, current.parent, current.parent.parent);
			}

			parent = current;
			if (current.data.compareTo(data) > 0) {
				current = current.left;
				isLeft = true;
			} else {
				current = current.right;
				isLeft = false;
			}
		}

		// always insert new children with RED color.
		Node<T> node = new Node<>(data, null, null, Color.RED);
		node.parent = parent;
		if (isLeft) {
			parent.left = node;
		} else {
			parent.right = node;
		}

		// after insert, check if we need to rotate.
		if (requiresRotation(parent)) {
			
			// check if newly inserted node is outside grand child of parent.
			handleRotation(node, parent, parent.parent);

		}
		printTree();
		if(!validRedBlackTree()) {
			throw new RuntimeException("Red black tree is not valid.");
		}
	}

	private void handleRotation(Node<T> X, Node<T> P, Node<T> G) {
		if (outsideGrandChild(X, P, G)) { // Inside grand child
			G.flipColor();
			P.flipColor();
			if (P.left != null && P.left.equals(X)) { // X is on left of P,
														// meaning we need to
				// rotate towards right.
				rotateRight(X, P, G);
			} else {
				rotateLeft(X, P, G); // X is on right of P, meaning we
										// need to
				// rotate left.
			}
		} else { // X is inside grand child.
			if (P.right != null && P.right.equals(X)) {
				X.flipColor();
				G.flipColor();
				rotateLeft(X, X, P);
				rotateRight(P, X, G);
			} else {
				X.flipColor();
				G.flipColor();
				rotateRight(X, X, P);
				rotateLeft(P, X, G);
			}
		}
	}

	private void rotateRight(Node<T> X, Node<T> P, Node<T> G) {

		if (G.parent != null) {
			if (G.parent.left != null && G.parent.left.equals(G)) {
				G.parent.left = P;
			} else {
				G.parent.right = P;
			}
		} else {
			ROOT = P;
			ROOT.parent = null;
		}

		P.right = G;
		P.parent = G.parent;
		G.parent = P;
		G.left = null;

	}

	private void rotateLeft(Node<T> X, Node<T> P, Node<T> G) {
		if (G.parent != null) {
			if (G.parent.left != null && G.parent.left.equals(G)) {
				G.parent.left = P;
			} else {
				G.parent.right = P;
			}
		} else {
			ROOT = P;
			ROOT.parent = null;
		}
		P.left = G;
		P.parent = G.parent;
		G.parent = P;
		G.right = null;

	}

	private boolean outsideGrandChild(Node<T> X, Node<T> P, Node<T> G) {
		return G.left != null && G.left.equals(P) && P.left != null && P.left.equals(X)
				|| G.right != null && G.right.equals(P) && P.right != null && P.right.equals(X);
	}

	private void flipColor(Node<T> node) {
		if (!ROOT.equals(node)) {
			node.flipColor();
		}
		if (node.left != null) {
			node.left.flipColor();
		}
		if (node.right != null) {
			node.right.flipColor();
		}
	}

	// Check if color flip is required for given node. color flip is required if
	// black root is found with both children as red.
	private boolean requiresColorFlip(Node<T> root) {
		if (root != null && root.color == Color.BLACK && root.left != null && root.left.color == Color.RED
				&& root.right != null && root.right.color == Color.RED) {
			return true;
		}
		return false;
	}

	// Rotation is required if 2 reds are consecutive.
	private boolean requiresRotation(Node<T> node) {
		if (node != null && node.color == Color.RED && (node.left != null && node.left.color == Color.RED
				|| node.right != null && node.right.color == Color.RED)) {
			return true;
		}
		return false;
	}

	public Node<T> search(T data) {
		Node<T> current = ROOT;
		while (current != null) {
			if (current.data.compareTo(data) == 0) {
				return current;
			} else if (data.compareTo(current.data) < 0) {
				current = current.left;
			} else {
				current = current.right;
			}
		}
		return null;
	}

	private boolean validRedBlackTree() {
		boolean validRBTree = true;
		if (!ROOT.color.equals(Color.BLACK)) {
			return false;
		}
		
		return DFSTraversal(ROOT, 0, getExpectedBlackNodeCount(ROOT, 0));

	}

	private int getExpectedBlackNodeCount(Node<T> current, int blackNodeCount) {
		if (current != null && current.color.equals(Color.BLACK)) {
			blackNodeCount++;
		}

		if (current == null || current.isLeafNode()) {
			return blackNodeCount++;
		}
		return getExpectedBlackNodeCount(current.left, blackNodeCount);

	}

	private boolean DFSTraversal(Node<T> current, int blackNodeCount, int expectedCount) {
		if (current != null && current.color.equals(Color.BLACK)) {
			blackNodeCount++;
		}
		
		if(requiresRotation(current)) {
			System.out.println("Has R-R relationship.\n" + printTree(current));
			
			return false;
		}

		if(current == null) {
			return true;
		}
		
		if (current.isLeafNode()) {
			return blackNodeCount == expectedCount;
		}

		return   DFSTraversal(current.left, blackNodeCount, expectedCount) && DFSTraversal(current.right, blackNodeCount, expectedCount);

	}

	public void printTree() {
		System.out.println(printTree(ROOT));
	}

	public String printTree(Node<T> node) {
		if (node == null) {
			return "NULL";
		}
		if (node.isLeafNode()) {
			return node.toString();
		}

		StringBuilder sb = new StringBuilder();

		String[] lhs = printTree(node.left).split("\n");
		String[] rhs = printTree(node.right).split("\n");

		int maxCol = getMaxCol(lhs) + getMaxCol(rhs) + 4;
		alignAtCenter(sb, node.toString(), maxCol, true);
		alignAtCenter(sb, "|", maxCol, true);
		addLineSeparator(sb, maxCol, getMaxCol(lhs), getMaxCol(rhs));
		addChildNode(sb, lhs, rhs, maxCol);
		return sb.toString();
	}

	private int getMaxCol(String[] strs) {
		int max = 0;
		for (String str : strs) {
			if (str.length() > max) {
				max = str.length();
			}
		}
		return max;
	}

	private void addChildNode(StringBuilder sb, String[] lhs, String[] rhs, int maxCol) {
		for (int i = 0; i < lhs.length || i < rhs.length; i++) {
			int currentPos = 0;
			if (i < lhs.length) {
				sb.append(lhs[i]);
				currentPos += lhs[i].length();
			}
			int rhsPosition = maxCol;
			if (i < rhs.length) {
				rhsPosition -= rhs[i].length();
			}
			for (int j = currentPos; j < rhsPosition; j++) {
				sb.append(' ');
			}
			if (i < rhs.length) {
				sb.append(rhs[i]);
			}
			sb.append("\n");
		}
	}

	private void addLineSeparator(StringBuilder sb, int maxCol, int leftLen, int rightLen) {
		int left = 0, right = 0;
		boolean firstOccurence = true;
		for (int i = 0; i < maxCol; i++) {
			if (i < leftLen / 2 || i > maxCol - rightLen / 2) {
				sb.append(' ');
			} else {
				if (firstOccurence) {
					left = i;
					firstOccurence = false;
				} else {
					right = i;
				}
				sb.append('-');
			}
		}
		/*
		 * sb.append("\n"); for(int i=0; i < maxCol; i++) { if(i == left -1 || i
		 * == right +1) { sb.append("|"); }else { sb.append(' '); } }
		 */
		sb.append("\n");
	}

	private void alignAtCenter(StringBuilder sb, String str, int maxCol, boolean newLine) {
		for (int i = 0; i < maxCol;) {
			if (maxCol / 2 == i) {
				sb.append(str);
				i = i + str.length();
			} else {
				sb.append(' ');
				i++;
			}
		}

		if (newLine) {
			sb.append("\n");
		}
	}

	private static enum Color {
		RED('R'), BLACK('B');
		char colorChar;

		private Color(char colorChar) {
			this.colorChar = colorChar;
		}

		public char getChar() {
			return this.colorChar;
		}

		public Color flip() {
			switch (colorChar) {
			case 'R':
				return BLACK;
			case 'B':
				return RED;
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	private static class Node<T extends Comparable<T>> {
		T data;
		Node<T> left;
		Node<T> right;
		Color color;
		Node<T> parent;

		public Node(T data, Node<T> left, Node<T> right, Color color) {
			this.data = data;
			this.left = left;
			this.right = right;
			this.color = color;
		}

		public Node(T data, Node<T> left, Node<T> right, Node<T> parent, Color color) {
			this.data = data;
			this.left = left;
			this.right = right;
			this.color = color;
			this.parent = parent;
		}

		public void setParent(Node<T> parent) {
			this.parent = parent;
		}

		public boolean isLeafNode() {
			return this.left == null && this.right == null;
		}

		public boolean hasBothChildren() {
			return this.left != null && this.right != null;
		}

		public void flipColor() {
			color = color.flip();
		}

		@Override
		public String toString() {
			return data == null ? "NULL" : data.toString() + "(" + color.getChar() + ")";
		}

	}

	public static void main(String[] args) {
		RedBlackTree<Integer> tree = new RedBlackTree<>();

		/*
		 * Integer[] dataArray = new Integer[] { 10, 20, 30, 40, 50, 60, 70, 80,
		 * 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210 };
		 */
		
		
		 Integer[] dataArray = new Integer[] {500,400,300,290,280,270,250,240,230,220,210, 200,190,180,170,160,150,140,130,120,110,100,90,80,70,60,50,40,30,20,10 };
		 
		//Integer[] dataArray = new Integer[] { 50, 25, 75, 12, 37,6,18,31,43,62,87,3 };

		for (Integer data : dataArray) {
			tree.insert(data);
		}

		tree.printTree();
		
		System.out.println(tree.validRedBlackTree());

		// tree.printInOrderTraversal();
		// tree.printPreOrderTraversal();
		// tree.printPostOrderTraversal();

		/*
		 * Node<Integer> foundNode = tree.search(70); if(foundNode == null) {
		 * System.out.println("Node not found"); }else { System.out.println(
		 * "Found node:\n" + tree.printTree(foundNode)); }
		 */
	}

}
