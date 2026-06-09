package ams.cms.services.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.ChannelsDao;
import ams.cms.model.Channels;
import ams.cms.services.ChannelsService;

@Transactional
@Service
public class ChannelsServiceImpl implements ChannelsService
{
	@Autowired
	ChannelsDao channelsDao;
	
	@Override
	public Channels getChannelsList(Channels channels) throws Exception 
	{
		return channelsDao.getChannelsList(channels);
	}

	@Override
	public Channels getChannelObject() {
		
		return null;
	}
	
	
}
