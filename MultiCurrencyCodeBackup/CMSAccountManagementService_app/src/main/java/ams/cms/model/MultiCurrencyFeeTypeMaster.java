package ams.cms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonAutoDetect
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Entity
@DynamicUpdate
@Table(name = "multi_currency_fee_type_master")
public class MultiCurrencyFeeTypeMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "fee_type")
	private String  feeType;
	
	@Column(name = "participant_id")
	private String strParticipantId;
	
	@Column(name = "fee_description")
	private String  feeDescription;
	
	@Column(name = "is_flat_fee")
	private String  IsFlatFee;
	
	@Column(name = "fee_amt")
	private double  feeAmt;
	
	@Column(name = "is_percentage_fee")
	private String  isPercentageFee;
	
	@Column(name = "percentage_fee")
	private double  percentageFee;
	
	@Column(name = "gl_account_type")
	private String  glAccountType;
	
	@Column(name = "gl_account_number")
	private String  glAccountNumber;
	
	@Column(name = "gst_type")
	private String  gstType;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getFeeDescription() {
		return feeDescription;
	}

	public void setFeeDescription(String feeDescription) {
		this.feeDescription = feeDescription;
	}


	public String getIsFlatFee() {
		return IsFlatFee;
	}

	public void setIsFlatFee(String isFlatFee) {
		IsFlatFee = isFlatFee;
	}

	public double getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(double feeAmt) {
		this.feeAmt = feeAmt;
	}

	public String getIsPercentageFee() {
		return isPercentageFee;
	}

	public void setIsPercentageFee(String isPercentageFee) {
		this.isPercentageFee = isPercentageFee;
	}

	public double getPercentageFee() {
		return percentageFee;
	}

	public void setPercentageFee(double percentageFee) {
		this.percentageFee = percentageFee;
	}

	

	public String getGlAccountType() {
		return glAccountType;
	}

	public void setGlAccountType(String glAccountType) {
		this.glAccountType = glAccountType;
	}

	public String getGlAccountNumber() {
		return glAccountNumber;
	}

	public void setGlAccountNumber(String glAccountNumber) {
		this.glAccountNumber = glAccountNumber;
	}

	public String getGstType() {
		return gstType;
	}

	public void setGstType(String gstType) {
		this.gstType = gstType;
	}

	public String getStrParticipantId() {
		return strParticipantId;
	}

	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}

	
	
	

}
