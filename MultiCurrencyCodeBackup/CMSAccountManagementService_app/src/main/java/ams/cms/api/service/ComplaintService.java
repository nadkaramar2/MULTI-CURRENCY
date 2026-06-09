package ams.cms.api.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import ams.cms.api.model.AccountRequest;
import ams.cms.api.model.Complaint;
import ams.cms.api.model.ComplaintDisplayRequest;
import ams.cms.api.model.ComplaintRequest;
import ams.cms.api.model.ComplaintResolveRequest;
import ams.cms.api.model.ComplaintType;
import ams.cms.api.model.ResponseModel;

//created by ankit
public interface ComplaintService {

	List<Complaint> findAllComplaints();

	int raiseComplaint(ComplaintRequest complaint);

	int resolveComplaint(ComplaintResolveRequest complaintResolve);

	Complaint findComplaintByComplaintId(ComplaintDisplayRequest complaint);

	List<ComplaintType> findAllComplaintTypes();
	
	List<String> getAllComplaintsOfAccount(AccountRequest accountRequest);
	
	List<String> findAllComplaintBasedOnType(AccountRequest accountRequest);
}
