package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.config.TransactionConfig;
import ams.cms.constants.TransactionType;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.txn.handler.GLTransactionHandler;

@RestController
@RequestMapping("/gl-account-type")
public class GLAccountTypeMasterController 
{
	@Autowired
	GLAccountTypeMasterService glAccountTypeMasterService;
	
	@Autowired
	GLTransactionHandler glTransactionHandler;
	
	@RequestMapping(value = "/getGLAccountTypeList", method = RequestMethod.POST)
	public ResponseEntity<?> getGLAcccountTypeListData(@RequestBody GLAccountTypeMaster glAccountTypeMaster)
	{
		try 
		{
			List<GLAccountTypeMaster> glAccountTypeMastersList = glAccountTypeMasterService.getGLAccountTypesList(glAccountTypeMaster);
			if (glAccountTypeMastersList!=null && glAccountTypeMastersList.size() > 0)
			{
				return ResponseEntity.ok(glAccountTypeMastersList);			
			}		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	@RequestMapping(value = "/get-glaccount-closing-bal", method = RequestMethod.POST)
	public ResponseEntity<?> getClosingBalanceOfGlAccount(@RequestBody GLAccountTypeMaster glAccountTypeMaster)
	{
		String result = null;
		try 
		{
			result = glAccountTypeMasterService.getClosingBalanceOfGlAccount(glAccountTypeMaster);
			return ResponseEntity.ok(result);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/update-glaccount-type-details", method = RequestMethod.POST)
	public ResponseEntity<?> updateGLAccountTypeDetails(@RequestBody GLAccountTypeMaster glAccountTypeMaster)
	{
		Integer result = null;
		try 
		{
			result = glAccountTypeMasterService.updateGLAccountTypeDetails(glAccountTypeMaster);
			return ResponseEntity.ok(result);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}

	
	@RequestMapping(value = "/getGlAccountTypedata", method = RequestMethod.POST)
	public ResponseEntity<?> GetGlAccountTypeData(@RequestBody GLAccountTypeMaster glAccountviewModel)
	{
		String result = null;
		try 
		{
			List<GLAccountTypeMaster>glAccountviewModels = glAccountTypeMasterService.GLAccountcreationView(glAccountviewModel);
			return ResponseEntity.ok(glAccountviewModels);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
		
		
	}
	
	@RequestMapping(value = "/addGLAccountEntry", method = RequestMethod.POST)
	public ResponseEntity<?> addGLAccountEntry(@RequestBody GLAccountTypeMaster glAccountTypeMaster)
	{
		try 
		{
			glAccountTypeMaster.setUserAccountType(glAccountTypeMaster.getUserAccountType());
			glAccountTypeMaster.setStrGLAccountType("CLB");
			
			glAccountTypeMaster.setTranType("DPT");
			glAccountTypeMaster.setStrGLAccountDescription(TransactionType.LOAD_BAL);
			
			TransactionConfig transactionConfig = new TransactionConfig();
			transactionConfig.setTxnAmount(glAccountTypeMaster.getTxnAmount());
			transactionConfig.setGlAccountTypeMaster(glAccountTypeMaster);
			
			glTransactionHandler.addAndUpdateGLAccount(transactionConfig);
			
			return ResponseEntity.ok("success");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	// Added by pankaj pawar for charts of accounts menu [Start]
	@RequestMapping(value = "/getGLAccountList", method = RequestMethod.POST)
	public ResponseEntity<?> getGLAcccountListData(@RequestBody GLAccountTypeMaster glAccountTypeMaster)
	{
		try 
		{
			List<GLAccountTypeMaster> glAccountMastersList = glAccountTypeMasterService.getGLAccountList(glAccountTypeMaster);
			if (glAccountMastersList!=null && glAccountMastersList.size() > 0)
			{
				return ResponseEntity.ok(glAccountMastersList);			
			}		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	// Added by pankaj pawar for charts of accounts menu [End]
	
	//created by ankit -start-
	//GL ACCOUNT TYPE LIST ALLOWED BY THIRD PARTY created by ankit
	@RequestMapping(value = "/getThirdPartyAllowGLAccountTypeList", method = RequestMethod.GET)
	public ResponseEntity<?> getGLAcccountTypeListThirdPartyAllow()
	{
		try 
		{
			List<GLAccountTypeMaster> glAccountTypesListThirdPartyAllow = glAccountTypeMasterService.getGLAccountTypesListThirdPartyAllow();
			if (glAccountTypesListThirdPartyAllow!=null && glAccountTypesListThirdPartyAllow.size() > 0)
			{
				return ResponseEntity.ok(glAccountTypesListThirdPartyAllow);	
			}		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	//GL ACCOUNT TYPE LIST ALLOWED BY THIRD PARTY
	
	@RequestMapping(value = "/getGLAccountData", method = RequestMethod.POST)
	public ResponseEntity<?> getGLAcccountTypeData(@RequestBody GLAccountTypeMaster glAccountTypeMaster)
	{
		try 
		{
			List<GLAccountTypeMaster> glAccountTypesListThirdPartyAllow = glAccountTypeMasterService.getDataOfGLAccount(glAccountTypeMaster);
			if (glAccountTypesListThirdPartyAllow!=null && glAccountTypesListThirdPartyAllow.size() > 0)
			{
				return ResponseEntity.ok(glAccountTypesListThirdPartyAllow);	
			}		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "/getGLDescription", method = RequestMethod.POST)
	public ResponseEntity<?> getGLDescription(@RequestBody GLAccountTypeMaster glAccountTypeMaster)
	{
		try 
		{
			List<GLAccountTypeMaster> glDescription = glAccountTypeMasterService.getGLDescription(glAccountTypeMaster);
			if (glDescription!=null && glDescription.size() > 0)
			{
				return ResponseEntity.ok(glDescription);	
			}		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	//created by ankit -end-
}
