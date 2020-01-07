package util;

public class Timer {
	
	private static long start;
	
	public static void start() {
		start = System.currentTimeMillis();
	}
	
	public static void finish() {
		long time = System.currentTimeMillis() - start;
		long seconds = time/1000;
		long mills = time%1000;
		System.out.printf("Time taken: %s seconds, %s milliseconds\n", seconds, mills);
	}
	
	public static void reset() {
		start = System.currentTimeMillis();
	}

}
