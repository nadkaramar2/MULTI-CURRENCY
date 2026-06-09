package ams.cms.scheduler.service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ams.cms.model.AccountCreation;
import ams.cms.model.BillingCycleModel;
import ams.cms.notification.EmailTemplate;
import ams.cms.scheduler.dao.SchedulerDao;
import ams.cms.scheduler.model.AccountCreditCardStatement;
import ams.cms.scheduler.model.AccountWiseInterestMaster;
import ams.cms.scheduler.model.CreditCardHeaderModel;
import ams.cms.scheduler.model.RevolvingCreditCardInterest;
import ams.cms.scheduler.schedule.SchedularModel;
import ams.cms.services.BillingCycleService;
import ams.cms.utility.Utils;
import ams.cms.model.RevolvingCreditCardTxnMaster;

@Service
public class SchedulerServiceImpl implements SchedulerService 
{
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	SchedulerDao schedulerDao;
	
	@Autowired
	BillingCycleService billingCycleService;

	@Override
	public void processInsertAcoountWiseInterest(List<AccountWiseInterestMaster> accountwiseInterestMasters) 
	{
		try 
		{
			int[] responseArr = schedulerDao.batchProcessForInsertAcoountWiseInterest(accountwiseInterestMasters);
		 	
			if (responseArr.length > 0)
		 	{
		 		System.out.println("-------------- All RECORD INSTERED SUCCESSFULLY ------------");
		 	}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
   
	@Override
	public List<AccountWiseInterestMaster> getCreditAccountWiseInterestList(AccountWiseInterestMaster accountwiseInterestMaster)
	{
		return schedulerDao.getCreditAccountWiseInterestList(accountwiseInterestMaster);
	}

	@Override
	public List<AccountCreditCardStatement> getAccountCreditStatementlist(AccountCreditCardStatement accountCreditCardStatement) {
		return schedulerDao.getAccountCreditStatementlist(accountCreditCardStatement);

	}

	@Override
	public void processInsertAccountCreditCardStatement(List<AccountCreditCardStatement> accountCreditCardStatement)throws Exception {
		try 
		{
			int[] responseArr = schedulerDao.batchProcessForInsertAccountCreditCardStatement(accountCreditCardStatement);
		 	
			if (responseArr.length > 0)
		 	{
		 		System.out.println("-------------- All RECORD INSTERED SUCCESSFULLY CHECK it------------");
		 	}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void processBillingCycle(List<AccountCreditCardStatement> accountCreditCardStatements) throws Exception 
	{
		try 
		{
			for (AccountCreditCardStatement accountCreditCardStatement: accountCreditCardStatements) 
			{
				BillingCycleModel billingCycleModel = new BillingCycleModel();	
				
				billingCycleModel.setStrParticipantId(accountCreditCardStatement.getStrParticipantId());
				billingCycleModel.setStrAccountType(accountCreditCardStatement.getStrAccountType());
				billingCycleModel.setStrAccountNumber(accountCreditCardStatement.getStrAccountNumber());
				billingCycleModel.setStrCardType(accountCreditCardStatement.getStrCardType());
				billingCycleModel.setStrCardNumber(accountCreditCardStatement.getStrCardNumber());
				billingCycleModel.setStrAvialbleCreditLimit(accountCreditCardStatement.getStrAvailableCreditLimit());
				billingCycleModel.setStrOutstandingAmt(accountCreditCardStatement.getStrTotalOutstandingBal());
				billingCycleModel.setStrMinimumAmountDue(accountCreditCardStatement.getStrMinimumAmountDue());
				billingCycleModel.setStrLastBillingCycleDate(accountCreditCardStatement.getStrLastBillingCycleDate());
				billingCycleModel.setStrCurrentBillingCycleDate(accountCreditCardStatement.getStrCurrentBillingCycleDate());			
				
				billingCycleService.saveBillingCyleData(billingCycleModel);
				
				schedulerDao.updateLastBillingCycleDateOFAccountType(accountCreditCardStatement);
				
				//Updating payment due date Start
				if (accountCreditCardStatement.getStrPayementDueDate() == null)
				{
					schedulerDao.updatePaymentDueDateOFAccountMaster(accountCreditCardStatement);
				}
				//Updating payment due date End
				
				sendAttachment(accountCreditCardStatement);
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<AccountCreation> getUserListForPaymentDueNotify() throws Exception {
		return schedulerDao.getUserListForPaymentDueNotify();
	}

	@Override
	public void sendPaymentDueNotification(List<AccountCreation> accountCreations) throws Exception 
	{
		try 
		{
			for (AccountCreation accountCreation : accountCreations) 
			{
				boolean isRequiredToNotify = false;
				if (accountCreation.getNotifyByPaymentDate() != null) 
				{
					Date currentDate = Utils.simpleDateFormat.parse(Utils.simpleDateFormat.format(new Date()));
					Date notifyPaymentDate = accountCreation.getNotifyByPaymentDate();
					
					if (notifyPaymentDate.compareTo(currentDate) == 0 ) 
					{
						isRequiredToNotify = true;
					}
				}
				else
				{
					isRequiredToNotify = true;
				}
				System.out.println("sendPaymentDueNotification isRequiredToNotify:::["+isRequiredToNotify+"]");
				if (isRequiredToNotify)
				{
					int i = schedulerDao.updateNotifyPaymentDueDateOfAccountMaster(accountCreation);
					System.out.println("-- Updated Payment Due Date--- value of i::["+i+"]");
					if(i > 0) 
					{
						StringBuilder bodyMsg = new StringBuilder("Dear User, ");
						bodyMsg.append("<br/><br/>"); 
						bodyMsg.append( "Your Outstanding balance payment data has been crossed. Please make/clear your Outstanding payment.");
						bodyMsg.append( "<br/><br/><br/>");
						bodyMsg.append( "Powered by AMS Technologies Pvt Ltd");
						
						String subject = "Regarding Payment Due";
						
						sendFileAttachment(accountCreation.getStrEmailID(), "contactus@AMStechnologies.com", subject, bodyMsg.toString(), null);
					}
				}
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendFileAttachment(String toMail, String fromMail, String subject, String bodyMsg, String attachmentFilePath) 
	{
		try 
		{
			String serverUrl = "http://localhost:8485/AccountManagementAPI/notify-by/sendEmail";
			
			EmailTemplate emailTemplate = Utils.getEmailTemplateForSendMail(fromMail, toMail, subject, bodyMsg);
			emailTemplate.setStrPathToAttachment(attachmentFilePath);
			
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.postForObject(serverUrl , emailTemplate, String.class);
			
			System.out.println("--- ATTACHMENT SENT TO EMAIL -----------");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public String getCreditCardStatementAttachmentPath(AccountCreditCardStatement accountCreditCardStatement) throws Exception 
	{
		String attachmentFilePath = "";
		try 
		{
			RestTemplate restTemplate = new RestTemplate();
			String serverUrl = "http://localhost:1170/FileDownloaderAPI/export-pdf/getDownloadFile";
			
			HttpHeaders headers = new HttpHeaders();
			headers = new HttpHeaders();
			
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			//changes for list fetches
			Map<String, Object> mainMapData = new HashMap<>();
			
			List<AccountCreditCardStatement> accountCreditCardStatements = getStatementListOfCustomer(accountCreditCardStatement);			
			mainMapData.put("bodyInfoInList", accountCreditCardStatements); 
			
			Map<String, Object> headerDataMap = new HashMap<>();			
			CreditCardHeaderModel creditCardHeaderModel = new CreditCardHeaderModel();
			
			creditCardHeaderModel.setStrTotalOutstandingBal(accountCreditCardStatement.getStrTotalOutstandingBal());
			creditCardHeaderModel.setStrAvailableCreditLimit(accountCreditCardStatement.getStrAvailableCreditLimit());
			
			headerDataMap.put("header", creditCardHeaderModel);			
			mainMapData.put("headerInfoMap", headerDataMap);
			mainMapData.put("logoImageLocation", "D:\\AMS\\Speta\\logo\\cbimage.png");
			
			//mainMapData.put("templateName", "credit_card_statement");
			mainMapData.put("templateName", "ams_credit_card_statement");
			mainMapData.put("fileName", "credit-card-statement-");
			
			HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(mainMapData, headers);
			System.out.println("Calling .....processToGETDownloadedFile Request");
			
			attachmentFilePath = restTemplate.postForObject(serverUrl, request, String.class);
			System.out.println("attachmentFilePath::"+attachmentFilePath);
			
			if (attachmentFilePath!=null)
			{
				attachmentFilePath = "D:\\AMS\\Speta\\downloadFile\\pdf\\"+attachmentFilePath.substring(attachmentFilePath.indexOf("=")+1);
				System.out.println("FINAL attachmentFilePath::["+attachmentFilePath+"]");
			}
			
			return attachmentFilePath;
		}
		catch (Exception e) {
		}
		return null;
	}

	@Override
	public List<AccountCreditCardStatement> getListOfCustomerAccountForBilling() throws Exception {
		return schedulerDao.getListOfCustomerAccountForBilling();
	}

	@Override
	public List<AccountCreditCardStatement> getStatementListOfCustomer(AccountCreditCardStatement accountCreditCardStatement) throws Exception 
	{
		return schedulerDao.getStatementListOfCustomer(accountCreditCardStatement);
	}
	
	@Override
	public List<RevolvingCreditCardInterest> getRevolvingCreditInterestTxn(RevolvingCreditCardInterest revolvingCreditCardInterest) throws Exception 
	{
	   return schedulerDao.getRevolvingCreditInterestTxn(revolvingCreditCardInterest);
	}

	@SuppressWarnings("null")
	@Override
	public void processInsertRevolvingCreditCardInterest(List<RevolvingCreditCardInterest> revolvingCreditCardInterest) 
	{
		try 
		 {
			 for(RevolvingCreditCardInterest revolvingCreditCardInterestobj: revolvingCreditCardInterest )
			 {
				if(revolvingCreditCardInterestobj.getTotalOutstandingInterest()== 0d)
				{
					RevolvingCreditCardTxnMaster revolvingCreditCardTxnMaster = null;
					List<RevolvingCreditCardTxnMaster> revolvingCreditTxnMaster = schedulerDao.getRevolvingCreditcardTxnMasterAccountWise(revolvingCreditCardInterestobj);
					if (revolvingCreditTxnMaster!=null && revolvingCreditTxnMaster.size() > 0)
					{
						revolvingCreditCardTxnMaster = revolvingCreditTxnMaster.get(0);
					}				
					revolvingCreditCardInterestobj.setTotalOutstandingInterest(revolvingCreditCardTxnMaster.getTotalOutstandingInterest());
					revolvingCreditCardInterestobj.setStrTotalCalculatedGST(revolvingCreditCardTxnMaster.getTotalCalGstAmount());
				}
			 }
			 
			int[] responseArraylist = schedulerDao.batchProcessForInsertRevolvingCreditCardInterest(revolvingCreditCardInterest);
			System.out.println("responseArraylist::["+responseArraylist+"]");
			if (responseArraylist.length > 0)
		 	{
		 		System.out.println("-------------- All REVOLVING CREDIT CARD INSERTED SUCCESFULLY ------------");
		 	}
			
		} catch (Exception e) {
			e.printStackTrace();	
		}
	}

	@Override
	public void sendAttachment(AccountCreditCardStatement accountCreditCardStatement) throws Exception 
	{
		try 
		{
			String attachmentFilePath = getCreditCardStatementAttachmentPath(accountCreditCardStatement);				
			
			StringBuilder bodyMsg = new StringBuilder("Dear User, ");
			bodyMsg.append("<br/><br/>"); 
			bodyMsg.append( "Please find the Credit Card Statement File Attachment.");
			bodyMsg.append( "<br/><br/><br/>");
			bodyMsg.append( "Powered by AMS Technologies Pvt Ltd");
			
			String subject = "Regarding to Credit Card Statement";
			
			//sendFileAttachment(accountCreditCardStatement.getStrEmailID(), "contactus@AMStechnologies.com", subject, bodyMsg.toString(), attachmentFilePath);
			
			sendFileAttachment("ssoni@AMStechnologies.com", "contactus@AMStechnologies.com", subject, bodyMsg.toString(), attachmentFilePath);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<SchedularModel> getScheduleInfo() throws Exception 
	{
		return schedulerDao.getScheduleInfo();
 	}

	@Override
	public int updateScheduleInfo(SchedularModel schedularModel) 
	{
		return schedulerDao.updateScheduleInfo(schedularModel);
	}

	@Override
	public void executeSpecificBeanFromSchedular(SchedularModel schedularModel) 
	{
		try 
		{
			//String str = Arrays.toString(context.getBeanDefinitionNames()); //Get All Beans information
			
			Object obj = context.getBean(schedularModel.getSchedularBeanName().trim());
			
			Class<?> classObj = obj.getClass();
			
			Method method = classObj.getDeclaredMethod(schedularModel.getSchedularBeanMethod(), String.class);
			
			method.invoke(obj, "test");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
