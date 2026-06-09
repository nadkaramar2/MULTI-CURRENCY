package ams.cms.dao.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.BeneficiaryTxnMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.BeneficiaryTxnMaster;

@Repository
public class BeneficiaryTxnMasterDaoImpl extends AbstractGenericDao<BeneficiaryTxnMaster> implements BeneficiaryTxnMasterDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(BeneficiaryTxnMasterDaoImpl.class);
	
	@Autowired
	private	JdbcTemplate jdbcTemplate;
	
	@Override
	public BeneficiaryTxnMaster getInfoByTxnID(BeneficiaryTxnMaster beneficiaryTxnMaster) 
	{

		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT bn.from_account_no AS strFromAccountNo, bn.beneficiary_account_no AS strBeneficiaryAccountNo, ");
			queryBuilder.append("bn.beneficiary_bank_ifsc AS strBeneficiaryBankIfsc, bn.txn_amount AS strTxnAmount, ");
			queryBuilder.append("bn.txn_status AS strTxnStatus FROM beneficiary_txn bn WHERE bn.txn_id = ? ;");
			
			List<BeneficiaryTxnMaster> beneficiaryTxnMasterData  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<BeneficiaryTxnMaster>(BeneficiaryTxnMaster.class),
			     new Object[]  { 
			    		 beneficiaryTxnMaster.getStrTxnId()
			    		 });
			if (beneficiaryTxnMasterData!=null && beneficiaryTxnMasterData.size() > 0) 
			{
				return beneficiaryTxnMasterData.get(0);
			}	
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int updateBeneficiaryTxnMaster(BeneficiaryTxnMaster beneficiaryTxnMaster) 
	{
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE beneficiary_txn SET txn_status = ? WHERE txn_id = ? ",
			new Object[] 
			{ 	
				beneficiaryTxnMaster.getStrTxnStatus(),
				beneficiaryTxnMaster.getStrTxnId() 
			});
			amsLogger.writeInfoLog("Count updateBeneficiaryTxnMaster::[" + count+"]");
			return count;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;	
	}
}
