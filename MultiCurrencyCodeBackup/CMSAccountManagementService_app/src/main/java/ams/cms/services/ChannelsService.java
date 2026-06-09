package ams.cms.services;

import java.util.List;

import ams.cms.model.Channels;

public interface ChannelsService 
{
	public Channels getChannelsList(Channels channels) throws Exception;

	public Channels getChannelObject();

}
