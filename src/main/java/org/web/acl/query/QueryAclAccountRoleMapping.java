package org.web.acl.query;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.web.domain.QueryBase;

@Setter
@Getter
public class QueryAclAccountRoleMapping extends QueryBase {

	
	private Long aclAccountRoleMappingId;
				
	private List < Long > aclAccountRoleMappingIdList;
		
	private String businessLine;
					
	private Long aclAccountId;
				
	private List < Long > aclAccountIdList;
		
	private String accountNum;
				
	private List < String > accountNumList;
		
	private Long aclRoleId;
				
	private List < Long > aclRoleIdList;
														
	private String isDelete;
				}

