package ams.cms.api.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.AccountTransactionLimitDao;
import ams.cms.api.model.AccountLimitRequest;
import ams.cms.api.model.AccountTransactionLimit;
import ams.cms.api.service.AccountTransactionLimitService;

@Transactional
@Service
public class AccountTransactionLimitServiceImpl implements AccountTransactionLimitService{

	@Autowired
	private AccountTransactionLimitDao accountTransactionLimitDao;
	
	@Override
	public AccountTransactionLimit getMonthlyAndDailyTransactionLimit(AccountLimitRequest accountLimitRequest) {
		try {
			AccountTransactionLimit accountTransactionLimitDetails = accountTransactionLimitDao
					.getMonthlyAndDailyTransactionLimit(accountLimitRequest);
			return accountTransactionLimitDetails;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AccountTransactionLimit getLimitDetails(AccountLimitRequest accountLimitRequest) {
		AccountTransactionLimit accountTransactionLimitation = null;
		try{
			accountTransactionLimitation = accountTransactionLimitDao.getAccountTransactionLimitation(accountLimitRequest);
			return accountTransactionLimitation;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return accountTransactionLimitation;
	}

}
