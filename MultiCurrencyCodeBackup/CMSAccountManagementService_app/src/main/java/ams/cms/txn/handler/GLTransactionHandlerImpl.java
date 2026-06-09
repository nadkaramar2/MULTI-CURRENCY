package ams.cms.txn.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.config.TransactionConfig;
import ams.cms.constants.TransactionType;
import ams.cms.logger.AMSLogger;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.services.GLAccountStatementService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.utility.Utils;

@Component
public class GLTransactionHandlerImpl implements GLTransactionHandler 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(GLTransactionHandlerImpl.class);
	
	@Autowired
	GLAccountTypeMasterService glAccountTypeMasterService;
	
	@Autowired
	GLAccountStatementService glAccountStatementService;
	
	@Override
	public void addAndUpdateGLAccount(TransactionConfig transactionConfig) throws Exception 
	{
		GLAccountTypeMaster glAccountTypeMaster = transactionConfig.getGlAccountTypeMaster();
		
		String tranMode = TransactionType.MODE.get(glAccountTypeMaster.getTranType());
		//Linked GL Account Relation Entries with updating balance Start
		GLAccountTypeMaster mappedGLAccountTypeMaster = glAccountTypeMasterService.getMappedGLAccountTypeMasterObjWithAccountType(glAccountTypeMaster);
		amsLogger.writeInfoLog("mappedGLAccountTypeMaster::"+mappedGLAccountTypeMaster);
		if (mappedGLAccountTypeMaster != null && mappedGLAccountTypeMaster.getStrID() != null) 
		{
			mappedGLAccountTypeMaster.setTranType(glAccountTypeMaster.getTranType());
			mappedGLAccountTypeMaster.setTranId(glAccountTypeMaster.getTranId());
			mappedGLAccountTypeMaster.setTranMode(tranMode);
			
			GLAccountStatement glAccountStatement = getGLAccountStatementEntity(transactionConfig.getTxnAmount(), mappedGLAccountTypeMaster);			
			glAccountStatementService.addGlAccountStatementData(glAccountStatement);
			
			updateGLAccountTypeMaster(transactionConfig.getTxnAmount(), mappedGLAccountTypeMaster);
		}
		//Linked GL Account Relation Entries with updating balance End
		
		//Non Linked GL Account Relation Entries with updating balance Start
		
		if (glAccountTypeMaster!=null && glAccountTypeMaster.getStrGLAccountType()!= null &&glAccountTypeMaster.getStrGLAccountType().trim().length() > 0) 
		{
			GLAccountTypeMaster channelBasedGlAccountTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(glAccountTypeMaster);
			amsLogger.writeInfoLog("channelBasedGlAccountTypeMaster::"+channelBasedGlAccountTypeMaster);
			if (channelBasedGlAccountTypeMaster != null && channelBasedGlAccountTypeMaster.getStrID() != null) 
			{
				amsLogger.writeInfoLog("addAndUpdateGLAccount:: tranType=["+tranMode+"]");
				if ("DEBIT".equalsIgnoreCase(tranMode)) 
				{
					tranMode = "CREDIT";
				}
				else
				{
					tranMode = "DEBIT";
				}
				amsLogger.writeInfoLog("addAndUpdateGLAccount::Updated tranType=["+tranMode+"]");
				
				channelBasedGlAccountTypeMaster.setTranType(glAccountTypeMaster.getTranType());
				channelBasedGlAccountTypeMaster.setTranMode(tranMode);
				channelBasedGlAccountTypeMaster.setTranId(glAccountTypeMaster.getTranId());
				
				GLAccountStatement glAccountStatement = getGLAccountStatementEntity(transactionConfig.getTxnAmount(), channelBasedGlAccountTypeMaster);			
				glAccountStatementService.addGlAccountStatementData(glAccountStatement);
				
				updateGLAccountTypeMaster(transactionConfig.getTxnAmount(), channelBasedGlAccountTypeMaster);
			}
		}
		
		//Non Linked GL Account Relation Entries with updating balance End
	}

	private void updateGLAccountTypeMaster(String txnAmount, GLAccountTypeMaster glAccountTypeMaster) throws Exception 
	{
		String closingBalance = glAccountTypeMaster.getStrClosingBalance();
		closingBalance = (closingBalance!=null && Double.parseDouble(closingBalance) > 0) ? closingBalance : "0";
		Double closingBalanceInDouble = Double.parseDouble(glAccountTypeMaster.getStrClosingBalance());
		
		if ("DEBIT".equalsIgnoreCase(glAccountTypeMaster.getTranMode())) 
		{
			closingBalanceInDouble = closingBalanceInDouble - Double.parseDouble(txnAmount) ;
		}
		else 
		{
			closingBalanceInDouble = closingBalanceInDouble + Double.parseDouble(txnAmount);
		}
		
		String updatedClosingBalance = Utils.decimalFormat.format(closingBalanceInDouble);
		amsLogger.writeInfoLog("updateGLAccountTypeMaster:: updatedClosingBalance=["+updatedClosingBalance+"]");
		glAccountTypeMaster.setStrClosingBalance(updatedClosingBalance);
		
		glAccountTypeMasterService.updateGLAccountTypeDetails(glAccountTypeMaster);
	}
	
	private GLAccountStatement getGLAccountStatementEntity(String txnAmount, GLAccountTypeMaster glAccountTypeMaster) 
	{
		GLAccountStatement gLAccountStatement = new GLAccountStatement();
		gLAccountStatement.setStrGLAccountType(glAccountTypeMaster.getStrGLAccountType());
		gLAccountStatement.setStrRef(glAccountTypeMaster.getStrGLAccountDescription());		
		gLAccountStatement.setStrAccountNumber(glAccountTypeMaster.getStrAccountNumber());		
		gLAccountStatement.setStrTxnId(glAccountTypeMaster.getTranId());		
		gLAccountStatement.setTransactionDate(Utils.getCurrentDate());		
		gLAccountStatement.setStrTranType(glAccountTypeMaster.getTranType());		
		gLAccountStatement.setStrTranMode(glAccountTypeMaster.getTranMode());	
		Double txnAmountInDouble = Double.parseDouble(txnAmount);
		
		gLAccountStatement.setStrAmount(Utils.decimalFormat.format(txnAmountInDouble));
		
		return gLAccountStatement;
	}

	@Override
	public GLAccountTypeMaster creatingDebitGLAccountTypeMaster(TransactionConfig transactionConfig) throws Exception
	{
		GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
		glAccountTypeMaster.setTranType("WDL");		
		glAccountTypeMaster.setTranId(transactionConfig.getTxnId());		
		glAccountTypeMaster.setStrGLAccountType(transactionConfig.getGlAccountType()); 		//glAccountTypeMaster.setStrGLAccountType("ULB");
		glAccountTypeMaster.setStrGLAccountDescription(transactionConfig.getGlAccountDescription()); //glAccountTypeMaster.setStrGLAccountDescription(TransactionType.LOAD_BAL);
		return glAccountTypeMaster;
	}

	@Override
	public GLAccountTypeMaster creatingCreditGLAccountTypeMaster(TransactionConfig transactionConfig) throws Exception
	{
		GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
		glAccountTypeMaster.setTranType("DPT");		
		glAccountTypeMaster.setTranId(transactionConfig.getTxnId());		
		glAccountTypeMaster.setStrGLAccountType(transactionConfig.getGlAccountType()); 		//glAccountTypeMaster.setStrGLAccountType("ULB");
		glAccountTypeMaster.setStrGLAccountDescription(transactionConfig.getGlAccountDescription()); 		//glAccountTypeMaster.setStrGLAccountDescription(TransactionType.LOAD_BAL);
		return glAccountTypeMaster;
	}

	@Override
	public GLAccountTypeMaster getReducingGLAccountBalance(TransactionConfig transactionConfig) throws Exception 
	{
		GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
		glAccountTypeMaster.setTranId(transactionConfig.getTxnId());		
		glAccountTypeMaster.setStrGLAccountType(transactionConfig.getGlAccountType());
		glAccountTypeMaster.setStrGLAccountDescription(transactionConfig.getGlAccountDescription());
		glAccountTypeMaster.setStrAccountNumber(transactionConfig.getAccountNo());
		glAccountTypeMaster.setTranType("WDL");
		glAccountTypeMaster.setTranMode(TransactionType.MODE.get(glAccountTypeMaster.getTranType()));
		
		String closingBalance = transactionConfig.getAvailableAccountClosingBalance();
		closingBalance = (closingBalance!=null) ? closingBalance : "0";
		Double closingBalanceInDouble = Double.parseDouble(closingBalance);
		
		Double updatedClosingBalance = closingBalanceInDouble - Double.parseDouble(transactionConfig.getTxnAmount());
		
		glAccountTypeMaster.setStrClosingBalance(String.valueOf(updatedClosingBalance));
		return glAccountTypeMaster;
	}

	@Override
	public GLAccountTypeMaster getIncreasingGLAccountBalance(TransactionConfig transactionConfig) throws Exception 
	{
		GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
		glAccountTypeMaster.setTranId(transactionConfig.getTxnId());		
		glAccountTypeMaster.setStrGLAccountType(transactionConfig.getGlAccountType());
		glAccountTypeMaster.setStrGLAccountDescription(transactionConfig.getGlAccountDescription());
		glAccountTypeMaster.setStrAccountNumber(transactionConfig.getAccountNo());
		glAccountTypeMaster.setTranType("DPT");
		glAccountTypeMaster.setTranMode(TransactionType.MODE.get(glAccountTypeMaster.getTranType()));

		String closingBalance = transactionConfig.getAvailableAccountClosingBalance();
		closingBalance = (closingBalance!=null && Double.parseDouble(closingBalance) > 0) ? closingBalance : "0";
		Double closingBalanceInDouble = Double.parseDouble(closingBalance);
		
		Double updatedClosingBalance = closingBalanceInDouble + Double.parseDouble(transactionConfig.getTxnAmount());
		glAccountTypeMaster.setStrClosingBalance(String.valueOf(updatedClosingBalance));
		return glAccountTypeMaster;
	}

	@Override
	public GLAccountStatement creatingDebitGLAccountStatement(TransactionConfig transactionConfig) throws Exception 
	{
		return getGLAccountStatementEntity(transactionConfig.getTxnAmount(), transactionConfig.getGlAccountTypeMaster());
	}

	@Override
	public GLAccountStatement creatingCreditGLAccountStatement(TransactionConfig transactionConfig) throws Exception 
	{
		return getGLAccountStatementEntity(transactionConfig.getTxnAmount(), transactionConfig.getGlAccountTypeMaster());
	}
}
