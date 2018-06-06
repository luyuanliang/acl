package org.web.acl.controller;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web.acl.domain.AclResourceDO;
import org.web.acl.helper.ACLConstant;
import org.web.domain.ViewResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by luyl on 17-9-7.
 */
@Scope("prototype")
@Controller
@RequestMapping("error")
public class ErrorController implements ACLConstant {

    @RequestMapping(value = "error", method = {RequestMethod.GET, RequestMethod.POST})
    public String land(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String msg = request.getParameter("msg");
        request.setAttribute("msg", msg);
        return "error/error";
    }


    /**
     * 用于展示错误消息。
     */
    @RequestMapping(value = "errorMessage", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String errorMessage(HttpServletRequest request, String message) throws IOException {
        String info = "";
        if (StringUtils.isNotBlank(message)) {
            info = message;
        } else {
            info = (String) request.getAttribute(ERROR_MESSAGE);
        }
        ViewResult<AclResourceDO> result = new ViewResult(false);
        result.setTitle("操作失败");
        result.setType("error");
        result.setMsg(info);
        return new Gson().toJson(result);
    }
}
