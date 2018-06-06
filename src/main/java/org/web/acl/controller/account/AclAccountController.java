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
import org.web.acl.domain.AclAccountDO;
import org.web.acl.domain.AclResourceAccountMappingDO;
import org.web.acl.domain.AclResourceDO;
import org.web.acl.helper.ACLConstant;
import org.web.acl.helper.AclHelper;
import org.web.acl.helper.SessionAccountHelper;
import org.web.acl.query.QueryAclAccount;
import org.web.acl.service.AclAccountService;
import org.web.acl.service.AclResourceAccountMappingService;
import org.web.domain.ViewResult;
import org.web.helper.EnumHelper;
import org.web.helper.HttpRequestHelper;
import org.web.helper.ListHelper;
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
@RequestMapping("/account/aclAccount/")
@SuppressWarnings({"rawtypes", "unchecked"})
public class AclAccountController implements ACLConstant {

    private static final Logger logger = LoggerFactory.getLogger(AclAccountController.class);

    @Resource
    private AclAccountService aclAccountService;

    @Resource
    private AclResourceAccountMappingService aclResourceAccountMappingService;

    @RequestMapping(value = "insertAclAccount", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String insertAclAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ViewResult<AclAccountDO> result = new ViewResult();
        Gson gson = new Gson();
        try {
            AclAccountDO bclAccountDO = AclHelper.buildBeanByRequest(request, AclAccountDO.class);
            aclAccountService.insertAclAccount(bclAccountDO);
            result.setMsg("添加成功");
            result.setResult(true);
            result.setType("warning");
        } catch (Exception e) {
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }

        return gson.toJson(result);
    }

    @RequestMapping(value = "deleteAclAccount", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String deleteAclAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ViewResult<Integer> result = new ViewResult(true);
        Gson gson = new Gson();
        try {
            List<AclAccountDO> aclAccountList = (List<AclAccountDO>) AclHelper.getRequestAttribute(request, ACCOUNT_LIST_PAGE, "");
            for (AclAccountDO aclAccountDO : aclAccountList) {
                AclAccountDO update = new AclAccountDO();
                update.setAclAccountId(aclAccountDO.getAclAccountId());
                update.setIsDelete(EnumHelper.DELETE.Y.name());
                aclAccountService.updateAclAccountByAclAccountId(update);
            }
            result.setTotal(aclAccountList.size());
        } catch (Exception e) {
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }

        return gson.toJson(result);
    }

    @RequestMapping(value = "updateAclAccount", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String updateAclAccount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ViewResult<AclAccountDO> result = new ViewResult();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            AclAccountDO aclAccountDO = AclHelper.buildBeanByRequest(request, AclAccountDO.class);
            int num = aclAccountService.updateAclAccountByAclAccountId(aclAccountDO);
            logger.info("num is " + num);
            result.setData(aclAccountDO);
            result.setMsg("修改成功");
            result.setResult(true);
            result.setType("info");
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
        return gson.toJson(result);
    }

    @RequestMapping(value = "selectAclAccountDetail", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String selectAclAccountDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ViewResult<AclAccountDO> result = new ViewResult();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            String bossMemberId = request.getParameter("aclAccountId");
            AclAccountDO bossMemberDO = aclAccountService.selectAclAccountByAclAccountId(Long.valueOf(bossMemberId));
            result.setData(bossMemberDO);
            result.setMsg("查询成功");
            result.setResult(true);
            result.setType("info");
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
        return gson.toJson(result);
    }

    @RequestMapping(value = "selectAclAccountList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String selectAclAccountList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ViewResult<List<AclAccountDO>> result = new ViewResult();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            QueryAclAccount queryAclAccount = AclHelper.buildBeanByRequest(request, QueryAclAccount.class);
            String accountIdStr = request.getParameter("accountNum");
            if (StringUtils.isNotBlank(accountIdStr) && accountIdStr.contains(",")) {
                List<String> list = ListHelper.transStr2List(accountIdStr, ",", String.class);
                queryAclAccount.setAccountNumList(list);
                queryAclAccount.setAccountNum(null);
            }
            int count = aclAccountService.countAclAccountList(queryAclAccount);
            List<AclAccountDO> list = aclAccountService.selectAclAccountList(queryAclAccount);
            result.setRows(list);
            result.setMsg("查询成功");
            result.setResult(true);
            result.setType("info");
            result.setTotal(count);
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
        return gson.toJson(result);
    }

    @RequestMapping(value = "selectDistinctList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String selectDistinctList(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String flag = request.getParameter("flag");
        String distinct = request.getParameter("distinct");
        QueryAclAccount queryAclAccount = new QueryAclAccount();
        queryAclAccount.setDistinct(distinct);
        queryAclAccount.setBusinessLine(HttpRequestHelper.getValueByCookie(request, ACL_COOKIE_BUSINESSLINE));
        List<String> list = aclAccountService.selectDistinctList(queryAclAccount);
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        if (TRUE_STRING.equals(flag)) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("text", "全部");
            listMap.add(map);
        }

        for (String value : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("text", value);
            listMap.add(map);
        }
        return new Gson().toJson(listMap);
    }

    @RequestMapping(value = "aclAccount", method = {RequestMethod.GET, RequestMethod.POST})
    public String bossMember(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "account/aclAccount";
    }

    @RequestMapping(value = "bindAccountOpen", method = {RequestMethod.GET, RequestMethod.POST})
    public String bindAccountOpen(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("aclResourceList", request.getParameter("ids"));
        QueryAclAccount queryAclAccount = new QueryAclAccount();
        queryAclAccount.setBusinessLine(HttpRequestHelper.getValueByCookie(request, ACLConstant.ACL_COOKIE_BUSINESSLINE));
        List<AclAccountDO> aclAccountList = aclAccountService.selectAclAccountList(queryAclAccount);
        request.setAttribute("aclAccountList", aclAccountList);
        return "account/bindAccountOpen";
    }


    @RequestMapping(value = "bindAccount", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String bindAccount(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ViewResult<Integer> result = new ViewResult(true);
        Gson gson = new Gson();
        try {
            AclAccountDO aclAccountDO = (AclAccountDO) AclHelper.getRequestAttribute(request, ACCOUNT_ID_PAGE, "资源ID有误！");
            List<AclResourceDO> list = (List<AclResourceDO>) AclHelper.getRequestAttribute(request, RESOURCE_LIST_PAGE, "资源ID有误！");
            for (AclResourceDO aclResourceDO : list) {
                AclResourceAccountMappingDO aclResourceAccountMappingDO = new AclResourceAccountMappingDO();
                aclResourceAccountMappingDO.setAclAccountId(aclAccountDO.getAclAccountId());
                aclResourceAccountMappingDO.setAclResourceId(aclResourceDO.getAclResourceId());
                aclResourceAccountMappingDO.setBusinessLine(HttpRequestHelper.getValueByParamOrCookie(request, ACL_COOKIE_BUSINESSLINE));
                aclResourceAccountMappingDO.setInputer(SessionAccountHelper.getSessionAccountVaue(request));
                aclResourceAccountMappingService.insertAclResourceAccountMapping(aclResourceAccountMappingDO);
            }
            result.setTotal(list.size());
        } catch (Exception e) {
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }

        return gson.toJson(result);
    }
}


