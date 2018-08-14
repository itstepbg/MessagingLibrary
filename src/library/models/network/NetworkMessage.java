package library.models.network;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NetworkMessage {

	public static final int STATUS_OK = 0;
	public static final int STATUS_ERROR_CREATING_USER = 1;
	public static final int STATUS_ERROR_LOGGING_IN = 2;
	public static final int STATUS_ERROR_CREATING_FILE = 3;

	private MessageType type;
	private Long messageId;
	private String clientFQDN;
	private String salt;
	private int iterations;
	private String initVector;
	private String actor;
	private String user;
	private Long groupId;
	private String text;
	private String password;
	private String email;
	private String filePath;
	private String newFilePath;
	private Integer status;

	public NetworkMessage() {

	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public void setMessageId(Long messageId) {
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

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public MessageType getType() {
		return type;
	}

	public Long getMessageId() {
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

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public Integer getStatus() {
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

	public String getClientFQDN() {
		return clientFQDN;
	}

	public void setClientFQDN(String clientFQDN) {
		this.clientFQDN = clientFQDN;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt2) {
		this.salt = salt2;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int encodedIterations) {
		this.iterations = encodedIterations;
	}

	public String getInitVector() {
		return initVector;
	}

	public void setInitVector(String initVector) {
		this.initVector = initVector;
	}
}
