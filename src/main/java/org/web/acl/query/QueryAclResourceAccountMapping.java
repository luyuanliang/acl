package org.web.acl.query;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.web.domain.QueryBase;

@Setter
@Getter
public class QueryAclResourceAccountMapping extends QueryBase {

	
	private Long aclResourceAccountMappingId;
				
	private List < Long > aclResourceAccountMappingIdList;
		
	private String businessLine;
					
	private Long aclResourceId;
				
	private List < Long > aclResourceIdList;
		
	private Long aclAccountId;
				
	private List < Long > aclAccountIdList;
		
	private String accountNum;
											
	private Date moreThanCreateTime;
	private Date lessThanCreateTime;
	private Date equalAndMoreThanCreateTime;
	private Date equalAndLessThanCreateTime;
					
	private Date moreThanUpdateTime;
	private Date lessThanUpdateTime;
	private Date equalAndMoreThanUpdateTime;
	private Date equalAndLessThanUpdateTime;
			
	private String isDelete;
				}

