package ams.cms.services.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.AccountLoadMasterDao;
import ams.cms.model.AccountLoadMaster;
import ams.cms.services.AccountLoadMasterService;

@Transactional
@Service
public class AccountLoadMasterServiceImpl implements AccountLoadMasterService
{
	@Autowired
	AccountLoadMasterDao accountLoadMasterDao;
	
	@Override
	public AccountLoadMaster saveAccountLoadMasterInfo(AccountLoadMaster accountLoadMaster) throws Exception 
	{
		accountLoadMaster.setDateOfLoading(new Date());
		accountLoadMasterDao.save(accountLoadMaster);
		return accountLoadMaster;
	}
}
