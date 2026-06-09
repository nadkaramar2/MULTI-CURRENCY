package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.DormantToActiveMaster;
import ams.cms.services.DormantAccountReqResStatusService;
import ams.cms.util.ProcessResponse;

@RestController
@RequestMapping("/dormant")
public class DormantAccountController 
{
	@Autowired
	private DormantAccountReqResStatusService dormantToActiveService;

	@RequestMapping(value = "/getPendingCheckerUserList", method = RequestMethod.GET)
	public ResponseEntity<?> getPendingCheckerUserList() 
	{
		ProcessResponse processResponse = new ProcessResponse();
		//String result = null;
		try {
			DormantToActiveMaster dormantToActiveMaster = new DormantToActiveMaster();
			List<DormantToActiveMaster> dormantToActiveMasters = dormantToActiveService.getPendingCheckerUserList(dormantToActiveMaster);
			
			if (dormantToActiveMasters.size() > 0) 
			{
				processResponse.setCode("S0000");
				processResponse.setMessage("Data Fetch Scussefully");
				processResponse.setDormantToActiveMasters(dormantToActiveMasters);
				return ResponseEntity.ok(processResponse);
			}else {
			
				processResponse.setCode("E0000");
				processResponse.setMessage("No Data Found");
				
			}

		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(processResponse);
	}

}
