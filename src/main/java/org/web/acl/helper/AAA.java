package org.web.acl.helper;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by luyl on 17-10-20.
 */
public class AAA implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if(true){
            return ;
        }
        chain.doFilter(request,response);
        long current = System.currentTimeMillis();
    }

    @Override
    public void destroy() {

    }
}
