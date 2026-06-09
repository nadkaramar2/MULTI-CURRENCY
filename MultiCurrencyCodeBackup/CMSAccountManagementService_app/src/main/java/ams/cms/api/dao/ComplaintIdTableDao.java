package ams.cms.api.dao;

import java.util.List;

//created By ankit
import ams.cms.api.model.ComplaintIdTable;

public interface ComplaintIdTableDao {

	int saveComplaintIdDetails(ComplaintIdTable complaintIdTable);

	List<ComplaintIdTable> getComplaintIdTableList(String year, String julianDate);

	int updateComplaintIdDetails(ComplaintIdTable complaintIdTable);

}
