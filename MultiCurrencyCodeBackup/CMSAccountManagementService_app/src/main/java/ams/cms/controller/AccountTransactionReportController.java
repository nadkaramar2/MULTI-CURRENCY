package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.AccountTranMaster;
import ams.cms.model.JournalTransfer;
import ams.cms.model.TransactionTypeModel;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.AccountTransactionReportService;
import ams.cms.services.JournalTransferService;

@RestController
@RequestMapping("/txnReport")
public class AccountTransactionReportController 
{
	@Autowired
	AccountTransactionReportService accountTransactionReportService;
	
	@Autowired
	AccountTranMasterService  accountTranMasterService;
		
	@Autowired
	JournalTransferService journalTransferService;
	
	@RequestMapping(value = "/getAccountTxnType", method = RequestMethod.POST)
    public ResponseEntity<?> TransactionReport(@RequestBody TransactionTypeModel transactionTypeModel)
	{
		String result = null;
		try 
		{
			List<TransactionTypeModel> txnReportlist = accountTransactionReportService.getTxnAccountTypelist(transactionTypeModel);
			if (txnReportlist!=null && txnReportlist.size() > 0)
			{
				return ResponseEntity.ok(txnReportlist);			
			}		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	} 
	
	@RequestMapping(value = "/getTxnReportStatement", method = RequestMethod.POST)
    public ResponseEntity<?> TxnStatementReport(@RequestBody AccountTranMaster accountTranMaster)
	{
		String result = null;
		try 
		{
			List<AccountTranMaster> txnStatementReportlist = accountTranMasterService.getTxnAccountTypelist(accountTranMaster);
			return ResponseEntity.ok(txnStatementReportlist);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	} 
	
	@RequestMapping(value = "/getJouranalTxnReport", method = RequestMethod.POST)
	public ResponseEntity<?> getAuthoriseAccountTXnID(@RequestBody JournalTransfer journalTransfer)
	{
		String result = null;
		try 
		{
			List<JournalTransfer> journalTxnlist = journalTransferService.getTxnJournalReport(journalTransfer);
		    return ResponseEntity.ok(journalTxnlist);					
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
}