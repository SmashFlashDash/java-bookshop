package com.example.MyBookShopApp.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggerAspect {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Pointcut("@annotation(com.example.MyBookShopApp.aspects.annotations.LogResult)")
    public void logResult() {
    }
    // все методы класса, или метод с аннотацией
    @AfterReturning(value = "@within(com.example.MyBookShopApp.aspects.annotations.LogResult) " +
            "|| @annotation(com.example.MyBookShopApp.aspects.annotations.LogResult)", returning = "response")
    public void uuidServicesArgCatcherAdvice(JoinPoint joinPoint, Object response) {
        logger.warning(joinPoint.getSignature() + "." + joinPoint.getTarget() + ": result=" + response);
    }
    @AfterThrowing(value = "@within(com.example.MyBookShopApp.aspects.annotations.LogResult) " +
            "|| @annotation(com.example.MyBookShopApp.aspects.annotations.LogResult)", throwing = "ex")
    public void uuidServicesArgCatcherAdvice(JoinPoint joinPoint, Exception ex) {
        logger.warning(joinPoint.getSignature() + "." + joinPoint.getTarget() + ": throwd=" + ex.getLocalizedMessage());
    }

    // все контроллеры
    @Pointcut("within(com.example.MyBookShopApp.controllers..*)")
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
