package com.ywzs.filter;


import cn.hutool.core.text.AntPathMatcher;
import com.alibaba.fastjson.JSON;
import com.ywzs.common.R;
import com.ywzs.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@WebFilter("/*")
@Slf4j
public class loginCheckFilter implements Filter {
    public static final AntPathMatcher pathMatcher = new AntPathMatcher();
//     private final String[] urls = new String[]{
//            "/employee/login",
//            "/employee/logout",
//
//            "/backend/js/**",
//            "/backend/api/**",
//            "/backend/plugins/**",
//            "/backend/styles/**",
//            "/backend/images/**",
//            "/backend/page/login/login.html",
//
//            "/front/styles/**",
//            "/front/api/**",
//            "/front/images/**",
//            "/front/js/**",
//            "/front/fonts/**",
//
//    };
    //由于这个项目的前端做了拦截，所以后端可以少写一点
    private final String[] urls = new String[]{
            "/employee/login",
            "/employee/logout",

            "/backend/**",

            "/front/**",

            "/favicon.ico",

            "/user/login",   //前端用户登录
            "/user/sendMsg",
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        log.info("收到请求{}", requestURI);
        Boolean match = match(urls, requestURI);
        if (match) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //后端登录
        Object employeeId = request.getSession().getAttribute("Employee");
        if (employeeId != null) {
            //已经登录
            Long id = (Long) employeeId;
            UserHolder.save(id);     //在当前线程中保存
            filterChain.doFilter(request, response);
            log.info("释放了线程所存储的id");
            UserHolder.removeId();
            return;
        }
        //移动端登录
        Object user = request.getSession().getAttribute("User");
        if (user != null) {
            //已经登录
            Long id = (Long) user;
            UserHolder.save(id);     //在当前线程中保存
            filterChain.doFilter(request, response);
            log.info("释放了线程所存储的id");
            UserHolder.removeId();
            return;
        }
        //response.sendRedirect("/backend/page/login/login.html");
        log.warn("拦截本次请求(没有登录)"+requestURI);
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));  //出于与前端的约定
    }

    /**
     * 遍历配备每个url
     *
     * @param urls 需要放行的
     * @param uri  当前请求的
     * @return 是否放行
     */
    private Boolean match(String[] urls, String uri) {
        for (String url : urls) {
            boolean match = pathMatcher.match(url, uri);
            if (match) {
                return true;
            }
        }
        return false;

    }
}
