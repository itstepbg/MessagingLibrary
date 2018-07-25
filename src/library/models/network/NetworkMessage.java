package library.models.network;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NetworkMessage {

	public static final int STATUS_OK = 0;
	public static final int STATUS_ERROR_CREATING_USER = 1;
	public static final int STATUS_ERROR_LOGGING_IN = 2;
	public static final int STATUS_ERROR_CREATING_FILE = 3;

	private MessageType type;
	private long messageId;
	private String actor;
	private String user;
	private Long groupId;
	private String text;
	private String passwordHash;
	private String email;
	private String filePath;
	private String newFilePath;
	private int status;

	public NetworkMessage() {

	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public MessageType getType() {
		return type;
	}

	public long getMessageId() {
		return messageId;
	}

	public String getActor() {
		return actor;
	}

	public String getUser() {
		return user;
	}

	public Long getGroupId() {
		return groupId;
	}

	public String getText() {
		return text;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public String getEmail() {
		return email;
	}

	public int getStatus() {
		return status;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getNewFilePath() {
		return newFilePath;
	}

	public void setNewFilePath(String newFilePath) {
		this.newFilePath = newFilePath;
	}
}
