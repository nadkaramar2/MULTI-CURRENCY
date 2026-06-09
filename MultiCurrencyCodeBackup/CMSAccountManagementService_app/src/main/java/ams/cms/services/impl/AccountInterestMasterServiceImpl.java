package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.AccountInterestMasterDao;
import ams.cms.model.AccountInterestMaster;
import ams.cms.services.AccountInterestMasterService;

@Transactional
@Service
public class AccountInterestMasterServiceImpl implements AccountInterestMasterService
{
	@Autowired
	private AccountInterestMasterDao accountInterestMasterDao;
	
	@Override
	public List<AccountInterestMaster> getOutStandingInterestList(AccountInterestMaster accountInterestMaster) throws Exception {
		return accountInterestMasterDao.getOutStandingInterestList(accountInterestMaster);
	}
	
}
