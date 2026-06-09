package ams.cms.services.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.config.CustomerIdCreationConfig;
import ams.cms.dao.CustomerIdTableDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.CustomerIdTable;
import ams.cms.services.CustomerIdTableService;

@Transactional
@Service
public class CustomerIdTableServiceImpl implements CustomerIdTableService
{
	private AMSLogger amsLogger = AMSLogger.getInstance(CustomerIdCreationConfig.class);
	
	@Autowired
	private CustomerIdTableDao customerIdTableDao;
	
	@Override
	public int updateCustIdTable(CustomerIdTable customerIdTable)
	{
		int resultCount = 0;
		try 
		{
			customerIdTable.setStrCreatedDate(new Date());
			if(customerIdTable.getStrAction().equals("A"))
			{
				customerIdTableDao.save(customerIdTable);
				resultCount = Integer.parseInt(customerIdTable.getStrID());
			}
			else if(customerIdTable.getStrAction().equals("U"))
			{
				resultCount = customerIdTableDao.updateCustidtbl(customerIdTable);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return resultCount;
	}

}
