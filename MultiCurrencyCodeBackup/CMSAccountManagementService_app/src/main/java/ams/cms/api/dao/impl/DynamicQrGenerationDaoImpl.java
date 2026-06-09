package ams.cms.api.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.DynamicQrGenerationDao;
import ams.cms.api.model.DynamicQrGeneration;
import ams.cms.dao.generic.AbstractGenericDao;

@Repository
public class DynamicQrGenerationDaoImpl extends AbstractGenericDao<DynamicQrGeneration> implements DynamicQrGenerationDao  {
	
	@Autowired
	JdbcTemplate jdbcCMSTemplate;
	//Added by Pankaj Pawar for dynamic QrGeneration code [Start]
	@Override
	public int updateDynamicQrCode(DynamicQrGeneration dynamicQrGeneration) {
		try
		{
			StringBuilder querySb = new StringBuilder("UPDATE dynamic_qr ");
			querySb.append("SET qr_code = ?, qr_path = ?, qr_code_image_url = ? ");
			querySb.append("WHERE from_account_number = ? and ref_no = ?");
			
			int count = jdbcCMSTemplate.update(querySb.toString(),
			new Object[]
			{
					dynamicQrGeneration.getQrCode(),
					dynamicQrGeneration.getQrPath(),
					dynamicQrGeneration.getStrQrCodeImageUrl(),
					dynamicQrGeneration.getFromAccountNumber(),
					dynamicQrGeneration.getRefNo()					
			});
			return count;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
		//Added by Pankaj Pawar for dynamic QrGeneration code[End]
	}
	
	@Override
	public int updateQrCodeFieldsAfterTxn(DynamicQrGeneration dynamicQrGeneration) {
		try
		{
			StringBuilder querySb = new StringBuilder("UPDATE dynamic_qr ");
			querySb.append("SET paid_by_account_type = ?, paid_by_account_number = ?, tran_id = ?, ");
			querySb.append("payment_received_date = ?, payment_received_time = ?, ");
			querySb.append("response_code = ? WHERE ref_no = ? ");
			
			int count = jdbcCMSTemplate.update(querySb.toString(),
			new Object[]
			{
					dynamicQrGeneration.getPaidByAccountType(),
					dynamicQrGeneration.getPaidByAccountNumber(),
					dynamicQrGeneration.getTranId(),
					dynamicQrGeneration.getPaymentReceivedDate(),
					dynamicQrGeneration.getPaymentReceivedTime(),
					dynamicQrGeneration.getResponseCode(),
					dynamicQrGeneration.getRefNo()
			});
			return count;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
