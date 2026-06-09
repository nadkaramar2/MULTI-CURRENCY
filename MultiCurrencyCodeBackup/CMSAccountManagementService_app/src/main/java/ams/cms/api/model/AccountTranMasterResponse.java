package ams.cms.api.model;

import java.io.Serializable;

public class AccountTranMasterResponse implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String strTxn_id;
	
	private String strSys_id;
	
	private String strTran_type;
	
	private String strAccountType;

	private String strTransaction_amount;
	
	private String strLocal_tran_time;
	
	private String strLocal_tran_date;
	
	private String strFrom_account_number;
	
	private String strTo_account_number;
	
	private String strClosingBalance;
	
	private String strSwitchTxnDate;


	public String getStrTxn_id() {
		return strTxn_id;
	}

	public void setStrTxn_id(String strTxn_id) {
		this.strTxn_id = strTxn_id;
	}

	public String getStrSys_id() {
		return strSys_id;
	}

	public void setStrSys_id(String strSys_id) {
		this.strSys_id = strSys_id;
	}

	public String getStrTransaction_amount() {
		return strTransaction_amount;
	}

	public void setStrTransaction_amount(String strTransaction_amount) {
		this.strTransaction_amount = strTransaction_amount;
	}

	public String getStrLocal_tran_time() {
		return strLocal_tran_time;
	}

	public void setStrLocal_tran_time(String strLocal_tran_time) {
		this.strLocal_tran_time = strLocal_tran_time;
	}

	public String getStrLocal_tran_date() {
		return strLocal_tran_date;
	}

	public void setStrLocal_tran_date(String strLocal_tran_date) {
		this.strLocal_tran_date = strLocal_tran_date;
	}

	public String getStrFrom_account_number() {
		return strFrom_account_number;
	}

	public void setStrFrom_account_number(String strFrom_account_number) {
		this.strFrom_account_number = strFrom_account_number;
	}

	public String getStrTo_account_number() {
		return strTo_account_number;
	}

	public void setStrTo_account_number(String strTo_account_number) {
		this.strTo_account_number = strTo_account_number;
	}

	public String getStrClosingBalance() {
		return strClosingBalance;
	}

	public void setStrClosingBalance(String strClosingBalance) {
		this.strClosingBalance = strClosingBalance;
	}
	
	public String getStrSwitchTxnDate() {
		return strSwitchTxnDate;
	}
	public void setStrSwitchTxnDate(String strSwitchTxnDate) {
		this.strSwitchTxnDate = strSwitchTxnDate;
	}
	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrTran_type() {
		return strTran_type;
	}

	public void setStrTran_type(String strTran_type) {
		this.strTran_type = strTran_type;
	}
	
}
