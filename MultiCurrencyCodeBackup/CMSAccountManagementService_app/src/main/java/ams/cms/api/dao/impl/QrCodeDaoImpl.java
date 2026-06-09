/*Added by pankaj Pawar for QR CODE Generation*/

package ams.cms.api.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.QrCodeDao;
import ams.cms.api.model.QrInfo;

@Repository
public class QrCodeDaoImpl implements QrCodeDao
{
	@Autowired
	JdbcTemplate jdbcCMSTemplate;

	@Override
	public int updateQrCodetoAccount(QrInfo qrInfo) 
	{
		try
		{
			StringBuilder querySb = new StringBuilder("UPDATE account_master ");
			querySb.append("SET qr_code_data = ?, qr_code_file_path = ?, qr_code_image_url = ? ");
			querySb.append("WHERE account_number = ? and account_type = ?");
			
			int count = jdbcCMSTemplate.update(querySb.toString(),
			new Object[]
			{
				qrInfo.getBase64Image(),
				qrInfo.getFilePath(),
				qrInfo.getQrCodeImageUrl(),
				qrInfo.getAccount_no(),
				qrInfo.getAccount_type()
			});
			return count;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
