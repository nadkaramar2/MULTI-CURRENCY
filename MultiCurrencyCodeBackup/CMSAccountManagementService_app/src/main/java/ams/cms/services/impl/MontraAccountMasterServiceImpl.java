package ams.cms.services.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ams.cms.services.AccountTypeMasterService;
import ams.cms.api.controller.SignUpController;
import ams.cms.dao.MontraAccountMasterDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.MontraAccountMaster;
import ams.cms.services.MontraAccountMasterService;
import ams.cms.util.ProcessResponse;
import ams.cms.model.AccountTypeMaster;

@Transactional
@Service
public class MontraAccountMasterServiceImpl  implements MontraAccountMasterService 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(MontraAccountMasterServiceImpl.class);
	
	@Autowired
	private MontraAccountMasterDao montraAccountMasterDao;
	
	@Autowired
	private AccountTypeMasterService accountTypeMasterService;
	
	@Override
	public ProcessResponse validateMontraIdAndCustId(MontraAccountMaster montraAccountMaster) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		processResponse.setCode("S0000");
		processResponse.setStatus("Success");
		
		MontraAccountMaster montraAccountMasterInstance = montraAccountMasterDao.findMontraAccountMasterByCustIdAndMontraId(montraAccountMaster);
		if(montraAccountMasterInstance != null && montraAccountMasterInstance.getStrCustId() != null && montraAccountMasterInstance.getCid() != null) 
		{
			processResponse.setMessage("Data Retrieve Successfully");
		}
		else 
		{	
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("No Cust Id And Cid Available");
		}	
		return processResponse;
	}

	@Override
	public ProcessResponse validateMontraId(MontraAccountMaster montraAccountMaster) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		MontraAccountMaster montraAccountMasterObj = montraAccountMasterDao.findByMontraId(montraAccountMaster);
		if(montraAccountMasterObj != null) 
		{
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");
			processResponse.setMessage("Data Retrieve Successfully.");
		}
		else
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("No MontraId Available.");
		}	
		return processResponse;
	}

	@Override
	public MontraAccountMaster getMontraAccountMasterInstance(MontraAccountMaster montraAccountMaster) throws Exception 
	{
		return montraAccountMasterDao.getMontraAccountMasterInstance(montraAccountMaster);
	}

	@Override
	public ProcessResponse validateMontraIdAndCustId(String cid, String custId) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			cid = (cid != null && cid.trim().length() > 0) ? cid.trim() : "";
			custId = (custId != null && custId.trim().length() > 0) ? custId.trim() : "";			
			
			MontraAccountMaster montraAccountMaster = new MontraAccountMaster();
			montraAccountMaster.setStrCustId(custId);
			montraAccountMaster.setCid(cid);
			
			MontraAccountMaster montraAccountMasterObj = montraAccountMasterDao.findMontraAccountMasterByCustIdAndMontraId(montraAccountMaster);
			if(montraAccountMasterObj != null && montraAccountMasterObj.getStrCustId() != null && montraAccountMasterObj.getCid() != null) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Data Retrieve Successfully");
			}
			else 
			{	
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("No Cust Id And Cid Available");
			}	
			return processResponse;
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error");
			e.printStackTrace();
		}
		return processResponse;
	}

	//Need to add changes BID Start
	@Override
	public ProcessResponse validateBid(MontraAccountMaster montraAccountMaster) 
	{
		{
			ProcessResponse processResponse = new ProcessResponse();
			MontraAccountMaster montraAccountMasterObj = montraAccountMasterDao.findByBid(montraAccountMaster);
			if(montraAccountMasterObj != null) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Bid Already Exist.");
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Bid Not Avaialble.");
			}	
			return processResponse;
		}
		//return montraAccountMasterDao.validateBid(montraAccountMaster);
		
	}

	//Need to add changes BID End	

	@Override
	public ProcessResponse validateCid(MontraAccountMaster montraAccountMaster) {
	{
			ProcessResponse processResponse = new ProcessResponse();
			MontraAccountMaster montraAccountMasterObj = montraAccountMasterDao.findByMontraCid(montraAccountMaster);
			if(montraAccountMasterObj != null) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Cid already Exist.");
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Cid Not Available.");
			}	
			return processResponse;
		}
	}

	@Override
	public ProcessResponse validateMontraRequest(ProcessResponse processResponse, MontraAccountMaster montraAccountMaster) 
	{
		try 
		{
			Boolean isCustIdInReq = false;
			
			AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
			accountTypeMaster.setStrAccountType(montraAccountMaster.getStrAccountType());
			
			accountTypeMaster = accountTypeMasterService.getAccountTypeObject(accountTypeMaster);
			if(accountTypeMaster != null && accountTypeMaster.getStrAccountType() != null)
			{
				if(montraAccountMaster.getStrCustId() != null && montraAccountMaster.getStrCustId().trim().length() > 0)
				{
					isCustIdInReq = true;
					
					MontraAccountMaster montraAccountMasterInstance = montraAccountMasterDao.findMontraAccountMasterByCustIdAndMontraId(montraAccountMaster);							
					if(!(montraAccountMasterInstance.getCid() != null && montraAccountMasterInstance.getCid().trim().length() > 0))
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("No Cust Id And Cid Available");
					}
				}
				//CId compulasory Validation in Request
				if(!(montraAccountMaster.getCid() != null && montraAccountMaster.getCid().trim().length() > 0))
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Cid Not Found For this Request");
				}
				if(!"P".equalsIgnoreCase(accountTypeMaster.getStrAccountTypeCategory()))
				{
					if(!(montraAccountMaster.getBid() != null && montraAccountMaster.getBid().trim().length() > 0))
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Bid Not Found For this Request");
					}
				}
				else 
				{
					processResponse = validateMontraIdAccountTypeAndCustId(montraAccountMaster);
				}
				
				// Added by Prashant T 15-Sept2023 for Mcc Code validation
				
				//if (accountTypeMaster.getStrIsMccCheck() != null && "Y".equalsIgnoreCase(accountTypeMaster.getStrIsMccCheck())) 
				boolean isMccCheckRequired = (accountTypeMaster.getStrIsMccCheck() != null && "Y".equalsIgnoreCase(accountTypeMaster.getStrIsMccCheck())) ? true : false;
				amsLogger.writeInfoLog("In validateMontraRequest isMccCheckRequired=["+isMccCheckRequired+"]"); 
				if(isMccCheckRequired)
				{
					processResponse = validateMccForAcc(processResponse, montraAccountMaster);
				}
				else 
				{
					if (montraAccountMaster.getMccCode() != null && montraAccountMaster.getMccCode().trim().length() > 0) 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Mcc code not applicable for this account!");
					}
				}
				// Ended by Prashant T 15-Sept2023 for Mcc Code validation
				
				if("S0000".equalsIgnoreCase(processResponse.getCode()))
				{
					//This is First time on Boarding Condition Check 
					if(!isCustIdInReq && montraAccountMaster.getCid() != null && montraAccountMaster.getCid().trim().length() > 0)
					{
						MontraAccountMaster montraAccountMasterObj = montraAccountMasterDao.findByMontraCid(montraAccountMaster);
						if(montraAccountMasterObj != null) 
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Cid already Exist.");
						}
					} 
					
					//validate duplication BID
					/*
					if(montraAccountMaster.getBid() != null && montraAccountMaster.getBid().trim().length() > 0)
					{
						MontraAccountMaster montraAccountMasterObj = montraAccountMasterDao.findByBid(montraAccountMaster);
						if(montraAccountMasterObj != null) 
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Bid already Exist.");
						}
					}
					*/
				}
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Account Type Not Configured");
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return processResponse;
	}
	
	//MPA 101 123
	@Override
	public ProcessResponse validateMontraIdAccountTypeAndCustId(MontraAccountMaster montraAccountMaster) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		processResponse.setCode("S0000");
		processResponse.setStatus("Success");
		
		MontraAccountMaster montraAccountMasterInstance = montraAccountMasterDao.findMontraAccountMasterByCustIdAccountTypeAndMontraId(montraAccountMaster);
		if(montraAccountMasterInstance != null && montraAccountMasterInstance.getStrCustId() != null && montraAccountMasterInstance.getCid() != null) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Account Already Exist");
		}
		
		return processResponse;
	}

	@Override
	public MontraAccountMaster findMontraAccountMasterByCustIdAndMontraId(MontraAccountMaster montraRequestModel) 
	{
		return montraAccountMasterDao.findMontraAccountMasterByCustIdAndMontraId(montraRequestModel);
	}
	
	@Override
	public ProcessResponse validateMccForAcc(ProcessResponse processResponse, MontraAccountMaster montraAccountMaster)
	{
		processResponse.setCode("S0000");
		processResponse.setStatus("success");
		try 
		{
			if (montraAccountMaster.getMccCode() != null && montraAccountMaster.getMccCode().trim().length() > 0) 
			{
				MontraAccountMaster montraAccountMast = montraAccountMasterDao.validateMCCAgainstAccountType(montraAccountMaster);
				if (montraAccountMast != null && montraAccountMast.getMccCode() != null) 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Mcc Code already Exist");
				}
			} 
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Mcc Code Cannot be Blank");
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return processResponse;
	}

	@Override
	public MontraAccountMaster addMontraAccountMaster(MontraAccountMaster montraAccountMaster) 
	{
		montraAccountMasterDao.save(montraAccountMaster);
		return montraAccountMaster;
	}
	
	@Override
	public ProcessResponse accountInfoByCid(MontraAccountMaster montraAccountMaster) {
	{
			ProcessResponse processResponse = new ProcessResponse();
			MontraAccountMaster montraAccountMasters = montraAccountMasterDao.accountInfoByMontraCid(montraAccountMaster);
			if(montraAccountMasters != null) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMontraAccountMasters(montraAccountMasters);
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Cid and CustId Not Available.");
			}	
			return processResponse;
		}
	}
}
