
package graph;

/*
 * Disjoint set is used to detect loop in undirected graph. Based on find and union operation.
 * http://www.geeksforgeeks.org/union-find/
 */
public class DisjointSet {

	int[] parent;
	int[] rank;
	int n;

	public DisjointSet(int n) {
		parent = new int[n];
		rank = new int[n];
		this.n = n;

		// initially every node belongs to different set.
		for (int i = 0; i < n; i++) {
			parent[i] = i;
		}
	}

	int find(int node) {
		if (parent[node] == node) {
			return node;
		}
		int rep = find(parent[node]);
		parent[node] = rep;
		return rep;
	}
	
	void union(int source, int dest) {
		int sourceRep =  find(source);
		int destRep = find(dest);
		
		//Already part of same tree, just return.
		if(sourceRep == destRep) {
			return;
		}
		//If rank of source rep is higher than destRep, then make sourceRep as parent of destRep
		if(rank[sourceRep] > rank[destRep]) {
			parent[destRep] = sourceRep;
		} //make destRep as parent of source rep here.
		else if(rank[sourceRep] < rank[destRep]) {
			parent[sourceRep] = destRep;
		}else {
			//both are equal, so does not matter who becomes the parent. basically need to choose rep here.
			//For now, choosing sourceRep as rep and incrementing the rank so that all other connection keeps making source rep as their rep.
			parent[destRep] =sourceRep;
			rank[sourceRep]++;
		}
	}
	
	public static void main(String[] args) {
		// Let there be 5 persons with ids as
        // 0, 1, 2, 3 and 4
        int n = 5;
        DisjointSet ds =
                       new DisjointSet(n);
 
        // 0 is a friend of 2
        ds.union(0, 2);
 
        // 4 is a friend of 2
        ds.union(4, 2);
 
        // 3 is a friend of 1
        ds.union(3, 1);
 
        // Check if 4 is a friend of 0
        if (ds.find(4) == ds.find(0))
            System.out.println("Yes");
        else
            System.out.println("No");
 
        // Check if 1 is a friend of 0
        if (ds.find(1) == ds.find(0))
            System.out.println("Yes");
        else
            System.out.println("No");
        
	}
	
	

}
