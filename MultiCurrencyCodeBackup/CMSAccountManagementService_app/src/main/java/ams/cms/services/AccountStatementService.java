package ams.cms.services;

import java.util.List;

import ams.cms.api.model.AccountStatementResponse;
import ams.cms.api.model.AccountStatementSortResponse;
import ams.cms.api.model.AccountTranscationResponse;
import ams.cms.api.response.TransactionSummaryDetails;
import ams.cms.api.response.TxnStatementDetailsMaster;
import ams.cms.model.AccountStatement;
import ams.cms.model.TransactionInformation;

public interface AccountStatementService 
{
	AccountStatement addAccountStatement(AccountStatement accountStatement) throws Exception;
	
	AccountStatement addAccountTransactionData(AccountStatement accountStatement) throws Exception;
	
	List<AccountStatement> getAccountStatementBasedonParameter(AccountStatement accountStatement) throws Exception;

	List<AccountStatementResponse> getAccountStatementlist(AccountStatement accountStatement) throws Exception;

	List<AccountStatementSortResponse>  updateAccountStatementlist(AccountStatement accountStatement) throws Exception;	
	
	List<AccountStatement> getAccountStatements(AccountStatement accountStatement) throws Exception;
	
	List<AccountStatement> getAccountStatementByDate(AccountStatement accountStatement) throws Exception;
	
	AccountStatement saveSenderAccountstAccountStatement(AccountStatement accountStatement) throws Exception;
	
	AccountStatement saveRecipientAccountStatement(AccountStatement accountStatement) throws Exception;
	
	//Add for Account Transaction list data filter by sagark-- Start
	List<AccountTranscationResponse> getAccountTranscation(AccountStatement accountStatement);

	List<AccountTranscationResponse> getLastFiveAccountTxn(AccountStatement accountStatement);
	//Add for Account Transaction list data filter by sagark-- End
	
	AccountStatement addAccountStatementData(AccountStatement accountStatement);	
	
	int[] batchEntryOfAccountStatementMaster(List<AccountStatement> accountStatements) throws Exception;
	
	List<AccountTranscationResponse> getAccountTransaction(AccountStatement accountStatement, TransactionInformation transactionInformation);
	
	List<AccountTranscationResponse> getAgencyAccountStatementTransactionList(AccountStatement accountStatement, TransactionInformation transactionInformation);
	
	List<AccountTranscationResponse> getLastFiveAccountTransaction(AccountStatement accountStatement, TransactionInformation transactionInformation);
	
	List<AccountTranscationResponse> getLastFiveAgencyAccountStatementTransactionList(AccountStatement accountStatement, TransactionInformation transactionInformation);
	
	TransactionInformation getTotalCreditDebitAmountAndCount(AccountStatement accountStatement);
	//Added By Sunil Y , New StatementView Apis , [2023-10-05] Started 
	TransactionSummaryDetails getAllTransactionSummaryDetails(AccountStatement accountStatement);

	TxnStatementDetailsMaster viewAccountStatementDetails(AccountStatement accountStatement);
	//Added By Sunil Y , New StatementView Apis , [2023-10-05] End 

	void addBatchEntryAccountTransactionData(List<AccountStatement> accountStatementList);

	List<AccountStatementResponse> getlastFiveWalletAccountStatements(AccountStatement accountStatement);

	List<AccountStatementResponse> getWalletAccountStatementsDateWise(AccountStatement accountStatement);
}
