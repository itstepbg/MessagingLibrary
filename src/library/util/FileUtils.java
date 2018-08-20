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

import library.models.data.Directory;

public class FileUtils {

	public final static int FILE_READ_WRITE_BUFFER_SIZE = 512;
	public final static String EOF_TAG = "!EOF!";

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

	public static String readFromFile(InputStream inputStream) {
		int totalBytesRead = -1;
		byte[] readBuffer = new byte[FILE_READ_WRITE_BUFFER_SIZE];
		byte[] readChunk = new byte[] {};

		try {
			totalBytesRead = inputStream.read(readBuffer, 0, FILE_READ_WRITE_BUFFER_SIZE);
			if (totalBytesRead > -1) {
				if (totalBytesRead < FILE_READ_WRITE_BUFFER_SIZE) {
					readChunk = new byte[totalBytesRead];
					System.arraycopy(readBuffer, 0, readChunk, 0, totalBytesRead);
				} else {
					readChunk = readBuffer;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (totalBytesRead > 0) {
//			return Crypto.encryptAES256(new String(readChunk), initVector, key);
			return Base64.getEncoder().encodeToString(readChunk);
		} else {
			return FileUtils.EOF_TAG;
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
		File newDir = new File(path);
		if (!newDir.exists()) {
			newDir.mkdirs();
			return true;
		}
		return false;
	}

	public static boolean moveFile(String sourcePath, String targetPath) {
		Path newPath = null;
		if (!Files.exists(Paths.get(sourcePath)) || Files.exists(Paths.get(targetPath))) {
			return false;
		}
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
		// if source path doesn't exist
		// or the file that we are copying, already exists in the target directory
		if (!Files.exists(Paths.get(sourcePath)) || Files.exists(Paths.get(targetPath))) {
			return false;
		}
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

	public static Directory listFiles(String dir, Directory defaultDir) {
		File file = new File(dir);

		File[] listFiles = file.listFiles();

		for (File subFile : listFiles) {
			if (subFile.isDirectory()) {
				// add it to list directories and recur it
				Directory newDir = new Directory(subFile.getPath());
				defaultDir.addDirectory(newDir);
				if (subFile.listFiles().length != 0) {
					return listFiles(subFile.getPath(), defaultDir);
				}
			} else {
				File newFile = new File(subFile.getPath());
				defaultDir.addFile(newFile);
			}
		}
		return defaultDir;

	}
}
