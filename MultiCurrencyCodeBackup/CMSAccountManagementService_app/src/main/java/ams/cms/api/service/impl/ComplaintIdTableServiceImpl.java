package ams.cms.api.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.ComplaintIdTableDao;
import ams.cms.api.model.ComplaintIdTable;
import ams.cms.api.service.ComplaintIdTableService;

//added by ankit
@Transactional
@Service
public class ComplaintIdTableServiceImpl implements ComplaintIdTableService{

	@Autowired
	private ComplaintIdTableDao complaintIdTableDao;
	
	@Override
	public int  saveComplaintIdDetails(ComplaintIdTable complaintIdTable){
		int count = complaintIdTableDao.saveComplaintIdDetails(complaintIdTable);

		return count;
	}
	
	public List<ComplaintIdTable> getComplaintIdList(String year,String julianDate){
		return complaintIdTableDao.getComplaintIdTableList(year, julianDate);
	}
	
	public int updateComplaintIdDetails(ComplaintIdTable complaintIdTable) {
		return complaintIdTableDao.updateComplaintIdDetails(complaintIdTable);
	}

		
}
