package ams.cms.utility;

import java.io.Serializable;

public class ThirdPartyRequestParam implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String bankIFSC;
	private String bankName;
	
	private String custId;
	private String custAccountType;	
	private String custAccountNo;
	private String custPIN;
	private String txnType;
	private String txnAmount;
	
	private String beneficiaryBankName;
	private String beneficiaryBankIFSC;
	private String beneficiaryBankAccountNo;
	private String beneficiaryName;
	private String beneficiaryVPAId;
	
	private String beneficiaryBankCode;
	private String beneficiaryAccountNo;
	private String beneficiaryInstitutionCode;
	private String beneficiaryCurrencyCode;
	private String authCode;
	
	private String montraTxnId;
	private String pGtxnId;
	private String secretCode;
	private String amsTxnId;
	
	private String cid;
	private String bid;
	
	private String feeType;
	private String feeApplicableTo;
	private String fee;
	private String vat;
	
	private String entityInfo;
	private String entityNumber;
	
	public String getBankIFSC() {
		return bankIFSC;
	}
	public void setBankIFSC(String bankIFSC) {
		this.bankIFSC = bankIFSC;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCustAccountType() {
		return custAccountType;
	}
	public void setCustAccountType(String custAccountType) {
		this.custAccountType = custAccountType;
	}
	public String getCustAccountNo() {
		return custAccountNo;
	}
	public void setCustAccountNo(String custAccountNo) {
		this.custAccountNo = custAccountNo;
	}
	public String getCustPIN() {
		return custPIN;
	}
	public void setCustPIN(String custPIN) {
		this.custPIN = custPIN;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}
	public String getBeneficiaryBankName() {
		return beneficiaryBankName;
	}
	public void setBeneficiaryBankName(String beneficiaryBankName) {
		this.beneficiaryBankName = beneficiaryBankName;
	}
	public String getBeneficiaryBankIFSC() {
		return beneficiaryBankIFSC;
	}
	public void setBeneficiaryBankIFSC(String beneficiaryBankIFSC) {
		this.beneficiaryBankIFSC = beneficiaryBankIFSC;
	}
	public String getBeneficiaryBankAccountNo() {
		return beneficiaryBankAccountNo;
	}
	public void setBeneficiaryBankAccountNo(String beneficiaryBankAccountNo) {
		this.beneficiaryBankAccountNo = beneficiaryBankAccountNo;
	}
	public String getBeneficiaryVPAId() {
		return beneficiaryVPAId;
	}
	public void setBeneficiaryVPAId(String beneficiaryVPAId) {
		this.beneficiaryVPAId = beneficiaryVPAId;
	}
	public String getBeneficiaryName() {
		return beneficiaryName;
	}
	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}
	public String getBeneficiaryBankCode() {
		return beneficiaryBankCode;
	}
	public void setBeneficiaryBankCode(String beneficiaryBankCode) {
		this.beneficiaryBankCode = beneficiaryBankCode;
	}
	public String getBeneficiaryAccountNo() {
		return beneficiaryAccountNo;
	}
	public void setBeneficiaryAccountNo(String beneficiaryAccountNo) {
		this.beneficiaryAccountNo = beneficiaryAccountNo;
	}
	public String getMontraTxnId() {
		return montraTxnId;
	}
	public void setMontraTxnId(String montraTxnId) {
		this.montraTxnId = montraTxnId;
	}
	public String getpGtxnId() {
		return pGtxnId;
	}
	public void setpGtxnId(String pGtxnId) {
		this.pGtxnId = pGtxnId;
	}
	public String getSecretCode() {
		return secretCode;
	}
	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}
	public String getAmsTxnId() {
		return amsTxnId;
	}
	public void setAmsTxnId(String amsTxnId) {
		this.amsTxnId = amsTxnId;
	}
	public String getBeneficiaryInstitutionCode() {
		return beneficiaryInstitutionCode;
	}
	public void setBeneficiaryInstitutionCode(String beneficiaryInstitutionCode) {
		this.beneficiaryInstitutionCode = beneficiaryInstitutionCode;
	}
	public String getBeneficiaryCurrencyCode() {
		return beneficiaryCurrencyCode;
	}
	public void setBeneficiaryCurrencyCode(String beneficiaryCurrencyCode) {
		this.beneficiaryCurrencyCode = beneficiaryCurrencyCode;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getFeeApplicableTo() {
		return feeApplicableTo;
	}
	public void setFeeApplicableTo(String feeApplicableTo) {
		this.feeApplicableTo = feeApplicableTo;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getVat() {
		return vat;
	}
	public void setVat(String vat) {
		this.vat = vat;
	}
	public String getEntityInfo() {
		return entityInfo;
	}
	public void setEntityInfo(String entityInfo) {
		this.entityInfo = entityInfo;
	}
	public String getEntityNumber() {
		return entityNumber;
	}
	public void setEntityNumber(String entityNumber) {
		this.entityNumber = entityNumber;
	}	
}
