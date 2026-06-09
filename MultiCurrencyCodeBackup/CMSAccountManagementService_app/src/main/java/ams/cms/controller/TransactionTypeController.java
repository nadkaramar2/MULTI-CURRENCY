package ams.cms.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.TransactionTypeModel;
import ams.cms.services.TransactionTypeService;


@RestController
@RequestMapping("/transaction_type_master")
public class TransactionTypeController
{
	@Autowired
	TransactionTypeService  transactionTypeService;
	
	
	@RequestMapping(value = "/saveTxnTypeCreationData", method = RequestMethod.POST)
	public ResponseEntity<?> saveTxnTypeCreationData(@RequestBody TransactionTypeModel transactionTypeModel) 
	{
		String result = null;
		try 
		{
			TransactionTypeModel txnTypeMaster =  transactionTypeService.addTransactionTypelist(transactionTypeModel);
			return ResponseEntity.ok(txnTypeMaster);
		} 
		catch (Exception e) {
			System.out.println("Exception in getAccountTxnLimitBasedOnParam::" + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getTransactionTypeModelData", method = RequestMethod.POST)
	public ResponseEntity<?> getTxnTypeCreationData(@RequestBody TransactionTypeModel transactionTypeModel) 
	{
		String result = null;
		try 
		{
			List<TransactionTypeModel> txnTypeMaster =  transactionTypeService.getTransactionTypeData(transactionTypeModel);
			return ResponseEntity.ok(txnTypeMaster);
			
		} 
		catch (Exception e) 
		{
			System.out.println("TransactionTypeController.getTxnTypeCreationData()"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	@RequestMapping(value = "/isTransactionTypeAlreadyExist", method = RequestMethod.POST)
	public ResponseEntity<?> isTransactionTypeAlreadyExist(@RequestBody TransactionTypeModel transactionTypeModel)
	{
		boolean isTransactionTypeAlreadyExist = false;
		try
		{
			isTransactionTypeAlreadyExist = transactionTypeService.isTransactionTypeAlreadyExist(transactionTypeModel);			
			return ResponseEntity.ok(isTransactionTypeAlreadyExist);  
		}
		catch (Exception e)
		{
		//	amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			System.out.println("isTransactionTypeAlreadyExist::"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(isTransactionTypeAlreadyExist);
	}
	
	@RequestMapping(value = "/isGLAccountTypeExist", method = RequestMethod.POST)
	public ResponseEntity<?> isGLAccountTypeExist(@RequestBody TransactionTypeModel transactionTypeModel)
	{
		boolean isGLAccountTypeExist = false;
		try
		{
			isGLAccountTypeExist = transactionTypeService.isGLAccountTypeExist(transactionTypeModel);			
			return ResponseEntity.ok(isGLAccountTypeExist);  
		}
		catch (Exception e)
		{
		//	amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			System.out.println("isGLAccountTypeExist::"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(isGLAccountTypeExist);
	}
}
