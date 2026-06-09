package ams.cms.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ams.cms.util.JsonDateDeserializer;
import ams.cms.util.JsonDateSerializer;

@JsonAutoDetect
@Entity
@DynamicUpdate
@Table(name = "dormant_to_active")
public class DormantToActiveMaster implements Serializable
{	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id;
	
	@Column(name = "account_number")
	private String accountNumber;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "request_date")
	private Date requestDate;
	
	@Column(name = "request_time")
	private Time requestTime;
	
	@Column(name = "account_type")
	private String accountType;
	
	@Column(name = "dormant_marked_date")
	private Date dormantMarkedDate;
	
	@Column(name = "request_raised_by")
	private String requestRaisedBy;
	
	@Column(name = "reason_for_active")
	private String reasonForActive;
	
	@Column(name = "request_authorised_by")
	private String requestAuthorisedBy;	

	@Column(name = "request_authorised_date")
	private Date requestAuthorisedDate;
	
	@Column(name = "request_authorised_time")
	private Time requestAuthorisedTime;
	
	@Column(name = "request_authorised_reason")
	private String requestAuthorisedReason;
	
	@Column(name = "request_rejected_by")
	private String requestRejectedBy;
	
	@Column(name = "request_rejected_date")
	private Date requestRejectedDate;
	
	@Column(name = "request_rejected_time")
	private Time requestRejectedTime;
	
	@Column(name = "request_rejected_reason")
	private String requestRejectedReason;
	
	@Column(name = "status")
	private String status;
	
	public String getRequestRejectedReason() {
		return requestRejectedReason;
	}

	public void setRequestRejectedReason(String requestRejectedReason) {
		this.requestRejectedReason = requestRejectedReason;
	}	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Time getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Time requestTime) {
		this.requestTime = requestTime;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Date getDormantMarkedDate() {
		return dormantMarkedDate;
	}

	public void setDormantMarkedDate(Date dormantMarkedDate) {
		this.dormantMarkedDate = dormantMarkedDate;
	}

	public String getRequestRaisedBy() {
		return requestRaisedBy;
	}

	public void setRequestRaisedBy(String requestRaisedBy) {
		this.requestRaisedBy = requestRaisedBy;
	}

	public String getReasonForActive() {
		return reasonForActive;
	}

	public void setReasonForActive(String reasonForActive) {
		this.reasonForActive = reasonForActive;
	}

	public String getRequestAuthorisedBy() {
		return requestAuthorisedBy;
	}

	public void setRequestAuthorisedBy(String requestAuthorisedBy) {
		this.requestAuthorisedBy = requestAuthorisedBy;
	}

	public Date getRequestAuthorisedDate() {
		return requestAuthorisedDate;
	}

	public void setRequestAuthorisedDate(Date requestAuthorisedDate) {
		this.requestAuthorisedDate = requestAuthorisedDate;
	}

	public Time getRequestAuthorisedTime() {
		return requestAuthorisedTime;
	}

	public void setRequestAuthorisedTime(Time requestAuthorisedTime) {
		this.requestAuthorisedTime = requestAuthorisedTime;
	}

	public String getRequestAuthorisedReason() {
		return requestAuthorisedReason;
	}

	public void setRequestAuthorisedReason(String requestAuthorisedReason) {
		this.requestAuthorisedReason = requestAuthorisedReason;
	}

	public String getRequestRejectedBy() {
		return requestRejectedBy;
	}

	public void setRequestRejectedBy(String requestRejectedBy) {
		this.requestRejectedBy = requestRejectedBy;
	}

	public Date getRequestRejectedDate() {
		return requestRejectedDate;
	}

	public void setRequestRejectedDate(Date requestRejectedDate) {
		this.requestRejectedDate = requestRejectedDate;
	}

	public Time getRequestRejectedTime() {
		return requestRejectedTime;
	}

	public void setRequestRejectedTime(Time requestRejectedTime) {
		this.requestRejectedTime = requestRejectedTime;
	}	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
