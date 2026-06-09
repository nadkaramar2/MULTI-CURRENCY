package ams.cms.dao.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.BankDetailsDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.BankDetails;

@Repository
public class BankDetailsDaoImpl extends AbstractGenericDao<BankDetails> implements BankDetailsDao 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(BankDetailsDaoImpl.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<BankDetails> getAllBankNameList() 
	{
		try 
		{
			String selectQuery = "SELECT DISTINCT bd.bank_name AS strBankName FROM bank_details bd";
			amsLogger.writeInfoLog("selectQuery::["+selectQuery+"]");
			List<BankDetails> bankListData = jdbcTemplate.query(selectQuery, new BeanPropertyRowMapper<BankDetails>(BankDetails.class), new Object[]{});
			amsLogger.writeInfoLog(bankListData);
			return bankListData;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BankDetails getBankDetailsInstanceBasedOnParam(BankDetails bankDetails) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			
			if (bankDetails.getStrIfscCode() != null && bankDetails.getStrIfscCode().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strIfscCode", bankDetails.getStrIfscCode().trim()));
			}
			if (bankDetails.getStrBankName() !=null && bankDetails.getStrBankName().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strBankName", bankDetails.getStrBankName().trim()));
			}
			if (bankDetails.getStrBranchName() !=null && bankDetails.getStrBranchName().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strBranchName", bankDetails.getStrBranchName().trim()));
			}
			List<BankDetails> listData = (List<BankDetails>) criteria.list();
			if (listData !=null && listData.size() > 0) 
			{
				return listData.get(0);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

}
