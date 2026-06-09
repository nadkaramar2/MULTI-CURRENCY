package ams.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.RevolvingCreditCardTxnMaster;
import ams.cms.services.RevolvingCreditCardTxnService;

@RestController
@RequestMapping("/revolvingCreditCardTxn")
public class RevolvingCreditCardTxnController {
	
	@Autowired
	RevolvingCreditCardTxnService revolvingCreditCardTxnService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addAccountStatementData(@RequestBody RevolvingCreditCardTxnMaster revolvingCreditCardTxnMaster) 
	{
		String result = null;
		try 
		{	
			revolvingCreditCardTxnMaster = revolvingCreditCardTxnService.addAccountTransactionData(revolvingCreditCardTxnMaster);
			return ResponseEntity.ok(revolvingCreditCardTxnMaster);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}

}
