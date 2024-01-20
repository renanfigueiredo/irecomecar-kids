package com.irecomecar.kids.model.connect;

import java.util.Objects;

import org.springframework.data.annotation.Id;

public class Connect {

	@Id
	private String id;
	private String name;
	private String birthDate;
	private String responsible;
	private String phone;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getResponsible() {
		return responsible;
	}
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Connect other = (Connect) obj;
		if(name != null) {
			name = name.toUpperCase();
		}
		if(other.name != null) {
			other.name = other.name.toUpperCase();
		}
		return Objects.equals(name, other.name);
	}
	@Override
	public String toString() {
		return "Connect [id=" + id + ", name=" + name + ", birthDate=" + birthDate + ", responsible=" + responsible
				+ ", phone=" + phone + "]";
	}
	
	
	
	
	
	
}
