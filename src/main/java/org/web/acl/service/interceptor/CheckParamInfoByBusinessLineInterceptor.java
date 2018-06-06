package org.web.acl.service.interceptor;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.web.acl.domain.*;
import org.web.acl.helper.ACLConstant;
import org.web.acl.query.*;
import org.web.acl.service.*;
import org.web.helper.HttpRequestHelper;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yufan on 2017/11/26.
 *
 * @Description: 校验role，resource，mapping是否与当前产品线匹配。
 */
public class CheckParamInfoByBusinessLineInterceptor implements HandlerInterceptor, ACLConstant {


    @Resource
    private AclResourceAccountMappingService aclResourceAccountMappingService;

    @Resource
    private AclAccountRoleMappingService aclAccountRoleMappingService;

    @Resource
    private AclResourceService aclResourceService;

    @Resource
    private AclRoleService aclRoleService;

    @Resource
    private AclAccountService aclAccountService;

    @Resource
    private AclResourceRoleMappingService aclResourceRoleMappingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String businessLine = HttpRequestHelper.getValueByCookie(request, ACL_COOKIE_BUSINESSLINE);
        RequestDispatcher rd = request.getRequestDispatcher("/acl/errorMessage");
        boolean flag = false;
        if (StringUtils.isNotBlank(request.getParameter(ROLEID_ID_PAGE))) {
            Long aclRoleId = Long.valueOf(request.getParameter(ROLEID_ID_PAGE));
            AclRoleDO aclRoleDO = aclRoleService.selectAclRoleByAclRoleId(Long.valueOf(aclRoleId));
            request.setAttribute(ROLEID_ID_PAGE, aclRoleDO);
            if (aclRoleDO == null) {
                request.setAttribute(ERROR_MESSAGE, "角色信息没有找到");
            } else if (!businessLine.equalsIgnoreCase(aclRoleDO.getBusinessLine())) {
                request.setAttribute(ERROR_MESSAGE, "角色与当前产品线不匹配");
            }
        }

        if (StringUtils.isNotBlank(request.getParameter(ROLE_LIST_PAGE))) {
            List<Long> list = transStr2Long(request.getParameter(ROLE_LIST_PAGE));
            QueryAclRole queryAclRole = new QueryAclRole();
            queryAclRole.setAclRoleIdList(list);
            List<AclRoleDO> aclRoleList = aclRoleService.selectAclRoleList(queryAclRole);
            request.setAttribute(ROLE_LIST_PAGE, aclRoleList);
            for (AclRoleDO aclRoleDO : aclRoleList) {
                if (!businessLine.equalsIgnoreCase(aclRoleDO.getBusinessLine())) {
                    request.setAttribute(ERROR_MESSAGE, "有角色与当前产品线不匹配,资源ID ：" + aclRoleDO.getAclRoleId());
                    break;
                }
            }
        }


        if (StringUtils.isNotBlank(request.getParameter(RESOURCE_ID_PAGE))) {
            Long aclResourceId = Long.valueOf(request.getParameter(RESOURCE_ID_PAGE));
            AclResourceDO aclResourceDO = aclResourceService.selectAclResourceByAclResourceId(Long.valueOf(aclResourceId));
            request.setAttribute(RESOURCE_ID_PAGE, aclResourceDO);
            if (aclResourceDO == null) {
                request.setAttribute(ERROR_MESSAGE, "资源信息没有找到");
            } else if (!businessLine.equalsIgnoreCase(aclResourceDO.getBusinessLine())) {
                request.setAttribute(ERROR_MESSAGE, "资源与当前产品线不匹配");
            }
        }
        if (StringUtils.isNotBlank(request.getParameter(RESOURCE_LIST_PAGE))) {
            List<Long> list = transStr2Long(request.getParameter(RESOURCE_LIST_PAGE));
            QueryAclResource queryAclResource = new QueryAclResource();
            queryAclResource.setAclResourceIdList(list);
            List<AclResourceDO> aclResourceDOList = aclResourceService.selectAclResourceList(queryAclResource);
            request.setAttribute(RESOURCE_LIST_PAGE, aclResourceDOList);
            for (AclResourceDO aclResourceDO : aclResourceDOList) {
                if (!businessLine.equalsIgnoreCase(aclResourceDO.getBusinessLine())) {
                    request.setAttribute(ERROR_MESSAGE, "有资源与当前产品线不匹配,资源ID ：" + aclResourceDO.getAclResourceId());
                    break;
                }
            }
        }

        if (StringUtils.isNotBlank(request.getParameter(RESOURCE_ROLE_LIST_PAGE))) {
            List<Long> list = transStr2Long(request.getParameter(RESOURCE_ROLE_LIST_PAGE));
            QueryAclResourceRoleMapping queryAclResourceRoleMapping = new QueryAclResourceRoleMapping();
            queryAclResourceRoleMapping.setAclResourceRoleMappingIdList(list);
            List<AclResourceRoleMappingDO> aclResourceRoleMappingList = aclResourceRoleMappingService.selectAclResourceRoleMappingList(queryAclResourceRoleMapping);
            request.setAttribute(RESOURCE_ROLE_LIST_PAGE, aclResourceRoleMappingList);
            for (AclResourceRoleMappingDO aclResourceRoleMappingDO : aclResourceRoleMappingList) {
                if (!businessLine.equalsIgnoreCase(aclResourceRoleMappingDO.getBusinessLine())) {
                    request.setAttribute(ERROR_MESSAGE, "有映射关系与当前产品线不匹配,aclResourceRoleMappingId ：" + aclResourceRoleMappingDO.getAclResourceId());
                    break;
                }
            }
        }

        if (StringUtils.isNotBlank(request.getParameter(RESOURCE_ACCOUNT_LIST_PAGE))) {
            List<Long> list = transStr2Long(request.getParameter(RESOURCE_ACCOUNT_LIST_PAGE));
            QueryAclResourceAccountMapping queryAclResourceAccountMapping = new QueryAclResourceAccountMapping();
            queryAclResourceAccountMapping.setAclResourceAccountMappingIdList(list);
            List<AclResourceAccountMappingDO> aclResourceAccountMappingList = aclResourceAccountMappingService.selectAclResourceAccountMappingList(queryAclResourceAccountMapping);
            request.setAttribute(RESOURCE_ACCOUNT_LIST_PAGE, aclResourceAccountMappingList);
            for (AclResourceAccountMappingDO aclResourceAccountMapping : aclResourceAccountMappingList) {
                if (!businessLine.equalsIgnoreCase(aclResourceAccountMapping.getBusinessLine())) {
                    request.setAttribute(ERROR_MESSAGE, "有映射关系与当前产品线不匹配,aclResourceRoleMappingId ：" + aclResourceAccountMapping.getAclResourceAccountMappingId());
                    break;
                }
            }
        }

        if (StringUtils.isNotBlank(request.getParameter(ACCOUNT_ROLE_LIST_PAGE))) {
            List<Long> list = transStr2Long(request.getParameter(ACCOUNT_ROLE_LIST_PAGE));
            QueryAclAccountRoleMapping queryAclAccountRoleMapping = new QueryAclAccountRoleMapping();
            queryAclAccountRoleMapping.setAclAccountRoleMappingIdList(list);
            List<AclAccountRoleMappingDO> aclAccountRoleMappingDOList = aclAccountRoleMappingService.selectAclAccountRoleMappingList(queryAclAccountRoleMapping);
            request.setAttribute(ACCOUNT_ROLE_LIST_PAGE, aclAccountRoleMappingDOList);
            for (AclAccountRoleMappingDO aclAccountRoleMappingDO : aclAccountRoleMappingDOList) {
                if (!businessLine.equalsIgnoreCase(aclAccountRoleMappingDO.getBusinessLine())) {
                    request.setAttribute(ERROR_MESSAGE, "有映射关系与当前产品线不匹配,aclAccountRoleMappingId ：" + aclAccountRoleMappingDO.getAclAccountRoleMappingId());
                    break;
                }
            }
        }


        if (StringUtils.isNotBlank(request.getParameter(ACCOUNT_ID_PAGE))) {
            Long aclAccountId = Long.valueOf(request.getParameter(ACCOUNT_ID_PAGE));
            AclAccountDO aclAccountDO = aclAccountService.selectAclAccountByAclAccountId(aclAccountId);
            request.setAttribute(ACCOUNT_ID_PAGE, aclAccountDO);
            if (aclAccountDO == null) {
                request.setAttribute(ERROR_MESSAGE, "账户信息没有找到");
            } else if (!businessLine.equalsIgnoreCase(aclAccountDO.getBusinessLine())) {
                request.setAttribute(ERROR_MESSAGE, "账户ID与当前产品线不匹配");
            }
        }


        if (StringUtils.isNotBlank(request.getParameter(ACCOUNT_LIST_PAGE))) {
            List<Long> list = transStr2Long(request.getParameter(ACCOUNT_LIST_PAGE));
            QueryAclAccount queryAclAccount = new QueryAclAccount();
            queryAclAccount.setAclAccountIdList(list);
            List<AclAccountDO> aclAccountList = aclAccountService.selectAclAccountList(queryAclAccount);
            request.setAttribute(ACCOUNT_LIST_PAGE, aclAccountList);
            for (AclAccountDO aclAccountDO : aclAccountList) {
                if (!businessLine.equalsIgnoreCase(aclAccountDO.getBusinessLine())) {
                    request.setAttribute(ERROR_MESSAGE, "有用户与当前产品线不匹配,用户ID ：" + aclAccountDO.getAclAccountId());
                    break;
                }
            }
        }


        if (request.getAttribute(ERROR_MESSAGE) != null) {
            rd.forward(request, response);
            return false;
        } else {
            return true;
        }
    }

    private List<Long> transStr2Long(String value) {
        String array[] = value.split(VIEW_SEPERATE);
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            list.add(Long.valueOf(array[i].trim()));
        }
        return list;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
