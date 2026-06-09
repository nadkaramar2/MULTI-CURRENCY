package ams.cms.api.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

//@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class CardDetailsRequest implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String strCardNo;
	private String strTokenCard;
	private String strCustId;
	private String strPIN;
	public String getStrCardNo() 
	{
		return strCardNo;
	}
	public void setStrCardNo(String strCardNo) 
	{
		this.strCardNo = strCardNo;
	}
	public String getStrTokenCard() {
		return strTokenCard;
	}
	public void setStrTokenCard(String strTokenCard) {
		this.strTokenCard = strTokenCard;
	}
	public String getStrCustId() {
		return strCustId;
	}
	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}
	public String getStrPIN() {
		return strPIN;
	}
	public void setStrPIN(String strPIN) {
		this.strPIN = strPIN;
	}
}
