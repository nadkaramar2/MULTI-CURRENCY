package ams.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.MccWiseInterestDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.MccWiseInterestModel;

@Repository
public class MccWiseInterestDaoImpl extends AbstractGenericDao<MccWiseInterestModel> implements MccWiseInterestDao
{
	@Autowired
	JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<MccWiseInterestModel> getMccWiseInterest(MccWiseInterestModel mccWiseInterestModel) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (mccWiseInterestModel.getStrParticipantID()!=null && mccWiseInterestModel.getStrParticipantID().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strParticipantID", mccWiseInterestModel.getStrParticipantID()));
			}
			if (mccWiseInterestModel.getStrAccountType()!=null && mccWiseInterestModel.getStrAccountType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountType", mccWiseInterestModel.getStrAccountType()));
			}
			
			List<MccWiseInterestModel> listData = (List<MccWiseInterestModel>) criteria.list();
			return listData;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	//Added by Pankaj P for validate values - Start
	@Override
	public Boolean validateMccWiseInterst(MccWiseInterestModel mccWiseInterestModel) {
		
		try {	
			String sql = "SELECT COUNT(mcc_code) FROM mcc_wise_interest "
					+ "where account_type = ? "
					+ "AND mcc_code = ?";

			String validateCount = this.jdbcTemplate.queryForObject(sql, String.class,
					new Object[]
					{
						mccWiseInterestModel.getStrAccountType(),
						mccWiseInterestModel.getStrMccCode()
					}
			);
			if(Integer.valueOf(validateCount) > 0)
			{
				return true;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	//Added by Pankaj P for validate values - End
	
	
	//Added by Abhishek T for view Mcc_wise_interest table-start
	@Override
	public List<MccWiseInterestModel> getMccWiseInterestView(MccWiseInterestModel mccWiseInterestModel)
	{
		try 
		{
		   String statementQuery = "SELECT account_type As strAccountType,mcc_code AS strMccCode ,interest_rate AS strInterestRate,grace_period AS strGracePeriod,"
		    + "payment_received_within AS strPaymentReceivedWithinDays FROM mcc_wise_interest ORDER BY created_date DESC ";
			List<MccWiseInterestModel> merchantCategoryCodeMasters  = jdbcTemplate.query(statementQuery,
					new BeanPropertyRowMapper<MccWiseInterestModel>(MccWiseInterestModel.class),
					new Object[]{}
			);
			return merchantCategoryCodeMasters ;
		}
	catch (Exception e) 
	{
		System.out.println("Exception in AccountTransactionlist::"+e);
		e.printStackTrace();
	}
	return null;
	}
	//Added by Abhishek T for view Mcc_wise_interest table-End
	
	@SuppressWarnings("deprecation")
	@Override
	public String getGracePeriod(String mcc) {
	try 
	{
		
		String gracePeriod = this.jdbcTemplate.queryForObject("select grace_period from mcc_wise_interest"
				+ " where mcc_code = ?",
				new Object[] { mcc }, String.class);
		System.out.println("gracePeriod::" + gracePeriod);
		return gracePeriod;
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
	}
}
