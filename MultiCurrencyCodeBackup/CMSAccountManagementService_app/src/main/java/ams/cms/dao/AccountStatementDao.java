package ams.cms.dao;

import java.util.List;

import ams.cms.model.AccountStatement;

public interface AccountStatementDao extends GenericDao<AccountStatement>
{
	List<AccountStatement> getAccountStatementBasedonParameter(AccountStatement accountStatement);
	List<AccountStatement> getAccountStatementlist(AccountStatement accountStatement);	
	List<AccountStatement> updateAccountStatementlist(AccountStatement accountStatement);	
	List<AccountStatement> getAccountStatements(AccountStatement accountStatement);	
	List<AccountStatement> getAccountStatementByDate(AccountStatement accountStatement);	
	//Add for Account Transaction list data filter by sagar k [Start]
	List<AccountStatement> getAccountTranscation(AccountStatement accountStatement);	
	List<AccountStatement> getLastFiveAccountTxn(AccountStatement accountStatement);
	List<AccountStatement> getLastFiveAccountTxnForMontra(AccountStatement accountStatement);
	//Add for Account Transaction list data filter by sagar K [End]
	
	int[] batchEntryOfAccountStatementMaster(List<AccountStatement> accountStatements);
	List<AccountStatement> getAgencyAccountStatementTranscationList(AccountStatement accountStatement);
	
	List<AccountStatement> getLastFiveAccountTransaction(AccountStatement accountStatement);
	List<AccountStatement> getLastFiveAgencyAccountStatementTransactionList(AccountStatement accountStatement);
	
	List<AccountStatement> getTotalTransModeAndTransactionAmount(AccountStatement accountStatement);
	//Added by Sunil Y , 2023-10-05 , Started
	List<AccountStatement> getDistinctListOfAccountStatement(AccountStatement accountStatement);
	List<AccountStatement> viewAccountStatementTxnDetails(AccountStatement accountStatement);
	//Added by Sunil Y , 2023-10-05 , End
	List<AccountStatement> getLastFiveAccountStatementBasedonParameter(AccountStatement accountStatement);
	List<AccountStatement> getWalletAccountStatementBasedonParameter(AccountStatement accountStatement);
}
