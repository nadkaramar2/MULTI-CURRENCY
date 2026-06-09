package ams.cms.api.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.ComplaintIdTableDao;
import ams.cms.api.model.ComplaintIdTable;
/*
 * created by ankit
*/
@Repository
public class ComplaintIdTableDaoImpl implements ComplaintIdTableDao {

	@Autowired
	JdbcTemplate jdbcCMSTemplate;
	
	@Override
	public int saveComplaintIdDetails(ComplaintIdTable complaintIdDetails){
		int count = this.jdbcCMSTemplate.update(
				"INSERT INTO complaint_id_table (year,julian_date,last_complaint_serial_no,created_date,created_by) VALUES (?,?,?,?,?)",
				new Object[] { complaintIdDetails.getStrYear(), complaintIdDetails.getStrJulianDate(),complaintIdDetails.getStrLastComplaintSerialNo(),complaintIdDetails.getStrCreatedDate(),"xyz" });
		return count;
		
	}
	
	@Override
	public int updateComplaintIdDetails(ComplaintIdTable complaintIdDetails) {
		int count = jdbcCMSTemplate.update("UPDATE complaint_id_table "
				+ "SET last_complaint_serial_no = ?, created_date = ?, created_by = ? "
				+ "WHERE year = ? and julian_date = ?",
				new Object[]
				{
					complaintIdDetails.getStrLastComplaintSerialNo(),
					complaintIdDetails.getStrCreatedDate(),
						"System", 
						complaintIdDetails.getStrYear(), 
						complaintIdDetails.getStrJulianDate()
				});
		return count;
	}
	
	@Override
	public List<ComplaintIdTable> getComplaintIdTableList(String year,String julianDate){
		String sql = "SELECT id AS strID, year AS strYear,"
				+ "julian_date AS strJulianDate,last_complaint_serial_no AS strLastComplaintSerialNo "
				+ "from complaint_id_table "
				+ "where year = ? AND julian_date = ? ORDER BY id DESC LIMIT 1";
		List<ComplaintIdTable> getTransactionIdTableList = jdbcCMSTemplate.query(sql,
				new BeanPropertyRowMapper<ComplaintIdTable>(ComplaintIdTable.class), new Object[] {year,julianDate});
        return getTransactionIdTableList;
	}
}
