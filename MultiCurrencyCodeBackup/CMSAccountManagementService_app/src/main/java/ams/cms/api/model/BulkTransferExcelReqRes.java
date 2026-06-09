package ams.cms.api.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(content = Include.NON_NULL)
public class BulkTransferExcelReqRes implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Integer strId;
	private Double strAmount;
	
	private String strFromAccountNo;
	private String strFromAccountType;
	private String strFromAccountName;
	private String strToAccountNo;
	private String strToAccountType;
	private String strToAccountName;
	private String strBulkMode;
	private String strMakerId;
	private String strNarration;
	
	public String getStrFromAccountNo()
	{
		return strFromAccountNo;
	}

	public void setStrFromAccountNo(String strFromAccountNo)
	{
		this.strFromAccountNo = strFromAccountNo;
	}

	public String getStrFromAccountType()
	{
		return strFromAccountType;
	}

	public void setStrFromAccountType(String strFromAccountType)
	{
		this.strFromAccountType = strFromAccountType;
	}

	public String getStrToAccountNo()
	{
		return strToAccountNo;
	}

	public void setStrToAccountNo(String strToAccountNo)
	{
		this.strToAccountNo = strToAccountNo;
	}

	public String getStrToAccountType()
	{
		return strToAccountType;
	}

	public void setStrToAccountType(String strToAccountType)
	{
		this.strToAccountType = strToAccountType;
	}

	public Integer getStrId()
	{
		return strId;
	}

	public void setStrId(Integer strId)
	{
		this.strId = strId;
	}

	public Double getStrAmount()
	{
		return strAmount;
	}

	public void setStrAmount(Double strAmount)
	{
		this.strAmount = strAmount;
	}

	public String getStrFromAccountName()
	{
		return strFromAccountName;
	}

	public void setStrFromAccountName(String strFromAccountName)
	{
		this.strFromAccountName = strFromAccountName;
	}

	public String getStrToAccountName()
	{
		return strToAccountName;
	}

	public void setStrToAccountName(String strToAccountName)
	{
		this.strToAccountName = strToAccountName;
	}

	public String getStrBulkMode() {
		return strBulkMode;
	}

	public void setStrBulkMode(String strBulkMode) {
		this.strBulkMode = strBulkMode;
	}

	public String getStrMakerId() {
		return strMakerId;
	}

	public void setStrMakerId(String strMakerId) {
		this.strMakerId = strMakerId;
	}

	public String getStrNarration() {
		return strNarration;
	}

	public void setStrNarration(String strNarration) {
		this.strNarration = strNarration;
	}
}
