package ams.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.services.RevolvingCreditCardMasterService;

@RestController
@RequestMapping("/revolvingCreditCardMstr")
public class RevolvingCreditCardMasterController {
	
	@Autowired
	RevolvingCreditCardMasterService revolvingCreditCardMasterService;
	
	@RequestMapping(value = "/getGracePeriod", method = RequestMethod.POST)
	public ResponseEntity<?> getGracePeriod(@RequestBody String accountType)
	{
		String result = null;
		try 
		{
			result = revolvingCreditCardMasterService.getGracePeriod(accountType);
			if (result != null) 
			{
				return ResponseEntity.ok(result);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			result = "Exception while save AccountTypes:"+e.getMessage();
		}
		return ResponseEntity.ok(result);
	}

}
