package org.web.acl.controller.account;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.web.acl.domain.*;
import org.web.acl.helper.ACLConstant;
import org.web.acl.helper.AclHelper;
import org.web.acl.helper.ExcelHelper;
import org.web.acl.helper.SessionAccountHelper;
import org.web.acl.service.*;
import org.web.helper.HttpRequestHelper;
import org.web.helper.ServiceExceptionHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by luyl on 17-9-14.
 */
@Scope("prototype")
@Controller
@RequestMapping("account/upload/")
@SuppressWarnings({"rawtypes", "unchecked"})
public class UploadController implements ACLConstant {

    private static final Logger logger = Logger.getLogger(UploadController.class);

    @Resource
    private AclAccountService aclAccountService;
    @Resource
    private AclRoleService aclRoleService;
    @Resource
    private AclResourceService aclResourceService;
    @Resource
    private AclResourceRoleMappingService aclResourceRoleMappingService;
    @Resource
    private AclResourceAccountMappingService aclResourceAccountMappingService;
    @Resource
    private AclAccountRoleMappingService aclAccountRoleMappingService;


    @RequestMapping(value = "uploadView", method = {RequestMethod.GET, RequestMethod.POST})
    public String aclRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "account/uploadView";
    }


    @RequestMapping(value = "uploadFile", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> iter = multipartRequest.getFileNames();
        String work = request.getParameter("work");
        while (iter.hasNext()) {
            MultipartFile file = multipartRequest.getFile(iter.next());
            if (file != null) {
                // 取得当前上传文件的文件名称
                String myFileName = file.getOriginalFilename();
                // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                if (myFileName.trim() != "") {
                    // 定义上传路径
                    if ("1".equals(work)) {
                        List<Object> list = ExcelHelper.parseExcel(file.getInputStream(), "org.web.acl.domain");
                        for (Object obj : list) {
                            try {
                                AclAccountDO kk = AclHelper.transObject(obj, AclAccountDO.class);
                                kk.setBusinessLine(HttpRequestHelper.getValueByCookie(request, ACL_COOKIE_BUSINESSLINE));
                                kk.setInputer(SessionAccountHelper.getSessionAccountVaue(request));
                                aclAccountService.saveAndUpdateAclAccount(kk);
                            } catch (Exception e) {
                                logger.error(ServiceExceptionHelper.getExceptionInfo(e));
                            }
                        }
                    } else if ("2".equals(work)) {
                        List<Object> list = ExcelHelper.parseExcel(file.getInputStream(), "org.web.acl.domain");
                        for (Object obj : list) {
                            try {
                                AclRoleDO kk = AclHelper.transObject(obj, AclRoleDO.class);
                                kk.setBusinessLine(HttpRequestHelper.getValueByCookie(request, ACL_COOKIE_BUSINESSLINE));
                                kk.setInputer(SessionAccountHelper.getSessionAccountVaue(request));
                                aclRoleService.insertAclRole(kk);
                            } catch (Exception e) {
                                logger.error(ServiceExceptionHelper.getExceptionInfo(e));
                            }
                        }
                    } else if ("3".equals(work)) {
                        List<Object> list = ExcelHelper.parseExcel(file.getInputStream(), "org.web.acl.domain");
                        for (Object obj : list) {
                            try {
                                AclResourceDO kk = AclHelper.transObject(obj, AclResourceDO.class);
                                kk.setBusinessLine(HttpRequestHelper.getValueByCookie(request, ACL_COOKIE_BUSINESSLINE));
                                kk.setInputer(SessionAccountHelper.getSessionAccountVaue(request));
                                aclResourceService.saveAndUpdateAclResource(kk);
                            } catch (Exception e) {
                                logger.error(ServiceExceptionHelper.getExceptionInfo(e));
                            }
                        }
                    } else if ("4".equals(work)) {
                        List<Object> list = ExcelHelper.parseExcel(file.getInputStream(), "org.web.acl.domain");
                        for (Object obj : list) {
                            try {
                                AclResourceRoleMappingDO kk = AclHelper.transObject(obj, AclResourceRoleMappingDO.class);
                                kk.setBusinessLine(HttpRequestHelper.getValueByCookie(request, ACL_COOKIE_BUSINESSLINE));
                                kk.setInputer(SessionAccountHelper.getSessionAccountVaue(request));
                                aclResourceRoleMappingService.insertAclResourceRoleMapping(kk);
                            } catch (Exception e) {
                                logger.error(ServiceExceptionHelper.getExceptionInfo(e));
                            }
                        }
                    } else if ("5".equals(work)) {
                        List<Object> list = ExcelHelper.parseExcel(file.getInputStream(), "org.web.acl.domain");
                        for (Object obj : list) {
                            try {
                                AclResourceAccountMappingDO kk = AclHelper.transObject(obj, AclResourceAccountMappingDO.class);
                                kk.setBusinessLine(HttpRequestHelper.getValueByCookie(request, ACL_COOKIE_BUSINESSLINE));
                                kk.setInputer(SessionAccountHelper.getSessionAccountVaue(request));
                                aclResourceAccountMappingService.insertAclResourceAccountMapping(kk);
                            } catch (Exception e) {
                                logger.error(ServiceExceptionHelper.getExceptionInfo(e));
                            }
                        }
                    } else if ("6".equals(work)) {
                        List<Object> list = ExcelHelper.parseExcel(file.getInputStream(), "org.web.acl.domain");
                        for (Object obj : list) {
                            try {
                                AclAccountRoleMappingDO kk = AclHelper.transObject(obj, AclAccountRoleMappingDO.class);
                                kk.setBusinessLine(HttpRequestHelper.getValueByCookie(request, ACL_COOKIE_BUSINESSLINE));
                                kk.setInputer(SessionAccountHelper.getSessionAccountVaue(request));
                                aclAccountRoleMappingService.insertAclAccountRoleMapping(kk);
                            } catch (Exception e) {
                                logger.error(ServiceExceptionHelper.getExceptionInfo(e));
                            }
                        }
                    }

                }
            }
        }
        return "success";
    }


    @RequestMapping(value = "downloadDemo.xlsx", method = {RequestMethod.GET, RequestMethod.POST})
    public void downloadDemo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        request.setCharacterEncoding("UTF-8");
        try {
            String rootpath = request.getSession().getServletContext().getRealPath("");
            File f = new File(rootpath + File.separator + "WEB-INF" + File.separator + "views" + File.separator + "account" + File.separator + "batch.xlsx");
            response.setContentType("application/x-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=batch.xlsx");
            response.setHeader("Content-Length", String.valueOf(f.length()));
            in = new BufferedInputStream(new FileInputStream(f));
            out = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len = in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
