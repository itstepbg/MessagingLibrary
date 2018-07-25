package library.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

public class FileUtils {

	public static void writeToFile(OutputStream outputStream, String base64) {
		byte[] decodedBytes = Base64.getDecoder().decode(base64);

		try {
			outputStream.write(decodedBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readFromFile(InputStream inputStream, int offset) {
		byte[] readChunk = null;
		try {
			inputStream.read(readChunk, offset, 100);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Base64.getEncoder().encodeToString(readChunk);
	}

	public static OutputStream getFileOutputStream(String filePath) {
		OutputStream outputStream = null;
		File file = null;
		try {
			file = new File(filePath);
			outputStream = new FileOutputStream(file);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return outputStream;
	}

	public static InputStream getFileInputStrem(String filePath) {
		InputStream inputStream = null;
		File file = null;
		try {
			file = new File(filePath);
			inputStream = new FileInputStream(file);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return inputStream;
	}

	boolean moveFile(String source, String destination) {
		return false;
	}

	boolean deleteFile(String path) {
		return false;
	}

	boolean renameFile(String path, String newName) {
		return false;
	}

	boolean createDirectory(String path) {
		return false;
	}
}
