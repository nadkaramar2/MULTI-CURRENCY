package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.model.AccountStatementHeader;
import ams.cms.dao.GLAccountStatementDao;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.services.GLAccountStatementService;
import ams.cms.utility.Utils;

@Transactional
@Service
public class GLAccountStatementServiceImpl implements GLAccountStatementService 
{
	@Autowired
	private GLAccountStatementDao glAccountStatementDao;
	
	@Override
	public GLAccountStatement addGlAccountStatementData(GLAccountStatement glAccountStatement) throws Exception
	{
			glAccountStatement.setCreatedDate(Utils.getCurrentDate());
			glAccountStatementDao.save(glAccountStatement);
			return glAccountStatement;
	}

	@Override
	public List<GLAccountStatement> getGLAccountStatement(GLAccountStatement glAccountStatement) throws Exception
	{
			return glAccountStatementDao.getGLAccountStatement(glAccountStatement);
	}
	
	//added by sunil Y , new api creation for header response in gl account statement and account statement , started
	@Override
	public List<AccountStatementHeader> getGLAccountStatementData(String strAccountNumber) throws Exception
	{
			return glAccountStatementDao.getAccountStatementAndGlDetails(strAccountNumber);
	}
	
	//added by sunil Y , new api creation for header response in gl account statement and account statement , started
		@Override
		public List<GLAccountTypeMaster> getGLAccountStatementDataHeader(String strAccountNumber) throws Exception
		{
				return glAccountStatementDao.getGlDetailsHeader(strAccountNumber);
		}
	
	
	@Override
	public GLAccountStatement addGLAccountStatementData(GLAccountStatement glAccountStatement) throws Exception
	{
		glAccountStatement.setCreatedDate(Utils.getCurrentDate());
		glAccountStatementDao.save(glAccountStatement);
		return glAccountStatement;
	}

	//@Transactional
	@Override
	public int[] batchEntryOfGLAccountStatement(List<GLAccountStatement> glAccountStatements) throws Exception 
	{
		return glAccountStatementDao.batchEntryOfGLAccountStatement(glAccountStatements);
	}

	@Override
	public GLAccountStatement addGLAccountStatement(GLAccountStatement glAccountStatement) throws Exception {
		glAccountStatement.setCreatedDate(Utils.getCurrentDate());
		glAccountStatementDao.save(glAccountStatement);
		return glAccountStatement;
	}

	/*
	 * @Override public List<AccountStatementHeader>
	 * getGLAccountStatementData(String strAccountNumber) throws Exception { return
	 * glAccountStatementDao.getAccountStatementAndGlDetails(strAccountNumber); }
	 */

}
