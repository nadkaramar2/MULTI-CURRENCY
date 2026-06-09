package ams.cms.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.dao.WalletAccountMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.WalletAccountMaster;

@Repository
public class WalletAccountMasterDaoImpl extends AbstractGenericDao<WalletAccountMaster> implements WalletAccountMasterDao
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public int[] batchEntryOfWalletAccountMaster(List<WalletAccountMaster> walletAccountMasters) 
	{
		int[] batchResponse = null;
		try
		{
			return this.jdbcTemplate.batchUpdate("INSERT INTO wallet_account_master "
					+ "(participant_id, account_type, account_number, wallet_account_number, mcc_code, "
					+ "creation_date, created_by) "
					+ "values(?,?,?,?,?,?,?)", new BatchPreparedStatementSetter() 
					{
						@Override
						public void setValues(PreparedStatement psmt, int i) throws SQLException 
						{
							psmt.setString(1, walletAccountMasters.get(i).getStrParticipantID());
							psmt.setString(2, walletAccountMasters.get(i).getStrAccountType());
							psmt.setString(3, walletAccountMasters.get(i).getStrAccountNumber());
							psmt.setString(4, walletAccountMasters.get(i).getStrWalletAccountNumber());
							psmt.setString(5, walletAccountMasters.get(i).getStrMccCode());
							psmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
							psmt.setString(7, walletAccountMasters.get(i).getStrCreatedBy());
						}
						
						@Override
						public int getBatchSize() {
							return walletAccountMasters.size();
						}
					});
		}
		catch (Exception e) {
			System.out.println("Exception in batchEntryOfInstanAccount::"+e);
			e.printStackTrace();
		}
		return batchResponse;
	}
	
	@Override
	public List<WalletAccountMaster> getWalletBalancelist(WalletAccountMaster walletAccountMaster) 
	{
		try 
		{
			String selectQuery = "SELECT participant_id AS strParticipantID, "
					+ "account_type AS strAccountType, account_number AS strAccountNumber, "
					+ "wallet_account_number AS strWalletAccountNumber, mcc_code AS strMccCode, "
					+ "percentage AS strPercentage, available_balance AS strAvailableBalance "
					+ "FROM wallet_account_master where account_type = ? "
					+ "and account_number = ?";
			
			List<WalletAccountMaster> accountList = jdbcTemplate.query(selectQuery,
					new BeanPropertyRowMapper<WalletAccountMaster>(WalletAccountMaster.class),
			new Object[] 
			{ 
				walletAccountMaster.getStrAccountType(), 
				walletAccountMaster.getStrAccountNumber(),
			});
			return accountList;
		}
		catch (Exception e) 
		{
			System.out.println("Exception in getWalletBalancelist::"+e);
			e.printStackTrace();
		}
		return null;
	}
  
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<WalletAccountMaster> getWalletShowBalance(WalletAccountMaster walletAccountMaster) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (walletAccountMaster.getStrAccountType() !=null && walletAccountMaster.getStrAccountType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountType", walletAccountMaster.getStrAccountType()));
			}
			if (walletAccountMaster.getStrAccountNumber()!=null && walletAccountMaster.getStrAccountNumber().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountNumber", walletAccountMaster.getStrAccountNumber()));
			}
			List<WalletAccountMaster> walletBalanceShowlistData = (List<WalletAccountMaster>) criteria.list();
			return walletBalanceShowlistData;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}	
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<WalletAccountMaster> getLinkedAccountWalletList(WalletAccountMaster walletAccountMaster)
	{
		try
		{
			String sqlQuery = "SELECT wallet_account_number AS strWalletAccountNumber, "
					+ "account_type AS strAccountType,mcc_code AS strMccCode, "
					+ "account_number AS strAccountNumber, "
					+ "percentage AS strPercentage,available_balance AS strAvailableBalance "
					+ "FROM wallet_account_master where account_number = ?";
			List<WalletAccountMaster> walletAccountMasters = this.jdbcTemplate.query(sqlQuery, 
				(RowMapper) new BeanPropertyRowMapper(WalletAccountMaster.class),
				new Object[] 
				{
					walletAccountMaster.getStrAccountNumber()
				}
			);
			System.out.println("getLinkedAccountWalletList" + walletAccountMasters);
			return walletAccountMasters;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	//Wallet Percentage Edit By Jyoti S
	public int updateWalletPercentage(WalletAccountMaster walletAccountMaster) {
		int i = this.jdbcTemplate.update("UPDATE wallet_account_master "
				+ "SET percentage = ? "
				+ "WHERE wallet_account_number = ? "
				+ "and account_type = ?",
				new Object[] { walletAccountMaster.getStrPercentage(), walletAccountMaster.getStrWalletAccountNumber(), walletAccountMaster.getStrAccountType() });
		return i;
	}

}
