package ams.cms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name = "merchant_category_code_master")
public class MerchantCategoryCodeMaster implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "mcc_code")
	private String strMccCode;
	
	@Column(name = "mcc_desc")
	private String strMccCodeDesc;
	
	@Transient
	private String strParticipantID;
	
	public String getStrMccCode() {
		return strMccCode;
	}

	public void setStrMccCode(String strMccCode) {
		this.strMccCode = strMccCode;
	}

	public String getStrMccCodeDesc() {
		return strMccCodeDesc;
	}

	public void setStrMccCodeDesc(String strMccCodeDesc) {
		this.strMccCodeDesc = strMccCodeDesc;
	}

	public String getStrParticipantID() {
		return strParticipantID;
	}
	
	public void setStrParticipantID(String strParticipantID) {
		this.strParticipantID = strParticipantID;
	}
}
