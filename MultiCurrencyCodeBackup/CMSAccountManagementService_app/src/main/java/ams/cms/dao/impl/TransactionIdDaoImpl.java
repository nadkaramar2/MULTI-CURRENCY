package ams.cms.dao.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.TransactionIdDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.TransactionIdTable;
import ams.cms.utility.Utils;

@Repository
public class TransactionIdDaoImpl extends AbstractGenericDao<TransactionIdTable> implements TransactionIdDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(TransactionIdDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<TransactionIdTable> getTransactionIdList(String year, String julianDate) 
	{
		StringBuilder selectSb = new StringBuilder("SELECT id AS strID, year AS strYear, julian_date AS strJulianDate, last_txn_serial_no AS strLastTxnSerialNo ");
		selectSb.append("from transaction_id_table where year = ? AND julian_date = ? ORDER BY id DESC ");
		
		List<TransactionIdTable> getTransactionIdTableList = jdbcTemplate.query(selectSb.toString(), new BeanPropertyRowMapper<TransactionIdTable>(TransactionIdTable.class), new Object[] 
				{
					year, 
					julianDate
				});
        return getTransactionIdTableList;
	}

	@Override
	public int updateTransactionIdDetails(TransactionIdTable transactionIdDetails) 
	{
		String updateQuery = "UPDATE transaction_id_table SET last_txn_serial_no = ?, created_date = ? WHERE year = ? and julian_date = ?";
		int count = jdbcTemplate.update(updateQuery,
				new Object[]
				{
						transactionIdDetails.getStrLastTxnSerialNo(),
						transactionIdDetails.getStrCreatedDate(),
						transactionIdDetails.getStrYear(), 
						transactionIdDetails.getStrJulianDate()
				});
		return count;
	}

	@Override
	public void savePreTransactionIdDetails(TransactionIdTable transactionIdTable) 
	{
		String updateQuery = "INSERT INTO pre_transaction_id_table (year, julian_date, last_txn_serial_no, created_date, created_by) VALUES (?,?,?,?,?)";
		jdbcTemplate.update(updateQuery, new Object[]
		{
			transactionIdTable.getStrYear(), 
			transactionIdTable.getStrJulianDate(),
			transactionIdTable.getStrLastTxnSerialNo(),
			Utils.getCurrentDate(),
			"System"
		});
	}

	@Override
	public int updatePreTransactionIdDetails(TransactionIdTable transactionIdTable) 
	{
		String updateQuery = "UPDATE pre_transaction_id_table SET last_txn_serial_no = ?, created_date = ? WHERE year = ? and julian_date = ?";
		return jdbcTemplate.update(updateQuery,	new Object[]
				{
					transactionIdTable.getStrLastTxnSerialNo(),
					transactionIdTable.getStrCreatedDate(),
					transactionIdTable.getStrYear(), 
					transactionIdTable.getStrJulianDate()
				});
	}

	@Override
	public List<TransactionIdTable> getPreTransactionIdList(String year, String julianDate) 
	{
		StringBuilder selectSb = new StringBuilder("SELECT id AS strID, year AS strYear, julian_date AS strJulianDate, last_txn_serial_no AS strLastTxnSerialNo ");
		selectSb.append("from pre_transaction_id_table where year = ? AND julian_date = ? ");
		
		List<TransactionIdTable> getTransactionIdTableList = jdbcTemplate.query(selectSb.toString(), new BeanPropertyRowMapper<TransactionIdTable>(TransactionIdTable.class), new Object[] 
		{
			year, 
			julianDate
		});
        return getTransactionIdTableList;
	}

	@Override
	public String getNextTransactionId(String year, String julianDay, String newTxnSerialNumber) 
	{
		try 
		{
			String sql = "SELECT getNextTranId('"+year+"', '"+julianDay+"', '"+newTxnSerialNumber+"') AS nextTranId";
			String nextTranId = jdbcTemplate.queryForObject(sql, String.class, new Object[] {});
			amsLogger.writeInfoLog("Inside getNextTransactionId Next Transaction Id=["+nextTranId+"]");
			return nextTranId;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

}
