package ams.cms.services.impl;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.controller.MultiCurrencyWalletAccountController;
import ams.cms.dao.AcTypeLrsTcsMasterDao;
import ams.cms.dao.AccountMasterDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AcTypeLrsTcsMaster;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountTypeLrsView;
import ams.cms.model.AccountTypeMaster;
import ams.cms.model.ChannelLrsDetails;
import ams.cms.model.LrsView;
import ams.cms.model.MultiCurrencyFinancialYearMaster;
import ams.cms.services.AcTypeLrsTcsMasterService;
import ams.cms.services.AccountTypeMasterService;
import ams.cms.services.MultiCurrencyFinancialYearMasterService;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.Utils;

@Transactional
@Service
public class AcTypeLrsTcsMasterServiceImpl implements AcTypeLrsTcsMasterService {
	
	private AMSLogger amsLogger = AMSLogger.getInstance(AcTypeLrsTcsMasterServiceImpl.class);

	
	@Autowired
	private AcTypeLrsTcsMasterDao acTypeLrsTcsMasterDao;
	
	@Autowired
	private AccountMasterDao accountMasterDao;
	
	@Autowired
	private MultiCurrencyFinancialYearMasterService currencyFinancialYearMasterService;
	
	@Autowired
	private AccountTypeMasterService accountTypeMasterService;
	
	DecimalFormat formatter = new DecimalFormat("#0.00"); 
	
	
	@Override
	public AcTypeLrsTcsMaster addLrsTcs(AcTypeLrsTcsMaster acTypeLrsTcsMaster) {
			acTypeLrsTcsMaster.setStrDate(new Date());
		 acTypeLrsTcsMasterDao.save(acTypeLrsTcsMaster);
		 return acTypeLrsTcsMaster;
	}

	@Override
	public AcTypeLrsTcsMaster getAccountTypeLrsAndTcs(AcTypeLrsTcsMaster acTypeLrsTcsMaster) {
		
		return acTypeLrsTcsMasterDao.getAccountTypeLrsAndTcs(acTypeLrsTcsMaster);
	}

	@Override
	public ProcessResponse checkLrsLimit(AcTypeLrsTcsMaster accountCreation) {
		
		
		ProcessResponse processResponse = new ProcessResponse();
		try {
		
			amsLogger.writeInfoLog("Inside Method checkLRSLimit[2]::"+processResponse); 
			
			
		LrsView lrsView = new LrsView();
		if(accountCreation.getStrAccountType()!= null) {
			amsLogger.writeInfoLog("Inside Method checkLRSLimit[3]::"+processResponse); 
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			AccountCreation accountCreationObj = new AccountCreation();
			accountCreationObj.setStrAccountNumber(accountCreation.getStrAccountNumber());
			accountCreationObj = accountMasterDao.getAccountInformation(accountCreationObj);
			if(accountCreation != null && accountCreation.getStrAccountNumber() != null) {
			
				
				String financialYear ;
				String nextYear ;
				
				int CurrentMonth = Calendar.getInstance().get(Calendar.MONTH);
				if(CurrentMonth==3) {
				
					 financialYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					 nextYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR) + 1);	
					
				}else {
					 financialYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - 1);
					 nextYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				}
			
			List<ChannelLrsDetails> channelLrsDetails = new ArrayList<>();
			
			AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
			accountTypeMaster.setStrAccountType(accountCreation.getStrAccountType());
			accountTypeMaster = accountTypeMasterService.getAccountTypeObject(accountTypeMaster);
			if("Y".equalsIgnoreCase(accountTypeMaster.getIsMultiCurrencySupport())) {
				amsLogger.writeInfoLog("Inside Method checkLRSLimit[4]::"+accountTypeMaster); 
			boolean isChannelConfigerd = false;
			AcTypeLrsTcsMaster acTypeLrsTcsMaster = new AcTypeLrsTcsMaster();
			acTypeLrsTcsMaster.setStrAccountType(accountCreation.getStrAccountType());
			List<AcTypeLrsTcsMaster> acTypeLrsTcsMastersList = acTypeLrsTcsMasterDao.getAccountTypeLrsBasedOnAccountType(acTypeLrsTcsMaster);
			if(acTypeLrsTcsMastersList.size()>0) {
				amsLogger.writeInfoLog("Inside Method checkLRSLimit[5]::"+acTypeLrsTcsMastersList); 
				
				
				AccountTypeLrsView accountTypeLrsView = new AccountTypeLrsView();
				
				for(AcTypeLrsTcsMaster acTypeLrsTcsMastersListObj: acTypeLrsTcsMastersList){
					
					//chek for channel 
					if("N".equalsIgnoreCase(accountTypeMaster.getIsChannel())) {
						isChannelConfigerd = false;
						amsLogger.writeInfoLog("Inside Method checkLRSLimit[6]::"+acTypeLrsTcsMastersList); 
						
						
						//get finanicial Details
						MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMasterObj = new MultiCurrencyFinancialYearMaster();
						multiCurrencyFinancialYearMasterObj.setAccountType(acTypeLrsTcsMastersListObj.getStrAccountType());
						multiCurrencyFinancialYearMasterObj.setFinancialYear(String.valueOf(financialYear.substring(2)+"-"+nextYear.substring(2)));
						multiCurrencyFinancialYearMasterObj.setAccountNumber(accountCreation.getStrAccountNumber());
						multiCurrencyFinancialYearMasterObj = currencyFinancialYearMasterService.getFinancialYearNyAccountNumber(multiCurrencyFinancialYearMasterObj);
						if(multiCurrencyFinancialYearMasterObj != null) {
							
							lrsView.setLrsAssigendLimit(String.valueOf(formatter.format(multiCurrencyFinancialYearMasterObj.getLrsLimit())));
							lrsView.setLrsLimitConsumed(String.valueOf(formatter.format(multiCurrencyFinancialYearMasterObj.getTotalLrsConsumed())));
							lrsView.setTcsApplied(String.valueOf(formatter.format( multiCurrencyFinancialYearMasterObj.getTotalTcsOn())));
							lrsView.setTotalAmountLoaded(String.valueOf(formatter.format(multiCurrencyFinancialYearMasterObj.getTotalLoaded())));
							
						}else {
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Finincial Data Not Found");
						}
					}else {
						isChannelConfigerd = true;
						amsLogger.writeInfoLog("Inside Method checkLRSLimit[7]::"+isChannelConfigerd); 
						
						
						MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMasterObj = new MultiCurrencyFinancialYearMaster();
						multiCurrencyFinancialYearMasterObj.setAccountType(acTypeLrsTcsMastersListObj.getStrAccountType());
						multiCurrencyFinancialYearMasterObj.setFinancialYear(String.valueOf(financialYear.substring(2)+"-"+nextYear.substring(2)));
						multiCurrencyFinancialYearMasterObj.setAccountNumber(accountCreation.getStrAccountNumber());
						multiCurrencyFinancialYearMasterObj.setChannelCode(acTypeLrsTcsMastersListObj.getStrChannelCode());
						List<MultiCurrencyFinancialYearMaster> multiCurrencyFinancialYearMasterObjList = currencyFinancialYearMasterService.getListFinancialYearNyAccountNumberAndChannel(multiCurrencyFinancialYearMasterObj);
						if(multiCurrencyFinancialYearMasterObjList.size()>0) {
							amsLogger.writeInfoLog("Inside Method checkLRSLimit[8]::"+multiCurrencyFinancialYearMasterObjList); 
							multiCurrencyFinancialYearMasterObjList.forEach(i -> {
								
								ChannelLrsDetails channelLrsDetailsObj = new ChannelLrsDetails();
								channelLrsDetailsObj.setChannel(i.getChannelCode());
								channelLrsDetailsObj.setLrsConsumed(String.valueOf(formatter.format( i.getTotalLrsConsumed())));
								channelLrsDetailsObj.setLrsLimit(String.valueOf(formatter.format( i.getLrsLimit())));
								channelLrsDetailsObj.setTcsApplied(String.valueOf(formatter.format( i.getTotalTcsOn())));
								channelLrsDetailsObj.setTotalLoaded(String.valueOf(formatter.format( i.getTotalLoaded())));
								channelLrsDetails.add(channelLrsDetailsObj);
								
							});
						
						}else {
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("No Lrs For Channel Configerd");
						}
							
					}
					
					}
				
				if(isChannelConfigerd==true) {
					amsLogger.writeInfoLog("Inside isChannelConfigerd = true::"+processResponse); 
				lrsView.setChannelConfigured(true);
				lrsView.setFinancialYear(String.valueOf(financialYear.substring(2)+"-"+nextYear.substring(2)));
				lrsView.setCurrentDate(String.valueOf(df.format(Utils.getCurrentDate())));
				lrsView.setChannelLrsDetails(channelLrsDetails);
				
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Data Fetch Scussefully");
				processResponse.setLrsView(lrsView);
			
				
				}else if(isChannelConfigerd==false) {
					
					amsLogger.writeInfoLog("Inside isChannelConfigerd = false::"+processResponse); 
					
					lrsView.setChannelConfigured(false);
					lrsView.setFinancialYear(String.valueOf(financialYear.substring(2)+"-"+nextYear.substring(2)));
					lrsView.setCurrentDate(String.valueOf(df.format(Utils.getCurrentDate())));
					
					processResponse.setCode("S0000");
					processResponse.setStatus("Success");
					processResponse.setMessage("Data Fetch Scussefully");
					processResponse.setLrsView(lrsView);
				}
				
			}else {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("No Lrs Configerd");
			}
			}else {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("MultiCurrency Not Configerd");
			}
			}else {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Account Not Found");
			}
		}else {
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Please Provide AccountType");
		}
		} 
		catch (Exception e) 
		{
			amsLogger.writeInfoLog("ERROR ::"+e.getMessage()); 
			
			amsLogger.writeInfoLog("Inside finalRespose for LrsView processResp2::"+processResponse); 
		}
		return processResponse;
	}

	@Override
	public List<AcTypeLrsTcsMaster> getAccountTypeTcsAndLrs(AcTypeLrsTcsMaster acTypeLrsTcsMaster) 
	{
		return  acTypeLrsTcsMasterDao.getAccountTypeTcsAndLrs(acTypeLrsTcsMaster);
	}
	
	@Override
	public void createMultiCurrencyFinancialYearMasterForAccount(AccountCreation accountCreation) {
		try 
		{
		//get account Type Creation
		AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
		accountTypeMaster.setStrAccountType(accountCreation.getStrAccountType());
		accountTypeMaster = accountTypeMasterService.getAccountTypeObject(accountTypeMaster);
		
		if(accountTypeMaster != null) 
		{
			if("Y".equalsIgnoreCase(accountTypeMaster.getIsMultiCurrencySupport())) 
			{		
			//For Each AccountTypeTcsLrs Create Finicial Year Master 
				String financialYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				String nextYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR) + 1);
				
				
				AcTypeLrsTcsMaster acTypeLrsTcsMaster = new AcTypeLrsTcsMaster();
				acTypeLrsTcsMaster.setStrAccountType(accountCreation.getStrAccountType());
				List<AcTypeLrsTcsMaster> acTypeLrsTcsMastersList = getAccountTypeTcsAndLrs(acTypeLrsTcsMaster);
				
				if(acTypeLrsTcsMastersList.size() > 0) {	
				    if("Y".equalsIgnoreCase(accountTypeMaster.getIsChannel())) {
				        for(AcTypeLrsTcsMaster i : acTypeLrsTcsMastersList) {
				            MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMaster = new MultiCurrencyFinancialYearMaster();
				            multiCurrencyFinancialYearMaster.setStrParticipantId(accountCreation.getStrParticipantID());
				            multiCurrencyFinancialYearMaster.setAccountNumber(accountCreation.getStrAccountNumber());
				            multiCurrencyFinancialYearMaster.setAccountType(accountCreation.getStrAccountType());
				            multiCurrencyFinancialYearMaster.setCustId(accountCreation.getStrCustId());
				            multiCurrencyFinancialYearMaster.setChannelCode(i.getStrChannelCode());
				            multiCurrencyFinancialYearMaster.setAvailableLrsLimit(0);
				            multiCurrencyFinancialYearMaster.setTotalTcsOn(0);
				            multiCurrencyFinancialYearMaster.setTotalLrsConsumed(0);
				            multiCurrencyFinancialYearMaster.setTotalLoaded(0);
				            multiCurrencyFinancialYearMaster.setTotalExcessLoading(0);
				            multiCurrencyFinancialYearMaster.setLrsLimit(0);
				            multiCurrencyFinancialYearMaster.setCreatedDate(Utils.getCurrentSqlDate());
				            multiCurrencyFinancialYearMaster.setFinancialYear(String.valueOf(financialYear.substring(2)+"-"+nextYear.substring(2)));
				            currencyFinancialYearMasterService.addMultiCurrencyFinancialYearMaster(multiCurrencyFinancialYearMaster);
				        }
				    } else {
				        MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMaster = new MultiCurrencyFinancialYearMaster();
				        multiCurrencyFinancialYearMaster.setStrParticipantId(accountCreation.getStrParticipantID());
				        multiCurrencyFinancialYearMaster.setAccountNumber(accountCreation.getStrAccountNumber());
				        multiCurrencyFinancialYearMaster.setAccountType(accountCreation.getStrAccountType());
				        multiCurrencyFinancialYearMaster.setCustId(accountCreation.getStrCustId());
				        multiCurrencyFinancialYearMaster.setAvailableLrsLimit(0);
				        multiCurrencyFinancialYearMaster.setTotalTcsOn(0);
				        multiCurrencyFinancialYearMaster.setTotalLrsConsumed(0);
				        multiCurrencyFinancialYearMaster.setTotalLoaded(0);
				        multiCurrencyFinancialYearMaster.setTotalExcessLoading(0);
				        multiCurrencyFinancialYearMaster.setLrsLimit(0);
				        multiCurrencyFinancialYearMaster.setCreatedDate(Utils.getCurrentSqlDate());
				        multiCurrencyFinancialYearMaster.setFinancialYear(String.valueOf(financialYear.substring(2)+"-"+nextYear.substring(2)));
				        currencyFinancialYearMasterService.addMultiCurrencyFinancialYearMaster(multiCurrencyFinancialYearMaster);
				        }			
				}
		    }
		  }	
		}
		catch (Exception e) 
		{
			e.printStackTrace();			
		}
   }



}
