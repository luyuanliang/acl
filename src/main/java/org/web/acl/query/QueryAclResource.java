package org.web.acl.query;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.web.domain.QueryBase;

@Setter
@Getter
public class QueryAclResource extends QueryBase {


    private Long aclResourceId;

    private List < Long > aclResourceIdList;

    private String businessLine;

    private String resourceName;

    private String resourceKey;

    private String resourceValue;

    private String resourceLevel;

    private String extendsValue;


    private String moreThanExtendsValue;
    private String lessThanExtendsValue;
    private String equalAndMoreThanExtendsValue;
    private String equalAndLessThanExtendsValue;


    private String resourceStatus;

    private List < String > resourceStatusList;

    private String resourceType;

    private String firstLevel;

    private List < String > firstLevelList;

    private String secondLevel;

    private List < String > secondLevelList;

    private String thirdLevel;

    private List < String > thirdLevelList;

    private String fourthLevel;

    private List < String > fourthLevelList;

    private String fifthLevel;

    private List < String > fifthLevelList;

    private Date inputTime;

    private Date moreThanInputTime;
    private Date lessThanInputTime;
    private Date equalAndMoreThanInputTime;
    private Date equalAndLessThanInputTime;

    private Date updateTime;

    private Date moreThanUpdateTime;
    private Date lessThanUpdateTime;
    private Date equalAndMoreThanUpdateTime;
    private Date equalAndLessThanUpdateTime;

    private String isDelete;
}
