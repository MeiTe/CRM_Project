package it.com.crm.web.filter;


import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init````");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        System.out.println("对中文乱码问题进行过滤处理");
        //对于请求进行设置
        req.setCharacterEncoding("UTF-8");
        //对相应的中文进行乱码处理
        resp.setContentType("text/html;charset=utf-8");
        //进行放行
        chain.doFilter(req,resp);
    }

    @Override
    public void destroy() {
        System.out.println("destroy````");
    }
}

