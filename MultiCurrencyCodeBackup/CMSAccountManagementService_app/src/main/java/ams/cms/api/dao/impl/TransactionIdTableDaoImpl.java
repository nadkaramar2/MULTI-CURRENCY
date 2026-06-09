package ams.cms.api.dao.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.TransactionIdTableDao;
import ams.cms.api.model.TransactionIdTable;
/*
 * Note : -sessions are not implemented so Setting "System" as LoginName as Statically inside Queries
 * */
@Repository
public class TransactionIdTableDaoImpl implements TransactionIdTableDao{

	@Autowired
	JdbcTemplate jdbcCMSTemplate;
	
	public int saveTransactionIdDetails(TransactionIdTable transactionIdDetails){
		int count = this.jdbcCMSTemplate.update(
				"INSERT INTO transaction_id_table (year,julian_date,last_txn_serial_no,created_date,created_by) VALUES (?,?,?,?,?)",
				new Object[] { transactionIdDetails.getStrYear(), transactionIdDetails.getStrJulianDate(),transactionIdDetails.getStrLastTxnSerialNo(),transactionIdDetails.getStrCreatedDate(),"System" });
		return count;
	}
	
	public int updateTransactionIdDetails(TransactionIdTable transactionIdDetails) {
		int count = jdbcCMSTemplate.update("UPDATE transaction_id_table "
				+ "SET last_txn_serial_no = ?, created_date = ?, created_by = ? "
				+ "WHERE year = ? and julian_date = ?",
				new Object[]
				{
						transactionIdDetails.getStrLastTxnSerialNo(),
						transactionIdDetails.getStrCreatedDate(),
						"System",
						transactionIdDetails.getStrYear(), 
						transactionIdDetails.getStrJulianDate()
				});
		return count;
	}
	
	public List<TransactionIdTable> getTransactionIdTableList(String year,String julianDate){
		String sql = "SELECT id AS strID, year AS strYear,"
				+ "julian_date AS strJulianDate,last_txn_serial_no AS strLastTxnSerialNo "
				+ "from transaction_id_table "
				+ "where year = ? AND julian_date = ? ORDER BY id DESC LIMIT 1";
		List<TransactionIdTable> getTransactionIdTableList = jdbcCMSTemplate.query(sql,
				new BeanPropertyRowMapper<TransactionIdTable>(TransactionIdTable.class), new Object[] {year,julianDate});
        return getTransactionIdTableList;
	}
}
