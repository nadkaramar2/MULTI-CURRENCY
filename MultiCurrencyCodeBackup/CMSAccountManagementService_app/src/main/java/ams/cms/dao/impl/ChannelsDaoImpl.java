package ams.cms.dao.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.ChannelsDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.Channels;

@Repository
public class ChannelsDaoImpl extends AbstractGenericDao<Channels> implements ChannelsDao
{
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public Channels getChannelsList(Channels channels) {
		
		try 
	{	
			StringBuilder sql = new StringBuilder("SELECT ch.channel_code AS channelCode, ch.channel_type AS strChannelType ,  ch.channel_description AS strChannelDescription , ");
			sql.append( "  ch.gl_account_number AS glAccountNumber , ch.gl_account_type AS glAccountType ");
			sql.append( " FROM channels ch WHERE ch.channel_code= '"+channels.getChannelCode().trim()+"'");
			
			

			List<Channels> accountMasters  = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<Channels>(Channels.class), new Object[]  {});
			if (accountMasters!=null && accountMasters.size() > 0) 
			{
				return accountMasters.get(0);
			}	
		} 
	catch (Exception e) 
	{
		
	}

	return null;
	}
	
}
