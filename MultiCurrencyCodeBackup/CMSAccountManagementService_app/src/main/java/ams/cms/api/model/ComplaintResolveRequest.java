package ams.cms.api.model;

import java.io.Serializable;
//created By ankit
public class ComplaintResolveRequest implements Serializable{

	private static final long serialVersionUID = 4717995362046594617L;
	private String strComplaintId;
	private String strComplaintSolution;
	private String strRemarks;
	private String strComplaintResolvedBy;
	public String getStrComplaintId() {
		return strComplaintId;
	}
	public void setStrComplaintId(String strComplaintId) {
		this.strComplaintId = strComplaintId;
	}
	public String getStrComplaintSolution() {
		return strComplaintSolution;
	}
	public void setStrComplaintSolution(String strComplaintSolution) {
		this.strComplaintSolution = strComplaintSolution;
	}
	public String getStrRemarks() {
		return strRemarks;
	}
	public void setStrRemarks(String strRemarks) {
		this.strRemarks = strRemarks;
	}
	public String getStrComplaintResolvedBy() {
		return strComplaintResolvedBy;
	}
	public void setStrComplaintResolvedBy(String strComplaintResolvedBy) {
		this.strComplaintResolvedBy = strComplaintResolvedBy;
	}
}
