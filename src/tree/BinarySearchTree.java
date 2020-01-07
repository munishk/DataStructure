package tree;

public class BinarySearchTree<T extends Comparable<T>> {
	Node<T> ROOT;

	public void insert(T data) {
		Node<T> node = new Node<>(data, null, null);
		if (ROOT == null) {
			ROOT = node;
			return;
		}

		Node<T> current = ROOT, parent = ROOT;
		boolean isLeft = false;
		while (current != null) {
			parent = current;
			if (node.data.compareTo(current.data) <= 0) {
				current = current.left;
				isLeft = true;
			} else {
				current = current.right;
				isLeft = false;
			}
		}

		// finally, add it to parent left or right.
		node.parent = parent;
		if (isLeft) {
			parent.left = node;
		} else {
			parent.right = node;
		}
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

	public Node<T> delete(T data) {
		Node<T> foundNode = search(data);
		if (foundNode == null) {
			return foundNode;
		}
		// when leaf node, simply set this node to null;
		if (foundNode.isLeafNode()) {
			if(foundNode.parent.left.equals(foundNode)) {
				foundNode.parent.left = null;
			}else {
				foundNode.parent.right = null;
			}
		} //When both nodes are present, find successive node
		else if (foundNode.hasBothChildren()) { 
			Node<T> successor = successor(foundNode);
			if (successor.equals(foundNode.right)) {
				successor.left = foundNode.left;
				foundNode.parent = successor;
			} else {
				successor.parent.left = null;
				
				//replace parent
				successor.parent = foundNode.parent;
				foundNode.left.parent = successor;
				foundNode.right.parent = successor;
				
				successor.right = foundNode.right;
				successor.left = foundNode.left;
				
				
				
			}
			if (foundNode.equals(ROOT)) {
				ROOT = successor;
				ROOT.parent = null;
			}
		} else if (foundNode.left != null) {
			if (foundNode.equals(ROOT)) {
				ROOT = foundNode.left;
				ROOT.parent = null;
			} else if (foundNode.parent.left.equals(foundNode)) {
				foundNode.parent.left = foundNode.left;
				foundNode.left.parent = foundNode.parent;
			} else {
				foundNode.parent.right = foundNode.left;
				foundNode.left.parent = foundNode.parent;
			}
		} else {
			if (foundNode.equals(ROOT)) {
				ROOT = foundNode.right;
				ROOT.parent = null;
			} else if (foundNode.parent.left.equals(foundNode)) {
				foundNode.parent.left = foundNode.right;
				foundNode.right.parent = foundNode.parent;
			} else {
				foundNode.parent.right = foundNode.right;
				foundNode.right.parent = foundNode.parent;
			}

		}
		foundNode.parent = null; //remove parent referece so that it get garbage collected.
		return foundNode;
	}

	// Find successor of given node.
	private Node<T> successor(Node<T> node) {
		Node<T> currentNode = node.right;
		while (currentNode.left != null) {
			currentNode = currentNode.left;
		}
		return currentNode;

	}

	public void printInOrderTraversal() {
		System.out.println("Inorder traversal:\n" + inOrderTraversal(ROOT));
	}

	public String inOrderTraversal(Node<T> node) {
		final StringBuilder sb = new StringBuilder();
		if (node != null) {
			if (node.isLeafNode()) {
				return node.toString();
			}
			String left = inOrderTraversal(node.left);
			sb.append(left).append(" ").append(node);
			String right = inOrderTraversal(node.right);
			sb.append(" ").append(right);

		}
		return sb.toString();
	}

	public void printPreOrderTraversal() {
		System.out.println("Preorder traversal:\n" + preOrderTraversal(ROOT));
	}

	public String preOrderTraversal(Node<T> node) {
		final StringBuilder sb = new StringBuilder();
		if (node != null) {
			if (node.isLeafNode()) {
				return node.toString();
			}
			sb.append(node.data);
			String left = preOrderTraversal(node.left);
			sb.append(" ").append(left);
			String right = preOrderTraversal(node.right);
			sb.append(" ").append(right);
		}
		return sb.toString();
	}

	public void printPostOrderTraversal() {
		System.out.println("Postorder traversal:\n" + postOrderTraversal(ROOT));
	}

	public String postOrderTraversal(Node<T> node) {
		final StringBuilder sb = new StringBuilder();
		if (node != null) {
			if (node.isLeafNode()) {
				return node.toString();
			}
			String left = postOrderTraversal(node.left);
			sb.append(left).append(" ");
			String right = postOrderTraversal(node.right);
			sb.append(right).append(" ");
			sb.append(node.data);
		}
		return sb.toString();
	}

	public void printTree() {
		System.out.println(printTree(ROOT));
	}

	public String printTree(Node<T> node) {
		if (node == null) {
			return "NULL";
		}
		if (node.isLeafNode()) {
			return node.data.toString();
		}

		StringBuilder sb = new StringBuilder();

		String[] lhs = printTree(node.left).split("\n");
		String[] rhs = printTree(node.right).split("\n");

		int maxCol = getMaxCol(lhs) + getMaxCol(rhs) + 4;
		alignAtCenter(sb, node.data.toString(), maxCol, true);
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

	private static class Node<T extends Comparable<T>> {
		T data;
		Node<T> left;
		Node<T> right;
		Node<T> parent;

		public Node(T data, Node<T> left, Node<T> right) {
			this.data = data;
			this.left = left;
			this.right = right;
		}

		public boolean isLeafNode() {
			return this.left == null && this.right == null;
		}

		public boolean hasBothChildren() {
			return this.left != null && this.right != null;
		}

		public void setParent(Node<T> parent) {
			this.parent = parent;
		}

		@Override
		public String toString() {
			return data == null ? "NULL" : data.toString();
		}
	}

	public static void main(String[] args) {
		BinarySearchTree<Integer> tree = new BinarySearchTree<>();

		// Integer[] dataArray = new Integer[] {20,10,5,12,25,22,27};
		// Integer[] dataArray = new Integer[] {10,20,30,40,50,60};
		// Integer[] dataArray = new Integer[] {60,50,40,30,20,10};
		Integer[] dataArray = new Integer[] { 50, 25, 75, 15, 70, 80, 10, 20, 65, 77 };
		// Integer[] dataArray = new Integer[] { 50, 25, 75};
		for (Integer data : dataArray) {
			tree.insert(data);
		}

		tree.printTree();

		System.out.println("Deleted node:\n" + tree.delete(50));

		System.out.println("Tree after deletion:\n");
		tree.printTree();

		System.out.println("Deleted node:\n" + tree.delete(77));

		System.out.println("Tree after deletion:\n");
		tree.printTree();
		
		System.out.println("Deleted node:\n" + tree.delete(25));

		System.out.println("Tree after deletion:\n");
		tree.printTree();
		// tree.printInOrderTraversal();
		// tree.printPreOrderTraversal();
		// tree.printPostOrderTraversal();

		/*
		 * Node<Integer> foundNode = tree.search(70); if(foundNode == null) {
		 * System.out.println("Node not found"); }else { System.out.println(
		 * "Found node:\n" + tree.printTree(foundNode)); }
		 */
	}

	public static void main1(String[] args) {
		BinarySearchTree<Character> tree = new BinarySearchTree<>();

		// Integer[] dataArray = new Integer[] {20,10,5,12,25,22,27};
		// Integer[] dataArray = new Integer[] {10,20,30,40,50,60};
		// Integer[] dataArray = new Integer[] {60,50,40,30,20,10};
		Character[] dataArray = new Character[] { 'F', 'B', 'G', 'A', 'D', 'C', 'E', 'I', 'H' };
		// Integer[] dataArray = new Integer[] { 50, 25, 75};
		for (Character data : dataArray) {
			tree.insert(data);
		}

		tree.printTree();

		tree.printInOrderTraversal();
		tree.printPreOrderTraversal();
		tree.printPostOrderTraversal();

		/*
		 * Node<Integer> foundNode = tree.search(70); if(foundNode == null) {
		 * System.out.println("Node not found"); }else { System.out.println(
		 * "Found node:\n" + tree.printTree(foundNode)); }
		 */
	}

}
