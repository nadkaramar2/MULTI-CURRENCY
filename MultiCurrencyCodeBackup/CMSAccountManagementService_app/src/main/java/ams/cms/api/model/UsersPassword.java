package ams.cms.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@DynamicUpdate
@Table(name = "users_passwords")
public class UsersPassword implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "mobile_no")
	private String strMobileNo;
	
	@Column(name = "password")
	private String strNewPassword;
	
	@Transient
	private String strOldPassword;

	@Column(name = "password_created_date")
	private Date strPasswordCreatedDate;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getStrMobileNo() {
		return strMobileNo;
	}

	public void setStrMobileNo(String strMobileNo) {
		this.strMobileNo = strMobileNo;
	}
	
	public Date getStrPasswordCreatedDate() {
		return strPasswordCreatedDate;
	}

	public void setStrPasswordCreatedDate(Date strPasswordCreatedDate) {
		this.strPasswordCreatedDate = strPasswordCreatedDate;
	}
	
	public String getStrNewPassword() {
		return strNewPassword;
	}

	public void setStrNewPassword(String strNewPassword) {
		this.strNewPassword = strNewPassword;
	}

	public String getStrOldPassword() {
		return strOldPassword;
	}

	public void setStrOldPassword(String strOldPassword) {
		this.strOldPassword = strOldPassword;
	}

	@Override
	public String toString() {
		return "UsersPassword [strID=" + strID + ", strMobileNo=" + strMobileNo + ", strPassword=" + strNewPassword + "]";
	}
	
	

}
