package ams.cms.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.dao.ParticipantWiseWalletDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.ParticipantWiseWalletMaster;

@Repository
public class ParticipantWiseWalletDaoImpl extends AbstractGenericDao<ParticipantWiseWalletMaster> implements ParticipantWiseWalletDao
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ParticipantWiseWalletMaster> getParticipantBasedMcc(ParticipantWiseWalletMaster participantWiseWalletMaster) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			criteria.add(Restrictions.eq("strParticipantID", participantWiseWalletMaster.getStrParticipantID()));
			
			List<ParticipantWiseWalletMaster> participantWiseWalletMasters = (List<ParticipantWiseWalletMaster>) criteria.list();
			if (participantWiseWalletMasters !=null && participantWiseWalletMasters.size() > 0) 
			{
				return participantWiseWalletMasters;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	@Override
	public List<ParticipantWiseWalletMaster> getParticipantBasedMccAndDescr(ParticipantWiseWalletMaster participantWiseWalletMaster) 
	{
		try
		{
			List<ParticipantWiseWalletMaster> participantWiseWalletMasters = this.jdbcTemplate.query(
					"SELECT pwwm.mcc_code AS strMccCode, mccm.mcc_desc AS strMccCodeDesc "
					+ "FROM participant_wise_wallet_master pwwm "
					+ "INNER JOIN merchant_category_code_master mccm "
					+ "ON pwwm.mcc_code = mccm.mcc_code WHERE pwwm.participant_id= ?",
					new Object[] { participantWiseWalletMaster.getStrParticipantID() },
					(RowMapper) new BeanPropertyRowMapper(ParticipantWiseWalletMaster.class));
			
			return participantWiseWalletMasters;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int[] batchEntryforParticipantWiseWallet(List<ParticipantWiseWalletMaster> listOfParticipantWiseWalletMasters) 
	{
		int[] batchResponse = null;
		try
		{
			return this.jdbcTemplate.batchUpdate("INSERT INTO participant_wise_wallet_master "
					+ "(participant_id, mcc_code, created_by, creation_date) "
					+ "values(?,?,?,?)", new BatchPreparedStatementSetter() 
					{
						@Override
						public void setValues(PreparedStatement psmt, int i) throws SQLException 
						{
							psmt.setString(1, listOfParticipantWiseWalletMasters.get(i).getStrParticipantID());
							psmt.setString(2, listOfParticipantWiseWalletMasters.get(i).getStrMccCode());
							psmt.setString(3, listOfParticipantWiseWalletMasters.get(i).getStrCreatedBy());
							//psmt.setDate(4, new Date(listOfParticipantWiseWalletMasters.get(i).getStrDateOfCreation().getTime()));
							psmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
						}
						
						@Override
						public int getBatchSize() {
							return listOfParticipantWiseWalletMasters.size();
						}
					});
		}
		catch (Exception e) {
			System.out.println("Exception in batchEntryOfInstanAccount::"+e);
			e.printStackTrace();
		}
		return batchResponse;
	
	}
	
	
}
