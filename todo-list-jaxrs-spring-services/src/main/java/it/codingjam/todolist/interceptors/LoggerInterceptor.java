package it.codingjam.todolist.interceptors;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.inject.Named;

/**
 *
 * Log method calls
 *
 */
@Aspect
@Named
public class LoggerInterceptor {

    @Around("within(it.codingjam.todolist.services..*)")
    public Object log(ProceedingJoinPoint call) throws Throwable {
        Logger logger = Logger.getLogger(call.getTarget().getClass());
        logger.debug("Calling method " + call.getSignature());
        return call.proceed();
    }
}
