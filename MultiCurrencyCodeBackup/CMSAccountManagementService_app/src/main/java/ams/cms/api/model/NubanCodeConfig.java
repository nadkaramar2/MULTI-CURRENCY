package ams.cms.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nuban_code_config")
public class NubanCodeConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int strId;
	
	@Column(name = "nuban_type")
    private String strNubanType;
	
	@Column(name = "nuban_code")
    private String strNubanCode;
	
	@Column(name = "nuban_serial_no")
    private String strNubanSerialNo;
	
	@Column(name = "participant_id")
    private String strParticipantId;
	
	@Column(name = "created_date")
    private Date strCreatedDate;
	
	public int getStrId() {
		return strId;
	}
	public void setStrId(int strId) {
		this.strId = strId;
	}
	public String getStrNubanType() {
		return strNubanType;
	}
	public void setStrNubanType(String strNubanType) {
		this.strNubanType = strNubanType;
	}
	public String getStrNubanCode() {
		return strNubanCode;
	}
	public void setStrNubanCode(String strNubanCode) {
		this.strNubanCode = strNubanCode;
	}
	public String getStrNubanSerialNo() {
		return strNubanSerialNo;
	}
	public void setStrNubanSerialNo(String strNubanSerialNo) {
		this.strNubanSerialNo = strNubanSerialNo;
	}
	public String getStrParticipantId() {
		return strParticipantId;
	}
	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}
	public Date getStrCreatedDate() {
		return strCreatedDate;
	}
	public void setStrCreatedDate(Date strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}
	@Override
	public String toString() {
		return "NubanCodeConfig [strId=" + strId + ", strNubanType=" + strNubanType + ", strNubanCode=" + strNubanCode
				+ ", strNubanSerialNo=" + strNubanSerialNo + ", strParticipantId=" + strParticipantId
				+ ", strCreatedDate=" + strCreatedDate + "]";
	}
}
