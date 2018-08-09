package library.models.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Directory {
	private String name;
	private List<Directory> directories;
	private List<File> files;

	public Directory(String name, ArrayList<Directory> directories, ArrayList<File> files) {
		this.name = name;
		this.directories = directories;
		this.files = files;
	}

	public Directory(String name) {
		this.name = name;
		this.directories = new ArrayList<Directory>();
		this.files = new ArrayList<File>();
	}

	public void addFile(File file) {
		files.add(file);
	}

	public void addDirectory(Directory dir) {
		directories.add(dir);
	}

	public List<Directory> getDirectories() {
		return directories;
	}

	public List<File> getFiles() {
		return files;
	}

	@Override
	public String toString() {
		return "Directory [name=" + name + ", directories=" + directories + ", files=" + files + "]";
	}

	public void printDirectories() {
		for (Directory dir : directories) {
			System.out.println(dir);
		}
	}

	public void printFiles() {
		for (File file : files) {
			System.out.println(file);
		}
	}

	public String getName() {
		return name;
	}
}
