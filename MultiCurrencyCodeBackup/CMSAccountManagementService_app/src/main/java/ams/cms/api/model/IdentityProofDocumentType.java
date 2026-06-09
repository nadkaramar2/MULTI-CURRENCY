package ams.cms.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "identity_proof_document_type_master")
public class IdentityProofDocumentType implements Serializable
{
	private static final long serialVersionUID = 7818460562072935722L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "TEXT", name = "id", unique = true)
	private long strId;
	
	@Column(columnDefinition = "TEXT", name = "document_type")
	private String strDocumentType;
	
	@Column(columnDefinition = "TEXT", name = "document_description", unique = true)
	private String strDocumentDescription;
	
	@Transient
	private String montraId;
	
	public long getStrId() {
		return strId;
	}
	public void setStrId(long strId) {
		this.strId = strId;
	}
	public String getStrDocumentType() {
		return strDocumentType;
	}
	public void setStrDocumentType(String strDocumentType) {
		this.strDocumentType = strDocumentType;
	}
	public String getStrDocumentDescription() {
		return strDocumentDescription;
	}
	public void setStrDocumentDescription(String strDocumentDescription) {
		this.strDocumentDescription = strDocumentDescription;
	}
	public String getMontraId() {
		return montraId;
	}
	public void setMontraId(String montraId) {
		this.montraId = montraId;
	}
}
