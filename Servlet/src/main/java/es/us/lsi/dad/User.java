package es.us.lsi.dad;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
	
	private String user;
	private String password;
	private String name;
	private String surname;
	private int age;
	private List<String> address;
	
	public User() {
		super();
		address = new ArrayList<String>();
	}

	public User(String user, String password, String name, String surname, int age, List<String> address) {
		super();
		this.user = user;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.age = age;
		this.address = address;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<String> getAddress() {
		return address;
	}

	public void setAddress(List<String> address) {
		this.address = address;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, age, name, password, surname, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(address, other.address) && age == other.age && Objects.equals(name, other.name)
				&& Objects.equals(password, other.password) && Objects.equals(surname, other.surname)
				&& Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "User [user=" + user + ", password=" + password + ", name=" + name + ", surname=" + surname + ", age="
				+ age + ", address=" + address + "]";
	}
	
	
	
	
}
