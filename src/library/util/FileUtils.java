package library.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class FileUtils {

	public final static int FILE_READ_WRITE_BUFFER_SIZE = 512;
	public final static String EOF_TAG = "</EOF>";

	// TODO Read and write should be done through file channels.
	public static void writeToFile(OutputStream outputStream, String base64) {
		byte[] decodedBytes = Base64.getDecoder().decode(base64);

		try {
			outputStream.write(decodedBytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String readFromFile(InputStream inputStream, int offset) {
		int totalBytesRead = -1;
		byte[] readChunk = null;
		try {
			totalBytesRead = inputStream.read(readChunk, offset, FILE_READ_WRITE_BUFFER_SIZE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (totalBytesRead > -1) {
			return Base64.getEncoder().encodeToString(readChunk);
		} else {
			return null;
		}
	}

	public static OutputStream getFileOutputStream(String filePath) {
		OutputStream outputStream = null;
		File file = null;
		try {
			file = new File(filePath);
			outputStream = new FileOutputStream(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return inputStream;
	}

	public static boolean createDirectory(String path) {
		Path newDir = null;
		try {
			newDir = Files.createDirectories(Paths.get(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newDir != null;
	}

	public static boolean moveFile(String sourcePath, String targetPath) {
		Path newPath = null;
		try {
			newPath = Files.move(Paths.get(sourcePath), Paths.get(targetPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newPath != null;
	}

	public static boolean renameFile(String path, String newPath) {
		File file = new File(path);
		return file.renameTo(new File(newPath));
	}

	public static boolean copyFile(String sourcePath, String targetPath) {
		Path newPath = null;
		try {
			newPath = Files.copy(Paths.get(sourcePath), Paths.get(targetPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newPath != null;
	}

	public static boolean deleteFile(String path) {
		boolean success = false;
		try {
			Files.delete(Paths.get(path));
			success = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	public static int calculateOriginalNumberOfBytes(String base64) {
		int result = -1;
		if (base64 != null & !base64.equals("")) {
			int padding = 0;
			if (base64.endsWith("==")) {
				padding = 2;
			} else {
				if (base64.endsWith("=")) {
					padding = 1;
				}
			}
			result = (((int) (Math.ceil(base64.length() / 4) * 3)) - padding) * 3;
		}
		return result;
	}
}
