package com.endava.server.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class MethodExecutionTimeAspect {
    private Logger logger = LoggerFactory.getLogger(MethodExecutionTimeAspect.class);

    @Around("execution(* com.endava.server.controller.*Controller.*(..))")
    public void logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object output = joinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        logger.info(joinPoint.getSignature().getName() + " called. Execution time: " + elapsedTime + " ms");

    }
}
