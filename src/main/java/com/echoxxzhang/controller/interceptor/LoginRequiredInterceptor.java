package com.echoxxzhang.controller.interceptor;

import com.echoxxzhang.annotation.LoginRequired;
import com.echoxxzhang.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // handler:拦截的目标对象
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();  // 获取到拦截的方法
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);  // 当这些方法上有LoginRequired注解时
            if (loginRequired != null && hostHolder.getUser() == null) {
                // 用户没有登陆、重定向到登陆页面
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }
}
