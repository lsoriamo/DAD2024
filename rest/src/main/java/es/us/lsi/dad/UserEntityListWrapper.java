package es.us.lsi.dad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserEntityListWrapper {
	private List<UserEntity> userList;

	public UserEntityListWrapper() {
		super();
	}

	public UserEntityListWrapper(Collection<UserEntity> userList) {
		super();
		this.userList = new ArrayList<UserEntity>(userList);
	}
	
	public UserEntityListWrapper(List<UserEntity> userList) {
		super();
		this.userList = new ArrayList<UserEntity>(userList);
	}

	public List<UserEntity> getUserList() {
		return userList;
	}

	public void setUserList(List<UserEntity> userList) {
		this.userList = userList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userList == null) ? 0 : userList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserEntityListWrapper other = (UserEntityListWrapper) obj;
		if (userList == null) {
			if (other.userList != null)
				return false;
		} else if (!userList.equals(other.userList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserEntityListWrapper [userList=" + userList + "]";
	}

}
