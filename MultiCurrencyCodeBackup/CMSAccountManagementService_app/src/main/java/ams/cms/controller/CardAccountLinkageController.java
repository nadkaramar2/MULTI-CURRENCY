package ams.cms.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.CardAccountLinkage;
import ams.cms.services.CardAccountLinkageService;

@RestController
@RequestMapping("/card-account-linkage")
public class CardAccountLinkageController
{
	@Autowired
	CardAccountLinkageService cardAccountLinkageService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addCardAccountLinkageData(@RequestBody CardAccountLinkage cardAccountLinkage)
	{
		String result = null;
		try 
		{
			CardAccountLinkage cardAccountLinkageDta = cardAccountLinkageService.addCardAccountLinkageData(cardAccountLinkage);
			return ResponseEntity.ok(cardAccountLinkageDta);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/checkDataExist", method = RequestMethod.POST)
	public ResponseEntity<?> isExistCardAccountLinkageData(@RequestBody CardAccountLinkage cardAccountLinkage)
	{
		String result = null;
		try 
		{
			Boolean isDataExist = cardAccountLinkageService.isCardAccountLinkageDataExist(cardAccountLinkage);
			return ResponseEntity.ok(isDataExist);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getLinkagedata-based-on-account", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountLinkageWithCardBasedOnAccount(@RequestBody CardAccountLinkage cardAccountLinkage)
	{
		String result = null;
		try 
		{
			List<CardAccountLinkage> cardAccountLinkages = cardAccountLinkageService.getCardAccountLinkageBasedOnAccount(cardAccountLinkage);
			return ResponseEntity.ok(cardAccountLinkages);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getLinkagedata-based-on-card", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountLinkageWithCardBasedOnCard(@RequestBody CardAccountLinkage cardAccountLinkage)
	{
		String result = null;
		try 
		{
			List<CardAccountLinkage> cardAccountLinkages = cardAccountLinkageService.getCardAccountLinkageDataBasedOnCard(cardAccountLinkage);
			return ResponseEntity.ok(cardAccountLinkages);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getLinkagedata", method = RequestMethod.POST)
	public ResponseEntity<?> getLinkagedataObject(@RequestBody CardAccountLinkage cardAccountLinkage)
	{
		String result = null;
		try 
		{
			cardAccountLinkage = cardAccountLinkageService.getCardAccountLinkages(cardAccountLinkage);
			return ResponseEntity.ok(cardAccountLinkage);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	@RequestMapping(value = "/getCardLinkAccountList", method = RequestMethod.POST)
	public ResponseEntity<?> getCardLinkAccountList(@RequestBody CardAccountLinkage cardAccountLinkage)
	{
		String result = null;
		try 
		{
			List<CardAccountLinkage> lisAccountLinkages = cardAccountLinkageService.getCardLinkAccountList(cardAccountLinkage);
			return ResponseEntity.ok(lisAccountLinkages);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getLinkageCardDetailsBasedOnCustId", method = RequestMethod.POST)
	public ResponseEntity<?> getLinkageCardDetailsBasedOnCustId(@RequestBody CardAccountLinkage cardAccountLinkage)
	{
		String result = null;
		try 
		{
			List<CardAccountLinkage> lisAccountLinkages = cardAccountLinkageService.getLinkageCardDetailsBasedOnCustId(cardAccountLinkage);
			return ResponseEntity.ok(lisAccountLinkages);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	
	@RequestMapping(value = "/updateExpiryDate", method = RequestMethod.POST)
	public ResponseEntity<?> updateExpiryDate(@RequestBody CardAccountLinkage cardAccountLinkage)
	{
		Integer result = null;
		try 
		{
			 /*Date date = Calendar.getInstance().getTime();  
             DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
             String strDate = dateFormat.format(date);*/  
             
            cardAccountLinkage.setCreationDate(new Date());
			result = cardAccountLinkageService.updateExpiryDate(cardAccountLinkage);
			return ResponseEntity.ok(result);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	

}
