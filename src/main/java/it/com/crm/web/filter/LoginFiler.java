package it.com.crm.web.filter;

import it.com.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFiler implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("验证是否登录过init····");
    }

    @Override
    public void destroy() {
        System.out.println("验证是否登录过destroy····");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入验证有没有登录过的过滤器！");
        HttpServletRequest request= (HttpServletRequest) req;
        HttpServletResponse response= (HttpServletResponse) resp;


        //对于登录页面，那么不应该进行拦截
        String path = request.getServletPath();
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            //进行放行，因为要进行登录
            chain.doFilter(req,resp);
        }else {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            //判断user是否存在
            if (user!=null){
                //表示用户已经登录，那么正常的访问
                chain.doFilter(req,resp);
            }else {
                //没有登录，那么进行重定位到，登录页面，进行登录
                /*
                 * 重定向和转发的区别
                 * 转发，不会改变地址。因为设计页面的跳转，所以需要使用重定向，因为重定向可以改变地址。
                 * */
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }



    }
}
