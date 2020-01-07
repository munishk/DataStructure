package util;

public class StringUtils {
	
	public static String toString(int[] arr) {
		StringBuilder sb = new StringBuilder();
		for (int value : arr) {
			sb.append(value + " ");
		}
		return sb.toString();
	}

}
