package ams.cms.api.model;


import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "dynamic_qr")
public class DynamicQrGeneration {
	
	private static final long serialVersionUID = 5459581698798549309L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "qr_date")
	private Date qrDate;
	
	@Column(name = "qr_time")
	private Time qrTime;

	@Column(name = "from_account_type")
	private String fromAccountType;
	
	@Column(name = "from_account_number")
	private String fromAccountNumber;
	
	@Column(name = "amount")
	private String amount;
	
	@Column(name = "ref_no")
	private String refNo;
	
	@Column(name = "payment_received_date")
	private Date paymentReceivedDate;
	
	@Column(name = "payment_received_time")
	private Time paymentReceivedTime;
	
	@Column(name = "paid_by_account_type")
	private String paidByAccountType;
	
	@Column(name = "paid_by_account_number")
	private String paidByAccountNumber;
	
	@Column(name = "tran_id")
	private String tranId;
	
	@Column(name = "response_code")
	private String responseCode;
	
	@Column(name = "qr_code")
	private String qrCode;
	
	@Column(name = "qr_path")
	private String qrPath;
	
	@Column(name = "qr_code_image_url")
	private String strQrCodeImageUrl;
	
	@Transient
	private String qrData;
	
	@Transient
	private String Base64Image;
	
	@Transient
	private String actualBase64Img;
	
	@Transient
	private AccountMaster accountMaster;
	
	
	public String getBase64Image() {
		return Base64Image;
	}

	public void setBase64Image(String base64Image) {
		Base64Image = base64Image;
	}

	public String getQrData() {
		return qrData;
	}

	public void setQrData(String qrData) {
		this.qrData = qrData;
	}

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getFromAccountType() {
		return fromAccountType;
	}

	public void setFromAccountType(String fromAccountType) {
		this.fromAccountType = fromAccountType;
	}

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public Date getPaymentReceivedDate() {
		return paymentReceivedDate;
	}

	public void setPaymentReceivedDate(Date paymentReceivedDate) {
		this.paymentReceivedDate = paymentReceivedDate;
	}

	

	public Time getQrTime() {
		return qrTime;
	}

	public void setQrTime(Time qrTime) {
		this.qrTime = qrTime;
	}

	public Time getPaymentReceivedTime() {
		return paymentReceivedTime;
	}

	public void setPaymentReceivedTime(Time paymentReceivedTime) {
		this.paymentReceivedTime = paymentReceivedTime;
	}

	public String getPaidByAccountType() {
		return paidByAccountType;
	}

	public void setPaidByAccountType(String paidByAccountType) {
		this.paidByAccountType = paidByAccountType;
	}

	public String getPaidByAccountNumber() {
		return paidByAccountNumber;
	}

	public void setPaidByAccountNumber(String paidByAccountNumber) {
		this.paidByAccountNumber = paidByAccountNumber;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getQrPath() {
		return qrPath;
	}

	public void setQrPath(String qrPath) {
		this.qrPath = qrPath;
	}
	
	public Date getQrDate() {
		return qrDate;
	}

	public void setQrDate(Date qrDate) {
		this.qrDate = qrDate;
	}

	public String getActualBase64Img() {
		return actualBase64Img;
	}

	public void setActualBase64Img(String actualBase64Img) {
		this.actualBase64Img = actualBase64Img;
	}

	public String getStrQrCodeImageUrl() {
		return strQrCodeImageUrl;
	}

	public void setStrQrCodeImageUrl(String strQrCodeImageUrl) {
		this.strQrCodeImageUrl = strQrCodeImageUrl;
	}

	public AccountMaster getAccountMaster() {
		return accountMaster;
	}

	public void setAccountMaster(AccountMaster accountMaster) {
		this.accountMaster = accountMaster;
	}
}
