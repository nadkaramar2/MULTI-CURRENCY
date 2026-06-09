package ams.cms.api.model;

import java.io.Serializable;
import java.util.Date;

public class UpgradeTierReqRes implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String strId;
	private String strCustId;
	private String strReqStatus;
	private String strRejectedReason;
	private String strTierType;
	private Date strReqDateTime;
	private Date strResDateTime;
	
	public String getStrId() {
		return strId;
	}
	public void setStrId(String strId) {
		this.strId = strId;
	}
	public String getStrTierType() {
		return strTierType;
	}
	public void setStrTierType(String strTierType) {
		this.strTierType = strTierType;
	}
	public String getStrCustId() {
		return strCustId;
	}
	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}
	public String getStrReqStatus() {
		return strReqStatus;
	}
	public void setStrReqStatus(String strReqStatus) {
		this.strReqStatus = strReqStatus;
	}
	public Date getStrReqDateTime() {
		return strReqDateTime;
	}
	public void setStrReqDateTime(Date strReqDateTime) {
		this.strReqDateTime = strReqDateTime;
	}
	public Date getStrResDateTime() {
		return strResDateTime;
	}
	public void setStrResDateTime(Date strResDateTime) {
		this.strResDateTime = strResDateTime;
	}
	
	public String getStrRejectedReason() {
		return strRejectedReason;
	}
	public void setStrRejectedReason(String strRejectedReason) {
		this.strRejectedReason = strRejectedReason;
	}
	@Override
	public String toString() {
		return "UpgradeTierReqRes [strId=" + strId + ", strCustId=" + strCustId + ", strReqStatus=" + strReqStatus
				+ ", strTierType=" + strTierType + ", strReqDateTime=" + strReqDateTime + ", strResDateTime="
				+ strResDateTime + "]";
	}
}
