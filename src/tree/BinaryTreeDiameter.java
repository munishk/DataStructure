package tree;

public class BinaryTreeDiameter {
	
	
	static class Node {
		String data;
		Node left, right;
		public Node(String data, Node left, Node right) {
			super();
			this.data = data;
			this.left = left;
			this.right = right;
		}
	}
	
	static class Height {
		int h;
	}
	
	private static int diameter(Node root, Height height) {
		if(root == null) {
			height.h =0;
			return 0;
		}
		
		Height lh = new Height();
		Height rh = new Height();
		
		int lDiameter = diameter(root, lh);
		int rDiameter = diameter(root, rh);
		
		height.h = Math.max(lh.h, rh.h) + 1;
		
		return Math.max(lh.h + rh.h + 1, Math.max(lDiameter, rDiameter));
		
	}

	//  Complexity O(n2) since for each node, we are calculating height
	private static int diameter(Node root) {
		if(root == null) {
			return 0;
		}
		
		int lHeight = height(root.left);
		int rHeight = height(root.right);
		
		int lDiameter = diameter(root.left);
		int rDiameter = diameter(root.right);
 		
		int currentDiameter = lHeight + rHeight + 1;
		
		return Math.max(currentDiameter, Math.max(lDiameter, rDiameter));
	}
	
	private static int height(Node root) {
		if(root == null) {
			return 0;
		}
		
		int lHeight = height(root.left);
		int rHeight = height(root.right);
		
		return 1 + Math.max(lHeight, rHeight);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
