package library.util;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	
	File openFile(String path, boolean write) {
		return null;
	}
	
	void writeToFile(OutputStream outputStream, String base64) {
	}
	
	String readFromFile(InputStream inputStream) {
		return null;
	}
	
	OutputStream getFileOutputStream(File file) {
		return null;
	}
	
	InputStream getFileInputStrem(File file) {
		return null;
	}
	
	byte[] decodeBase64(String base64) {
		return null;
	}
	
	String encodeBase64(byte[] byteArray) {
		return null;
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
