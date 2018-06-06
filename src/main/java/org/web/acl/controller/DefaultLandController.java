package org.web.acl.controller;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web.acl.helper.ACLConstant;
import org.web.acl.domain.SessionAccountDO;
import org.web.acl.query.QuerySessionAccount;
import org.web.acl.service.SessionAccountService;
import org.web.domain.ViewResult;
import org.web.exception.ResultMessageEnum;
import org.web.helper.ServiceExceptionHelper;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by luyl on 17-9-4.
 */
@Scope("prototype")
@Controller
@RequestMapping("land")
public class DefaultLandController implements ACLConstant {

    @Resource
    private SessionAccountService sessionAccountService;

    private static final Logger logger = Logger.getLogger(DefaultLandController.class);


    @RequestMapping(value = "land", method = {RequestMethod.GET, RequestMethod.POST})
    public String land(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return "land/land";
    }

    @RequestMapping(value = "checkPassword", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String checkPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ViewResult<String> viewResult = new ViewResult(true);
        Gson gson = new Gson();
        try {
            String accountNum = request.getParameter("accountNum");
            String password = request.getParameter("password");
            QuerySessionAccount querySessionAccount = new QuerySessionAccount();
            querySessionAccount.setAccountNum(accountNum);
            SessionAccountDO sessionAccountDO = sessionAccountService.selectOneSessionAccount(querySessionAccount);
            if (sessionAccountDO == null) {
                SessionAccountDO newSessionAccountDO = new SessionAccountDO();

                String[] array = accountNum.split("/");
                if (array.length < 2) {
                    throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.PARAM_FORMAT_INVALID, "用户名或者密码不正确");
                }

                newSessionAccountDO.setAccountNum(array[0]);
                newSessionAccountDO.setAccountName(array[1]);
                newSessionAccountDO.setPassword(password);
                sessionAccountService.insertSessionAccount(newSessionAccountDO);
                newSessionAccountDO.setPassword(null);
                Cookie cookie = new Cookie(ACL_COOKIE_ACCOUNT,genCookieValueBySessionAccountDO(sessionAccountDO));
                cookie.setPath("/");
                response.addCookie(cookie);
            } else if (!StringUtils.equals(password, sessionAccountDO.getPassword())) {
                throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.DATA_LOSE_EFFICACY, "用户名或者密码不正确");
            } else {
                sessionAccountDO.setPassword(null);
                String value = new Gson().toJson(sessionAccountDO);
                Cookie cookie = new Cookie(ACL_COOKIE_ACCOUNT,genCookieValueBySessionAccountDO(sessionAccountDO));
                cookie.setPath("/");
                response.addCookie(cookie);
            }

            return gson.toJson(viewResult);
        } catch (Exception e) {
            logger.error(ServiceExceptionHelper.getExceptionInfo(e));
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
    }

    private String genCookieValueBySessionAccountDO(SessionAccountDO sessionAccountDO){
        String value2 = sessionAccountDO.getAccountNum()+"@"+sessionAccountDO.getAccountName();
        return value2;
    }

}
