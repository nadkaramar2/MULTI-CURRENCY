package ams.cms.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
//created by ankit
@Entity
@Table(name = "complaints")
public class Complaint implements Serializable {
	private static final long serialVersionUID = -1291622401761687699L;
	
	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "TEXT", name = "id", unique = true)
	private long strId;
	
	@Column(columnDefinition = "TEXT", name = "participant_id")
	private String strParticipantId;
    
	@JsonIgnore
	@Column(columnDefinition = "TEXT", name = "complaint_id", unique = true)
	private String strComplaintId;
	
	@Column(columnDefinition = "TEXT", name = "complaint_type")
	private String strComplaintType;
	
	@Column(columnDefinition = "TEXT", name = "complaint_description")
	private String strComplaintDescription;
	
	@Column(columnDefinition = "TEXT", name = "complaint_by")
	private String strComplaintBy;
	
	@Column(columnDefinition = "TEXT", name = "complaint_status")
	private String strComplaintStatus;
	
	@Column(columnDefinition = "TEXT", name = "complaint_solution")
	private String strComplaintSolution;
	
	@Column(columnDefinition = "TEXT", name = "remarks")
	private String strRemarks;
	
	@Column(name = "complaint_date")
	private Date strComplaintCreationDate;
	
	@Column(name = "complaint_resolved_date")
	private Date strComplaintResolvedDate;
	
	@Column(name = "complaint_resolved_by")
	private String strComplaintResolvedBy;
	
	public long getStrId() {
		return strId;
	}

	public void setStrId(long strId) {
		this.strId = strId;
	}

	public String getStrComplaintId() {
		return strComplaintId;
	}

	public void setStrComplaintId(String strComplaintId) {
		this.strComplaintId = strComplaintId;
	}

	public String getStrComplaintType() {
		return strComplaintType;
	}

	public void setStrComplaintType(String strComplaintType) {
		this.strComplaintType = strComplaintType;
	}

	public String getStrComplaintDescription() {
		return strComplaintDescription;
	}

	public void setStrComplaintDescription(String strComplaintDescription) {
		this.strComplaintDescription = strComplaintDescription;
	}

	public String getStrComplaintStatus() {
		return strComplaintStatus;
	}
	public void setStrComplaintStatus(String strComplaintStatus) {
		this.strComplaintStatus = strComplaintStatus;
	}
	public String getStrComplaintSolution() {
		return strComplaintSolution;
	}
	public void setStrComplaintSolution(String strComplaintSolution) {
		this.strComplaintSolution = strComplaintSolution;
	}
	public String getStrRemarks() {
		return strRemarks;
	}
	public void setStrRemarks(String strRemarks) {
		this.strRemarks = strRemarks;
	}
	public Date getStrComplaintCreationDate() {
		return strComplaintCreationDate;
	}
	public void setStrComplaintCreationDate(Date strComplaintCreationDate) {
		this.strComplaintCreationDate = strComplaintCreationDate;
	}
	public Date getStrComplaintResolvedDate() {
		return strComplaintResolvedDate;
	}
	public void setStrComplaintResolvedDate(Date strComplaintResolvedDate) {
		this.strComplaintResolvedDate = strComplaintResolvedDate;
	}
	public String getStrComplaintBy() {
		return strComplaintBy;
	}
	public void setStrComplaintBy(String strComplaintBy) {
		this.strComplaintBy = strComplaintBy;
	}
	public String getStrParticipantId() {
		return strParticipantId;
	}
	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}
	public String getStrComplaintResolvedBy() {
		return strComplaintResolvedBy;
	}
	public void setStrComplaintResolvedBy(String strComplaintResolvedBy) {
		this.strComplaintResolvedBy = strComplaintResolvedBy;
	}
}
