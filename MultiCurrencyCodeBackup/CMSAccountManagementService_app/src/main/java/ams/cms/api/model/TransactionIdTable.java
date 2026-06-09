package ams.cms.api.model;

import java.io.Serializable;
//copy pasted -- from cms.model by ankit
public class TransactionIdTable implements Serializable{
	private static final long serialVersionUID = 1240654731930540553L;
	private String strID;
	private String strYear;
	private String strJulianDate;
	private String strLastTxnSerialNo;
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
	public String getStrLastTxnSerialNo() {
		return strLastTxnSerialNo;
	}
	public void setStrLastTxnSerialNo(String strLastTxnSerialNo) {
		this.strLastTxnSerialNo = strLastTxnSerialNo;
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
