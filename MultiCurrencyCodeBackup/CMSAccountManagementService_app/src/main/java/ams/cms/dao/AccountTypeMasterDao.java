package ams.cms.dao;

import java.util.List;

import ams.cms.api.model.AccountMaster;
import ams.cms.model.AccountTypeMaster;

public interface AccountTypeMasterDao extends GenericDao<AccountTypeMaster>
{
	AccountTypeMaster getAccountTypeObject(AccountTypeMaster accountTypeMster);
	
	List<AccountTypeMaster> getNonCreditAccounTypeObject(AccountTypeMaster accountTypeMster);
	
	int updateLastAccountNumber(AccountTypeMaster accountTypeMster);
	
	String getLastAccounNumberBasedOnAccountType(String accountType);
	
	List<AccountTypeMaster> getAccountTypeMastersByParticipantWise(String participantId);	
	
	boolean isAccounTypeAlreadyExist(AccountTypeMaster accountTypeMster);

	List<AccountTypeMaster> getYCreditAccounTypeObject(AccountTypeMaster accountTypeMaster);
	
	int updateIsRevolvingCreditFromMcc(AccountTypeMaster accountTypeMaster);

	int updateIsRevolvingCredit(AccountTypeMaster accountTypeMaster);
	
	String getisRevolingCredit(String accountType);
	
	List<AccountTypeMaster> getCreditAccounTypeObject(AccountTypeMaster accountTypeMaster);

	//created by ankit
	List<AccountTypeMaster> getAccountDescription(AccountTypeMaster accountTypeMaster);
	//created by ankit
	
	//created by ankit on 18-04-2023
	List<AccountTypeMaster> getOnlyAccountStatus(AccountTypeMaster accountTypeMaster);
	//created by ankit on 18-04-2023

	//created by ankit on 18-04-2023
	List<AccountTypeMaster> getGLAccountNoAndType(AccountTypeMaster accountTypeMaster);
	//created by ankit on 18-04-2023

	AccountTypeMaster getGLAccTypeAccNoOnAccType(AccountTypeMaster accountTypeMaster);

	boolean isGLAccountTypeAlreadyExist(AccountTypeMaster accountTypeMaster);

	List<AccountTypeMaster> getAccountTypeMasterDetailsBasedOnAccountType(AccountTypeMaster accountTypeMaster);

	int updateAccountTypeDetails(AccountTypeMaster accountTypeMaster);

	AccountTypeMaster getAccountTypeMasterBasedOnAccountType(AccountMaster accountTypeMaster);
	
	AccountTypeMaster getAccountTypeCode(AccountTypeMaster accountTypeMaster);
	
	void updateNubanSerialNumberByAccountType(AccountTypeMaster accountTypeMaster);
	
	boolean isCreditAccounType(AccountTypeMaster accountTypeMster);
}
