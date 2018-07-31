package com.haier.im.aop.logger;


import com.haier.im.dao.IMOperMapper;
import com.haier.im.po.IMOper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Aspect
@Component
public class LogAspect {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Resource
    private IMOperMapper imOperMapper;

    /**
     * 保存系统操作日志
     */
    @Around(value = "@annotation(com.haier.im.aop.logger.OperLog)")
    public Object saveLog(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.debug("===start write oper log ");

        String methodName = joinPoint.getSignature().getName();//方法名
        //获取参数列表
        Object[] args = joinPoint.getArgs();//方法上的参数
        String argsStr = null;

        if (Objects.nonNull(args) && args.length > 0) {
            argsStr = "";
            for (Object arg : args
                    ) {
                argsStr = argsStr + arg;
            }
        }


        IMOper imOper = new IMOper();
        imOper.setOperateName(methodName);
        imOper.setOperateArgs(argsStr);
        imOper.setOperateTime(new Date());

        //写日志
        imOperMapper.insertOper(imOper);
        logger.debug("===method ===" + imOper.getOperateName() + "===args==" + imOper.getOperateArgs());


        //方法执行前拦截
        return joinPoint.proceed();
    }


}
