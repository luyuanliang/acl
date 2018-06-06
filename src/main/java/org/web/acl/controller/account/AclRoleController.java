package org.web.acl.controller.account;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web.acl.domain.*;
import org.web.acl.helper.ACLConstant;
import org.web.acl.helper.AclHelper;
import org.web.acl.helper.SessionAccountHelper;
import org.web.acl.query.QueryAclRole;
import org.web.acl.service.AclAccountRoleMappingService;
import org.web.acl.service.AclResourceRoleMappingService;
import org.web.acl.service.AclRoleService;
import org.web.domain.ViewResult;
import org.web.helper.EnumHelper;
import org.web.helper.HttpRequestHelper;
import org.web.helper.ServiceExceptionHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope("prototype")
@Controller
@RequestMapping("account/aclRole/")
@SuppressWarnings({"rawtypes", "unchecked"})
public class AclRoleController implements ACLConstant {

    private static final Logger logger = LoggerFactory.getLogger(AclRoleController.class);

    @Resource
    private AclRoleService aclRoleService;

    @Resource
    private AclResourceRoleMappingService aclResourceRoleMappingService;

    //@Resource
    //private AclResourceRoleMappingService aclResourceRoleMappingService;

    @Resource
    private AclAccountRoleMappingService aclAccountRoleMappingService;

    @RequestMapping(value = "insertAclRole", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String insertAclRole(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ViewResult<AclRoleDO> result = new ViewResult();
        Gson gson = new Gson();
        try {
            AclRoleDO aclRoleDO = AclHelper.buildBeanByRequest(request, AclRoleDO.class);
            aclRoleService.insertAclRole(aclRoleDO);
            result.setMsg("添加成功");
            result.setResult(true);
            result.setType("warning");
        } catch (Exception e) {
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }

        return gson.toJson(result);
    }

    @RequestMapping(value = "deleteAclRole", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String deleteAclRole(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ViewResult<Integer> result = new ViewResult(true);
        Gson gson = new Gson();
        try {
            List<AclRoleDO> aclRoleList = (List<AclRoleDO>) AclHelper.getRequestAttribute(request, ROLE_LIST_PAGE, "");
            for (AclRoleDO aclRoleDO : aclRoleList) {
                AclRoleDO update = new AclRoleDO();
                update.setAclRoleId(aclRoleDO.getAclRoleId());
                update.setIsDelete(EnumHelper.DELETE.Y.name());
                aclRoleService.updateAclRoleByAclRoleId(update);
            }
            result.setTotal(aclRoleList.size());
        } catch (Exception e) {
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }

        return gson.toJson(result);
    }

    @RequestMapping(value = "updateAclRole", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String updateAclRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ViewResult<AclRoleDO> result = new ViewResult();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            AclRoleDO aclRoleDO = AclHelper.buildBeanByRequest(request, AclRoleDO.class);
            if (ACL_GROUP_ADMIN.equals(aclRoleDO.getRoleGroup())) {
                // admin 分组默认不允许修改角色名词与组名称。
                aclRoleDO.setRoleGroup(null);
                aclRoleDO.setRoleName(null);
            }
            int num = aclRoleService.updateAclRoleByAclRoleId(aclRoleDO);
            logger.info("num is " + num);
            result.setData(aclRoleDO);
            result.setMsg("修改成功");
            result.setResult(true);
            result.setType("info");
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
        return gson.toJson(result);
    }

    @RequestMapping(value = "selectAclRoleDetail", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String selectAclRoleDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ViewResult<AclRoleDO> result = new ViewResult();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            String aclRoleId = request.getParameter("aclRoleId");
            AclRoleDO aclRoleDO = aclRoleService.selectAclRoleByAclRoleId(Long.valueOf(aclRoleId));
            result.setData(aclRoleDO);
            result.setMsg("查询成功");
            result.setResult(true);
            result.setType("info");
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
        return gson.toJson(result);
    }


    @RequestMapping(value = "selectAclRoleList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String selectAclRoleList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ViewResult<List<AclRoleDO>> result = new ViewResult();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            QueryAclRole queryAclRole = AclHelper.buildBeanByRequest(request, QueryAclRole.class);
            List<AclRoleDO> list = aclRoleService.selectAclRoleList(queryAclRole);
            result.setRows(list);
            result.setMsg("查询成功");
            result.setResult(true);
            result.setType("info");
            int count = aclRoleService.countAclRoleList(queryAclRole);
            result.setTotal(count);
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
        return gson.toJson(result);
    }

    @RequestMapping(value = "aclRole", method = {RequestMethod.GET, RequestMethod.POST})
    public String aclRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "account/aclRole";
    }

    @RequestMapping(value = "bindRoleOpen", method = {RequestMethod.GET, RequestMethod.POST})
    public String bindRoleOpen(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String type = request.getParameter("type");
        request.setAttribute("type", type);
        request.setAttribute("aclResourceList", request.getParameter("ids"));
        QueryAclRole queryAclRole = new QueryAclRole();
        queryAclRole.setBusinessLine(HttpRequestHelper.getValueByCookie(request, ACLConstant.ACL_COOKIE_BUSINESSLINE));
        List<AclRoleDO> roleList = aclRoleService.selectAclRoleList(queryAclRole);
        request.setAttribute("roleList", roleList);
        return "account/bindRoleOpen";
    }

    @RequestMapping(value = "bindRole", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String bindRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ViewResult<Integer> result = new ViewResult(true);
        try {
            AclRoleDO aclRoleDO = (AclRoleDO) AclHelper.getRequestAttribute(request, ROLEID_ID_PAGE, "角色信息有误！");
            String type = request.getParameter("type");
            String inputer = SessionAccountHelper.getSessionAccountVaue(request);
            String businessLine = HttpRequestHelper.getValueByCookie(request, ACL_COOKIE_BUSINESSLINE);
            if ("aclAccount".equals(type)) {
                List<AclAccountDO> list = (List<AclAccountDO>) AclHelper.getRequestAttribute(request, ACCOUNT_LIST_PAGE, "角色信息有误！");
                for (AclAccountDO aclAccountDO : list) {
                    AclAccountRoleMappingDO aclAccountRoleMappingDO = new AclAccountRoleMappingDO();
                    aclAccountRoleMappingDO.setAclRoleId(aclRoleDO.getAclRoleId());
                    aclAccountRoleMappingDO.setBusinessLine(businessLine);
                    aclAccountRoleMappingDO.setAclAccountId(aclAccountDO.getAclAccountId());
                    aclAccountRoleMappingDO.setInputer(inputer);
                    aclAccountRoleMappingService.insertAclAccountRoleMapping(aclAccountRoleMappingDO);
                }
                result.setData(list.size());
            } else {
                List<AclResourceDO> list = (List<AclResourceDO>) AclHelper.getRequestAttribute(request, RESOURCE_LIST_PAGE, "资源ID有误！");
                for (AclResourceDO aclResourceDO : list) {
                    AclResourceRoleMappingDO aclResourceRoleMappingDO = new AclResourceRoleMappingDO();
                    aclResourceRoleMappingDO.setAclRoleId(aclRoleDO.getAclRoleId());
                    aclResourceRoleMappingDO.setAclResourceId(aclResourceDO.getAclResourceId());
                    aclResourceRoleMappingDO.setBusinessLine(businessLine);
                    aclResourceRoleMappingDO.setInputer(inputer);
                    aclResourceRoleMappingService.insertAclResourceRoleMapping(aclResourceRoleMappingDO);
                }
                result.setData(list.size());
            }
        } catch (Exception e) {
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
        return gson.toJson(result);

    }


    @RequestMapping(value = "selectDistinctList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String selectDistinctList(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String flag = request.getParameter("flag");
        String distinct = request.getParameter("distinct");
        String roleGroup = request.getParameter("roleGroup");
        QueryAclRole queryAclRole = new QueryAclRole();
        if (StringUtils.isNotBlank(roleGroup)) {
            queryAclRole.setRoleGroup(roleGroup);
        }
        queryAclRole.setDistinct(distinct);
        List<String> list = aclRoleService.selectDistinctList(queryAclRole);
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        if (TRUE_STRING.equals(flag)) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("text", "全部");
            listMap.add(map);
        }
        for (String value : list) {
            Map<String, String> map2 = new HashMap<String, String>();
            map2.put("text", value);
            listMap.add(map2);
        }
        return new Gson().toJson(listMap);
    }

}


