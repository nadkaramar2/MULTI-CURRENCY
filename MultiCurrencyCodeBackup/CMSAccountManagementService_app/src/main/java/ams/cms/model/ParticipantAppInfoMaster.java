package ams.cms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name = "participant_app_info")
public class ParticipantAppInfoMaster implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "participant_id")
	private String strParticipantId;
	
	@Column(name = "secret_key")
	private String strSecretKey;
	
	@Column(name = "api_key")
	private String strApiKey;
	
	@Column(name = "iv")
	private String strIv;
	
	@Column(name = "phrase")
	private String strPhrase;
	
	@Column(name = "salt")
	private String strSalt;
	
	@Column(name = "created_date")
	private Date strCreatedDate;
	
	@Transient
	private String strParticipantName;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getStrParticipantId() {
		return strParticipantId;
	}

	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}

	public String getStrSecretKey() {
		return strSecretKey;
	}

	public void setStrSecretKey(String strSecretKey) {
		this.strSecretKey = strSecretKey;
	}

	public String getStrApiKey() {
		return strApiKey;
	}

	public void setStrApiKey(String strApiKey) {
		this.strApiKey = strApiKey;
	}

	public String getStrIv() {
		return strIv;
	}

	public void setStrIv(String strIv) {
		this.strIv = strIv;
	}

	public String getStrPhrase() {
		return strPhrase;
	}

	public void setStrPhrase(String strPhrase) {
		this.strPhrase = strPhrase;
	}

	public Date getStrCreatedDate() {
		return strCreatedDate;
	}

	public void setStrCreatedDate(Date strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}

	public String getStrParticipantName() {
		return strParticipantName;
	}

	public void setStrParticipantName(String strParticipantName) {
		this.strParticipantName = strParticipantName;
	}

	public String getStrSalt() {
		return strSalt;
	}

	public void setStrSalt(String strSalt) {
		this.strSalt = strSalt;
	}

	@Override
	public String toString() {
		return "ParticipantAppInfoMaster [strID=" + strID + ", strParticipantId=" + strParticipantId + ", strSecretKey="
				+ strSecretKey + ", strApiKey=" + strApiKey + ", strIv=" + strIv + ", strPhrase=" + strPhrase
				+ ", strSalt=" + strSalt + ", strCreatedDate=" + strCreatedDate + ", strParticipantName="
				+ strParticipantName + "]";
	}	
}
