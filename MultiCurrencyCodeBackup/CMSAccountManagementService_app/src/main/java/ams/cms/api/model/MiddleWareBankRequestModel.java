package ams.cms.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@DynamicUpdate
@Table(name = "middleware_app_info")
public class MiddleWareBankRequestModel implements Serializable
{	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "participant_id")
	private String participantId;
	
	@Column(name = "app_id")
	private String appId;
	
	@Column(name = "audit_id")
	private String auditId;
	
	@Column(name = "channel_code")
	private String channelCode;
	
	@Column(name = "user_loginid")
	private String userLoginId;
	
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "initial_value")
	private String strInitialValue;
	
	@Column(name = "year")
	private String strYear;
	
	@Column(name = "julian_date")
	private String strJulianDate;
	
	@Column(name = "last_audit_ser_no")
	private String strLastAuditSerNo;
	
	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getUserLoginId() {
		return userLoginId;
	}

	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}

	public String getStrInitialValue() {
		return strInitialValue;
	}

	public void setStrInitialValue(String strInitialValue) {
		this.strInitialValue = strInitialValue;
	}

	public String getStrYear() {
		return strYear;
	}

	public void setStrYear(String strYear) {
		this.strYear = strYear;
	}

	public String getStrJulianDate() {
		return strJulianDate;
	}

	public void setStrJulianDate(String strJulianDate) {
		this.strJulianDate = strJulianDate;
	}

	public String getStrLastAuditSerNo() {
		return strLastAuditSerNo;
	}

	public void setStrLastAuditSerNo(String strLastAuditSerNo) {
		this.strLastAuditSerNo = strLastAuditSerNo;
	}

	@Override
	public String toString() {
		return "MiddleWareBankRequestModel [strID=" + strID + ", participantId=" + participantId + ", appId=" + appId
				+ ", auditId=" + auditId + ", channelCode=" + channelCode + ", userLoginId=" + userLoginId
				+ ", createdDate=" + createdDate + ", strInitialValue=" + strInitialValue + ", strYear=" + strYear
				+ ", strJulianDate=" + strJulianDate + ", strLastAuditSerNo=" + strLastAuditSerNo + "]";
	}
}
