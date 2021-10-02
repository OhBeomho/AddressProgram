package program.address.datas;

import java.io.File;

public class Person {
	public static final File PERSONS_FILE = new File("C:/AddressProgram/persons/persons.txt");

	private String name, address, tel;
	private int age;

	public Person(String name, String address, String tel, int age) {
		this.name = name;
		this.address = address;
		this.tel = tel;
		this.age = age;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
