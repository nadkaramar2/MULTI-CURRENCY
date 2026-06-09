package ams.cms.model;

import java.io.Serializable;

public class CustomerIdMap implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	private String strCustID;
	private String strYear;
	private String strJulianDate;
	private String strAction;
	
	public String getStrCustID() {
		return strCustID;
	}
	public void setStrCustID(String strCustID) {
		this.strCustID = strCustID;
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
	public String getStrAction() {
		return strAction;
	}
	public void setStrAction(String strAction) {
		this.strAction = strAction;
	}

}
