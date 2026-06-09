package ams.cms.services;

import java.util.List;

import ams.cms.model.BankDetails;
import ams.cms.utility.BankDetailsResponse;

public interface BankDetailsService 
{
	List<BankDetails> getAllBankNameList() throws Exception;
	
	BankDetails getBankDetailsInstanceBasedOnParam(BankDetails bankDetails) throws Exception;
}
