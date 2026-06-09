package ams.cms.api.model;

import java.io.Serializable;
//created by ankit
public class ComplaintIdTable implements Serializable {

	private static final long serialVersionUID = 1L;
	private String strID;
	private String strYear;
	private String strJulianDate;
	private String strLastComplaintSerialNo;
	private String strCreatedDate;
	private String strCreatedBy;
	public String getStrID() {
		return strID;
	}
	public void setStrID(String strID) {
		this.strID = strID;
	}
	public String getStrYear() {
		return strYear;
	}
	public void setStrYear(String strYear) {
		this.strYear = strYear;
	}
	public String getStrJulianDate() {
		return strJulianDate;
	}
	public void setStrJulianDate(String strJulianDate) {
		this.strJulianDate = strJulianDate;
	}
	public String getStrLastComplaintSerialNo() {
		return strLastComplaintSerialNo;
	}
	public void setStrLastComplaintSerialNo(String strLastComplaintSerialNo) {
		this.strLastComplaintSerialNo = strLastComplaintSerialNo;
	}
	public String getStrCreatedDate() {
		return strCreatedDate;
	}
	public void setStrCreatedDate(String strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}
	public String getStrCreatedBy() {
		return strCreatedBy;
	}
	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}
}