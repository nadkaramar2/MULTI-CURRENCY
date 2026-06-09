package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.api.model.AccountStatementHeader;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.services.GLAccountStatementService;

@RestController
@RequestMapping("/gl-account-statement")
public class GlAccountStatementController {
	
	@Autowired
	GLAccountStatementService glAccountStatementService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addGLAccountType(@RequestBody GLAccountStatement glAccountStatement)
	{
		String result = null;
		try
		{
			GLAccountStatement glAccountStatementObj = glAccountStatementService.addGlAccountStatementData(glAccountStatement);			
			if(glAccountStatementObj != null &&	glAccountStatementObj.getStrID() != null)
			{
				return ResponseEntity.ok(glAccountStatementObj);  
			}			
		}
		catch (Exception e)
		{
			System.out.println("addGLAccountType::"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/getGLAccountStatementList", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountStatementListData(@RequestBody GLAccountStatement glAccountStatement) 
	{
		String result = null;
		try 
		{
				List<GLAccountStatement> gLAccountStatementList = glAccountStatementService.getGLAccountStatement(glAccountStatement);
				return ResponseEntity.ok(gLAccountStatementList);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getAccountStatementandGlAccountStatementHeader", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountStatementAndGlAccountStatementHeader(@RequestBody GLAccountStatement glAccountStatement) 
	{
		String result = null;
		try 
		{
				List<AccountStatementHeader> gLAccountStatementList = glAccountStatementService.getGLAccountStatementData(glAccountStatement.getStrAccountNumber());
				return ResponseEntity.ok(gLAccountStatementList);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	//added by sunil Y , new api creation for header response in gl account statement and account statement , started

	//added by sunil Y , new api creation for header response in gl account statement and account statement , started
		@RequestMapping(value = "/getGlAccountStatementHeader", method = RequestMethod.POST)
		public ResponseEntity<?> getGlAccountStatementHeader(@RequestBody GLAccountStatement glAccountStatement) 
		{
			String result = null;
			try 
			{
					List<GLAccountTypeMaster> gLAccountStatementList = glAccountStatementService.getGLAccountStatementDataHeader(glAccountStatement.getStrAccountNumber());
					return ResponseEntity.ok(gLAccountStatementList);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			return ResponseEntity.ok(result);
		}
		//added by sunil Y , new api creation for header response in gl account statement and account statement , started

}
