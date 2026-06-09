package ams.cms.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.AcTypeLrsTcsMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.AcTypeLrsTcsMaster;

@Repository
public class AcTypeLrsTcsMasterDaoImpl extends AbstractGenericDao<AcTypeLrsTcsMaster> implements AcTypeLrsTcsMasterDao{

	@Autowired
	private	JdbcTemplate jdbcTemplate;
	
	@Override
	public AcTypeLrsTcsMaster getAccountTypeLrsAndTcs(AcTypeLrsTcsMaster acTypeLrsTcsMaster) {
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT atl.account_type AS strAccountType , atl.channel_code AS strChannelCode , atl.tcs AS strTcs , atl.lrs AS strLrs");
			if(acTypeLrsTcsMaster.getStrChannelCode() != null) {
				queryBuilder.append(" FROM account_type_wise_tcs_lrs_master atl WHERE atl.account_type= '"+acTypeLrsTcsMaster.getStrAccountType()+"' AND atl.channel_code= '"+acTypeLrsTcsMaster.getStrChannelCode()+"'");
					
			}else {
				queryBuilder.append(" FROM account_type_wise_tcs_lrs_master atl WHERE atl.account_type= '"+acTypeLrsTcsMaster.getStrAccountType()+"' ");
				
			}
			
			List<AcTypeLrsTcsMaster> acTypeLrsTcsMasters  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AcTypeLrsTcsMaster>(AcTypeLrsTcsMaster.class),
			new Object[]  {});
			
			if (acTypeLrsTcsMasters!=null && acTypeLrsTcsMasters.size() > 0) 
			{
				return acTypeLrsTcsMasters.get(0);
			}	
		}
		catch (Exception e) 
		{
			System.out.println("Exception in getAccountCreditCardTxnWise::"+e);
			e.printStackTrace();
		}
		return null;
}

	@Override
	public List<AcTypeLrsTcsMaster> getAccountTypeLrsBasedOnAccountType(AcTypeLrsTcsMaster acTypeLrsTcsMaster) {
		List<AcTypeLrsTcsMaster> acTypeLrsTcsMastersList   = new ArrayList<>();
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT atl.account_type AS strAccountType , atl.channel_code AS strChannelCode , atl.tcs AS strTcs , atl.lrs AS strLrs");
			queryBuilder.append(" FROM account_type_wise_tcs_lrs_master atl WHERE atl.account_type= '"+acTypeLrsTcsMaster.getStrAccountType()+"'");
			
			
			 acTypeLrsTcsMastersList  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AcTypeLrsTcsMaster>(AcTypeLrsTcsMaster.class),
			new Object[]  {});
			
			if (acTypeLrsTcsMastersList!=null && acTypeLrsTcsMastersList.size() > 0) 
			{
				return acTypeLrsTcsMastersList;
			}	
		}
		catch (Exception e) 
		{
			System.out.println("Exception in getAccountCreditCardTxnWise::"+e);
			e.printStackTrace();
		}
		return null;
}

	@Override
	public List<AcTypeLrsTcsMaster> getAccountTypeTcsAndLrs(AcTypeLrsTcsMaster acTypeLrsTcsMaster) {
		List<AcTypeLrsTcsMaster> acTypeLrsTcsMastersList = new ArrayList<>();
		try
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT atl.account_type AS strAccountType , atl.channel_code AS strChannelCode , atl.tcs AS strTcs , atl.lrs AS strLrs");
			queryBuilder.append(" FROM account_type_wise_tcs_lrs_master atl WHERE atl.account_type= '"+acTypeLrsTcsMaster.getStrAccountType()+"'");
			
			acTypeLrsTcsMastersList  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AcTypeLrsTcsMaster>(AcTypeLrsTcsMaster.class),
			new Object[]  {});
			
			System.out.println(acTypeLrsTcsMastersList);
			if (acTypeLrsTcsMastersList!=null && acTypeLrsTcsMastersList.size() > 0)
			{
				return acTypeLrsTcsMastersList;
			}	
		}
		catch (Exception e)
		{
			System.out.println("Exception in getAccountCreditCardTxnWise::"+e);
			e.printStackTrace();
		}
		return acTypeLrsTcsMastersList;
    }

}
