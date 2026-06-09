package ams.cms.api.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.DenominationMasterDao;
import ams.cms.api.model.DenominationMaster;
import ams.cms.dao.generic.AbstractGenericDao;

@Repository
public class DenominationMasterDaoImpl extends AbstractGenericDao<DenominationMaster> implements DenominationMasterDao
{
	
	@Autowired
	JdbcTemplate jdbcCMSTemplate;
	
	/*
	@Override
	public List<DenominationMaster> getTxnDenominationDetails(DenominationMaster denominationMaster)
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT atm.local_tran_date AS strDate, atm.local_tran_time AS strTime, atm.txn_id AS txnId, ");
			queryBuilder.append("atm.from_account_number AS fromAccount, atm.to_account_number AS toAccount, ");
			queryBuilder.append("atm.tran_type AS strTxnType, atm.transaction_amount AS strTxnAmount, dm.d_10 AS d10, dm.d_20 AS d20, ");
			queryBuilder.append("dm.d_50 AS d50, dm.d_100 AS d100, dm.d_200 AS d200, dm.d_500 AS d500, ");
			queryBuilder.append("dm.d_1000 AS d1000, dm.d_2000 AS d2000 FROM account_tran_master atm ");
			queryBuilder.append("INNER JOIN denomination_master dm ON atm.txn_id = dm.txn_id ");
			queryBuilder.append("WHERE atm.local_tran_date BETWEEN ? AND ? ORDER BY atm.local_tran_date ASC ");
			
			List<DenominationMaster> denominationMasterResp  = jdbcCMSTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<DenominationMaster>(DenominationMaster.class),
			     new Object[]  {
			    		 denominationMaster.getStrFromDate(),
			    		 denominationMaster.getStrToDate()
			    		 
			     });
			if (denominationMasterResp!=null && denominationMasterResp.size() > 0) 
			{
				return denominationMasterResp;
			}	
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	*/
	
	@Override
	public List<DenominationMaster> getTxnDenominationDetails(DenominationMaster denominationMaster)
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT DATE(atm.switch_txn_date) AS strDate, TIME(atm.switch_txn_date) AS strTime, ");
			queryBuilder.append("atm.txn_id AS txnId, atm.from_account_number AS fromAccount, atm.to_account_number AS toAccount, ");
			queryBuilder.append("atm.tran_type AS strTxnType, atm.transaction_amount AS strTxnAmount, ");
			queryBuilder.append("IFNULL(dm.d_10,'0') AS d10, IFNULL(dm.d_20,'0') AS d20, ");
			queryBuilder.append("IFNULL(dm.d_50,'0') AS d50, IFNULL(dm.d_100,'0') AS d100, ");
			queryBuilder.append("IFNULL(dm.d_200,'0') AS d200, IFNULL(dm.d_500,'0') AS d500, ");
			
			queryBuilder.append("IFNULL(dm.d_1000,'0') AS d1000, IFNULL(dm.d_2000,'0') AS d2000 ");
			queryBuilder.append("FROM account_tran_master atm ");
			queryBuilder.append("INNER JOIN denomination_master dm ON atm.txn_id = dm.txn_id ");
			
			queryBuilder.append("WHERE atm.switch_txn_date BETWEEN '"+denominationMaster.getStrFromDate()+" 00:00:00' AND '"+denominationMaster.getStrToDate()+" 23:59:59' ");
			queryBuilder.append("AND atm.tran_type = '"+denominationMaster.getStrTxnType()+"' ");
			
			if ("DPT".equalsIgnoreCase(denominationMaster.getStrTxnType())) 
			{
				if (denominationMaster.getFromAccount()!=null && denominationMaster.getFromAccount().trim().length() > 0) 
				{
					queryBuilder.append("AND atm.from_account_number = '"+denominationMaster.getFromAccount()+"' ");
				}
			}
			else 
			{
				if (denominationMaster.getToAccount()!=null && denominationMaster.getToAccount().trim().length() > 0)
				{
					queryBuilder.append("AND atm.to_account_number='"+denominationMaster.getToAccount()+"' ");
				}
			}
			
			queryBuilder.append("ORDER BY atm.switch_txn_date DESC ");
			
			List<DenominationMaster> denominationMasterResp  = jdbcCMSTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<DenominationMaster>(DenominationMaster.class),
			new Object[]  {});
			
			if (denominationMasterResp!=null && denominationMasterResp.size() > 0) 
			{
				return denominationMasterResp;
			}	
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	public List<DenominationMaster> getTxnDenominationDetailsbyAgent(DenominationMaster denominationMaster)
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT atm.local_tran_date AS strDate, atm.local_tran_time AS strTime, atm.txn_id AS txnId, ");
			queryBuilder.append("atm.from_account_number AS fromAccount, atm.to_account_number AS toAccount, ");
			queryBuilder.append("atm.tran_type AS strTxnType, atm.transaction_amount AS strTxnAmount, dm.d_10 AS d10, dm.d_20 AS d20, ");
			queryBuilder.append("dm.d_50 AS d50, dm.d_100 AS d100, dm.d_200 AS d200, dm.d_500 AS d500, ");
			queryBuilder.append("dm.d_1000 AS d1000, dm.d_2000 AS d2000 FROM account_tran_master atm ");
			queryBuilder.append("INNER JOIN denomination_master dm ON atm.txn_id = dm.txn_id");
			queryBuilder.append("WHERE atm.from_account_number = ? AND atm.local_tran_date BETWEEN ? AND ? ");
			
			List<DenominationMaster> denominationMasterResp  = jdbcCMSTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<DenominationMaster>(DenominationMaster.class),
			     new Object[]  {
			    		 denominationMaster.getFromAccount(),
			    		 denominationMaster.getStrFromDate(),
			    		 denominationMaster.getStrToDate()
			    		 
			     });
			if (denominationMasterResp!=null && denominationMasterResp.size() > 0) 
			{
				return denominationMasterResp;
			}	
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
