package ams.cms.services;

import java.util.List;

import ams.cms.api.model.AccountStatementHeader;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;

public interface GLAccountStatementService 
{
	GLAccountStatement addGlAccountStatementData(GLAccountStatement glAccountStatement) throws Exception;
	
	List<GLAccountStatement> getGLAccountStatement(GLAccountStatement glAccountStatement) throws Exception;
	
	GLAccountStatement addGLAccountStatementData(GLAccountStatement glAccountStatement) throws Exception;

	List<AccountStatementHeader> getGLAccountStatementData(String strAccountNumber) throws Exception;

	List<GLAccountTypeMaster> getGLAccountStatementDataHeader(String strAccountNumber) throws Exception;
	
	int[] batchEntryOfGLAccountStatement(List<GLAccountStatement> glAccountStatements) throws Exception;
	
	GLAccountStatement addGLAccountStatement(GLAccountStatement glAccountStatement) throws Exception;
}
