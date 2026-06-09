package ams.cms.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.AccountTypeWiseWalletDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.AccountTypeWiseWalletMaster;

@Repository
public class AccountTypeWiseWalletDaoImpl extends AbstractGenericDao<AccountTypeWiseWalletMaster> implements AccountTypeWiseWalletDao
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AccountTypeWiseWalletMaster> getAccountTypeWiseWalletMasterList(AccountTypeWiseWalletMaster accountTypeWiseWalletMaster) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			criteria.add(Restrictions.eq("strParticipantID", accountTypeWiseWalletMaster.getStrParticipantID()));
			criteria.add(Restrictions.eq("strAccounType", accountTypeWiseWalletMaster.getStrAccounType()));
			
			List<AccountTypeWiseWalletMaster> listData = (List<AccountTypeWiseWalletMaster>) criteria.list();
			if (listData !=null && listData.size() > 0) 
			{
				return listData;
			}
		}
		catch (Exception e) 
		{
			System.out.println("Exception in getAccountTypeWiseWalletMasterList::"+e);
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int[] batchEntryforAccountTypeWiseWallet(List<AccountTypeWiseWalletMaster> listOfAccountTypeWiseWalletMasters) 
	{
		int[] batchResponse = null;
		try
		{
			return this.jdbcTemplate.batchUpdate("INSERT INTO account_type_wise_wallet_master "
					+ "(participant_id, account_type, mcc_code, percentage, created_by, creation_date) "
					+ "values(?,?,?,?,?,?)", new BatchPreparedStatementSetter() 
					{
						@Override
						public void setValues(PreparedStatement psmt, int i) throws SQLException 
						{
							psmt.setString(1, listOfAccountTypeWiseWalletMasters.get(i).getStrParticipantID());
							psmt.setString(2, listOfAccountTypeWiseWalletMasters.get(i).getStrAccounType());
							psmt.setString(3, listOfAccountTypeWiseWalletMasters.get(i).getStrMccCode());
							psmt.setString(4, listOfAccountTypeWiseWalletMasters.get(i).getStrPercentage());
							psmt.setString(5, listOfAccountTypeWiseWalletMasters.get(i).getStrCreatedBy());
							//psmt.setDate(6, new Date(listOfAccountTypeWiseWalletMasters.get(i).getStrDateOfCreation().getTime()));
							psmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
						}
						
						@Override
						public int getBatchSize() {
							return listOfAccountTypeWiseWalletMasters.size();
						}
					});
		}
		catch (Exception e) {
			System.out.println("Exception in batchEntryOfInstanAccount::"+e);
			e.printStackTrace();
		}
		return batchResponse;
	}
}
