package ams.cms.scheduler.service;

import java.util.List;

import ams.cms.model.AccountCreation;
import ams.cms.scheduler.model.AccountCreditCardStatement;
import ams.cms.scheduler.model.AccountWiseInterestMaster;
import ams.cms.scheduler.model.RevolvingCreditCardInterest;
import ams.cms.scheduler.schedule.SchedularModel;
import ams.cms.model.RevolvingCreditCardTxnMaster;

public interface SchedulerService 
{
	void processInsertAcoountWiseInterest(List<AccountWiseInterestMaster> accountwiseInterestMasters) throws Exception;
	
	List<AccountWiseInterestMaster> getCreditAccountWiseInterestList(AccountWiseInterestMaster accountwiseInterestMaster) throws Exception;

	List<AccountCreditCardStatement> getAccountCreditStatementlist(AccountCreditCardStatement accountCreditCardStatement) throws Exception;
     
	void processInsertAccountCreditCardStatement(List<AccountCreditCardStatement> accountCreditCardStatement) throws Exception;

	void processBillingCycle(List<AccountCreditCardStatement> accountCreditCardStatements) throws Exception;
	
	void sendFileAttachment(String toMail, String fromMail, String subject, String bodyMsg, String attachmentFilePath);
	
	String getCreditCardStatementAttachmentPath(AccountCreditCardStatement accountCreditCardStatement) throws Exception;
	
	List<AccountCreditCardStatement> getListOfCustomerAccountForBilling() throws Exception;
	
	List<AccountCreditCardStatement> getStatementListOfCustomer(AccountCreditCardStatement accountCreditCardStatement) throws Exception;
	
	void processInsertRevolvingCreditCardInterest(List<RevolvingCreditCardInterest> revolvingCreditCardInterest);
	
	List<RevolvingCreditCardInterest> getRevolvingCreditInterestTxn(RevolvingCreditCardInterest revolvingCreditCardInterest) throws Exception;
	
	List<AccountCreation> getUserListForPaymentDueNotify() throws Exception;
	
	void sendPaymentDueNotification(List<AccountCreation> accountCreations) throws Exception;	
	
	void sendAttachment(AccountCreditCardStatement accountCreditCardStatement) throws Exception;
	
	List<SchedularModel> getScheduleInfo() throws Exception;
	
	int updateScheduleInfo(SchedularModel schedularModel);
	
	void executeSpecificBeanFromSchedular(SchedularModel schedularModel);
}
