package ams.cms.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.config.TransactionConfig;
import ams.cms.config.TransactionIdCreationConfigDao;
import ams.cms.constants.TransactionType;
import ams.cms.handler.AccountWiseChargesHandler;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountStatement;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.AccountTypeMaster;
import ams.cms.model.AccountWiseCharges;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountStatementService;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.AccountTypeMasterService;
import ams.cms.services.GLAccountStatementService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.utility.Utils;

@Component
public class AccountWiseChargesHandlerImpl implements AccountWiseChargesHandler {

	@Autowired
	AccountMasterService accountMasterService;

	@Autowired
	GLAccountTypeMasterService glAccountTypeMasterService;

	@Autowired
	AccountStatementService accountStatementService;

	@Autowired
	GLAccountStatementService glAccountStatementService;

	@Autowired
	AccountTypeMasterService accountTypeMasterService;
	
	@Autowired
	AccountTranMasterService accountTranMasterService;
	
	@Autowired
	TransactionIdCreationConfigDao transactionIdCreationConfigDao;

	@Override
	public AccountWiseCharges processToCharge(AccountWiseCharges accountWiseCharges) 
	{
		String txnId = null;
		
		AccountCreation accountCreation = new AccountCreation();
		accountCreation.setStrAccountNumber(accountWiseCharges.getStrAccountNumber());
		accountCreation.setStrAccountType(accountWiseCharges.getStrAccountType());
		accountCreation.setStrChargeType(accountWiseCharges.getStrChargeType());
		
		accountCreation = accountMasterService.getClosingBalanceonAccTypeAccNo(accountCreation);

		accountWiseCharges.setStrAccountType(accountCreation.getStrAccountType());
		accountWiseCharges.setStrAccountNumber(accountCreation.getStrAccountNumber());
		accountWiseCharges.setStrChargeType(accountCreation.getStrChargeType());
		accountWiseCharges.setStrAmount(accountCreation.getStrAmount());
		try {
			txnId = transactionIdCreationConfigDao.getTransactionId();
			accountWiseCharges.setStrTransactionId(txnId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		accountWiseCharges.setAccountCreation(accountCreation);

		updateClosingBalance(accountWiseCharges);  

		updateGLAccountClosingBalance(accountWiseCharges);    
 
		addEntryInAccountStatement(accountWiseCharges);     

		addDebitEntryInGLAccountStatement(accountWiseCharges);  

		addCreditEntryInGLAccountStatemt(accountWiseCharges);  

		addEntryInAccountTranMaster(accountWiseCharges);      

		return accountWiseCharges;

	}

	private void updateClosingBalance(AccountWiseCharges accountWiseCharges) {
		try {
			AccountCreation accountCreation = accountWiseCharges.getAccountCreation();
			double chargeAmount = Utils.stringToDouble(accountCreation.getStrAmount());
			double updatableClosingBalance = Utils.stringToDouble(accountCreation.getStrClosingBalance()) - Utils.stringToDouble(accountCreation.getStrAmount());

			accountCreation.setStrClosingBalance(Utils.decimalFormat.format(updatableClosingBalance));

			accountMasterService.updateClosingBalance(accountCreation);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateGLAccountClosingBalance(AccountWiseCharges accountWiseCharges) {
		try {
			GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
			glAccountTypeMaster.setStrGLAccountType(accountWiseCharges.getStrChargeType());
			glAccountTypeMaster = glAccountTypeMasterService.getGLAccountNumberOnAccountType(glAccountTypeMaster);
			

			accountWiseCharges.setStrGLAccountNumber(glAccountTypeMaster.getStrAccountNumber());
			accountWiseCharges.setStrClosingBalance(glAccountTypeMaster.getStrClosingBalance());
			accountWiseCharges.setStrAccountType(glAccountTypeMaster.getStrGLAccountType());

			if (glAccountTypeMaster.getStrGLAccountType() != null && glAccountTypeMaster.getStrAccountNumber() != null) {
				double GlclosingBalance = Utils.stringToDouble(glAccountTypeMaster.getStrClosingBalance()) + Utils.stringToDouble(accountWiseCharges.getStrAmount());
				glAccountTypeMaster.setStrClosingBalance(Utils.decimalFormat.format(GlclosingBalance));
				glAccountTypeMasterService.updateGLAccountTypeDetails(glAccountTypeMaster);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addEntryInAccountStatement(AccountWiseCharges accountWiseCharges) {
		try {
			AccountStatement accountStatement = new AccountStatement();
			accountStatement.setStrAccountNumber(accountWiseCharges.getStrAccountNumber());
			accountStatement.setStrAccountType(accountWiseCharges.getStrAccountType());
			accountStatement.setStrClosingBalance(accountWiseCharges.getStrClosingBalance());
			accountStatement.setStrIsGLType("N");
			accountStatement.setStrTransactionAmount(accountWiseCharges.getStrAmount());
			accountStatement.setStrNaration("Transaction Successful.");
			accountStatement.setStrTransactionID(accountWiseCharges.getStrTransactionId());
			accountStatement.setStrTranType("WDL");
			accountStatement.setStrTranMode(TransactionType.MODE.get(accountStatement.getStrTranType()));  //DEBIT

			accountStatementService.addAccountStatementData(accountStatement);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addDebitEntryInGLAccountStatement(AccountWiseCharges accountWiseCharges) {
		try {
			
		
			
			AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
			accountTypeMaster = accountTypeMasterService.getGLAccTypeAccNoOnAccType(accountTypeMaster);
			accountTypeMaster.setStrGLAccountType(accountWiseCharges.getStrAccountType());
			accountTypeMaster.setStrGLAccountNumber(accountWiseCharges.getStrGLAccountNumber());
			
			if (accountTypeMaster.getStrGLAccountType() != null && accountTypeMaster.getStrGLAccountNumber() != null) {
				GLAccountStatement glAccountStatement = new GLAccountStatement();
				glAccountStatement.setStrGLAccountType(accountWiseCharges.getStrAccountType());
				glAccountStatement.setStrAccountNumber(accountWiseCharges.getStrAccountNumber());
				glAccountStatement.setStrRef("Charges Applied");
				glAccountStatement.setStrTxnId(accountWiseCharges.getStrTransactionId());
				glAccountStatement.setStrAmount(accountWiseCharges.getStrAmount());
				glAccountStatement.setStrTranType("WDL");
				glAccountStatement.setStrTranMode(TransactionType.MODE.get(glAccountStatement.getStrTranType())); // DEBIT
				glAccountStatement.setTransactionDate(Utils.getCurrentDate());
				glAccountStatement.setStrCreated_by("System");
				
				glAccountStatementService.addGLAccountStatementData(glAccountStatement);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addCreditEntryInGLAccountStatemt(AccountWiseCharges accountWiseCharges) {
		try {
			
			GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
			glAccountTypeMaster = glAccountTypeMasterService.getGLAccountNumberOnAccountType(glAccountTypeMaster);
			glAccountTypeMaster.setStrAccountNumber(accountWiseCharges.getStrGLAccountNumber());
			glAccountTypeMaster.setStrGLAccountType(accountWiseCharges.getStrChargeType());

			if (glAccountTypeMaster.getStrAccountNumber() != null) {
				GLAccountStatement glAccountStatement = new GLAccountStatement();
				glAccountStatement.setStrGLAccountType(accountWiseCharges.getStrChargeType());
				glAccountStatement.setStrAccountNumber(accountWiseCharges.getStrAccountNumber());
				glAccountStatement.setStrRef("Charges Applied");
				glAccountStatement.setStrTxnId(accountWiseCharges.getStrTransactionId());
				glAccountStatement.setStrAmount(accountWiseCharges.getStrAmount());
				glAccountStatement.setStrTranType("DPT");
				glAccountStatement.setStrTranMode(TransactionType.MODE.get(glAccountStatement.getStrTranType())); // CREDIT
				glAccountStatement.setTransactionDate(Utils.getCurrentDate());
				glAccountStatement.setStrCreated_by("System");
				
				glAccountStatementService.addGLAccountStatementData(glAccountStatement);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addEntryInAccountTranMaster(AccountWiseCharges accountWiseCharges) 
	{
		AccountTranMaster accountTranMaster = new AccountTranMaster();
		accountTranMaster.setStrTxn_id(accountWiseCharges.getStrTransactionId());
		accountTranMaster.setStrParticipantId(accountWiseCharges.getParticipantId());
		accountTranMaster.setStrSys_id(null);
		accountTranMaster.setStrMti(null);
		accountTranMaster.setStrTran_type(accountWiseCharges.getStrChargeType());
		accountTranMaster.setStrProcessingCode(null);
		accountTranMaster.setStrTransaction_amount(accountWiseCharges.getStrAmount());
		accountTranMaster.setStrResponseCode(null);
		accountTranMaster.setStrAuthCode(null);
		accountTranMaster.setStrAccountNumber(accountWiseCharges.getStrAccountNumber());
		accountTranMaster.setStrFrom_account_number(accountWiseCharges.getStrAccountNumber());
		accountTranMaster.setStrTo_account_number(accountWiseCharges.getStrGLAccountNumber());
		
		accountTranMasterService.addEntryInAccountTranMasterData(accountTranMaster);
		
	}
}
