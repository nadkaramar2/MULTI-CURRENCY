package ams.cms.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.JournalTransferDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.JournalTransfer;

@Repository
public class JournalTransferDaoImpl extends AbstractGenericDao<JournalTransfer> implements JournalTransferDao
{
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<JournalTransfer> getAccountVeriflylist(JournalTransfer journalTransfer) 
	{
		try 
		{
			StringBuilder sqlQuery = new StringBuilder("SELECT  jt.txn_id AS strTxnId,jt.txn_date AS strTxnDate,jt.txn_time AS strTxnTime, " 
					+ "jt.amout_to_transfer AS strAmoutToTransfer ,jt.maker_Id AS strMakerId, jt.txn_journal_transfer_type AS txnJournalTransferType "
					+ "from journal_transfer AS jt  WHERE jt.txn_status='pending' AND jt.maker_Id <> '"+journalTransfer.getLoginUserCode()+"' ");
			List<JournalTransfer> viewAccountAuthlist = this.jdbcTemplate.query(sqlQuery.toString(),
					new BeanPropertyRowMapper<JournalTransfer>(JournalTransfer.class),
					new Object[] 
					{
							//journalTransfer.getStrTxnStatus()	
					});
			return viewAccountAuthlist;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public List<JournalTransfer> getTxnIdApprovalInfo(JournalTransfer journalTransfer) 
	{ 
		try 
		{
			StringBuilder sqlQuery = new StringBuilder("SELECT jt.txn_id AS strTxnId, jt.from_account_type AS strFromAccountType, jt.from_account_number AS strFromAccountNumber, ");
			sqlQuery.append("IFNULL(jt.from_account_name,'-') AS strFromAccountName ,jt.to_account_type AS strToAccountType, ");
			sqlQuery.append("jt.to_account_number AS strToAccountNumber, IFNULL(jt.to_account_name,'-') AS strToAccountName, ");
			sqlQuery.append("jt.amout_to_transfer AS strAmoutToTransfer, jt.narration AS strNarration, jt.txn_journal_transfer_type AS txnJournalTransferType  ");   
			sqlQuery.append("from journal_transfer AS jt WHERE jt.txn_id = ? ");

			List<JournalTransfer> viewAccountAuthlist = this.jdbcTemplate.query(sqlQuery.toString(),
					new BeanPropertyRowMapper<JournalTransfer>(JournalTransfer.class),
					new Object[] 
					{
							journalTransfer.getStrTxnId()	
					});
			return viewAccountAuthlist;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unused")
	@Override
	public int getUpdateReasonlist(JournalTransfer journalTransfer)
	{
		try 
		{
			String updateReason = "UPDATE journal_transfer as jt SET  jt.txn_status  =?, jt.reject_reason=? "
					+ "WHERE jt.txn_id=? ";
			int chedckUpdate = this.jdbcTemplate.update(updateReason, new Object[] { journalTransfer.getStrTxnId(),
					journalTransfer.getStrRejectReason(), journalTransfer.getStrTxnStatus()

			});
			return chedckUpdate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;	
	}


	@Override
	public int updateJournalTransferTxnStatus(JournalTransfer journalTransfer) 
	{
		try 
		{//from_account_number to_account_number
			List<Object> objectList = new ArrayList<Object>();
			
			StringBuilder queryBuilder = new StringBuilder("UPDATE journal_transfer as jt SET ");
			
			if (journalTransfer.getStrTxnStatus() != null && journalTransfer.getStrTxnStatus().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append(" jt.txn_status = ").append("?");
				objectList.add(journalTransfer.getStrTxnStatus());
			}
			if (journalTransfer.getStrRejectReason() != null && journalTransfer.getStrRejectReason().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("jt.reject_reason = ").append("?");
				objectList.add(journalTransfer.getStrRejectReason());
			}
			if (journalTransfer.getStrCheckerId() != null && journalTransfer.getStrCheckerId().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("jt.checker_id = ").append("?");
				objectList.add(journalTransfer.getStrCheckerId());
			}
			queryBuilder.append(" WHERE jt.txn_id = ").append("?");			
			objectList.add(journalTransfer.getStrTxnId());			
			
			queryBuilder.append(" AND  jt.from_account_number = ").append("?");
			objectList.add(journalTransfer.getStrFromAccountNumber());
			
			queryBuilder.append(" AND  jt.to_account_number = ").append("?");
			objectList.add(journalTransfer.getStrToAccountNumber());
			
			Object[] object = objectList.toArray();
			
			int count = this.jdbcTemplate.update(queryBuilder.toString(), object);
			return count;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return 0;	
	}
	
	@Override
	public List<JournalTransfer> getTxnJournalReport(JournalTransfer journalTransfer) 
	{
		try 
		{
			StringBuilder sqlQuery = new StringBuilder("select jt.txn_date AS strTxnDte ,jt.txn_time AS strTxnTime,jt.txn_id AS strTxnId, ");
			sqlQuery.append("from_account_type AS strFromAccountType, from_account_number AS strFromAccountNumber, ");
			sqlQuery.append("jt.from_account_name AS strFromAccountName,jt.to_account_type AS strToAccountType,  ");
			sqlQuery.append("jt.to_account_number AS strToAccountNumber,jt.to_account_name AS strToAccountName, ");
			sqlQuery.append("jt.amout_to_transfer AS strAmoutToTransfer,jt.narration AS strNarration, ");   
			sqlQuery.append("jt.txn_status AS strTxnStatus,jt.maker_Id AS strMakerId, ");
			sqlQuery.append("jt.checker_Id AS strCheckerId FROM journal_transfer AS jt  ");
			sqlQuery.append("WHERE txn_date BETWEEN '"+journalTransfer.getFromDate()+" 00:00:00' AND '"+journalTransfer.getToDate()+" 23:59:59' ORDER BY txn_date DESC, txn_time DESC "); 
			
			List<JournalTransfer> viewJournallist = this.jdbcTemplate.query(sqlQuery.toString(),
					new BeanPropertyRowMapper<JournalTransfer>(JournalTransfer.class),
					new Object[] 
					{
					});
			return viewJournallist;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
}
