package ams.cms.model;

import java.io.Serializable;

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
@Table(name = "transaction_type_master")
public class TransactionTypeModel implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "participant_id")
	private String strParticipantId;
	
	@Column(name = "txn_type_id")
	private String strTxnTypeId;
	
	@Column(name = "txn_type_description")
	private String strTxnTypeDescrp;
	
	@Column(name = "txn_type_keyword")
	private String strTxnTypeKeyWord;
	
	@Column(name = "processing_code")
	private String strProcessingCode;
	
	@Column(name = "gl_account_type")
	private String strGLAccountType;
	
	@Column(name = "gl_account_no")
	private String strGLAccountNumber;
	
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
	public String getStrTxnTypeId() {
		return strTxnTypeId;
	}
	public void setStrTxnTypeId(String strTxnTypeId) {
		this.strTxnTypeId = strTxnTypeId;
	}
	public String getStrTxnTypeDescrp() {
		return strTxnTypeDescrp;
	}
	public void setStrTxnTypeDescrp(String strTxnTypeDescrp) {
		this.strTxnTypeDescrp = strTxnTypeDescrp;
	}
	public String getStrTxnTypeKeyWord() {
		return strTxnTypeKeyWord;
	}
	public void setStrTxnTypeKeyWord(String strTxnTypeKeyWord) {
		this.strTxnTypeKeyWord = strTxnTypeKeyWord;
	}
	public String getStrProcessingCode() {
		return strProcessingCode;
	}
	public void setStrProcessingCode(String strProcessingCode) {
		this.strProcessingCode = strProcessingCode;
	}
	
	public String getStrGLAccountType() {
		return strGLAccountType;
	}
	public void setStrGLAccountType(String strGLAccountType) {
		this.strGLAccountType = strGLAccountType;
	}
	public String getStrGLAccountNumber() {
		return strGLAccountNumber;
	}
	public void setStrGLAccountNumber(String strGLAccountNumber) {
		this.strGLAccountNumber = strGLAccountNumber;
	}
	@Override
	public String toString() {
		return "TransactionTypeModel [strID=" + strID + ", strParticipantId=" + strParticipantId + ", strTxnTypeId="
				+ strTxnTypeId + ", strTxnTypeDescrp=" + strTxnTypeDescrp + ", strTxnTypeKeyWord=" + strTxnTypeKeyWord
				+ ", strProcessingCode=" + strProcessingCode + ", glAccountType=" + strGLAccountType + ", glAccountNo="
				+ strGLAccountNumber + "]";
	}
}
