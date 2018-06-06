package org.web.acl.dao;

import java.util.List;
import org.web.acl.query.QueryAclResourceAccountMapping;
import org.web.acl.domain.AclResourceAccountMappingDO;

public interface AclResourceAccountMappingDAO {

	public AclResourceAccountMappingDO selectAclResourceAccountMappingByAclResourceAccountMappingId(Long aclResourceAccountMappingId);

	public List< AclResourceAccountMappingDO > selectAclResourceAccountMappingList(QueryAclResourceAccountMapping queryAclResourceAccountMapping);

	public Integer countAclResourceAccountMappingList(QueryAclResourceAccountMapping queryAclResourceAccountMapping);

	public int insertAclResourceAccountMapping(AclResourceAccountMappingDO aclResourceAccountMappingDO);

	public int updateAclResourceAccountMappingByAclResourceAccountMappingId(AclResourceAccountMappingDO aclResourceAccountMappingDO);
	
	public List<String> selectDistinctList(QueryAclResourceAccountMapping queryAclResourceAccountMapping);

}

