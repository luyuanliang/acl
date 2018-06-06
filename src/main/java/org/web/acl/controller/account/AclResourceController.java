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
import org.web.acl.domain.AclResourceDO;
import org.web.acl.helper.ACLConstant;
import org.web.acl.helper.AclHelper;
import org.web.acl.query.QueryAclResource;
import org.web.acl.service.AclResourceService;
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

/**
 * Created by luyl on 17-9-11.
 */

@Scope("prototype")
@Controller
@RequestMapping("/account/aclResource/")
@SuppressWarnings({"rawtypes", "unchecked"})
public class AclResourceController implements ACLConstant {


    private static final Logger logger = LoggerFactory.getLogger(AclResourceController.class);

    @Resource
    private AclResourceService aclResourceService;

    @RequestMapping(value = "aclResource", method = {RequestMethod.GET, RequestMethod.POST})
    public String bossMember(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "account/aclResource";
    }

    @RequestMapping(value = "insertAclResource", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String insertAclResource(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ViewResult<AclResourceDO> result = new ViewResult();
        Gson gson = new Gson();
        try {
            AclResourceDO aclResourceDO = AclHelper.buildBeanByRequest(request, AclResourceDO.class);
            aclResourceService.insertAclResource(aclResourceDO);
            result.setMsg("添加成功");
            result.setResult(true);
            result.setType("warning");
        } catch (Exception e) {
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }

        return gson.toJson(result);
    }


    @RequestMapping(value = "selectAclResourceList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String selectAclResourceList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ViewResult<List<AclResourceDO>> result = new ViewResult();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            QueryAclResource queryAclResource = AclHelper.buildBeanByRequest(request, QueryAclResource.class);
            List<AclResourceDO> list = aclResourceService.selectAclResourceList(queryAclResource);
            result.setRows(list);
            result.setMsg("查询成功");
            result.setResult(true);
            result.setType("info");
            int count = aclResourceService.countAclResourceList(queryAclResource);
            result.setTotal(count);
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
        return gson.toJson(result);
    }

    @RequestMapping(value = "deleteAclResource", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String deleteAclResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ViewResult<Integer> result = new ViewResult(true);
        Gson gson = new Gson();
        try {
            List<AclResourceDO> aclResourceList = (List<AclResourceDO>) AclHelper.getRequestAttribute(request, RESOURCE_LIST_PAGE, "资源ID有误！");
            for (AclResourceDO aclResourceDO : aclResourceList) {
                AclResourceDO update = new AclResourceDO();
                update.setAclResourceId(aclResourceDO.getAclResourceId());
                update.setIsDelete(EnumHelper.DELETE.Y.name());
                aclResourceService.updateAclResourceByAclResourceId(update);
            }
            result.setTotal(aclResourceList.size());
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
        QueryAclResource queryAclResource = new QueryAclResource();
        queryAclResource.setDistinct(distinct);
        queryAclResource.setBusinessLine(HttpRequestHelper.getValueByCookie(request, ACLConstant.ACL_COOKIE_BUSINESSLINE));
        List<String> list = aclResourceService.selectDistinctList(queryAclResource);
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        if (TRUE_STRING.equals(flag)) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("text", "全部");
            listMap.add(map);
        }
        for (String value : list) {
            Map<String, String> map = new HashMap<String, String>();
            if(StringUtils.isNotBlank(value)){
                map.put("text", value);
                listMap.add(map);
            }
        }
        if (listMap.size() == 0) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("text", "暂无");
            listMap.add(map);
        }
        return new Gson().toJson(listMap);
    }

    @RequestMapping(value = "updateAclResource", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String updateAclResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ViewResult<AclResourceDO> result = new ViewResult();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            AclResourceDO aclResourceDO = AclHelper.buildBeanByRequest(request, AclResourceDO.class);
            int num = aclResourceService.updateAclResourceByAclResourceId(aclResourceDO);
            logger.info("num is " + num);
            result.setData(aclResourceDO);
            result.setMsg("修改成功");
            result.setResult(true);
            result.setType("info");
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
        return gson.toJson(result);
    }

    @RequestMapping(value = "selectAclResourceDetail", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String selectAclResourceDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ViewResult<AclResourceDO> result = new ViewResult();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            AclResourceDO aclResourceDO = (AclResourceDO) AclHelper.getRequestAttribute(request, RESOURCE_ID_PAGE, "资源ID有误！");
            result.setData(aclResourceDO);
            result.setMsg("查询成功");
            result.setResult(true);
            result.setType("info");
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
        return gson.toJson(result);
    }
}
