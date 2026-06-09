package ams.cms.config;
  
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountStatement;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.MultiCurrencyWalletAccountMaster;
import ams.cms.model.TxnData;
import ams.cms.model.TxnReqRes;
import ams.cms.util.ProcessWebResponse;
  
public interface TransactionHandler 
{
	void getTransactionData(AccountCreation accountCreation ) throws Exception;
  
	void addTranMasterRequestEntry(TxnReqRes txnReqRes);
	
	void addTranMasterResponseEntry(TxnReqRes txnReqRes);
	
	TxnReqRes processTransaction(TxnReqRes txnReqRes); 
	
	TxnReqRes validationProcess(TxnReqRes txnReqRes, AccountCreation accountCreation);
	
	TxnReqRes validateTxnLimits(TxnReqRes txnReqRes, AccountCreation accountCreation);
	
	TxnReqRes validateBalance(TxnReqRes txnReqRes, AccountCreation accountCreation);
	
	TxnReqRes validateCreditLimits(TxnReqRes txnReqRes, AccountCreation accountCreation);
	
	TxnReqRes updatesAccountMaster(TxnReqRes txnReqRes, AccountCreation accountCreation);
	
	TxnReqRes updatesTxnLimits(TxnReqRes txnReqRes, AccountCreation accountCreation);
	
	TxnReqRes updatesCreditLimits(TxnReqRes txnReqRes, AccountCreation accountCreation);
	
	TxnReqRes addProcess(TxnReqRes txnReqRes, AccountCreation accountCreation);
	
	TxnReqRes addCreditCardTxns(TxnReqRes txnReqRes, AccountCreation accountCreation);
	
	TxnReqRes updateAccountBalance(TxnReqRes txnReqRes, AccountCreation accountCreation);
	
	TxnReqRes addAccountStatmentEntry(TxnReqRes txnReqRes, AccountCreation accountCreation);

	TxnReqRes viewAccountMasterDetails(TxnData txnData); 
	
public MultiCurrencyWalletAccountMaster updateClosingBalance(ProcessWebResponse processWebResponse);
	
	public MultiCurrencyWalletAccountMaster updateClosingBalanceFee(ProcessWebResponse processWebResponse);
	
	public MultiCurrencyWalletAccountMaster updateClosingBalanceGst(ProcessWebResponse processWebResponse);
	
	public GLAccountTypeMaster updateGlClosingBalance(ProcessWebResponse processWebResponse);
	
	public GLAccountTypeMaster updateFeeGlClosingBalance(ProcessWebResponse processWebResponse);
	
	public GLAccountTypeMaster updateGstGlClosingBalance(ProcessWebResponse processWebResponse);
	
	AccountStatement addAccountTransactionData(ProcessWebResponse processWebResponse) throws Exception; 
	
	void addGLStatements(ProcessWebResponse processWebResponse);
	
	public int updateClosingBalanceWithSameAmount(ProcessWebResponse processWebResponse);
	
	public int addClosingBalanceInWallet(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster);
	
	public void addBatchEntryAccountStatment(ProcessWebResponse processWebResponse) throws Exception;
	
	public GLAccountTypeMaster updateLinkedGl(ProcessWebResponse processWebResponse);
	
	public GLAccountStatement addGLAccountStatment(ProcessWebResponse processWebResponse);
	
	public MultiCurrencyWalletAccountMaster w2wupdateClosingBalance(ProcessWebResponse processWebResponse);
	
	public MultiCurrencyWalletAccountMaster w2wupdateClosingBalanceFee(ProcessWebResponse processWebResponse);
	
	public MultiCurrencyWalletAccountMaster w2wupdateClosingBalanceGst(ProcessWebResponse processWebResponse);
	
	public void w2waddBatchEntryAccountStatment(ProcessWebResponse processWebResponse) throws Exception;
	
	void w2wAddGLStatements(ProcessWebResponse processWebResponse);

	public AccountStatement w2wAddAccountTransactionData(ProcessWebResponse processWebResponse) throws Exception;
}
 