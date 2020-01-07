package tree;

public class BinaryIndexedTree {
	
	int[] tree;
	
	public BinaryIndexedTree(int[] nums) {
		tree = new int[nums.length+1];
		for(int i=0; i<nums.length; i++) {
			updateTree(i, nums[i]);
		}
	}
	
	private void updateTree(int index, int val) {
		for(int i=index+1; i<tree.length; i+= i & (-i)) {
			tree[i]+=val;
		}
	}
	
	private int getSum(int index) {
		int sum = 0;
		for(int i=index+1; i>0; i-= i & (-i)) {
			sum+=tree[i];
		}
		return sum;
	}
	
	
	
	private int getSum(int from, int to) {
		return getSum(to) - getSum(from-1);
	}
	

	public static void main(String[] args) {
		int[] nums = {2, 1, 1, 3, 2, 3, 4, 5, 6, 7, 8, 9};
		BinaryIndexedTree biTree = new BinaryIndexedTree(nums);
		System.out.println(biTree.getSum(3,6));
		biTree.updateTree(5, 2);
		System.out.println(biTree.getSum(3,6));
		
		for(int i=1; i<=20; i++) {
			System.out.println("I:" + i + ", Parent:" + parent(i));
		}
	}
	
	private static int parent(int i) {
		return i+= i & (-i);
	}

}
