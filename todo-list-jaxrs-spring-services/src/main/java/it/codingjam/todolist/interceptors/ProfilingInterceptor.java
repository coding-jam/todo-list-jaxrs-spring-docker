package it.codingjam.todolist.interceptors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StopWatch;

import javax.inject.Named;

/**
 *
 * Profiling interceptor
 *
 */
@Aspect
@Named
public class ProfilingInterceptor {

    @Around("@within(it.codingjam.todolist.interceptors.Profiling) || @annotation(it.codingjam.todolist.interceptors.Profiling)")
    public Object profile(ProceedingJoinPoint call) throws Throwable {
        StopWatch clock = new StopWatch("Profiling for " + call.getTarget().toString());
        try {
            clock.start(call.toShortString());
            return call.proceed();
        } finally {
            clock.stop();
            System.out.println(clock.prettyPrint());
        }
    }
}
