package ams.cms.dao;

import ams.cms.model.Channels;

public interface ChannelsDao extends GenericDao<Channels>
{
	Channels getChannelsList(Channels channels);

	
}
