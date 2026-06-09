package ams.cms.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.RevolvingCreditCardMaster;
import ams.cms.services.RevolvingCreditCardService;

@RestController
@RequestMapping("/revolvingCreditCard")
public class RevolvingCreditCardController
{
	@Autowired
	RevolvingCreditCardService revolvingCreditCardService;
	
	@RequestMapping(value = "/addRevolvingCreditCard", method = RequestMethod.POST)
	public ResponseEntity<?> addRevolvingCreditCardData(@RequestBody RevolvingCreditCardMaster revolvingCreditCardMaster)
	{
		String result = null;
		try {
			revolvingCreditCardMaster.setStrDateOfCreation(new Date());
			System.out.println("revolvingCreditCardMaster::"+revolvingCreditCardMaster);
			
			revolvingCreditCardMaster = revolvingCreditCardService.addRevolvingCreditCardData(revolvingCreditCardMaster);
			if(revolvingCreditCardMaster.getStrId() > 0)
			{
				return ResponseEntity.ok(revolvingCreditCardMaster.getStrId()+"");  
			}
			
		}catch (Exception e) {
			System.out.println("RevolvingCreditCardController.addRevolvingCreditCardData()"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/validateRevolvingCreditCard", method = RequestMethod.POST)
	public ResponseEntity<?> validateRevolvingCreditCard(@RequestBody RevolvingCreditCardMaster revolvingCreditCardMaster)
	{
		String result = null;
		try 
		{
			Boolean revolvingCreditCardMasters = revolvingCreditCardService.validateRevolvingCreditCard(revolvingCreditCardMaster);
			System.out.println("revolvingCreditCardMaster:::"+revolvingCreditCardMasters);
			return ResponseEntity.ok(revolvingCreditCardMasters);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
}
