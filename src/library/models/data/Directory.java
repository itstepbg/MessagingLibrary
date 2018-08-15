package library.models.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class Directory implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 7063738432593073790L;
	@XmlElement
	private String name;
	private List<Directory> directories;
	private List<File> files;
	private Map<String, UserNameList> fileShareMap;
	private UserNameList userNames;

	public Directory() {
		this.name = "";
		this.directories = new ArrayList<Directory>();
		this.files = new ArrayList<File>();
		this.fileShareMap = new HashMap<String, UserNameList>();
	}

	public Directory(String name) {
		this.name = name;
		this.directories = new ArrayList<Directory>();
		this.files = new ArrayList<File>();
	}

	public Directory(String name, ArrayList<Directory> directories, ArrayList<File> files) {
		this.name = name;
		this.directories = directories;
		this.files = files;
	}

	public void addFile(File file) {
		files.add(file);
	}

	public void addDirectory(Directory dir) {
		directories.add(dir);
	}

	@XmlElementWrapper
	@XmlElement(name = "directiories")
	public List<Directory> getDirectories() {
		return directories;
	}

	@XmlElementWrapper
	@XmlElement(name = "files")
	public List<File> getFiles() {
		return files;
	}

	public void printDirectories() {
		for (Directory dir : directories) {
			System.out.println(dir);
			for (File file : dir.getFiles()) {
				System.out.println(file);
			}

		}
	}

	@Override
	public String toString() {
		return name;
	}

	public void printFiles() {
		for (File file : files) {
			System.out.println(file);
		}
	}

	public String getName() {
		return name;
	}

	public void putInFileShareMap(String file, UserNameList userNames) {
		fileShareMap.put(file, userNames);
	}

	public void addUserToShareWith(String file, String userName) {
		// check if the map doesn't contain mapping for this file
		if (!fileShareMap.containsKey(file)) {
			userNames = new UserNameList();
		}
		if (!userNames.getUserNames().contains(userName)) {
			userNames.addUserName(userName);
		}
		putInFileShareMap(file, userNames);
	}

	public void printFilesSharedByYou() {
		for (Map.Entry<String, UserNameList> entry : this.fileShareMap.entrySet()) {
			String key = entry.getKey();
			UserNameList value = entry.getValue();
			System.out.println("File " + key + " is shared with: ");
			for (String name : value.getUserNames()) {
				System.out.print(name + " ");
			}
			System.out.println();
		}
	}

	public void printFilesSharedWithYou() {
		for (Map.Entry<String, UserNameList> entry : this.fileShareMap.entrySet()) {
			String key = entry.getKey();
			UserNameList value = entry.getValue();
			System.out.println("File " + key + " is shared with you by: ");
			for (String name : value.getUserNames()) {
				System.out.print(name + " ");
			}
			System.out.println();
		}
	}

	@XmlElementWrapper
	@XmlElement(name = "fileShareWith")
	public Map<String, UserNameList> getFileShareMap() {
		return fileShareMap;
	}
}