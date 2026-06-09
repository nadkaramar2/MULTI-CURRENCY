package ams.cms.api.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.ComplaintDao;
import ams.cms.api.dao.PreAccountMasterDao;
import ams.cms.api.model.AccountRequest;
import ams.cms.api.model.Complaint;
import ams.cms.api.model.ComplaintDisplayRequest;
import ams.cms.api.model.PreAccountMaster;
import ams.cms.dao.generic.AbstractGenericDao;


//Created By ankit
@Repository
public class ComplaintDaoImpl extends AbstractGenericDao<Complaint> implements ComplaintDao{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public int checkIfComplaintExist() {
		try {
			String checkIfTableEmpty = "SELECT COUNT(*) FROM complaints";
			int checkData = jdbcTemplate.queryForObject(checkIfTableEmpty, Integer.class);
			if(checkData > 0) {
				return 1;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Complaint getLastComplaintIdAndDate() {
		String queryToGetLastRecordId = "SELECT id as strId, complaint_date as strComplaintDate FROM complaints ORDER BY ID DESC LIMIT 1";
		List<Complaint> data = jdbcTemplate.query(queryToGetLastRecordId,
				(RowMapper) new BeanPropertyRowMapper(Complaint.class));
		if(data.size()>0) {
			Complaint complaint = new Complaint();
			complaint.setStrId(data.get(0).getStrId());
			complaint.setStrComplaintCreationDate(data.get(0).getStrComplaintCreationDate());
			return complaint;
		}
		return null;
	}
	
	@Override
	public int getLastCompaintId() {
		String queryToGetLastRecordId = "SELECT id FROM complaints ORDER BY ID DESC LIMIT 1";
		Integer lastRecordId = jdbcTemplate.queryForObject(queryToGetLastRecordId, Integer.class);
		if(lastRecordId>0) {
			return lastRecordId;
		}
		return 0;
	}
	
	@Override
	public Long getComplaintsPrimaryId(ComplaintDisplayRequest complaint) {
		String strComplaintId = complaint.getStrComplaintId();
		System.out.println(strComplaintId);
		String getComplaintId = "SELECT id FROM complaints where complaint_id=?";
		Long complaintId = jdbcTemplate.queryForObject(getComplaintId,
				new Object[] {complaint.getStrComplaintId()},
				Long.class);
		if(complaintId>0) {
			return complaintId;
		}
		return complaintId;
	}
	
	@Override
	public int resolveRaisedComplaint(Complaint complaint) {
		try {
		String sql = "update complaints set complaint_resolved_date=?, complaint_resolved_by=?, complaint_status=?, complaint_solution=?, remarks=? where complaint_id=?";
		int update = jdbcTemplate.update(sql,
							new Object[] {complaint.getStrComplaintResolvedDate(),
									complaint.getStrComplaintResolvedBy(), complaint.getStrComplaintStatus(),
									complaint.getStrComplaintSolution(), complaint.getStrRemarks(),
									complaint.getStrComplaintId()
									});
		if(update>0) {
			return update;
		}
		return 0;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<Complaint> findComplaintById(Complaint complaint) {
		try {
			
			String getComplaint = "SELECT participant_id as strParticipantId "
					+ ", complaint_resolved_date as strComplaintResolvedDate,"
					+ " complaint_id as strComplaintId,"
					+ " complaint_type as strComplaintType,"
					+ " complaint_by as strComplaintBy,"
					+ " complaint_description as strComplaintDescription, "
					+ "complaint_status as strComplaintStatus, "
					+ "complaint_solution as strComplaintSolution,"
					+ "remarks as strRemarks, "
					+ "complaint_date as strComplaintCreationDate, "
					+ "complaint_resolved_by as strComplaintResolvedBy,"
					+ " FROM complaints WHERE complaint_id=?";
			List<Complaint> data = jdbcTemplate.query(getComplaint,
					new Object[] {complaint.getStrComplaintId()},
					(RowMapper) new BeanPropertyRowMapper(Complaint.class));
			if(data.size() > 0) {
				return data;
			}
			return null;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> findByCreatedAccountNo(AccountRequest accountRequest) 
	{
		try 
		{
			String sql = "SELECT complaint_id FROM complaints WHERE complaint_by = ?";
			List<String> complaintIdList = jdbcTemplate.queryForList(sql, String.class, 
			new Object[] 
			{
				accountRequest.getStrAccountNo()
			}
			);
			if(complaintIdList.size() > 0)
			{
				return complaintIdList;
			}
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}		
}


/*   complaint_type participant_id  complaint_resolved_date  complaint_id   complaint_by  complaint_description   complaint_status  complaint_solution   remarks   complaint_date   complaint_resolved_by
 * 
 * */


 