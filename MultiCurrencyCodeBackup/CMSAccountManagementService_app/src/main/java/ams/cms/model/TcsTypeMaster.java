package ams.cms.model;

import java.sql.Date;

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
@Table(name = "tcs_type_master")
public class TcsTypeMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id;
	
	@Column(name = "participant_id")
	private String participantId;
	
	@Column(name = "tcs_type")
	private String tcsType;
	
	@Column(name = "tcs_description")
	private String tcsDescription;
	
	@Column(name = "gl_account_type")
	private String glAccountType;
	
	@Column(name = "gl_account_number")
	private String glAccountNumber;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private String createdBy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public String getTcsType() {
		return tcsType;
	}

	public void setTcsType(String tcsType) {
		this.tcsType = tcsType;
	}

	public String getTcsDescription() {
		return tcsDescription;
	}

	public void setTcsDescription(String tcsDescription) {
		this.tcsDescription = tcsDescription;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	

}
