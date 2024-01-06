package gui.components;

import java.util.Base64;
import java.io.*;

public class Base64Image {
	public static String encodeImage(String in_file_name) {
		final int BUF_SIZE = 1024;
		byte[] in = new byte[BUF_SIZE];
		String encoded = null;
		try {
			BufferedInputStream in_file = new BufferedInputStream(new FileInputStream(new File(in_file_name)));
			encoded = "";
			while (in_file.read(in, 0, BUF_SIZE) != -1) {
				encoded += Base64.getEncoder().encodeToString(in) + "\n";
			}
			in_file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return encoded;
	}

	public static byte[] decodedImage(String encoded) {
		byte[] decoded = null;
		try {
			ByteArrayOutputStream out_bytes = new ByteArrayOutputStream();
			BufferedReader reader = new BufferedReader(new StringReader(encoded));
			String line_encoded;
			while ((line_encoded = reader.readLine()) != null) {
				out_bytes.write(Base64.getDecoder().decode(line_encoded));
			}
			out_bytes.close();
			decoded = out_bytes.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return decoded;
	}
}