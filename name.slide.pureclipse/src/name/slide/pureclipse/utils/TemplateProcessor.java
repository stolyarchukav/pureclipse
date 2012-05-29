package name.slide.pureclipse.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TemplateProcessor {
	
	private static String TEMPLATE = "#{%s}";
	
	public static InputStream process(InputStream inputStream, Map<TemplateKey, String> params) throws IOException {
		if (inputStream == null) {
			throw new IOException("Template resource not found");
		}
		if (params == null || params.isEmpty()) {
			return inputStream;
		}
		Map<String, String> map = new HashMap<String, String>(params.size());
		for (TemplateKey key : params.keySet()) {
			map.put(String.format(TEMPLATE, key.name()), params.get(key));
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		while (reader.ready()) {
			String line = reader.readLine();
			for (String key : map.keySet()) {
				line = line.replace(key, map.get(key));
			}
			bos.write((line + "\n").getBytes());
		}
		bos.close();
		reader.close();
		return new ByteArrayInputStream(bos.toByteArray());
	}
	
}
