/*Added by pankaj Pawar for QR CODE Generation*/

package ams.cms.api.dao;

import java.util.Map;

import ams.cms.api.model.QrInfo;

public interface QrCodeDao {

	int updateQrCodetoAccount(QrInfo qrInfo);

}
