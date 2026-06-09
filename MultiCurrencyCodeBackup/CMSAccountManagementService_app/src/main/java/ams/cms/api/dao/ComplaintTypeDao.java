package ams.cms.api.dao;
import java.util.List;

import ams.cms.api.model.AccountRequest;
import ams.cms.api.model.ComplaintType;
import ams.cms.dao.GenericDao;


//created by ankit
public interface ComplaintTypeDao extends GenericDao<ComplaintType>
{
	List<String> findAllByComplaintType(AccountRequest accountRequest);
	
	
}
