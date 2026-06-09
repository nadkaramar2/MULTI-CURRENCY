package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.AccountTransactionReportDao;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.TransactionTypeModel;
import ams.cms.services.AccountTransactionReportService;

@Transactional
@Service
public class AccountTransactionReportServiceImpl implements AccountTransactionReportService
{
  
	@Autowired
	AccountTransactionReportDao  accountTransactionReportDao;
	
	@Override
	public List<AccountTranMaster> getTxnAccountlist(AccountTranMaster accountTranMaster) {
		return accountTransactionReportDao.getTxnAccountlist(accountTranMaster);
	}

	@Override
	public List<TransactionTypeModel> getTxnAccountTypelist(TransactionTypeModel transactionTypeModel) {
		return accountTransactionReportDao.getTxnAccountTypelist(transactionTypeModel);
	}
  

}
