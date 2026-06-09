package ams.cms.utility;


public class ComplaintRequest {

	private String strComplaintType;
	
	private String strComplaintDescription;

	public String getStrComplaintType() {
		return strComplaintType;
	}

	public void setStrComplaintType(String strComplaintType) {
		this.strComplaintType = strComplaintType;
	}

	public String getStrComplaintDescription() {
		return strComplaintDescription;
	}

	public void setStrComplaintDescription(String strComplaintDescription) {
		this.strComplaintDescription = strComplaintDescription;
	}

	@Override
	public String toString() {
		return "ComplaintRequest [strComplaintType=" + strComplaintType + ", strComplaintDescription="
				+ strComplaintDescription + "]";
	}

	public ComplaintRequest(String strComplaintType, String strComplaintDescription) {
		super();
		this.strComplaintType = strComplaintType;
		this.strComplaintDescription = strComplaintDescription;
	}

	public ComplaintRequest() {
		super();
	}
	
	
	
}
