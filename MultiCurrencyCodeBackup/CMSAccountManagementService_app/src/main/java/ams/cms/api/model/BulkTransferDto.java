package ams.cms.api.model;

import java.util.ArrayList;
import java.util.List;

public class BulkTransferDto
{
	private int id;
	private Double strTransferAmount;
	private Double strTransactionAmount;
	
	private String strPreTransactionId;
	private String strTransactionId;
	private String strToAccountNo;
	private String strFromAccountNo;
	private String strToAccountType;
	private String strFromAccountType;	
	
	private String strMakerId;
	private String strIsVerified;
	
	private List<BulkTransferFromAccounts> bulkTransferFromAccounts = new ArrayList<>();	
	private List<BulkTransferToAccounts> bulkTransferToAccounts = new ArrayList<>();
	private List<BulkTransferExcelReqRes> strListBulkTransferExcelReqRes = new ArrayList<BulkTransferExcelReqRes>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStrTransactionId() {
		return strTransactionId;
	}
	public void setStrTransactionId(String strTransactionId) {
		this.strTransactionId = strTransactionId;
	}
	public String getStrToAccountNo() {
		return strToAccountNo;
	}
	public void setStrToAccountNo(String strToAccountNo) {
		this.strToAccountNo = strToAccountNo;
	}
	public String getStrFromAccountNo() {
		return strFromAccountNo;
	}
	public void setStrFromAccountNo(String strFromAccountNo) {
		this.strFromAccountNo = strFromAccountNo;
	}
	public String getStrToAccountType() {
		return strToAccountType;
	}
	public void setStrToAccountType(String strToAccountType) {
		this.strToAccountType = strToAccountType;
	}
	public String getStrFromAccountType() {
		return strFromAccountType;
	}
	public void setStrFromAccountType(String strFromAccountType) {
		this.strFromAccountType = strFromAccountType;
	}
	
	public Double getStrTransferAmount() {
		return strTransferAmount;
	}
	public void setStrTransferAmount(Double strTransferAmount) {
		this.strTransferAmount = strTransferAmount;
	}
	public String getStrPreTransactionId() {
		return strPreTransactionId;
	}
	public void setStrPreTransactionId(String strPreTransactionId) {
		this.strPreTransactionId = strPreTransactionId;
	}
	
	public List<BulkTransferToAccounts> getBulkTransferToAccounts() {
		return bulkTransferToAccounts;
	}
	public void setBulkTransferToAccounts(List<BulkTransferToAccounts> bulkTransferToAccounts) {
		this.bulkTransferToAccounts = bulkTransferToAccounts;
	}
	public List<BulkTransferFromAccounts> getBulkTransferFromAccounts() {
		return bulkTransferFromAccounts;
	}
	public void setBulkTransferFromAccounts(List<BulkTransferFromAccounts> bulkTransferFromAccounts) {
		this.bulkTransferFromAccounts = bulkTransferFromAccounts;
	}
	public List<BulkTransferExcelReqRes> getStrListBulkTransferExcelReqRes() {
		return strListBulkTransferExcelReqRes;
	}
	public void setStrListBulkTransferExcelReqRes(List<BulkTransferExcelReqRes> strListBulkTransferExcelReqRes) {
		this.strListBulkTransferExcelReqRes = strListBulkTransferExcelReqRes;
	}
	public String getStrMakerId() {
		return strMakerId;
	}
	public void setStrMakerId(String strMakerId) {
		this.strMakerId = strMakerId;
	}
	public Double getStrTransactionAmount() {
		return strTransactionAmount;
	}
	public void setStrTransactionAmount(Double strTransactionAmount) {
		this.strTransactionAmount = strTransactionAmount;
	}
	public String getStrIsVerified() {
		return strIsVerified;
	}
	public void setStrIsVerified(String strIsVerified) {
		this.strIsVerified = strIsVerified;
	}
}
