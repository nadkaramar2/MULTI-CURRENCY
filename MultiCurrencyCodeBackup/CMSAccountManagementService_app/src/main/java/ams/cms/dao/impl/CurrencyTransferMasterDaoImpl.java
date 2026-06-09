package ams.cms.dao.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.CurrencyTransferMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.AccountCreation;
import ams.cms.model.CurrencyTransferMaster;

@Repository
public class CurrencyTransferMasterDaoImpl extends AbstractGenericDao<CurrencyTransferMaster> implements CurrencyTransferMasterDao{
	
	@Autowired
	private	JdbcTemplate jdbcTemplate;

	@Override
	public List<CurrencyTransferMaster> getCurrencyMasterByTranId(CurrencyTransferMaster currencyTransferMaster) {
		try 
		{
			StringBuilder selectQuerySb = new StringBuilder("SELECT cm.tran_id AS tranId, cm.sweep_out_currency_code AS sweepOutCurrencyCode, cm.sweep_out_currency_value AS sweepOutCurrencyValue,");
			selectQuerySb.append("cm.sweep_out_amnt AS sweepOutAmount, cm.sweep_in_amnt AS sweepInAmount, cm.sweep_in_currency_code AS sweepInCurrencyCode, cm.fee_amount AS feeAmount, cm.gst_amount AS gstAmount ");
			selectQuerySb.append("FROM currency_transfer_master AS cm WHERE cm.tran_id = ?");
			List<CurrencyTransferMaster> currencyList = jdbcTemplate.query(selectQuerySb.toString(),
			new BeanPropertyRowMapper<CurrencyTransferMaster>(CurrencyTransferMaster.class),
			new Object[] 
			{
					currencyTransferMaster.getTranId()
			}
			);
			return currencyList;
		}
		catch (Exception e) {
			e.getMessage();
		}
		return null;
	}

}
