package org.web.acl.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by luyl on 17-9-5.
 */
@Scope("prototype")
@Controller
@RequestMapping("")
public class AccountMainController {
public static void main(String[] args) {
	System.out.println("SS");
}
	
    @RequestMapping(value = "main", method = {RequestMethod.GET, RequestMethod.POST})
    public String land(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getRemotePort();
        return "account/main";
    }

}
