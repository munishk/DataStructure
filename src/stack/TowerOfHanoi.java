package stack;

import java.util.Stack;

public class TowerOfHanoi {
	
	static class Disc {
		int size;
		
		Disc(int size) {
			this.size = size;
		}
	}
	
	static class Tower {
		Stack<Disc> discs = new Stack<>();
		
		Tower(Stack<Disc> discs) {
			this.discs = discs;
		}
		
		public void push(Disc item) {
			if(discs == null) {
				discs = new Stack<>();
			}
			this.discs.push(item);
		}
		
		public Disc pop() {
			return this.discs.pop();
		}
		
		public void moveTop(Tower target) {
			target.push(pop());
		}
		
		@Override
		public String toString() {
			StringBuilder strBuilder = new StringBuilder();
			if(discs != null) {
			for(Disc disc : discs) {
				strBuilder.append(disc.size + ",");
			}
			}
			return strBuilder.toString();
		}
	}

	public static void main(String[] args) {
		int n=3;
		
		Stack<Disc> discs = new Stack<>();
		for(int i=n; i >=1; i--) {
			discs.push(new Disc(i));
		}
		
		Tower source = new Tower(discs);
		Tower target = new Tower(null);
		Tower buffer = new Tower(null);
		
		System.out.println("####### Before ###########");
		print(source, buffer, target);
		
		moveDiscs(n, source, target, buffer);
		
		System.out.println("####### AFter ###########");
		print(source, buffer, target);
		

	}
	
	private static void moveDiscs(int n, Tower source, Tower target, Tower buffer) {
		if(n <= 0) return;
		moveDiscs(n-1, source, buffer, target);
		System.out.println(String.format("####### Before (n=%s) ###########", n));
		print(source, buffer, target);
		source.moveTop(target);
		System.out.println(String.format("####### After-1 (n=%s) ###########", n));
		print(source, buffer, target);
		moveDiscs(n-1, buffer, target, source);
		System.out.println(String.format("####### After-2 (n=%s) ###########", n));
		print(source, buffer, target);
	}
	
	private static void print(Tower source, Tower buffer, Tower target) {
		System.out.println("Source:" + source.toString());
		System.out.println("Buffer:" + buffer.toString());
		System.out.println("Target:" + target.toString());}

}
