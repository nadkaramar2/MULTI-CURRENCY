package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.MccWiseInterestModel;
import ams.cms.services.MccWiseInterestService;

@RestController
@RequestMapping("/mcc-wise-interest")
public class MccWiseInterestController 
{
	@Autowired
	MccWiseInterestService mccWiseInterestService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> createInstanceAccounts(@RequestBody MccWiseInterestModel mccWiseInterestModel)
	{
		String result = null;
		try 
		{
			mccWiseInterestModel = mccWiseInterestService.saveMccWiseInterest(mccWiseInterestModel);
			return ResponseEntity.ok(mccWiseInterestModel);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getMccWiseInterest", method = RequestMethod.POST)
	public ResponseEntity<?> getMccWiseInterest(@RequestBody MccWiseInterestModel mccWiseInterestModel)
	{
		String result = null;
		try 
		{
			List<MccWiseInterestModel> mccWiseInterestModels = mccWiseInterestService.getMccWiseInterest(mccWiseInterestModel);
			return ResponseEntity.ok(mccWiseInterestModels);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	//Added by Pankaj P for validate values in Table - Start
	@RequestMapping(value = "/validateMccWiseInterst", method = RequestMethod.POST)
	public ResponseEntity<?> validateMccWiseInterst(@RequestBody MccWiseInterestModel mccWiseInterestModel)
	{
		String result = null;
		try 
		{
			Boolean mccWiseInterestModels = mccWiseInterestService.validateMccWiseInterst(mccWiseInterestModel);
			System.out.println("mccWiseInterestModels:::"+mccWiseInterestModels);
			return ResponseEntity.ok(mccWiseInterestModels);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	//Added by Pankaj P for validate values in Table - End
	  
	
	//Added by Abhishek T for view Mcc_wise_interest table-start
	@RequestMapping(value = "/getMccWiseInterestView", method = RequestMethod.POST)
	public ResponseEntity<?> getMccWiseInterestView(@RequestBody MccWiseInterestModel mccWiseInterestModel)
	{
		String result = null;
		try 
		{
			List<MccWiseInterestModel> mccWiseInterestModels = mccWiseInterestService.getMccWiseInterestView(mccWiseInterestModel);
			return ResponseEntity.ok(mccWiseInterestModels);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	//Added by Abhishek T for view Mcc_wise_interest table-End
}
