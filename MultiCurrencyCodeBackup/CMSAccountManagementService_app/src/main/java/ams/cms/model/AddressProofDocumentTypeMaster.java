package ams.cms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name = "address_proof_document_type_master")
public class AddressProofDocumentTypeMaster implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int strId;
	
	@Column(name = "participant_id")
	private String strParticipantId;
	
	@Column(name = "document_type")
	private String strDocumentType;
	
	@Column(name = "document_description")
	private String strDocumentDescr;

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

	public String getStrDocumentType() {
		return strDocumentType;
	}

	public void setStrDocumentType(String strDocumentType) {
		this.strDocumentType = strDocumentType;
	}
	
	public String getStrDocumentDescr() {
		return strDocumentDescr;
	}
	public void setStrDocumentDescr(String strDocumentDescr) {
		this.strDocumentDescr = strDocumentDescr;
	}
}
