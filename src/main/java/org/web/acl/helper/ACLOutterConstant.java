package org.web.acl.helper;

/**
 * 该接口，只为外部定制化需求服务。
 * */
public interface ACLOutterConstant {

    /**
     * 数据库固定属性
     * */
    // 数据库ACL_RESOURCE表RESOURCE_NAME的默认字段，表明是供外部使用描述消息的。
    public String FIXED_ROLE ="FIXED_ROLE";


    /***
     *  ACL系统的业务线
     */
    public static final String BUSINESS_LINE="BUSINESS_LINE";

    /***
     *  外部系统所属的业务线，取自AclResource表下RESOURCE_KEY的住处。resourceValue抽象定义为角色。
     */
    public static final String OUTTER_BUSINESS_LINE="OUTTER_BUSINESS_LINE";

}
