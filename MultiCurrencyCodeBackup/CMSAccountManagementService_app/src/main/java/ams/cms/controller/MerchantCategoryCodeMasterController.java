package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.MerchantCategoryCodeMaster;
import ams.cms.services.MerchantCategoryCodeMasterService;

@RestController
@RequestMapping("/mcc_code")
public class MerchantCategoryCodeMasterController 
{
	@Autowired
	MerchantCategoryCodeMasterService merchantCategoryCodeMasterService;
	
	@RequestMapping(value = "/getAllMccCode", method = RequestMethod.POST)
	public ResponseEntity<?> getAllMccCode(@RequestBody MerchantCategoryCodeMaster merchantCategoryCodeMaster)
	{
		String result = null;
		try 
		{
			List<MerchantCategoryCodeMaster> mccListData = merchantCategoryCodeMasterService.getAllListOfMerchantCategoryCode(merchantCategoryCodeMaster);
			if (mccListData!=null && mccListData.size() > 0) 
			{
				return ResponseEntity.ok(mccListData);
			}
		}
		catch (Exception e) {
			System.out.println("Exception in getAccountInfoListByParticipantId::"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}	
	
	@RequestMapping(value = "/getUnSelectedAllMCC", method = RequestMethod.POST)
	public ResponseEntity<?> getUnSelectedAllMCC(@RequestBody MerchantCategoryCodeMaster merchantCategoryCodeMaster)
	{
		String result = null;
		try 
		{
			List<MerchantCategoryCodeMaster> mccListData = merchantCategoryCodeMasterService.getUnSelectedMccAndDescrList(merchantCategoryCodeMaster);
			if (mccListData!=null && mccListData.size() > 0) 
			{
				return ResponseEntity.ok(mccListData);
			}
		}
		catch (Exception e) {
			System.out.println("Exception in getUnSelectedAllMCC::"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getSelectedMCCList", method = RequestMethod.POST)
	public ResponseEntity<?> getSelectedMCCList(@RequestBody MerchantCategoryCodeMaster merchantCategoryCodeMaster)
	{
		String result = null;
		try 
		{
			List<MerchantCategoryCodeMaster> mccListData = merchantCategoryCodeMasterService.getSelectedMccAndDescrList(merchantCategoryCodeMaster);
			if (mccListData!=null && mccListData.size() > 0) 
			{
				return ResponseEntity.ok(mccListData);
			}
		}
		catch (Exception e) {
			System.out.println("Exception in getSelectedMCCList::"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
}
