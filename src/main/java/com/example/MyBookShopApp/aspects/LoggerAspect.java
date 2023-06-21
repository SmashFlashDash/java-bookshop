package com.example.MyBookShopApp.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggerAspect {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

//    @Pointcut("@annotation(com.example.MyBookShopApp.aspects.annotations.Some)")
//    public void loggableMethod() {
//    }

    // все контроллеры
    @Pointcut("execution(* com.example.MyBookShopApp.controllers..*(..))")
    public void logControllers() {
    }

    @Around(value = "logControllers()")
    public Object  before(ProceedingJoinPoint proceedingJoinPoint) {
        long started = System.nanoTime();
        Object returnValue = null;
        String signature = proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName();
        try {
            returnValue = proceedingJoinPoint.proceed();
            logger.info(signature
                    + ": finished: duration="
                    + (System.nanoTime() - started));
        } catch (Throwable e) {
            logger.log(Level.WARNING, signature
                    + ": throwed: duration="
                    + (System.nanoTime() - started));
            logger.log(Level.WARNING, e.getMessage());
        }
        return returnValue;
    }


}
