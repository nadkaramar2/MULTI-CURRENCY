package ams.cms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonAutoDetect
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Entity
@Table(name = "account_type_wise_tcs_lrs_master")
public class AcTypeLrsTcsMaster {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int strId;
	
	@Column(name = "participant_id")
	private String strParticipantId;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "channel_code")
	private String strChannelCode;
	
	@Column(name = "tcs")
	private double strTcs;
	
	@Column(name = "lrs")
	private double strLrs;
	
	@Column(name = "created_date")
	private Date strDate;
	
	@Column(name = "created_by")
	private String strCreatedby;
	
	@Transient
	private String strAccountNumber;

	public int getStrId() {
		return strId;
	}

	public void setStrId(int strId) {
		this.strId = strId;
	}

	public String getStrParticipantId() {
		return strParticipantId;
	}

	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrChannelCode() {
		return strChannelCode;
	}

	public void setStrChannelCode(String strChannelCode) {
		this.strChannelCode = strChannelCode;
	}

	public double getStrTcs() {
		return strTcs;
	}

	public void setStrTcs(double strTcs) {
		this.strTcs = strTcs;
	}

	public double getStrLrs() {
		return strLrs;
	}

	public void setStrLrs(double strLrs) {
		this.strLrs = strLrs;
	}

	public Date getStrDate() {
		return strDate;
	}

	public void setStrDate(Date strDate) {
		this.strDate = strDate;
	}

	public String getStrCreatedby() {
		return strCreatedby;
	}

	public void setStrCreatedby(String strCreatedby) {
		this.strCreatedby = strCreatedby;
	}

	public String getStrAccountNumber() {
		return strAccountNumber;
	}

	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}
	
	
}
