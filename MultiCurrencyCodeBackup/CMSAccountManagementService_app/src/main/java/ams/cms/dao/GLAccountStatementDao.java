package ams.cms.dao;

import java.util.List;

import ams.cms.api.model.AccountStatementHeader;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;

public interface GLAccountStatementDao extends GenericDao<GLAccountStatement> 
{
	List<GLAccountStatement> getGLAccountStatement(GLAccountStatement glAccountStatement);
	
	List<AccountStatementHeader> getAccountStatementAndGlDetails(String  strAccountNumber);

	List<GLAccountTypeMaster> getGlDetailsHeader(String strAccountNumber);
	
	int[] batchEntryOfGLAccountStatement(List<GLAccountStatement> glAccountStatements); 
}
