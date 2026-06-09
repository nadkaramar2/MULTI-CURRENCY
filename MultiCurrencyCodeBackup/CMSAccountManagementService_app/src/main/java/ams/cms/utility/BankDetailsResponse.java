package ams.cms.utility;

import java.io.Serializable;

public class BankDetailsResponse implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String strBankName;	
	private String strIfscCode;	
	private String strBranchName;	
	private String strBankLocation;
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
	public String getStrBankLocation() {
		return strBankLocation;
	}
	public void setStrBankLocation(String strBankLocation) {
		this.strBankLocation = strBankLocation;
	}	
}
