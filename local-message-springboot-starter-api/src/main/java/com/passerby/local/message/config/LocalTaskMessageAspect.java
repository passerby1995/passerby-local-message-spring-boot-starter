package com.passerby.local.message.config;

import com.passerby.local.message.annoation.LocalTaskMessage;
import com.passerby.local.message.po.TaskMessageEntityCommand;
import com.passerby.local.message.repository.LocalTaskRepository;
import com.passerby.local.message.service.IHandleService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @Author: sunjunyao
 * @Date: 2026/1/6 14:52
 * @Descrpition:
 */
@Slf4j
@Aspect

public class LocalTaskMessageAspect {

    private IHandleService iHandleService;
    private TransactionTemplate transactionTemplate;


    public LocalTaskMessageAspect(IHandleService iHandleService, TransactionTemplate transactionTemplate) {
        this.iHandleService = iHandleService;
        this.transactionTemplate = transactionTemplate;
    }

    @Pointcut("@annotation(com.passerby.local.message.annoation.LocalTaskMessage)")
    public void pointcut() {
    }

    @Around("pointcut()&& @annotation(localTaskMessage)")
    public Object around(ProceedingJoinPoint joinPoint, LocalTaskMessage localTaskMessage) throws Throwable {
        Signature signature = joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        //校验事务是否存在存在则复用。不存在则开启新事务
        boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
        TaskMessageEntityCommand taskMessageEntityCommand = resolveTaskEntityCommand(joinPoint, localTaskMessage);
        if (isActive) {
            //先执行目标方法
            Object proceed = joinPoint.proceed();
            //把数据落库
            iHandleService.handle(taskMessageEntityCommand);
            //根据注解获取参数名，将数据落库
            return proceed;
        } else {
            return  transactionTemplate.execute(status -> {
                try {
                    Object proceed = joinPoint.proceed();
                    iHandleService.handle(taskMessageEntityCommand);
                    return proceed;
                } catch (Throwable e) {
                    status.setRollbackOnly();
                    throw new RuntimeException(e);
                }

            });
        }
    }

    private TaskMessageEntityCommand resolveTaskEntityCommand(ProceedingJoinPoint joinPoint, LocalTaskMessage localTaskMessage) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length != 0) {
            for (Object arg : args) {
                if (arg instanceof TaskMessageEntityCommand) {
                    return (TaskMessageEntityCommand) arg;
                }
            }
        } else {

            log.warn("暂时没弄");
            return null;
        }
        return null;
    }
}
