package com.endava.server.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ServiceLoggingAspect {
    private Logger logger = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    @Before("execution(* com.endava.server.service.*Service.*(..))")
    public void logServiceCreateMethodBefore(JoinPoint point){
        logger.info("calling " + point.getSignature().getName());
    }

    @After("execution(* com.endava.server.service.*Service.*(..))")
    public void logServiceCreateMethodAfter(JoinPoint point) {

        logger.info(point.getSignature().getName() + " called");
    }

}
