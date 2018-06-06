package org.web.acl.dao;

import java.util.List;
import org.web.acl.query.QueryAclResource;
import org.web.acl.domain.AclResourceDO;

public interface AclResourceDAO {

    public AclResourceDO selectAclResourceByAclResourceId(Long aclResourceId);

    public List< AclResourceDO > selectAclResourceList(QueryAclResource queryAclResource);

    public Integer countAclResourceList(QueryAclResource queryAclResource);

    public int insertAclResource(AclResourceDO aclResourceDO);

    public int updateAclResourceByAclResourceId(AclResourceDO aclResourceDO);

    public List<String> selectDistinctList(QueryAclResource queryAclResource);

}
