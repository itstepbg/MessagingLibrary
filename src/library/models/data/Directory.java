package library.models.data;

import java.util.List;

public class Directory {
	private final String name;
	private final List<Directory> directories;
	private final List<String> files;

	public Directory(String name, List<Directory> directories, List<String> files) {
		this.name = name;
		this.directories = directories;
		this.files = files;
	}

}
