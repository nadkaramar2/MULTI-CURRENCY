package ams.cms.api.model;

import java.io.Serializable;

public class NUBANAccountDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private String strAccountNo;
	private String strAccountType;
	private String strNubanType;
	private String strNubanCode;
	private String strParticipantId;
	
	public String getStrAccountNo() {
		return strAccountNo;
	}
	public void setStrAccountNo(String strAccountNo) {
		this.strAccountNo = strAccountNo;
	}
	public String getStrAccountType() {
		return strAccountType;
	}
	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}
	public String getStrNubanType() {
		return strNubanType;
	}
	public void setStrNubanType(String strNubanType) {
		this.strNubanType = strNubanType;
	}
	public String getStrParticipantId() {
		return strParticipantId;
	}
	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}
	public String getStrNubanCode() {
		return strNubanCode;
	}
	public void setStrNubanCode(String strNubanCode) {
		this.strNubanCode = strNubanCode;
	}
	
	@Override
	public String toString() {
		return "NUBANAccountDto [strAccountNo=" + strAccountNo + ", strAccountType=" + strAccountType
				+ ", strNubanType=" + strNubanType + ", strNubanCode=" + strNubanCode + ", strParticipantId="
				+ strParticipantId + "]";
	}
}
