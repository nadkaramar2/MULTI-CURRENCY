package ams.cms.utility;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.model.PreAccountMaster;
import ams.cms.model.UserLogin;
import ams.cms.services.UserLoginService;

@Component
public class UserSessionControl 
{
	private static final Logger logger = LoggerFactory.getLogger(UserSessionControl.class);

	private static ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private UserLoginService serviceProvider;

	//Logic to maintain session by Jyoti S
	public String CreateSessionRecord(PreAccountMaster preAccountMaster) throws Exception 
	{
		String sessionId = TokenUtils.generateSessionId(preAccountMaster);
		System.out.println("CreateSession - sessionId [" + sessionId + "]");
		UserLogin userLogin = new UserLogin();
		userLogin.setLoginTime(TokenUtils.getDate());
		userLogin.setActiveTime(userLogin.getLoginTime());
		userLogin.setIpAddress("");
		userLogin.setSessionId(sessionId);
		//userLogin.setUserId(preAccountMaster.getStrID());
		userLogin.setUserId(preAccountMaster.getStrMobileNo());
		userLogin.setStatus("A");

		serviceProvider.saveUserLogin(userLogin);

		System.out.println("validateLoginOtp - userLogin:\n" + TokenUtils.convertJsonToString(userLogin));
		return sessionId;
	}

	public String logOutSessionUpdate(PreAccountMaster preAccountMaster)
	{

		List<UserLogin> userLoginList = serviceProvider.getUserLoginDetail(preAccountMaster.getStrMobileNo(),preAccountMaster.getStrSessionId());

		if (null == userLoginList) {

			String message = "No record found";

			return message;
		}

		if (userLoginList.size() != 1) {

			String message = "Multiple record found";

			return message;
		}

		UserLogin userLoginDb = userLoginList.get(0);

		userLoginDb.setLogoutTime(TokenUtils.getDate());
		userLoginDb.setStatus("I");

		serviceProvider.updateUserLogin(userLoginDb);

		int result = serviceProvider.updateUserLogin(userLoginDb);
		if (result > 0) {
			return "Success";
		}
		return "Failure";
	}

	public String validateLoginSession(PreAccountMaster preAccountMaster) {

		System.out.println("validateLoginSession - Request-\n" + preAccountMaster);

		List<UserLogin> userLoginList = serviceProvider.getUserLoginDetail(preAccountMaster.getStrSessionId());

		if (null == userLoginList) {

			String message = "No record found";

			return message;
		}

		if (userLoginList.size() != 1) {

			String message = "Multiple record found";

			return message;
		}

		UserLogin userLoginDb = userLoginList.get(0);

		if (!("A".equalsIgnoreCase(userLoginDb.getStatus()))) {

			String message = "Session Expired";

			return message;

		}

		return null;
	}

	public String updateLoginSession(PreAccountMaster preAccountMaster) {

		List<UserLogin> userLoginList = serviceProvider.getUserLoginDetail(preAccountMaster.getStrID(),
				preAccountMaster.getStrSessionId());

		if (null == userLoginList) {

			String message = "No record found";

			return message;
		}

		if (userLoginList.size() != 1) {

			String message = "Multiple record found";

			return message;
		}

		UserLogin userLoginDb = userLoginList.get(0);

		if (!("A".equalsIgnoreCase(userLoginDb.getStatus()))) {

			String message = "Invalid Session";

			return message;

		}

		userLoginDb.setActiveTime(new Date());
		serviceProvider.updateUserLogin(userLoginDb);
		String message = "Successfull";

		return message;
	}

	public String terminateActiveSession(PreAccountMaster preAccountMaster) {

		System.out.println("terminateActiveSession - Request-\n" + preAccountMaster);

		List<UserLogin> userLoginList = serviceProvider.getUserLoginDetail(preAccountMaster.getStrMobileNo(),
				preAccountMaster.getStrSessionId());

		if (null == userLoginList) {

			String message = "No record found";

			return message;
		}

		if (userLoginList.size() != 1) {

			String message = "Multiple record found";

			return message;
		}

		UserLogin userLoginDb = userLoginList.get(0);

		if (!("A".equalsIgnoreCase(userLoginDb.getStatus()))) {

			String message = "Invalid Session";

			return message;

		}

		userLoginDb.setStatus("T");
		userLoginDb.setActiveTime(new Date());
		serviceProvider.updateUserLogin(userLoginDb);

		String message = "Success";

		return message;
	}
}
