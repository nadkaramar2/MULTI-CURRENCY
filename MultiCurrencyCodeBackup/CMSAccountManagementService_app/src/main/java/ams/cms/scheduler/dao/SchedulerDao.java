package ams.cms.scheduler.dao;

import java.util.List;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.RevolvingCreditCardTxnMaster;

import ams.cms.model.AccountCreation;
import ams.cms.scheduler.model.AccountCreditCardStatement;
import ams.cms.scheduler.model.AccountWiseInterestMaster;
import ams.cms.scheduler.model.RevolvingCreditCardInterest;
import ams.cms.scheduler.schedule.SchedularModel;

public interface SchedulerDao 
{
	List<AccountWiseInterestMaster> getCreditAccountWiseInterestList(AccountWiseInterestMaster accountwiseInterestMaster);
	
	int [] batchProcessForInsertAcoountWiseInterest(List<AccountWiseInterestMaster> accountwiseInterestMasters);
	
	List<AccountCreditCardStatement> getAccountCreditStatementlist(AccountCreditCardStatement accountCreditCardStatement);
	
	int[] batchProcessForInsertAccountCreditCardStatement(List<AccountCreditCardStatement> accountCreditCardStatement);
	
	List<AccountCreditCardStatement> getListOfCustomerAccountForBilling();
	
	List<AccountCreditCardStatement> getStatementListOfCustomer(AccountCreditCardStatement accountCreditCardStatement);
	
	int updateLastBillingCycleDateOFAccountType(AccountCreditCardStatement accountCreditCardStatement);

	List<RevolvingCreditCardInterest> getRevolvingCreditInterestTxn(RevolvingCreditCardInterest revolvingCreditCardInterest);

	int[] batchProcessForInsertRevolvingCreditCardInterest(List<RevolvingCreditCardInterest> revolvingCreditCardInterest);
	
	List<AccountCreation> getUserListForPaymentDueNotify();
	
	int updateNotifyPaymentDueDateOfAccountMaster(AccountCreation accountCreation);
	
	int updatePaymentDueDateOFAccountMaster(AccountCreditCardStatement accountCreditCardStatement);
	
	List<RevolvingCreditCardTxnMaster> getRevolvingCreditcardTxnMasterAccountWise(RevolvingCreditCardInterest revolvingCreditCardTxnMaster);
	
	List<SchedularModel> getScheduleInfo();
	
	int updateScheduleInfo(SchedularModel schedularModel);
}
