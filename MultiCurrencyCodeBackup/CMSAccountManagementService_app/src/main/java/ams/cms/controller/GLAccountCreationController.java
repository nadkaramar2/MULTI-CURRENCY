package ams.cms.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.logger.AMSLogger;
import ams.cms.model.GLAccountCreation;
import ams.cms.services.GLAccountCreationService;
import ams.cms.utility.Utils;

@RestController
@RequestMapping("/gl-account-type-creation")
public class GLAccountCreationController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(GLAccountCreationController.class);
	
	@Autowired
	GLAccountCreationService glAccountCreationService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addGLAccountType(@RequestBody GLAccountCreation glAccountCreation)
	{
		String result = null;
		try
		{
			GLAccountCreation glAccountCreationObj = glAccountCreationService.addGLAccountType(glAccountCreation);			
			if(glAccountCreationObj != null &&	glAccountCreation.getStrId() != null)
			{
				return ResponseEntity.ok(glAccountCreationObj);  
			}			
		}
		catch (Exception e)
		{
			System.out.println("addGLAccountType::"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/isGLAccountTypeExist", method = RequestMethod.POST)
	public ResponseEntity<?> isGLAccountType(@RequestBody GLAccountCreation glAccountCreation)
	{
		boolean isGLAccountTypeExist = false;
		try
		{
			isGLAccountTypeExist = glAccountCreationService.isGLAccountTypeAlreadyExist(glAccountCreation);			
			return ResponseEntity.ok(isGLAccountTypeExist);  
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(isGLAccountTypeExist);
	}
	
	@RequestMapping(value = "/isGLAccountNumberExist", method = RequestMethod.POST)
	public ResponseEntity<?> isGLAccountNumber(@RequestBody GLAccountCreation glAccountCreation)
	{
		boolean isGLAccountNumberExist = false;
		try
		{
			isGLAccountNumberExist = glAccountCreationService.isGLAccountAccountNumberAlreadyExist(glAccountCreation);			
			return ResponseEntity.ok(isGLAccountNumberExist);
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(isGLAccountNumberExist);
	}
	
	
	@RequestMapping(value = "/getGlAccTypeAccNumber", method = RequestMethod.POST)
	public ResponseEntity<?> getGlAccTypeAccNumber(@RequestBody GLAccountCreation glAccountCreation)
	{
		try 
		{
			List<GLAccountCreation> list = glAccountCreationService.getGlAccTypeAccNumber(glAccountCreation);
			return ResponseEntity.ok(list);				
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(null);
	}
	@RequestMapping(value = "/updateGLClosingBalance", method = RequestMethod.POST)
	public ResponseEntity<?> updateClosingBalance(@RequestBody GLAccountCreation glAccountCreation)
	{
		int glAccountTypeCount = 0;
		try
		{
			glAccountTypeCount = glAccountCreationService.updateClosingBalance(glAccountCreation);			
			return ResponseEntity.ok(glAccountTypeCount);  
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(glAccountTypeCount);
	}
	
	@RequestMapping(value = "/glCtrAccountTypeObj", method = RequestMethod.POST)
	public ResponseEntity<?> getCTRGlAccountTypeObj(@RequestBody GLAccountCreation glAccountCreation)
	{
		GLAccountCreation isGLAccountNumberExist = new GLAccountCreation();
		try
		{
			isGLAccountNumberExist = glAccountCreationService.isGLAccountTypeExist(glAccountCreation);	
			return ResponseEntity.ok(isGLAccountNumberExist);
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(isGLAccountNumberExist);
	}

	@RequestMapping(value = "/isGLAccountTypeExistdetails", method = RequestMethod.POST)
	public ResponseEntity<?> isGLAccountTypeExist(@RequestBody GLAccountCreation glAccountCreation)
	{
		GLAccountCreation isGLAccountNumberExist = new GLAccountCreation();
		try
		{
			isGLAccountNumberExist = glAccountCreationService.isGLAccountTypeExist(glAccountCreation);	
			if (isGLAccountNumberExist!=null && isGLAccountNumberExist.getStrGLAccountNumber()!=null) 
			{
				String strControlAccountBalance = glAccountCreationService.getAllGLTogetherBalance();
				if (strControlAccountBalance!=null) 
				{
					isGLAccountNumberExist.setStrClosingBalance(Utils.decimalFormat.format(Double.valueOf(strControlAccountBalance)));
				}
			}
			return ResponseEntity.ok(isGLAccountNumberExist);
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(isGLAccountNumberExist);
	}
	
	@RequestMapping(value = "/allGLAccountBalance", method = RequestMethod.POST)
	public ResponseEntity<?> getAllGLAccountBalance(@RequestBody GLAccountCreation glAccountCreation)
	{
		try
		{
			String strControlAccountBalance = glAccountCreationService.getAllGLTogetherBalance();
			if (strControlAccountBalance!=null) 
			{
				glAccountCreation.setStrAllGLBalance(Utils.decimalFormat.format(Double.valueOf(strControlAccountBalance)));
			}
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(glAccountCreation);
	}
	
	@RequestMapping(value = "/getGlAccTypeAccNumberList", method = RequestMethod.POST)
	public ResponseEntity<?> getGlAccTypeAccNumberList(@RequestBody GLAccountCreation glAccountCreation)
	{
		try 
		{
			List<GLAccountCreation> list = glAccountCreationService.getGlAccTypeAccNumberList(glAccountCreation);
			return ResponseEntity.ok(list);				
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(null);
	}
}
