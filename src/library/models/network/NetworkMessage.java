package library.models.network;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NetworkMessage {

	public static final int STATUS_OK = 0;
	public static final int STATUS_ERROR_CREATING_USER = 1;
	public static final int STATUS_ERROR_LOGGING_IN = 2;

	private MessageType type;
	private String actor;
	private String user;
	private Long groupId;
	private String text;
	private String passwordHash;
	private String email;
	private int status;

	public void setStatus(int status) {
		this.status = status;
	}

	public void setType(MessageType type) {
		this.type = type;
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

	public NetworkMessage() {

	}

	public NetworkMessage(String type, String actor, String user, Long groupId, String text, String passwordHash,
			String email, int status) {

		this.type = MessageType.valueOf(type.toUpperCase());
		this.actor = actor;
		this.user = user;
		this.groupId = groupId;
		this.text = text;
		this.passwordHash = passwordHash;
		this.email = email;
	}

	public MessageType getType() {
		return type;
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
}
