package org.web.acl.controller;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web.acl.domain.AclAccountRoleMappingDO;
import org.web.acl.domain.AclRoleDO;
import org.web.acl.domain.SessionAccountDO;
import org.web.acl.helper.ACLConstant;
import org.web.acl.helper.SessionAccountHelper;
import org.web.acl.query.QueryAclAccountRoleMapping;
import org.web.acl.query.QueryAclRole;
import org.web.acl.service.AclAccountRoleMappingService;
import org.web.acl.service.AclRoleService;
import org.web.domain.ViewResult;
import org.web.exception.ResultMessageEnum;
import org.web.helper.HttpRequestHelper;
import org.web.helper.ServiceExceptionHelper;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luyl on 17-9-6.
 */
@Scope("prototype")
@Controller
@RequestMapping("businessLine/")
public class BusinessLineController implements ACLConstant {

    private static final Logger logger = Logger.getLogger(BusinessLineController.class);

    @Resource
    private AclRoleService aclRoleService;

    @Resource
    private AclAccountRoleMappingService aclAccountRoleMappingService;

    @RequestMapping(value = "selectBusinessLineList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String selectBusinessLineList(HttpServletRequest request, HttpServletResponse response) {

        ViewResult<List<Map<String, String>>> viewResult = new ViewResult<>(true);
        try {
            Gson gson = new Gson();
            List<Map<String, String>> listMap = new ArrayList<>();
            viewResult.setData(listMap, true);
            SessionAccountDO sessionAccountDO = SessionAccountHelper.getSessionAccountFromRequest(request);
            QueryAclRole queryAclRole = new QueryAclRole();
            queryAclRole.setRoleName(ACL_GROUP_ADMIN);
            queryAclRole.setRoleGroup(ACL_ROLENAME_ADMIN);
            queryAclRole.setDistinct("aclRoleId");
            List<AclRoleDO> distinctAclRoleList = aclRoleService.selectAclRoleList(queryAclRole);
            if (distinctAclRoleList.size() == 0) {
                throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.RECORD_NOT_EXIST, "没有可用产品线，请先创建产品线!");
            }

            List<Long> aclRoleIdList = new ArrayList<>();
            for (AclRoleDO aclRoleDO : distinctAclRoleList) {
                aclRoleIdList.add(aclRoleDO.getAclRoleId());
            }

            QueryAclAccountRoleMapping queryAclAccountRoleMapping = new QueryAclAccountRoleMapping();
            queryAclAccountRoleMapping.setAclRoleIdList(aclRoleIdList);
            queryAclAccountRoleMapping.setAccountNum(SessionAccountHelper.getSessionAccountFromRequest(request).getAccountNum());
            List<AclAccountRoleMappingDO> aclAccountRoleMappingDOList = aclAccountRoleMappingService.selectAclAccountRoleMappingList(queryAclAccountRoleMapping);
            aclRoleIdList = new ArrayList<Long>();
            if (aclAccountRoleMappingDOList.size() == 0) {
                throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.RECORD_NOT_EXIST, "没有可用产品线，请先创建产品线!!");
            }
            for (AclAccountRoleMappingDO aclAccountRoleMappingDO : aclAccountRoleMappingDOList) {
                aclRoleIdList.add(aclAccountRoleMappingDO.getAclRoleId());
            }

            queryAclRole = new QueryAclRole();
            queryAclRole.setAclRoleIdList(aclRoleIdList);
            List<AclRoleDO> list = aclRoleService.selectAclRoleList(queryAclRole);

            int first = 0;
            for (AclRoleDO aclRoleDO : list) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("text", aclRoleDO.getBusinessLine());
                if (first == 0) {
                    map.put("selected", "true");
                    if (HttpRequestHelper.getValueByParamOrCookie(request, ACL_COOKIE_BUSINESSLINE) == null) {
                        Cookie cookie = new Cookie(ACL_COOKIE_BUSINESSLINE, aclRoleDO.getBusinessLine());
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                first++;
                listMap.add(map);
            }
            return new Gson().toJson(listMap);
        } catch (Exception e) {
            return new Gson().toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
    }

    @RequestMapping(value = "businessLine", method = {RequestMethod.GET, RequestMethod.POST})
    public String businessLine(HttpServletRequest request, HttpServletResponse response) {
        return "businessLine/businessLine";
    }

    @RequestMapping(value = "createBusinessLine", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String createBusinessLine(HttpServletRequest request, HttpServletResponse response) {
        ViewResult<AclRoleDO> viewResult = new ViewResult<>(true);
        try {
            String businessLine = request.getParameter("businessLineName");
            AclRoleDO aclRoleDO = new AclRoleDO();
            aclRoleDO.setRoleGroup(ACL_GROUP_ADMIN);
            aclRoleDO.setRoleName(ACL_ROLENAME_ADMIN);
            aclRoleDO.setBusinessLine(businessLine);
            aclRoleDO.setInputer(SessionAccountHelper.getSessionAccountVaue(request));
            aclRoleService.insertAclRole(aclRoleDO);

            AclAccountRoleMappingDO aclAccountRoleMappingDO = new AclAccountRoleMappingDO();
            SessionAccountDO sessionAccountDO = SessionAccountHelper.getSessionAccountFromRequest(request);
            aclAccountRoleMappingDO.setAclRoleId(aclRoleDO.getAclRoleId());

            aclAccountRoleMappingDO.setAccountNum(sessionAccountDO.getAccountNum());
            aclAccountRoleMappingDO.setBusinessLine(businessLine);
            aclAccountRoleMappingDO.setAclAccountId(Long.valueOf(sessionAccountDO.getAccountNum()));
            aclAccountRoleMappingDO.setInputer(SessionAccountHelper.getSessionAccountVaue(request));
            aclAccountRoleMappingService.insertAclAccountRoleMapping(aclAccountRoleMappingDO);
            //viewResult.setData(aclRoleDO);
            return new Gson().toJson(viewResult);
        } catch (Exception e) {
            logger.error(ServiceExceptionHelper.getExceptionInfo(e));
            return new Gson().toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
    }

    @RequestMapping(value = "changeBusinessLine", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String changeBusinessLine(HttpServletRequest request, HttpServletResponse response) {
        ViewResult<AclRoleDO> viewResult = new ViewResult<>(true);
        try {
            String businessLine = request.getParameter("businessLineName");
            QueryAclRole queryAclRole = new QueryAclRole();
            queryAclRole.setRoleGroup(ACL_GROUP_ADMIN);
            queryAclRole.setRoleName(ACL_ROLENAME_ADMIN);
            queryAclRole.setBusinessLine(businessLine);

            AclRoleDO aclRoleDO = aclRoleService.selectOneAclRole(queryAclRole);
            if (aclRoleDO == null) {
                throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.RECORD_NOT_EXIST, "产品线不存在！！！");
            }

            QueryAclAccountRoleMapping queryAclAccountRoleMapping = new QueryAclAccountRoleMapping();
            queryAclAccountRoleMapping.setAclRoleId(aclRoleDO.getAclRoleId());
            queryAclAccountRoleMapping.setAccountNum(SessionAccountHelper.getSessionAccountFromRequest(request).getAccountNum());
            int count = aclAccountRoleMappingService.countAclAccountRoleMappingList(queryAclAccountRoleMapping);
            if (count == 0) {
                throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.NO_PRIVILEGE, "沒有产品线管理员权限！！！");
            }
            Cookie cookie = new Cookie(ACL_COOKIE_BUSINESSLINE, businessLine);
            cookie.setPath("/");
            response.addCookie(cookie);
            return new Gson().toJson(viewResult);
        } catch (Exception e) {
            logger.error(ServiceExceptionHelper.getExceptionInfo(e));
            return new Gson().toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
    }

}