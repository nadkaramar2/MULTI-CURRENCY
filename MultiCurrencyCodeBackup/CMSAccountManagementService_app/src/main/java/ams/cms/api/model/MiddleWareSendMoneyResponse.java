package ams.cms.api.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import ams.cms.utility.BankObjectResponse;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class MiddleWareSendMoneyResponse implements Serializable
{
	private static final long serialVersionUID = 1L;

	@JsonProperty("resp_code")
	private String strRespCode;
	
	@JsonProperty("resp_desc")
	private String strRespDesc;
	
	@JsonProperty("tranid")
	private String strTranId;
	
	@JsonProperty("referencenumber")
	private String strRefrenceNumber;
	
	@JsonProperty("obj")
	private BankObjectResponse obj;
	
	public String getStrRespCode() 
	{
		return strRespCode;
	}
	public void setStrRespCode(String strRespCode) {
		this.strRespCode = strRespCode;
	}
	public String getStrRespDesc() {
		return strRespDesc;
	}
	public void setStrRespDesc(String strRespDesc) {
		this.strRespDesc = strRespDesc;
	}
	public String getStrTranId() {
		return strTranId;
	}
	public void setStrTranId(String strTranId) {
		this.strTranId = strTranId;
	}
	public String getStrRefrenceNumber() {
		return strRefrenceNumber;
	}
	public void setStrRefrenceNumber(String strRefrenceNumber) {
		this.strRefrenceNumber = strRefrenceNumber;
	}
	public BankObjectResponse getObj() {
		return obj;
	}
	public void setObj(BankObjectResponse obj) {
		this.obj = obj;
	}
	
	@Override
	public String toString() {
		return "MiddleWareSendMoneyResponse [strRespCode=" + strRespCode + ", strRespDesc=" + strRespDesc
				+ ", strTranId=" + strTranId + ", strRefrenceNumber=" + strRefrenceNumber + ", obj=" + obj + "]";
	}
}
