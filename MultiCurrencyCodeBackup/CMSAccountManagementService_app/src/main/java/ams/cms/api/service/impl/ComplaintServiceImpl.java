package ams.cms.api.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.ComplaintDao;
import ams.cms.api.dao.ComplaintTypeDao;
import ams.cms.api.model.AccountRequest;
import ams.cms.api.model.Complaint;
import ams.cms.api.model.ComplaintDisplayRequest;
import ams.cms.api.model.ComplaintRequest;
import ams.cms.api.model.ComplaintResolveRequest;
import ams.cms.api.model.ComplaintType;
import ams.cms.api.service.ComplaintService;
import ams.cms.api.utitlity.Utils;
import ams.cms.config.AppInfo;

//Created By ankit
@Transactional
@Service
public class ComplaintServiceImpl implements ComplaintService{

	@Autowired
	private ComplaintDao complaintDao;
	
	@Autowired
	private ComplaintTypeDao complaintTypeDao;
	
	 @Autowired
	 private AppInfo appInfo;
		
	@Override
	public int raiseComplaint(ComplaintRequest complaint) {
		try {
			Complaint complaintEntity = mapComplaintRequestToComplaintEntity(complaint);
			String generatedComplaintId = Utils.getGeneratedComplaintId();
			complaintEntity.setStrComplaintId(generatedComplaintId);
			Serializable checkId = complaintDao.save(complaintEntity);
			if(checkId.equals(0)) {
				return 0;
			}
			return 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public Complaint mapComplaintRequestToComplaintEntity(ComplaintRequest complaint){
		String strComplaintDescription = complaint.getStrComplaintDescription();
		String strComplaintType = complaint.getStrComplaintType();
		String strComplaintStatus = "Pending";
		String strAccountNo = complaint.getStrAccountNo();
		Complaint complaintEntity = new Complaint();
		complaintEntity.setStrComplaintType(strComplaintType);
		complaintEntity.setStrComplaintDescription(strComplaintDescription);
		complaintEntity.setStrComplaintCreationDate(new Date());
		complaintEntity.setStrComplaintBy(strAccountNo);
		complaintEntity.setStrComplaintStatus(strComplaintStatus);
		complaintEntity.setStrParticipantId(appInfo.getStrParticipantId());
		return complaintEntity;
	}
	
	@Override
	public List<Complaint> findAllComplaints() {
		try {
			List<Complaint> allComplaints = complaintDao.findAll();
			return allComplaints;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int resolveComplaint(ComplaintResolveRequest complaintResolve) {
		try {
			Complaint complaint = mapComplaintResolveRequestToComplaint(complaintResolve);
			int resolveRaisedComplaint = complaintDao.resolveRaisedComplaint(complaint);
			return resolveRaisedComplaint;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public Complaint mapComplaintResolveRequestToComplaint(ComplaintResolveRequest complaintResolve){
		String strComplaintId = complaintResolve.getStrComplaintId();
		String strComplaintSolution = complaintResolve.getStrComplaintSolution();
		String strRemarks = complaintResolve.getStrRemarks();
		String strComplaintResolvedBy = complaintResolve.getStrComplaintResolvedBy();
		String strComplaintStatus = "Success";
		Date date = new Date();
		Complaint complaint = new Complaint();
		complaint.setStrComplaintResolvedDate(date);
		complaint.setStrComplaintId(strComplaintId);
		complaint.setStrComplaintResolvedBy(strComplaintResolvedBy);
		complaint.setStrComplaintStatus(strComplaintStatus);
		complaint.setStrRemarks(strRemarks);
		complaint.setStrComplaintSolution(strComplaintSolution);
		return complaint;
	}
	@Override
	public Complaint findComplaintByComplaintId(ComplaintDisplayRequest complaint) {
		long complaintsPrimaryId = complaintDao.getComplaintsPrimaryId(complaint);
		Complaint complaintData = complaintDao.findById(complaintsPrimaryId);
		return complaintData;
	}
	
	@Override
	public List<ComplaintType> findAllComplaintTypes() {
		try{
			List<ComplaintType> allComplaintTypes = complaintTypeDao.findAll();
			return allComplaintTypes;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public List<String> getAllComplaintsOfAccount(AccountRequest accountRequest) 
	{
		List<String> findByCreatedAccountNo = complaintDao.findByCreatedAccountNo(accountRequest);
		return findByCreatedAccountNo;
	}


	@Override
	public List<String> findAllComplaintBasedOnType(AccountRequest accountRequest)
	{
		List<String> findAllByComplaintType = complaintTypeDao.findAllByComplaintType(accountRequest);
		return findAllByComplaintType;
	}
	
	
}
