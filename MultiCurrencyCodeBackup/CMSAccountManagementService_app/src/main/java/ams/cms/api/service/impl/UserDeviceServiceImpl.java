package ams.cms.api.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.UserDeviceDao;
import ams.cms.api.model.UserDevice;
import ams.cms.api.service.UserDeviceService;

@Transactional
@Service
public class UserDeviceServiceImpl implements UserDeviceService
{
	@Autowired
	UserDeviceDao userDeviceDao;
	
	@Override
	public UserDevice saveUserDevice(UserDevice userDevice) throws Exception 
	{
		userDeviceDao.save(userDevice);
		return userDevice;
	}

}
