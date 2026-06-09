package ams.cms.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ams.cms.api.handler.BulkTransferHandler;
import ams.cms.api.model.BulkTransfer;
import ams.cms.api.model.BulkTransferDto;
import ams.cms.logger.AMSLogger;
import ams.cms.util.ProcessResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@RestController
@RequestMapping("/bulkTransfer")
public class BulkTransferController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(BulkTransferController.class);
	
	@Autowired
	private BulkTransferHandler bulkTransferHandelr;
	
	//oneToMany API request       
	@RequestMapping(value = "/oneToMany", method = RequestMethod.POST)    
	//public ResponseEntity<?> addBulkTransferOneAccountToManyAccounts(@RequestBody PayloadReqRes req)
	public ResponseEntity<?> addBulkTransferOneAccountToManyAccounts(@RequestBody BulkTransferDto bulkTransferDto)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			/*
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			BulkTransferDto bulkTransferDto = new ObjectMapper().readValue(decrypt, BulkTransferDto.class);
			*/
			processResponse = bulkTransferHandelr.bulkTransferOneToMany(bulkTransferDto);
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(processResponse);
		//return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	//manyToOne API request
	@RequestMapping(value = "/manyToOne", method = RequestMethod.POST)
	//public ResponseEntity<?> addBulkTransferManyAccountsToOneAccount(@RequestBody PayloadReqRes req)
	public ResponseEntity<?> addBulkTransferManyAccountsToOneAccount(@RequestBody BulkTransferDto bulkTransferDto)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			/*
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			BulkTransferDto bulkTransferDto = new ObjectMapper().readValue(decrypt, BulkTransferDto.class);
			*/
			
			processResponse = bulkTransferHandelr.bulkTransferManyToOne(bulkTransferDto);
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		//return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		return ResponseEntity.ok(processResponse);
	}
	
	//verify request from Back-Office
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	public ResponseEntity<?> verifyBulkTrasnferEntries(@RequestBody BulkTransfer bulkTransfer)
	{
		try 
		{
			ProcessResponse verifyBulkTransferEntries = bulkTransferHandelr.verifyBulkTransferEntries(bulkTransfer);
			return ResponseEntity.ok(verifyBulkTransferEntries);	
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	//approve request from Back-Office
	@RequestMapping(value = "/approve", method = RequestMethod.POST)
	public ResponseEntity<?> approveBulkTransfer(@RequestBody BulkTransfer bulkTransfer)
	{
		ProcessResponse approveBulkTransfer = bulkTransferHandelr.approveBulkTransfers(bulkTransfer);
		return ResponseEntity.ok(approveBulkTransfer);
	}
	
	//reject request from Back-Office
	@RequestMapping(value = "/reject", method = RequestMethod.POST)
	public ResponseEntity<?> rejectedBulkTransfer(@RequestBody BulkTransfer bulkTransfer)
	{
		ProcessResponse rejectBulkTransfer = bulkTransferHandelr.rejectBulkTransfersWithReason(bulkTransfer);		
		return ResponseEntity.ok(rejectBulkTransfer);
	}
	
	@RequestMapping(value = "/saveBulkTransferExcelData", method = RequestMethod.POST)
	public ResponseEntity<?> saveBulkTransferExcelData(@RequestBody BulkTransferDto bulkTransfer)
	{
		ProcessResponse uploadBulkTransferResp = bulkTransferHandelr.saveBulkTransferExcelData(bulkTransfer);
		return ResponseEntity.ok(uploadBulkTransferResp);
	}
		
	//For Bulk Transfer Verifly PreTransaction Amount 
	@RequestMapping(value = "/getBulkTransferTransactions", method = RequestMethod.POST)
	public ResponseEntity<?> getPreTransactionIdByTransferAmount(@RequestBody BulkTransfer bulkTransfer)
	{
		try 
		{
			List<BulkTransfer> findTxnIdAmt = bulkTransferHandelr.getPreTransactionIdByTxnAmount(bulkTransfer);
			if(findTxnIdAmt.size()>0) 
			{
				return ResponseEntity.ok(findTxnIdAmt);
			}
			else 
			{
				return ResponseEntity.ok("Bulk Transfer Not Found");
				
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
		
	//For Bulk Transfer Authorize Transfer Amount 
	@RequestMapping(value = "/authorizeBulklist", method = RequestMethod.POST)
	public ResponseEntity<?> getAuthroizeByTransferAmount(@RequestBody BulkTransfer bulkTransfer)
	{
		try 
		{
			List<BulkTransfer> authorizeBlukTransferlist = bulkTransferHandelr.getAuthorizeBlukTransferList(bulkTransfer);
			if(authorizeBlukTransferlist.size()>0) 
			{
				return ResponseEntity.ok(authorizeBlukTransferlist);
			}
			else 
			{
				return ResponseEntity.ok("Bulk Transfer Not Found");
				
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	// {done}
	@RequestMapping(value = "/findAllByPreTransactionId", method = RequestMethod.POST)
	public ResponseEntity<?> getBulkTransferDetailsByPreTransactionId(@RequestBody BulkTransferDto bulkTransferDto)
	{
		List<BulkTransfer> findAll = bulkTransferHandelr.findAllByPreTransactionId(bulkTransferDto);
		if(findAll.size()>0) 
		{
			return new ResponseEntity<>(findAll,HttpStatus.OK);
		}
		else 
		{
			return new ResponseEntity<>("Bulk Trasnfer Not Found",HttpStatus.OK);
		}
	}
	
	//get bulk Trasnfer details in front end api 
	@RequestMapping(value = "/findAll", method = RequestMethod.POST)
	public ResponseEntity<?> getBulkTransferDetailsByTransactionId()
	{
		List<BulkTransfer> findAll = bulkTransferHandelr.findAll();
		if(findAll.size()>0)
		{
			return new ResponseEntity<>(findAll,HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>("Bulk Trasnfer Not Found",HttpStatus.OK);
		}
	}
	
	//For Bulk Transfer Authorize Transfer Amount 
	@RequestMapping(value = "/authorizePreTxnBulklist", method = RequestMethod.POST)
	public ResponseEntity<?> getAuthroizePreTxnId(@RequestBody BulkTransfer bulkTransfer)
	{
		try 
		{
			List<BulkTransfer> authorizePreTxnIdBluklist = bulkTransferHandelr.getAuthorizePreTxnIdBulklist(bulkTransfer);
			if(authorizePreTxnIdBluklist.size()>0) 
			{
				return ResponseEntity.ok(authorizePreTxnIdBluklist);
			}
			else 
			{
				return ResponseEntity.ok("Bulk Transfer list Not Found");						
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	//For Bulk Transfer Authorize Transfer Amount 
	@RequestMapping(value = "/preTxnBulklistForVerify", method = RequestMethod.POST)
	public ResponseEntity<?> getPreTxnBulklistForVerify(@RequestBody BulkTransfer bulkTransfer)
	{
		try 
		{
			amsLogger.writeInfoLog(">>inside getPreTxnBulklistForVerify bulkTransfer::["+bulkTransfer+"]");
			List<BulkTransfer> authorizePreTxnIdBluklist = bulkTransferHandelr.getPreTxnIdBulklistForVerify(bulkTransfer);
			if(authorizePreTxnIdBluklist.size()>0) 
			{
				return ResponseEntity.ok(authorizePreTxnIdBluklist);
			}
			else 
			{
				return ResponseEntity.ok("Bulk Transfer list Not Found");						
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("resource")
	@RequestMapping(value = "/uploadExcel", method = RequestMethod.POST)
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file)	
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			// Create a Workbook object to read the Excel file
			Workbook workbook = new XSSFWorkbook(file.getInputStream());
			processResponse = bulkTransferHandelr.getBulkTransferExcelReqRes(workbook);
			// Create a list to store the TransfersModel objects
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.OK).body(processResponse);
	}
	
	@RequestMapping(value = "/getAccountLimitInfoToCompareTransferAmount", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountLimitInfoToCompareTransferAmount(@RequestBody BulkTransferDto bulkTransfer)
	{
		ResponseEntity<?> accountLimitInfo = bulkTransferHandelr.getAccountLimitInfo(bulkTransfer);
		return accountLimitInfo;			
	}
	
	@RequestMapping(value = "/getCumlativeBalanceCompareTransAmt", method = RequestMethod.POST)
	public ResponseEntity<?> getCumlativeBalanceCompareTransAmt(@RequestBody BulkTransferDto bulkTransfer)
	{
		ResponseEntity<?> accountCumulativeLimitInfo = bulkTransferHandelr.getCumlativeBalanceCompareTransAmt(bulkTransfer);
		return accountCumulativeLimitInfo;			
	}
}
