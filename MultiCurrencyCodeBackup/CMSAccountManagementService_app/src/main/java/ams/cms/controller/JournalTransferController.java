package ams.cms.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.config.TransactionConfig;
import ams.cms.model.JournalTransfer;
import ams.cms.services.JournalTransferService;
import ams.cms.txn.handler.JournalTransferHandler;

//created by ankit
@RestController
@RequestMapping("/journalTransfer")
public class JournalTransferController 
{
	@Autowired
	private JournalTransferService journalTransferService;
	
	@Autowired
	JournalTransferHandler journalTransferHandler;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addJournalTransferEntry(@RequestBody JournalTransfer journalTransfer)
	{
		try 
		{
			journalTransfer = journalTransferService.addJournalTransferEntry(journalTransfer);
			if (journalTransfer!=null && journalTransfer.getStrId() > 0)
			{
				return ResponseEntity.ok(String.valueOf(journalTransfer.getStrId()));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.POST)
	public ResponseEntity<?> approveJournalTransfer(@RequestBody JournalTransfer journalTransfer)
	{
		try 
		{
			journalTransferHandler.processApproveJournalTransfer(journalTransfer);
			TransactionConfig transactionConfig = journalTransfer.getTransactionConfig();
			journalTransfer.setResponseMessage(transactionConfig.getMessage());
			if (transactionConfig!=null && !"S0000".equalsIgnoreCase(transactionConfig.getCode()) ) 
			{
				journalTransfer.setStrTxnStatus("Rejected");
				journalTransfer.setStrRejectReason(transactionConfig.getMessage());
				journalTransferHandler.processRejectJournalTransfer(journalTransfer);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(journalTransfer);
	}
	
	@RequestMapping(value = "/reject", method = RequestMethod.POST)
	public ResponseEntity<?> rejectJournalTransfer(@RequestBody JournalTransfer journalTransfer)
	{
		try 
		{
			journalTransferHandler.processRejectJournalTransfer(journalTransfer);
			TransactionConfig transactionConfig = journalTransfer.getTransactionConfig();
			journalTransfer.setResponseMessage(transactionConfig.getMessage());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(journalTransfer);
	}
	
	@RequestMapping(value = "/getAccountauthoriselist", method = RequestMethod.POST)
	public ResponseEntity<?> createInstanceAccounts(@RequestBody JournalTransfer journalTransfer)
	{
		String result = null;
		try 
		{
			List<JournalTransfer> authoriseVeriflyAcct = journalTransferService.getAccountVeriflylist(journalTransfer);
			return ResponseEntity.ok(authoriseVeriflyAcct);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}             
	
	@RequestMapping(value = "/getTxnIdAuthoriselist", method = RequestMethod.POST)
	public ResponseEntity<?> getAuthoriseAccountTXnID(@RequestBody JournalTransfer journalTransfer)
	{
		String result = null;
		try 
		{
			List<JournalTransfer> txnIdJournalAccount = journalTransferService.getTxnIdApprovalInfo(journalTransfer);
		    return ResponseEntity.ok(txnIdJournalAccount);					
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/updateReasonInfo", method = RequestMethod.POST)
	public ResponseEntity<?> UpdateReasonData(@RequestBody JournalTransfer journalTransfer)
	{
		String result = null;
		try 
		{
			int  txnIdJournalAccount = journalTransferService.getUpdateReasonlist(journalTransfer);
		    return ResponseEntity.ok(txnIdJournalAccount);					
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}  
}
