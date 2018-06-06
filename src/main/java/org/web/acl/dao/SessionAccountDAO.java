package org.web.acl.dao;

import java.util.List;
import org.web.acl.query.QuerySessionAccount;
import org.web.acl.domain.SessionAccountDO;

public interface SessionAccountDAO {

	public SessionAccountDO selectSessionAccountBySessionAcountId(Long sessionAcountId);

	public List< SessionAccountDO > selectSessionAccountList(QuerySessionAccount querySessionAccount);

	public Integer countSessionAccountList(QuerySessionAccount querySessionAccount);

	public int insertSessionAccount(SessionAccountDO sessionAccountDO);

	public int updateSessionAccountBySessionAcountId(SessionAccountDO sessionAccountDO);
	
	public List<String> selectDistinctList(QuerySessionAccount querySessionAccount);

}

