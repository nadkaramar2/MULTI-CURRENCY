package ams.cms.api.model;

import java.io.Serializable;

public class TierUpdateDto implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String strBvnNo;
	private String strPin;
	private String strCustId;
	private String strUpgradeTier;
	
	//From montra Start
	private String secretCode;
	private String cid;
	//From montra End
	
	public String getStrBvnNo() {
		return strBvnNo;
	}
	public void setStrBvnNo(String strBvnNo) {
		this.strBvnNo = strBvnNo;
	}
	public String getStrPin() {
		return strPin;
	}
	public void setStrPin(String strPin) {
		this.strPin = strPin;
	}
	public String getStrCustId() {
		return strCustId;
	}
	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}
	public String getStrUpgradeTier() {
		return strUpgradeTier;
	}
	public void setStrUpgradeTier(String strUpgradeTier) {
		this.strUpgradeTier = strUpgradeTier;
	}
	public String getSecretCode() {
		return secretCode;
	}
	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
}
