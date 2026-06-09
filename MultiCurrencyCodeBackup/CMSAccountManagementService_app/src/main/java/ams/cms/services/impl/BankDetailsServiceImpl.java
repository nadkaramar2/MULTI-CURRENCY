package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.BankDetailsDao;
import ams.cms.model.BankDetails;
import ams.cms.services.BankDetailsService;

@Transactional
@Service
public class BankDetailsServiceImpl implements BankDetailsService
{
	@Autowired
	BankDetailsDao bankDetailsDao;

	@Override
	public List<BankDetails> getAllBankNameList() throws Exception 
	{
		return bankDetailsDao.getAllBankNameList();
	}

	@Override
	public BankDetails getBankDetailsInstanceBasedOnParam(BankDetails bankDetails) throws Exception 
	{
		return bankDetailsDao.getBankDetailsInstanceBasedOnParam(bankDetails);
	}

}
