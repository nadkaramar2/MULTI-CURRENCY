package ams.cms.api.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(content = Include.NON_EMPTY,value = Include.NON_NULL)
@JsonAutoDetect
public class AccountInfoResponse implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String accountType;
	private String accountNo;
	private String accountBalance;
	private String accountName;
	private String mccCode;
	
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getMccCode() {
		return mccCode;
	}
	public void setMccCode(String mccCode) {
		this.mccCode = mccCode;
	}	
}
