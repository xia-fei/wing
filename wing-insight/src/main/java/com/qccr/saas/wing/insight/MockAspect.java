package com.qccr.saas.wing.insight;

import com.qccr.knife.result.CommonStateCode;
import com.qccr.knife.result.Results;
import com.qccr.saas.wing.core.mock.MockDataGenerate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * @author yeWanQing
 * @since 2017/5/23
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MockAspect {
    private static final Logger LOG = LoggerFactory.getLogger(MockAspect.class);
    private static final String QUERY_POINTCUT_EL = "@annotation(com.qccr.shprod.biz.manage.mock.Mock)";


    @Around(value = QUERY_POINTCUT_EL)
    public Object doQueryAround(final ProceedingJoinPoint joinPoint) throws Exception {
        Method method = MethodSignature.class.cast(joinPoint.getSignature()).getMethod();
        Object[] args = joinPoint.getArgs();
        Mock annotation = method.getAnnotation(Mock.class);
        //获取方法的泛型类型
        Class actualClass=getActualClass(method);
        MockDataGenerate mockDataGenerate =new MockDataGenerate();
        try {
            Object mockObject= mockDataGenerate.generateMock(actualClass);
            LOG.info("mock成功,method:{},result:{}",method.getName());
            return Results.newSuccessResult(mockObject,"mock数据");
        }catch (RuntimeException e){
            LOG.error("mock失败",e);
            return Results.newFailedResult(CommonStateCode.INNER_SERVER_ERROR,"mock数据生成失败");
        }


    }

    private Class getActualClass(Method method){
        ParameterizedType parameterizedType = (ParameterizedType) method.getGenericReturnType();
        return  (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }


}