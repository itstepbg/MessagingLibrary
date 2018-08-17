package library.models.network;

public enum MessageType {

	SERVER_HELLO, CLIENT_HELLO, WELCOME_MESSAGE, REGISTER_PLAIN, CONTINUE_WITH_PASS, REGISTER_PASS, REGISTRATION_ALLOWED,
	REGISTRATION_FAILED, AUTHENTICATION_FAILED,
	SALT, CREATE_USER, LOGIN, LOGOUT, CREATE_GROUP, INVITE_USER, JOIN_GROUP, CHAT_MESSAGE,
	LEAVE_GROUP, REMOVE_USER, DISBAND_GROUP, STATUS_RESPONSE, HEARTBEAT, UPLOAD_FILE, UPLOAD_CHUNK, MOVE_FILE, COPY_FILE,
	DELETE_FILE, RENAME_FILE, SHARE_FILE, DOWNLOAD_FILE, CREATE_DIRECTORY, LIST_FILES, LIST_FILES_SHARED_BY_YOU,
	LIST_FILES_SHARED_WITH_YOU, GAMEPLAY;

}