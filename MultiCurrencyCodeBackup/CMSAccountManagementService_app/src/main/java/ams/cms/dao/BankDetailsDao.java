package ams.cms.dao;

import java.util.List;

import ams.cms.model.BankDetails;

public interface BankDetailsDao extends GenericDao<BankDetails> 
{
	List<BankDetails> getAllBankNameList();
	
	BankDetails getBankDetailsInstanceBasedOnParam(BankDetails bankDetails);
}
