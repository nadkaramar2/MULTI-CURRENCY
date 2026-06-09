package ams.cms.api.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.TransactionIdTableDao;
import ams.cms.api.model.TransactionIdTable;
import ams.cms.api.service.TransactionIdTableService;

@Transactional
@Service
public class TransactionIdTableServiceImpl implements TransactionIdTableService {

	@Autowired
	private TransactionIdTableDao transactionIdTableDao;
	@Override
	public int  saveTransactionIdDetails(TransactionIdTable transactionIdTable){
		int count = transactionIdTableDao.saveTransactionIdDetails(transactionIdTable);

		return count;
	}
	public List<TransactionIdTable> getTransactionIdList(String year,String julianDate){
		return transactionIdTableDao.getTransactionIdTableList(year, julianDate);
	}
	public int updateTransactionIdDetails(TransactionIdTable transactionIdTable) {
		return transactionIdTableDao.updateTransactionIdDetails(transactionIdTable);
	}

}
