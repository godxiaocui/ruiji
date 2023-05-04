package com.czh.reggie.demo.fliter;

import com.alibaba.fastjson.JSON;
import com.czh.reggie.demo.common.BaseContext;
import com.czh.reggie.demo.common.R;
//import jdk.nashorn.internal.ir.CallNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否登陆
 */
@Slf4j
@WebFilter(filterName = "LoginFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    // 路径匹配，
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    /**
     * 实现5个功能
     * 1. 获取本次的uri
     * 2. 判断本次请求是否需要处理
     * 3. 如果不需要处理，直接放行
     * 4. 判断登陆状态，如果是已经登陆，直接放行
     * 5. 如果没有登陆返回登录结果
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        // 1. 获取本次的uri

        String requestURI = request.getRequestURI();
        // 2. 不需要处理的请求
        String[] urls = new String[]{
        "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/demo/**",
                "/common/**"
        };
        log.info("{}",urls);
        // 2. 判断本次请求是否需要处理
        boolean check = this.check(urls, requestURI);
        // 3. 如果不需要处理，直接放行
          if(check) {
              log.info("本次请求{}不要处理",requestURI);
              filterChain.doFilter(request, response);
              return;
          }

        // 4. 判断登陆状态，如果是已经登陆，直接放行
        if(null!=(request.getSession().getAttribute("employee"))){
            log.info("用户已经登陆",request.getSession().getAttribute("employee"));
            // 获取登陆状态
            Long id=(Long)request.getSession().getAttribute("employee");
            //保存到线程
            BaseContext.setCurrentId(id);
            filterChain.doFilter(request, response);
            return;
        }

        // 5. 如果没有登陆返回登录结果,通过输出流方式向客户端响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        log.info("拦截到的请求：{}",request.getRequestURI());
//        // log 中可以用占位符{}
//        log.info("拦截到的请求：{}",request.getRequestURI());
//        // 放行
//        filterChain.doFilter(request, response);
    }
    /**
     * 路径匹配器
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }

}
