package ams.cms.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ams.cms.util.JsonDateDeserializer;
import ams.cms.util.JsonDateSerializer;

public class AccountCreationResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String strParticipantID;
	
	private String strAccountType;
	
	private String strAccountNumber;
	
	private String strTitle;
	
	private String strFirstName;
	
	private String strMiddleName;
	
	private String strLastName;

	private String strEmailID;
	
	private String strMobileNo;
	
	private String strAddress1;
	
	private String strAddress2;

	private String strAddress3;
	
	private String strPinCode;
	
	private String strCity;
	
	private String strState;
	
//	private String strAccountIssueDate;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date strAccountIssueDate;
		
	private String strLastloginDate;

	public String getStrParticipantID() {
		return strParticipantID;
	}

	public void setStrParticipantID(String strParticipantID) {
		this.strParticipantID = strParticipantID;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrAccountNumber() {
		return strAccountNumber;
	}

	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}

	public String getStrTitle() {
		return strTitle;
	}

	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
	}

	public String getStrFirstName() {
		return strFirstName;
	}

	public void setStrFirstName(String strFirstName) {
		this.strFirstName = strFirstName;
	}

	public String getStrMiddleName() {
		return strMiddleName;
	}

	public void setStrMiddleName(String strMiddleName) {
		this.strMiddleName = strMiddleName;
	}

	public String getStrLastName() {
		return strLastName;
	}

	public void setStrLastName(String strLastName) {
		this.strLastName = strLastName;
	}

	public String getStrEmailID() {
		return strEmailID;
	}

	public void setStrEmailID(String strEmailID) {
		this.strEmailID = strEmailID;
	}

	public String getStrMobileNo() {
		return strMobileNo;
	}

	public void setStrMobileNo(String strMobileNo) {
		this.strMobileNo = strMobileNo;
	}

	public String getStrAddress1() {
		return strAddress1;
	}

	public void setStrAddress1(String strAddress1) {
		this.strAddress1 = strAddress1;
	}

	public String getStrAddress2() {
		return strAddress2;
	}

	public void setStrAddress2(String strAddress2) {
		this.strAddress2 = strAddress2;
	}

	public String getStrAddress3() {
		return strAddress3;
	}

	public void setStrAddress3(String strAddress3) {
		this.strAddress3 = strAddress3;
	}

	public String getStrPinCode() {
		return strPinCode;
	}

	public void setStrPinCode(String strPinCode) {
		this.strPinCode = strPinCode;
	}

	public String getStrCity() {
		return strCity;
	}

	public void setStrCity(String strCity) {
		this.strCity = strCity;
	}

	public String getStrState() {
		return strState;
	}

	public void setStrState(String strState) {
		this.strState = strState;
	}

	public Date getStrAccountIssueDate() {
		return strAccountIssueDate;
	}

	public void setStrAccountIssueDate(Date strAccountIssueDate) {
		this.strAccountIssueDate = strAccountIssueDate;
	}

	public void setStrLastloginDate(String strLastloginDate) {
		this.strLastloginDate = strLastloginDate;
	}

	public String getStrLastloginDate() {
		return strLastloginDate;
	}

	
}
