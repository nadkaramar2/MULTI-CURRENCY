package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.GLAccountTypeMasterDao;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.services.GLAccountTypeMasterService;

@Transactional
@Service
public class GLAccountTypeMasterServiceImpl implements GLAccountTypeMasterService
{
	@Autowired
	private	GLAccountTypeMasterDao glAccountTypeMasterDao;
	
	@Override
	public List<GLAccountTypeMaster> getGLAccountTypesList(GLAccountTypeMaster glAccountTypeMaster) 
	{
		return glAccountTypeMasterDao.getGLAccountTypesList(glAccountTypeMaster);
	}

	@Override
	public String getClosingBalanceOfGlAccount(GLAccountTypeMaster glAccountTypeMaster) 
	{
		return glAccountTypeMasterDao.getClosingBalanceOfGlAccount(glAccountTypeMaster);
	}
	
	//@Transactional
	@Override
	public int updateGLAccountTypeDetails(GLAccountTypeMaster glAccountTypeMaster) 
	{
		return glAccountTypeMasterDao.updateGLAccountTypeDetails(glAccountTypeMaster);
	}

	@Override
	public List<GLAccountTypeMaster> GLAccountcreationView( GLAccountTypeMaster glAccountviewModel) {
		return glAccountTypeMasterDao.GLAccountcreationView(glAccountviewModel);
		
	}

	@Override
	public GLAccountTypeMaster getSingleGLAccountTypeMasterObj(GLAccountTypeMaster glAccountviewModel) throws Exception 
	{
		return glAccountTypeMasterDao.getSingleGLAccountTypeMasterObj(glAccountviewModel);
	}

	@Override
	public GLAccountTypeMaster getMappedGLAccountTypeMasterObjWithAccountType(GLAccountTypeMaster glAccountTypeMaster) throws Exception 
	{
		return glAccountTypeMasterDao.getMappedGLAccountTypeMasterObjWithAccountType(glAccountTypeMaster);
	}
	
	// Added by pankaj pawar for charts of accounts menu [Start]
	@Override
	public List<GLAccountTypeMaster> getGLAccountList(GLAccountTypeMaster glAccountTypeMaster) {
		return glAccountTypeMasterDao.getGLAccountList(glAccountTypeMaster);
	}
	// Added by pankaj pawar for charts of accounts menu [End]
	
	//created by ankit -start-
	@Override
	public List<GLAccountTypeMaster> getGLAccountTypesListThirdPartyAllow() 
	{
		List<GLAccountTypeMaster> glAccountTypeListThirdPartyAllow = glAccountTypeMasterDao.getGLAccountTypeListThirdPartyAllow();
		return glAccountTypeListThirdPartyAllow;
	}
	
	@Override
	public List<GLAccountTypeMaster> getDataOfGLAccount(GLAccountTypeMaster glAccountTypeMaster) {
		
		List<GLAccountTypeMaster> dataOfGLAccount = glAccountTypeMasterDao.getDataOfGLAccount(glAccountTypeMaster);
		return dataOfGLAccount;
	}
	
	@Override
	public List<GLAccountTypeMaster> getGLDescription(GLAccountTypeMaster glAccountTypeMaster) {
		List<GLAccountTypeMaster> glAccountDescription = glAccountTypeMasterDao.getGLAccountDescription(glAccountTypeMaster);
		return glAccountDescription;
	}
	//created by ankit -end-
	
	//created by ankit on 04-05-2023
	@Override
	public List<GLAccountTypeMaster> getClosingBalanceAndDescriptionOfGlAccount(GLAccountTypeMaster glAccountTypeMaster)
	{
	    return glAccountTypeMasterDao.getGLClosingBalanceAndDescription(glAccountTypeMaster);
	    
	}
	//created by ankit on 04-05-2023
	
	@Override
	public GLAccountTypeMaster getGLAccountNumberOnAccountType(GLAccountTypeMaster glAccountTypeMaster) {

		return glAccountTypeMasterDao.getGLAccountNumberOnAccountType(glAccountTypeMaster);
	}

	//@Transactional
	@Override
	public int[] updatesBatchEntryOfLinkedGLAccount(List<GLAccountTypeMaster> glAccountTypeMasters) throws Exception 
	{
		return glAccountTypeMasterDao.updatesBatchEntryOfLinkedGLAccount(glAccountTypeMasters);
	}
}
