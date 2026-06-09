package ams.cms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@DynamicUpdate
@Table(name = "bank_details")
public class BankDetails implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "bank_name")
	private String strBankName;
	
	@Column(name = "ifsc_code")
	private String strIfscCode;
	
	@Column(name = "branch_name")
	private String strBranchName;
	
	@Column(name = "bank_address")
	private String strBankAddress;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getStrBankName() {
		return strBankName;
	}

	public void setStrBankName(String strBankName) {
		this.strBankName = strBankName;
	}

	public String getStrIfscCode() {
		return strIfscCode;
	}

	public void setStrIfscCode(String strIfscCode) {
		this.strIfscCode = strIfscCode;
	}

	public String getStrBranchName() {
		return strBranchName;
	}

	public void setStrBranchName(String strBranchName) {
		this.strBranchName = strBranchName;
	}

	public String getStrBankAddress() {
		return strBankAddress;
	}

	public void setStrBankAddress(String strBankAddress) {
		this.strBankAddress = strBankAddress;
	}	
}
