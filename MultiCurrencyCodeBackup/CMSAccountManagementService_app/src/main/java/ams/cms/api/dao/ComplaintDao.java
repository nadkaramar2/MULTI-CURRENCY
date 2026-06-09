package ams.cms.api.dao;

import java.util.List;

import ams.cms.api.model.AccountRequest;
import ams.cms.api.model.Complaint;
import ams.cms.api.model.ComplaintDisplayRequest;
import ams.cms.dao.GenericDao;

//created by ankit
public interface ComplaintDao extends GenericDao<Complaint>{

	int checkIfComplaintExist();

	int getLastCompaintId();

	Complaint getLastComplaintIdAndDate();

	int resolveRaisedComplaint(Complaint complaint);

	List<Complaint> findComplaintById(Complaint complaint);

	Long getComplaintsPrimaryId(ComplaintDisplayRequest complaint);
	
	List<String> findByCreatedAccountNo(AccountRequest accountRequest);
}
