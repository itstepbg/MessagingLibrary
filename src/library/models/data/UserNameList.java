package library.models.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "userNames")
class UserNameList {

	@XmlElement(name = "userName")
	private List<String> userNames;

	public UserNameList() {
		userNames = new ArrayList<String>();
	}

	public List<String> getUserNames() {
		return userNames;
	}

	public void setUserNames(List<String> userNames) {
		this.userNames = userNames;
	}

	public void addUserName(String userName) {
		this.userNames.add(userName);
	}

	public void addUserNames(List<String> userNames) {
		this.userNames.addAll(userNames);
	}
}
