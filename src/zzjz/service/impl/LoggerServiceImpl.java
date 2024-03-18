package zzjz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.sun.jersey.spi.container.ContainerRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author 梅宏振
 * @version 2016年5月24日 上午8:32:29
 * @ClassName: LoggerServiceImpl
 * @Description: 统一日志服务
 */
@Component
public class LoggerServiceImpl {

    private static Logger logger = Logger.getLogger(LoggerServiceImpl.class);

    private static String strMess = ".";

    //需要排除的请求（不需要做登陆验证）
    private static final List<String> EXCLUSION_REQUEST = Lists.newArrayList("zzjz.rest.LoginRest.userLogin",
            "zzjz.rest.ImageCodeRest.imageExecute", "zzjz.rest.UserRest.addUser", "zzjz.rest.LoginRest.getUserByUserName");

    /**
     * 拦截日志.
     *
     * @param joinPoint joinPoint对象
     * @return response
     * @throws Throwable throwable
     */
    public BaseResponse around(ProceedingJoinPoint joinPoint) {
        BaseResponse response = new BaseResponse();
        Object[] args = joinPoint.getArgs();
        String requestUri = joinPoint.getSignature().getDeclaringTypeName() + strMess +
                joinPoint.getSignature().getName();
        HttpServletRequest httpServletRequest = (HttpServletRequest) RequestContextHolder.getRequestAttributes();
        //不是登陆、获取验证码、注册等与权限无关操作
        if (!EXCLUSION_REQUEST.contains(requestUri)) {
            for (Object arg : args) {
                if (arg instanceof ContainerRequest) {
                    ContainerRequest containerRequest = (ContainerRequest) arg;
                    String userId = containerRequest.getSecurityContext().getAuthenticationScheme();
                    if (StringUtils.isEmpty(userId)) {
                        logger.debug("未登录！");
                        response.setMessage("您未登录或session已失效，请先登录！");
                        response.setResultCode(ResultCode.NOT_AUTHORIZED);
                        //throw new NotLoginException("未登录", new Throwable(), "111", new Object[]{11,22});
                        return response;
                    }
                }
            }
        }

        logger.debug("开始调用REST接口方法:" + joinPoint.getSignature().getDeclaringTypeName() + strMess
                + joinPoint.getSignature().getName());
        List<Object> argList = Arrays.asList(args);
        List<Object> list = new ArrayList(argList);
        Object toRemoveObject = null;
        for (Object object : list) {
            if (object.toString().startsWith("com.sun.jersey.spi.container.ContainerRequest")||object.toString().startsWith("org.jvnet.mimepull.DataHead$ReadMultiStream")) {
                toRemoveObject = object;
                continue;
            }
        }
        if (toRemoveObject != null) {
            list.remove(toRemoveObject);
        }
        String parameters = JSONObject.toJSONString(list);
        logger.debug("接口传入参数:" + parameters);
        if (toRemoveObject != null) {
            if (toRemoveObject.toString().startsWith("org.jvnet.mimepull.DataHead$ReadMultiStream")) {
                try {
                    response = ((BaseResponse) joinPoint.proceed()); //执行被拦截方法
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }else {
                String userId = ((ContainerRequest) toRemoveObject).getHeaderValue("userId");
                if (StringUtils.isNotEmpty(userId)) {
                    logger.debug("接口Header传入参数userId:" + userId);
                    try {
                        response = ((BaseResponse) joinPoint.proceed()); //执行被拦截方法
                        logger.debug("调用REST接口方法结果:" + response.getResultCode());
                    } catch (Throwable throwable) {
                        logger.debug("调用REST接口方法出现异常!");
                        throwable.printStackTrace();
                    }
                } else {
                    logger.debug("接口Header传入参数userId为空！");
                }
            }
        }
        logger.debug("调用REST接口方法结束:" + joinPoint.getSignature().getDeclaringTypeName() + strMess
                + joinPoint.getSignature().getName());
        return response;
    }

    /**
     * 接口方法前置日志.
     *
     * @param joinpoint joinPoint对象
     */
    public void before(JoinPoint joinpoint) {
        logger.debug("开始调用REST接口方法:" + joinpoint.getSignature().getDeclaringTypeName() + strMess
                + joinpoint.getSignature().getName());
        Object[] args = joinpoint.getArgs();
        List<Object> argList = Arrays.asList(args);
        List<Object> list = new ArrayList(argList);
        Object toRemoveObject = null;
        for (Object object : list) {
            if (object.toString().startsWith("com.sun.jersey.spi.container.ContainerRequest")) {
                toRemoveObject = object;

            }
        }
        if (toRemoveObject != null) {
            String userId = ((ContainerRequest) toRemoveObject).getHeaderValue("userId");
            if (StringUtils.isNotEmpty(userId)) {
                logger.debug("接口Header参数userId=" + userId);
            } else {
                logger.debug("接口Header参数userId为空!");
            }
            list.remove(toRemoveObject);
        }
        String parameters = JSONObject.toJSONString(list);
        logger.debug("接口传入参数:=" + parameters);
        return;
    }

    /**
     * 接口方法后置日志.
     *
     * @param joinpoint joinPoint对象
     * @param result    结果集
     */
    public void after(JoinPoint joinpoint, Object result) {
        if ("com.zzjz.entity.BaseResponse".equals(result.getClass().getName())) {
            BaseResponse baseResponse = (BaseResponse) result;
            logger.debug("调用REST接口方法结果:" + baseResponse.getResultCode());

        } else {
            logger.debug("接口返回类型不合法，不是BaseResponse!");
        }
        logger.debug("调用REST接口方法结束:" + joinpoint.getSignature().getDeclaringTypeName() + strMess
                + joinpoint.getSignature().getName());
        return;
    }
}
