package ams.cms.services.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.MultiCurrencyChargesReportDao;
import ams.cms.model.MultiCurrencyChargesReport;
import ams.cms.services.MultiCurrencyChargesReportService;

@Transactional
@Service
public class MultiCurrencyChargesReportServiceImpl implements MultiCurrencyChargesReportService {

	@Autowired
	private MultiCurrencyChargesReportDao multiCurrencyChargesReportDao;
	
	@Override
	public void saveMultiCurrencyChargeReport(MultiCurrencyChargesReport multiCurrencyFeeReport) {
		multiCurrencyChargesReportDao.save(multiCurrencyFeeReport);
		
	}
	
	@Override
	public MultiCurrencyChargesReport saveMultiCurrencyChargReport(MultiCurrencyChargesReport multiCurrencyFeeReport) {
		multiCurrencyChargesReportDao.save(multiCurrencyFeeReport);
		return multiCurrencyFeeReport;
	}

}
