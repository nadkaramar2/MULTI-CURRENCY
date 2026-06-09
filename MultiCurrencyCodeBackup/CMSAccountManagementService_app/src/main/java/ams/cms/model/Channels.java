package ams.cms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonAutoDetect
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Entity
@Table(name = "channels")
public class Channels implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "channel_id")
	private int strChannelId;
	
	@Column(name = "participant_id")
	private String strParticipantId;
	
	@Column(name = "channel_type")	
	private String strChannelType;
	
	@Column(name = "channel_description")
	private String strChannelDescription;
	
	@Column(name = "gl_account_number")
	private String glAccountNumber;
	
	@Column(name = "gl_account_type")
	private String glAccountType;

	
	
	@Column(name = "channel_code")
	private String channelCode;

	public int getStrChannelId() {
		return strChannelId;
	}

	public void setStrChannelId(int strChannelId) {
		this.strChannelId = strChannelId;
	}

	public String getStrChannelType() {
		return strChannelType;
	}

	public void setStrChannelType(String strChannelType) {
		this.strChannelType = strChannelType;
	}

	public String getStrChannelDescription() {
		return strChannelDescription;
	}

	public void setStrChannelDescription(String strChannelDescription) {
		this.strChannelDescription = strChannelDescription;
	}
	
	public String getStrParticipantId() {
		return strParticipantId;
	}
	
	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getGlAccountNumber() {
		return glAccountNumber;
	}

	public void setGlAccountNumber(String glAccountNumber) {
		this.glAccountNumber = glAccountNumber;
	}

	public String getGlAccountType() {
		return glAccountType;
	}

	public void setGlAccountType(String glAccountType) {
		this.glAccountType = glAccountType;
	}

	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
