package library.models.data;

import java.net.InetAddress;

public class User {

	public final static String TABLE_NAME = "users";
	public final static String TABLE_NAME_SHARED_FILES = "shared_files";
	public final static String COLUMN_ID = "id";
	public final static String COLUMN_NAME = "name";
	public final static String COLUMN_PASSWORD_HASH = "passwordHash";
	public final static String COLUMN_EMAIL = "email";
	public final static String COLUMN_USER_SHARED_TO = "user_shared_to";
	public final static String COLUMN_USER_SHARED_FROM = "user_shared_from";
	public final static String COLUMN_FILE_NAME = "file_name";
	public final static String COLUMN_FILE_PATH = "file_path";

	private enum Status {
		OFFLINE, ONLINE, AWAY, INVISIBLE, DND
	}

	private long userId;

	private Status status;

	private String name;

	private String passwordHash;

	private String email;

	private InetAddress ipAddress;
	private String userNameSharedTo;
	private String filePathSharedFile;

	public User() {

	}

	// Created initialization constructor.
	public User(String name, String passwordHash, String email) {
		this.name = name;
		this.passwordHash = passwordHash;
		this.email = email;
	}

	public User(long id, String name, String passwordHash, String email) {
		this(name, passwordHash, email);
		this.userId = id;
	}

	public User(String userNameSharedTo, String filePathSharedFile) {
		this.setUserNameSharedTo(userNameSharedTo);
		this.setFilePathSharedFile(filePathSharedFile);

	}

	// Created field accessors.
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public InetAddress getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserNameSharedTo() {
		return userNameSharedTo;
	}

	public void setUserNameSharedTo(String userNameSharedTo) {
		this.userNameSharedTo = userNameSharedTo;
	}

	public String getFilePathSharedFile() {
		return filePathSharedFile;
	}

	public void setFilePathSharedFile(String filePathSharedFile) {
		this.filePathSharedFile = filePathSharedFile;
	}

}
