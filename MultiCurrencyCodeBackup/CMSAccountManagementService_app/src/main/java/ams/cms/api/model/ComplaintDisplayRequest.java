package ams.cms.api.model;

import java.io.Serializable;
//created by ankit
public class ComplaintDisplayRequest implements Serializable{
	
	private static final long serialVersionUID = -2886626697974976407L;
	public String strComplaintId;
	public String getStrComplaintId() {
		return strComplaintId;
	}
	public void setStrComplaintId(String strComplaintId) {
		this.strComplaintId = strComplaintId;
	}
}
