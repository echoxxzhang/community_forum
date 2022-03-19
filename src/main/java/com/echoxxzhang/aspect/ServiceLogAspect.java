package com.echoxxzhang.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Aspect
public class ServiceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(* com.echoxxzhang.service.*.*(..))")
    public void pointcut() {

    }

    /**
     * service方法执行之后打印日志
     * @param joinPoint
     */
    // @Before("pointcut()")  // 引用
    public void before(JoinPoint joinPoint) {

        // 格式：用户[ip地址],在[xxx时间],访问了[xxx方法].
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null){
            return;
        }
        HttpServletRequest request = attributes.getRequest(); // 获取request对象
        String ip = request.getRemoteHost(); // 获取用户IP地址

        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String target = joinPoint.getSignature().getName(); // 方法名

        logger.info(String.format("用户[%s],在[%s],访问了[%s].", ip, now, target));
    }

}
