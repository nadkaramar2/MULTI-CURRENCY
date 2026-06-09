package ams.cms.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.MultiCurrencyFeeTypeMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.MultiCurrencyFeeTypeMaster;

@Repository
public class MultiCurrencyFeeTypeMasterDaoImpl extends AbstractGenericDao<MultiCurrencyFeeTypeMaster> implements MultiCurrencyFeeTypeMasterDao{

	
	@Autowired
	private	JdbcTemplate jdbcTemplate;
	
	@Override
	public MultiCurrencyFeeTypeMaster getFeeTypeDetails(MultiCurrencyFeeTypeMaster feeTypeMaster) {
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT mcft.fee_type AS feeType , mcft.fee_description AS feeDescription, mcft.is_flat_fee AS IsFlatFee ,mcft.fee_amt ");
			queryBuilder.append(" AS feeAmt , mcft.is_percentage_fee AS isPercentageFee ,mcft.percentage_fee AS feePercentage , mcft.gl_account_type as glAccountType , mcft.gl_account_number as glAccountNumber , ");
			queryBuilder.append(" mcft.gst_type AS gstType FROM multi_currency_fee_type_master mcft WHERE mcft.fee_type = '"+feeTypeMaster.getFeeType()+"' ");

			List<MultiCurrencyFeeTypeMaster> feeTypeMasters  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<MultiCurrencyFeeTypeMaster>(MultiCurrencyFeeTypeMaster.class),
			new Object[]  {});
			
			if (feeTypeMasters!=null && feeTypeMasters.size() > 0) 
			{
				return feeTypeMasters.get(0);
			}	
		}
		catch (Exception e) 
		{
			System.out.println("Exception in getAccountCreditCardTxnWise::"+e);
			e.printStackTrace();
		}
		return null;
}

}
