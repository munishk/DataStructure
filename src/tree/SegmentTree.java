package tree;

import util.StringUtils;

/**
 * http://www.geeksforgeeks.org/segment-tree-set-1-sum-of-given-range/
 * @author munishk
 *
 */
public class SegmentTree {
	
	int[] st;
	
	SegmentTree(int[] arr) {
	    int height = (int) Math.ceil(Math.log(arr.length)/Math.log(2));
		int maxSize = 2* (int)Math.pow(2, height) -1;
		st = new int[maxSize];
		constructSegmentTree(arr, 0, arr.length-1, 0);
	}
	
	int getMid(int l, int h) {
		return l + (h-l)/2;
	}
	
	int constructSegmentTree(int[] arr, int ss, int se, int si) {
		if(ss == se) {
			st[si] = arr[ss];
			return st[si];
		}
		
		int mid = getMid(ss, se);
		st[si] = constructSegmentTree(arr, ss, mid, 2*si +1) + constructSegmentTree(arr, mid+1, se, 2*si+2);
		return st[si];
	}
	
	int getSum(int ss, int se, int l, int r, int si) {
		if(l<=ss && r>=se) {
			return st[si];
		}
		
		if(r < ss || l > se) {
			return 0;
		}
		
		int mid = getMid(ss, se);
		return getSum(ss, mid, l, r, 2*si+1) + getSum(mid+1, se, l, r, 2*si+2);	
	}
	
	

	public static void main(String[] args) {
		int[] arr = {1, 3, 5, 7, 9, 11};
		SegmentTree st = new SegmentTree(arr);
		System.out.println("Segment tree:\n" + StringUtils.toString(st.st));
		int sum = st.getSum(0, arr.length-1, 1, 3, 0);
		System.out.println(sum);

	}

}
