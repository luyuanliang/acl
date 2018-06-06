package org.web.acl.query;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.web.domain.QueryBase;

@Setter
@Getter
public class QuerySessionAccount extends QueryBase {

	
	private Long sessionAcountId;
				
	private List < Long > sessionAcountIdList;
		
	private String accountName;
					
	private String accountNum;
					
	private String password;
					
	private String isDelete;
				}

