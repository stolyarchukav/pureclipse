package name.slide.pureclipse.utils;

public class StringUtils {

	public static String extractPackage(String containerName) {
		String[] path = containerName.split("/");
		StringBuilder result = new StringBuilder();
		if (path.length > 3) {
			for (int q = 3; q < path.length; q++) {
				result.append(path[q]);
				if (q < (path.length - 1)) {
					result.append(".");
				}
			}
		}
		return result.toString();
	}
	
	public static String getUpperPackage(String packageName) {
		return cutLastPart(packageName, ".");
	}
	
	public static String getUpperContainer(String containerName) {
		return cutLastPart(containerName, "/");
	}
	
	private static String cutLastPart(String string, String regex) {
		int lastPointIndex = string.lastIndexOf(regex);
		if (lastPointIndex != -1) {
			return string.substring(0, lastPointIndex);
		}
		return string;
	}
	
	public static String encode(String input) {
		return code(input, -1);
	}
	
	public static String decode(String input) {
		return code(input, 1);
	}
	
	private static String code(String input, int sign) {
		StringBuilder builder = new StringBuilder(input.length());
		for (int q = 0; q < input.length(); q++) {
			builder.append((char) (input.charAt(q) + (q % 2 == 0 ? sign : sign * 2)));
		}
		return builder.toString();
	}
	
}
