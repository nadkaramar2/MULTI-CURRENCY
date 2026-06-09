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

import ams.cms.dao.AccountTypeWiseBlockedMccDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.AccountTypeWiseBlockedMccMaster;

@Repository
public class AccountTypeWiseBlockedMccDaoImpl extends AbstractGenericDao<AccountTypeWiseBlockedMccMaster> implements AccountTypeWiseBlockedMccDao
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public int[] batchEntryforAccountTypeWiseBlockedMcc(List<AccountTypeWiseBlockedMccMaster> accountTypeWiseBlockedMccMasters) 
	{
		int[] batchResponse = null;
		try
		{
			return this.jdbcTemplate.batchUpdate("INSERT INTO blocked_mcc_account_type_wise "
					+ "(participant_id, account_type, blocked_mcc_code, created_by, creation_date) "
					+ "values(?,?,?,?,?)", new BatchPreparedStatementSetter() 
					{
						@Override
						public void setValues(PreparedStatement psmt, int i) throws SQLException 
						{
							psmt.setString(1, accountTypeWiseBlockedMccMasters.get(i).getStrParticipantID());
							psmt.setString(2, accountTypeWiseBlockedMccMasters.get(i).getStrAccounType());
							psmt.setString(3, accountTypeWiseBlockedMccMasters.get(i).getStrBlockedMCCCode());
							psmt.setString(4, accountTypeWiseBlockedMccMasters.get(i).getStrCreatedBy());
							psmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
						}
						
						@Override
						public int getBatchSize() {
							return accountTypeWiseBlockedMccMasters.size();
						}
					});
		}
		catch (Exception e) {
			System.out.println("Exception in batchEntryOfInstanAccount::"+e);
			e.printStackTrace();
		}
		return batchResponse;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AccountTypeWiseBlockedMccMaster> getAccountTypeWiseBlockedMccList(AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMaster) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();
			criteria.add(Restrictions.eq("strParticipantID", accountTypeWiseBlockedMccMaster.getStrParticipantID()));
			criteria.add(Restrictions.eq("strAccounType", accountTypeWiseBlockedMccMaster.getStrAccounType()));
			
			List<AccountTypeWiseBlockedMccMaster> listData = (List<AccountTypeWiseBlockedMccMaster>) criteria.list();
			if (listData !=null && listData.size() > 0) 
			{
				return listData;
			}
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	@Override
	public List<AccountTypeWiseBlockedMccMaster> getAccountTypeWiseUnBlockedMccMaster(AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMaster)
	{
		try
		{
			List<AccountTypeWiseBlockedMccMaster> accountTypeWiseBlockedMccMasters = this.jdbcTemplate.query(
					"SELECT pwwm.mcc_code AS strMccCode, mccm.mcc_desc AS strMccCodeDesc "
					+ "FROM participant_wise_wallet_master pwwm "
					+ "INNER JOIN merchant_category_code_master mccm "
					+ "ON pwwm.mcc_code = mccm.mcc_code WHERE pwwm.participant_id= ? ",
					/*+ "AND pwwm.mcc_code NOT IN(select blocked_mcc_code from blocked_mcc_account_type_wise "
					+ "where participant_id = ?)",*/
					//+ "where participant_id = ? and account_type = ?)",
					new Object[] { accountTypeWiseBlockedMccMaster.getStrParticipantID() 
							/*,accountTypeWiseBlockedMccMaster.getStrParticipantID(), 
							accountTypeWiseBlockedMccMaster.getStrAccounType()*/},
					(RowMapper) new BeanPropertyRowMapper(AccountTypeWiseBlockedMccMaster.class));
			
			return accountTypeWiseBlockedMccMasters;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	

	// added by Prashant for getting blocked mcc code according to account type
	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	@Override
	public List<AccountTypeWiseBlockedMccMaster> getBlockMccListAccountTypeWise(
			AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMaster) {
		try {
			List<AccountTypeWiseBlockedMccMaster> accountTypeWiseBlockedMccMasters = this.jdbcTemplate.query(
					// " SELECT blocked_mcc_code AS strBlockedMCCCode FROM
					// blocked_mcc_account_type_wise WHERE account_type = ? ",
					" SELECT bmatw.blocked_mcc_code AS strBlockedMCCCode , mccm.mcc_desc AS strMccCodeDesc "
							+ " FROM blocked_mcc_account_type_wise bmatw "
							+ "	INNER JOIN merchant_category_code_master mccm "
							+ "	ON bmatw.blocked_mcc_code = mccm.mcc_code WHERE bmatw.account_type= ? ",
					new Object[] { accountTypeWiseBlockedMccMaster.getStrAccounType() },
					(RowMapper) new BeanPropertyRowMapper(AccountTypeWiseBlockedMccMaster.class));
			System.out.println("AccountTypeWiseBlockedMccDaoImpl.getBlockMccListAccountTypeWise()"
					+ accountTypeWiseBlockedMccMasters);
			return accountTypeWiseBlockedMccMasters;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
