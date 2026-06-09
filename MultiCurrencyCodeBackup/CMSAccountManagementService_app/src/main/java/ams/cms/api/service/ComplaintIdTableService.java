package ams.cms.api.service;

import java.util.List;

import ams.cms.api.model.ComplaintIdTable;
//added by ankit
public interface ComplaintIdTableService {

	int updateComplaintIdDetails(ComplaintIdTable complaintIdTable);

	int saveComplaintIdDetails(ComplaintIdTable complaintIdTable);

	List<ComplaintIdTable> getComplaintIdList(String year, String julianDayFormat);

}
