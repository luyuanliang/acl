package org.web.acl.helper;

/**
 * Created by luyl on 17-9-6.
 */
public interface ACLConstant {

    public static final String ACL_COOKIE_ACCOUNT = "ACL_COOKIE_ACCOUNT";

    public static final String ACL_COOKIE_BUSINESSLINE = "ACL_COOKIE_BUSINESSLINE";

    public static final String ACL_GROUP_ADMIN = "admin";

    public static final String ACL_ROLENAME_ADMIN = "admin";

    public static final String TRUE_STRING = "true";

    public static final String ACL_RESOURCE_MENU = "MENU";

    public static final String ACL_MENU_SEPERATE = ",";

    public static final String ERROR_MESSAGE = "errorMessage";

    /***
     * 页面参数常量。用于定义页面传递到后台的变量名称。
     */
    public static final String VIEW_SEPERATE = ",";
    public static final String ID = "Id";
    public static final String LIST = "List";
    public static final String RESOURCE_ID_PAGE = "aclResource" + ID;
    public static final String ROLEID_ID_PAGE = "aclRole" + ID;
    public static final String ROLE_LIST_PAGE = "aclRole" + LIST;
    public static final String ACCOUNT_ID_PAGE = "aclAccount" + ID;
    public static final String RESOURCE_ROLE_ID_PAGE = "aclResourceRole" + ID;
    public static final String ACCOUNT_ROLE_ID_PAGE = "aclAccountRole" + ID;
    public static final String RESOURCE_ACCOUNT_ID_PAGE = "aclResourceAccount" + ID;
    public static final String RESOURCE_LIST_PAGE = "aclResource" + LIST;
    public static final String ROLELIST_LIST_PAGE = "aclRole" + LIST;
    public static final String ACCOUNT_LIST_PAGE = "aclAccount" + LIST;
    public static final String RESOURCE_ROLE_LIST_PAGE = "aclResourceRoleMapping" + LIST;
    public static final String ACCOUNT_ROLE_LIST_PAGE = "aclAccountRoleMapping" + LIST;
    public static final String RESOURCE_ACCOUNT_LIST_PAGE = "aclResourceAccountMapping" + LIST;

}

