package ams.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.MultiCurrencyWalletAccountTypeDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.MultiCurrencyWalletAccountTypeMaster;


@Repository
public class MultiCurrencyWalletAccountTypeDaoImpl extends AbstractGenericDao<MultiCurrencyWalletAccountTypeMaster> implements MultiCurrencyWalletAccountTypeDao
{

	@Autowired
	JdbcTemplate jdbcTemplate;
		

	@SuppressWarnings("unchecked")
	@Override
	public List<MultiCurrencyWalletAccountTypeMaster> getAccountTypeWiseMultiCurrencyWalletList(MultiCurrencyWalletAccountTypeMaster accountWisePriorityWallet) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			criteria.add(Restrictions.eq("baseCurrencyAccountType", accountWisePriorityWallet.getBaseCurrencyAccountType()));
			
			List<MultiCurrencyWalletAccountTypeMaster> listData = (List<MultiCurrencyWalletAccountTypeMaster>) criteria.list();
			if (listData !=null && listData.size() > 0) 
			{
				return listData;
			}
		}
		catch (Exception e) 
		{
			System.out.println("AccountWisePriorityWalletDaoImpl.getAccountTypeWiseMultiCurrencyWalletList()"+e);
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public MultiCurrencyWalletAccountTypeMaster getMultiCurrencyAccountTypeMasterByCurrencyCode(MultiCurrencyWalletAccountTypeMaster currnecyWalletAccount) {
		
		try 
	{	
			StringBuilder sql = new StringBuilder(" SELECT mcat.account_type AS strAccountType , mcat.currency_code AS strCurrencyCode , mcat.gl_account_type AS glAccountType , mcat.gl_account_number AS glAccountNumber");
			sql.append( " FROM multi_currency_wallet_account_type_master mcat WHERE mcat.currency_code= '"+currnecyWalletAccount.getStrCurrencyCode().trim()+"'  ");
			
			

			List<MultiCurrencyWalletAccountTypeMaster> accountMasters  = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<MultiCurrencyWalletAccountTypeMaster>(MultiCurrencyWalletAccountTypeMaster.class), new Object[]  {});
			if (accountMasters!=null && accountMasters.size() > 0) 
			{
				return accountMasters.get(0);
			}	
		} 
	catch (Exception e) 
	{
		
	}
	return null;
	}


	@Override
	public MultiCurrencyWalletAccountTypeMaster getMultiCurrencyAccountTypeMasterByCurrencyCodeAndAccountType(MultiCurrencyWalletAccountTypeMaster multiCurrencyWalletAccountTypeMaster) {
		
		try 
	{	
			StringBuilder sql = new StringBuilder(" SELECT mcat.account_type AS strAccountType , mcat.currency_code AS strCurrencyCode , mcat.gl_account_type AS glAccountType , mcat.gl_account_number AS glAccountNumber");
			sql.append( " FROM multi_currency_wallet_account_type_master mcat WHERE mcat.currency_code= '"+multiCurrencyWalletAccountTypeMaster.getStrCurrencyCode().trim()+"' AND  mcat.base_currency_account_type= '"+multiCurrencyWalletAccountTypeMaster.getBaseCurrencyAccountType().trim()+"'");

			List<MultiCurrencyWalletAccountTypeMaster> accountMasters  = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<MultiCurrencyWalletAccountTypeMaster>(MultiCurrencyWalletAccountTypeMaster.class), new Object[]  {});
			if (accountMasters!=null && accountMasters.size() > 0) 
			{
				return accountMasters.get(0);
			}	
		} 
	catch (Exception e) 
	{
		
	}
	return null;
	}

	

}
