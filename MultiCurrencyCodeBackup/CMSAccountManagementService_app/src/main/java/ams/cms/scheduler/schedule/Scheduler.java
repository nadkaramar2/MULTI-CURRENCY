package ams.cms.scheduler.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ams.cms.model.AccountCreation;
import ams.cms.scheduler.model.AccountCreditCardStatement;
import ams.cms.scheduler.model.AccountWiseInterestMaster;
import ams.cms.scheduler.service.SchedulerService;
import ams.cms.utility.Utils;
import ams.cms.scheduler.model.RevolvingCreditCardInterest;

@Component
@EnableScheduling
public class Scheduler
{
	@Autowired
	private SchedulerService schedulerService;
	
	//@Scheduled(cron = "0 0/15 * * * *")//15 minutes
	//0 0/15 * * * *
	@Scheduled(cron = "0 0/2 * * * *")//2 minutes
	public void executeSchedular() 
	{
		try 
		{
			List<SchedularModel> schedularModels = schedulerService.getScheduleInfo();
			
			if (schedularModels != null && schedularModels.size() > 0) 
			{
				for (SchedularModel schedularModel: schedularModels) 
				{
					try 
					{
						String scheduleDateStr = schedularModel.getScheduleDate();
						
						Date scheduleDte = Utils.simpleDateFormat5.parse(scheduleDateStr); 
					
						Date currentDate = new Date();
						String currentFormattedDate = Utils.simpleDateFormat5.format(currentDate);
						Date currentDte = Utils.simpleDateFormat5.parse(currentFormattedDate); 
						
						if (currentDte.compareTo(scheduleDte) == 0)  
						{
							schedulerService.executeSpecificBeanFromSchedular(schedularModel);
							
							Date nextDte = null;
							if ("D".equalsIgnoreCase(schedularModel.getScheduleParam())) 
							{
								nextDte = Utils.getNextDateBasedOnCount(scheduleDateStr, 1, Utils.simpleDateFormat5);
							}
							else if ("W".equalsIgnoreCase(schedularModel.getScheduleParam())) 
							{
								nextDte = Utils.getNextDateBasedOnCount(scheduleDateStr, 7, Utils.simpleDateFormat5);
							}
							else if ("M".equalsIgnoreCase(schedularModel.getScheduleParam())) 
							{
								nextDte = Utils.getNextDateBasedOnCount(scheduleDateStr, 30, Utils.simpleDateFormat5);
							}
							schedularModel.setUpdatedScheduleDate(nextDte);
							schedulerService.updateScheduleInfo(schedularModel);
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//After 12 am start scheduler data update ....
	//@Scheduled(cron = "0/6 * * ? * *")
	//@Scheduled(cron = "0 1 00 * * ?") 
	public void runScheduler() 
	{
		try
		{
			AccountWiseInterestMaster accountwiseInterestMaster = new AccountWiseInterestMaster();
			accountwiseInterestMaster.setStrIsPaid("N");
			
			List<AccountWiseInterestMaster> accountwiseInterestMasters = schedulerService.getCreditAccountWiseInterestList(accountwiseInterestMaster);
			
			if (accountwiseInterestMasters!=null && accountwiseInterestMasters.size() > 0) 
			{
				schedulerService.processInsertAcoountWiseInterest(accountwiseInterestMasters);
			}			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	//@Scheduled(cron = "0/15 * * ? * *")
	public void runScheduler1()
	{
		try 
		{
			 List<AccountCreditCardStatement> accountCreditCardStatements = schedulerService.getListOfCustomerAccountForBilling();//Get All Accounts For Billing			
			 if(accountCreditCardStatements!=null && accountCreditCardStatements.size() > 0)
			 {
				 schedulerService.processBillingCycle(accountCreditCardStatements);
			 }
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//@Scheduled(cron = "0/15 * * ? * *") 
	public void runScheduler2() 
	{
		try
		{
			RevolvingCreditCardInterest revolvingCreditCardInterest = new RevolvingCreditCardInterest();
			
			List <RevolvingCreditCardInterest> revolvingCreditCardInterests = schedulerService.getRevolvingCreditInterestTxn(revolvingCreditCardInterest);
			
			if (revolvingCreditCardInterests!=null && revolvingCreditCardInterests.size() > 0) 
			{
				schedulerService.processInsertRevolvingCreditCardInterest(revolvingCreditCardInterests);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	//@Scheduled(cron = "0/15 * * ? * *")
	//@Scheduled(cron = "0 0/15 * * * *")
	public void sendEmailNotification() 
	{
		try 
		{
			System.out.println("Start schedular::" + new Date());
			List<AccountCreation> accountCreationslist = schedulerService.getUserListForPaymentDueNotify();
			if (accountCreationslist != null && accountCreationslist.size() > 0) 
			{
				schedulerService.sendPaymentDueNotification(accountCreationslist);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String strDate = sdf.format(now);
		System.out.println("Java cron job expression:: " + strDate);
		System.out.println("---------------------XXXX---------------------------");
	}
	
	//@Scheduled(cron = "0 0/15 * * * *")
	//@Scheduled(cron = "0/15 * * ? * *")
	public void testAttachmentTemplate() 
	{
		try
		{
			System.out.println("-----SENDING ATTACHMENT--------------");
			schedulerService.sendAttachment(null);
			
			System.out.println("-----SENT ATTACHMENT TO USER--------------");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Scheduled(cron = "23 14 1 * * *")
	public void customScheduleWithCron() {   
		System.out.println("------------Running every minute------------");
	}
	
}
