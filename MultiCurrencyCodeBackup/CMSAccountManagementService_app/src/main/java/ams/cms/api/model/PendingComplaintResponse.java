package ams.cms.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//created by ankit
public class PendingComplaintResponse implements Serializable{

	private static final long serialVersionUID = -8729460736562504374L;

	private String strComplaintId;
	
	private String strComplaintType;
	
	private String strComplaintDescription;
	
	private String strComplaintStatus;

	public String getStrComplaintId() {
		return strComplaintId;
	}

	public void setStrComplaintId(String strComplaintId) {
		this.strComplaintId = strComplaintId;
	}

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

	public String getStrComplaintStatus() {
		return strComplaintStatus;
	}

	public void setStrComplaintStatus(String strComplaintStatus) {
		this.strComplaintStatus = strComplaintStatus;
	}
	
}
