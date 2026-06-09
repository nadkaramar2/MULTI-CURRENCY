package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.Channels;
import ams.cms.services.ChannelsService;

@RestController
@RequestMapping("/channels")
public class ChannelsController
{
	@Autowired
	ChannelsService channelsService;
	
	@RequestMapping(value = "/getChannelList", method = RequestMethod.POST)
	public ResponseEntity<?> getChannelList(@RequestBody Channels channels)
	{
		try 
		{
			Channels channelList = channelsService.getChannelsList(channels);
			return ResponseEntity.ok(channelList);				
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
}
