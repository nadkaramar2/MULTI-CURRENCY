package ams.cms.services;

import java.util.List;

import ams.cms.api.model.AccountMaster;
import ams.cms.model.AccountTypeMaster;

public interface AccountTypeMasterService 
{
	AccountTypeMaster saveAccountTypeMaster(AccountTypeMaster accountTypeMaster) throws Exception;
	
	List<AccountTypeMaster> getNonCreditAccounTypeObject(AccountTypeMaster accountTypeMster);
	
	List<AccountTypeMaster> getAllAccountTypeMasterList() throws Exception;
	
	AccountTypeMaster getAccountTypeObject(AccountTypeMaster accountTypeMster) throws Exception;
	
	int updateLastAccountNumber(AccountTypeMaster accountTypeMster) throws Exception;
	
	String getLastAccounNumberBasedOnAccountType(String accountType) throws Exception;
	
	List<AccountTypeMaster> getAccountTypeMastersByParticipantWise(String participantId) throws Exception;
	
	boolean isCreditTypeAccount(AccountTypeMaster accountTypeMster) throws Exception;
	
	boolean isAccounTypeAlreadyExist(AccountTypeMaster accountTypeMster) throws Exception;

	List<AccountTypeMaster> getYCreditAccounTypeObject(AccountTypeMaster accountTypeMaster);
	
	int updateIsRevolvingCredit(AccountTypeMaster accountTypeMaster) throws Exception;
	
	int updateIsRevolvingCreditFromMcc(AccountTypeMaster accountTypeMaster) throws Exception;
	
	String getisRevolingCredit(String accountType);
	
	List<AccountTypeMaster> getCreditAccounTypeObject(AccountTypeMaster accountTypeMaster);

	//created by ankit
	List<AccountTypeMaster> getAccountDescription(AccountTypeMaster accountTypeMaster);
	//created by ankit
	
	//created by ankit on 18-04-2023
	List<AccountTypeMaster> getOnlyAccountStatus(AccountTypeMaster accountTypeMaster);
	//created by ankit on 18-04-2023

	//created by ankit on 18-04-2023
	List<AccountTypeMaster> getGLAccontNoAndType(AccountTypeMaster accountTypeMaster);
	//created by ankit on 18-04-2023
	
	AccountTypeMaster getGLAccTypeAccNoOnAccType(AccountTypeMaster accountTypeMaster);
	
	boolean isGLAccountTypeAlreadyExist(AccountTypeMaster accountTypeMaster);

	List<AccountTypeMaster> getAccountTypeMasterDetailsBasedOnAccountType(AccountTypeMaster accountTypeMaster);

	int updateAccountTypeDetails(AccountTypeMaster accountTypeMaster);

	AccountTypeMaster getAccountTypeMasterDetailsBasedOnAccountMasterAccountType(AccountMaster accountTypeMaster);
	
	//added new by -ankit-
	void updateNubanSerialNumberByAccountType(AccountTypeMaster accountTypeMaster);

	List<AccountTypeMaster> getAllAccountTypeMasterCodeList();
	//added new by -ankit-
	
	boolean isCreditAccounType(AccountTypeMaster accountTypeMster) throws Exception;
}
