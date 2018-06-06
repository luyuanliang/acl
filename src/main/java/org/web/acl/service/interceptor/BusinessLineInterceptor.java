package org.web.acl.service.interceptor;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.web.acl.domain.AclRoleDO;
import org.web.acl.helper.ACLConstant;
import org.web.acl.helper.SessionAccountHelper;
import org.web.acl.query.QueryAclAccountRoleMapping;
import org.web.acl.query.QueryAclRole;
import org.web.acl.service.AclAccountRoleMappingService;
import org.web.acl.service.AclRoleService;
import org.web.helper.HttpRequestHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * Created by luyl on 17-9-7.
 */
@EqualsAndHashCode
public class BusinessLineInterceptor implements HandlerInterceptor, ACLConstant {


    @Resource
    private AclRoleService aclRoleService;

    @Resource
    private AclAccountRoleMappingService aclAccountRoleMappingService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String businessLine = HttpRequestHelper.getValueByCookie(httpServletRequest, ACL_COOKIE_BUSINESSLINE);
        String uri = httpServletRequest.getRequestURI();
        if (uri.contains("/account/businessLine/selectBusinessLineList")) {
            return true;
        } else if (StringUtils.isEmpty(businessLine)) {
            String msg = URLEncoder.encode("请先选择业务线！", "utf-8");
            httpServletResponse.sendRedirect("/acl/error/error.html?msg=" + msg);
            return false;
        }
        // 校验产品线与登陆账户的权限是否匹配
        String accountNum = SessionAccountHelper.getSessionAccountFromRequest(httpServletRequest).getAccountNum();
        QueryAclRole queryAclRole = new QueryAclRole();
        queryAclRole.setBusinessLine(businessLine);
        queryAclRole.setRoleGroup(ACL_GROUP_ADMIN);
        queryAclRole.setRoleName(ACL_ROLENAME_ADMIN);
        AclRoleDO aclRoleDO = aclRoleService.selectOneAclRole(queryAclRole);

        QueryAclAccountRoleMapping queryAclAccountRoleMapping = new QueryAclAccountRoleMapping();
        queryAclAccountRoleMapping.setAccountNum(accountNum);
        queryAclAccountRoleMapping.setAclRoleId(aclRoleDO.getAclRoleId());
        int count = aclAccountRoleMappingService.countAclAccountRoleMappingList(queryAclAccountRoleMapping);
        if (count < 1) {
            String msg = URLEncoder.encode("你没有该业务线的访问权限！", "utf-8");
            httpServletResponse.sendRedirect("/acl/error/error.html?msg=" + msg);
            return false;
        }

        httpServletRequest.setAttribute("businessLine", businessLine);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
